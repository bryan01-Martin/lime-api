package com.lime.limeEduApi.framework.common.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp;
import com.lime.limeEduApi.framework.common.util.aria.AriaCrypt;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.CookieGenerator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.*;


@Component
public class FtpFileTransfer {
	public static final Logger logger = LoggerFactory.getLogger(FtpFileTransfer.class);

	@Value("${ftp.host}")
	private String ftpIp;

	@Value("${ftp.id}")
	private String ftpId;

	@Value("${ftp.password}")
	private String ftpPwd;

	@Value("${ftp.port}")
	private String ftpPort;

	@Value("${ftp.rootDir}")
	private String rootDir;

	@Value("${ftp.serverType}")
	private String serverType;

	@Value("${spring.servlet.multipart.location}")
	private String tempLocation;

	/**
	 * FTP 서버 타입에따라 다르게 분기 ( passive )
	 * @return
	 * @throws IOException
	 * @throws InvalidKeyException
	 */
	FTPClient getFtpClient() throws IOException, InvalidKeyException {
		FTPClient ftp;
		if(serverType.equals("ubuntu")){
			ftp = new FTPClient(); // FTP Client 객체 생성
			ftp.setControlEncoding("UTF-8"); // 문자 코드를 UTF-8로 인코딩
			ftp.connect(ftpIp, Integer.parseInt(ftpPort)); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호
			String decryptFtpPwd = AriaCrypt.decrypt(ftpPwd);
			ftp.login(ftpId, decryptFtpPwd); // FTP 로그인 ID, PASSWORLD 입력
		}else{
			ftp = new FTPSClient(); // FTP Client 객체 생성
			ftp.setControlEncoding("UTF-8"); // 문자 코드를 UTF-8로 인코딩
			ftp.connect(ftpIp, Integer.parseInt(ftpPort)); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호
			String decryptFtpPwd = AriaCrypt.decrypt(ftpPwd);
			ftp.login(ftpId, decryptFtpPwd); // FTP 로그인 ID, PASSWORLD 입력
			ftp.enterLocalPassiveMode();
		}
		ftp.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅
		return ftp;
	}

	/**
	 * FTP 파일 업로드
	 * @param
	 * @return result,
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> ftpFileUpload(HttpServletRequest request, String typePath) throws Exception {
		List<HashMap<String,Object>> resultList = new ArrayList<HashMap<String,Object>>();
		// 파일 업로드
		final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		final Map<String, List<MultipartFile>> files = multiRequest.getMultiFileMap();

		// 파일 업로드 할 대상이 없을 경우 비어있는 list를 반환함
		if(files.isEmpty()) {

			return new ArrayList<HashMap<String,Object>>();
		}

		Iterator<String> keys = files.keySet().iterator();

		/*List<MultipartFile> mfList = new ArrayList<MultipartFile>();*/
		List<MultipartFile> mfList = files.get("files");
		/*files.keySet().stream().forEach(m -> {
			mfList.addAll(files.get(m).stream().collect(Collectors.toList()));
		});*/

		Channel channel = null;
		Session session = null;
		ChannelSftp sftp = null;
		JSch jsch = new JSch();

		try{
			session = jsch.getSession(ftpId, ftpIp, Integer.parseInt(ftpPort));
			session.setPassword(ftpPwd);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking","no");
			session.setConfig(config);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;

			logger.info("ftpFileUpload-mfList.size(): " + mfList.size());
			/*mfList.stream().forEach(mf -> {*/
			for(MultipartFile mf : mfList){

				HashMap<String,Object> resultMap = new HashMap<String,Object>();

				if(mf.getSize() <= 0){
					continue;
				}else{
					logger.info("ftpFileUpload.file-OriginalFilename: " + mf.getOriginalFilename());
					this.sendFileBySftp(sftp, mf, typePath, "N", resultList);
				}
			}
		}catch(Exception e){
			System.out.println("IO:"+e.getMessage());
		}finally{
			if(channel != null && channel.isConnected()){
				channel.disconnect();
			}
			if(session != null && session.isConnected()){
				session.disconnect();
			}
			if(sftp != null && sftp.isConnected()){
				sftp.disconnect();
			}
		}
		logger.info("ftpFileUpload-resultList.size(): " + resultList.size());
		return resultList;
	}

	/**
	 * FTP 파일 다운로드
	 * @param fileName
	 * @param originalFileName
	 * @param remoteFilePath
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public int ftpFileDown(String fileName, String originalFileName, String remoteFilePath, HttpServletResponse response) throws Exception {

		FTPClient ftp = new FTPClient();
		int result = 0;
		try {
			ftp = getFtpClient();

			//객체 생성
			ftp.changeWorkingDirectory(rootDir+remoteFilePath); // 작업 디렉토리 변경
			ftp.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅
			logger.info(fileName + " Downlaod!!");

			if(ftp.listFiles(fileName) != null && ftp.listFiles(fileName).length!=0)
			{
				InputStream inputStream = ftp.retrieveFileStream(fileName);

				byte fileByte[] = IOUtils.toByteArray(inputStream);
				inputStream.close();

				response.setContentType("application/octet-stream");

				response.setContentLength(fileByte.length);
				response.setHeader("Content-Disposition", "attachment; fileName=\"" +
						URLEncoder.encode(originalFileName,"UTF-8")+"\";");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.getOutputStream().write(fileByte);

				response.getOutputStream().flush(); response.getOutputStream().close();

				result = 1;
				logger.info("dowload success!");
			}

		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
		} finally {
			try {

				if (ftp.isConnected()) {
					ftp.logout();
					ftp.disconnect();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return result;
	}


	/**
	 * FTP 파일 가져오기
	 * @param fileName
	 * @param originalFileName
	 * @param remoteFilePath
	 * @return
	 * @throws Exception
	 */
	public File getFile(String fileName, String originalFileName, String remoteFilePath) throws Exception {

		FTPClient ftp = new FTPClient();
		File file = null;
		int result = 0;
		try {
			ftp = getFtpClient();
			ftp.changeWorkingDirectory(rootDir + remoteFilePath); // 작업 디렉토리 변경
			ftp.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅

			if(ftp.listFiles(fileName) != null && ftp.listFiles(fileName).length!=0)
			{
				InputStream inputStream = ftp.retrieveFileStream(fileName);


				byte fileByte[] = IOUtils.toByteArray(inputStream);
				inputStream.close();

				ByteArrayInputStream bis = new ByteArrayInputStream(fileByte);
				File tempFolder = new File("C:/Temp/download");
				if(!tempFolder.mkdir()) {
					tempFolder.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream("C:/Temp/download/"+originalFileName);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				int readCount = 0;

				byte[] outBuffer = new byte[65536];

				while((readCount = bis.read(outBuffer))>0) {
					bos.write(outBuffer,0,readCount);
				}

				file = new File("C:/Temp/download/"+originalFileName);


			}

		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
		} finally {
			try {

				if (ftp.isConnected()) {
					ftp.logout();
					ftp.disconnect();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return file;
	}

	/**
	 * 디렉토리가 존재하는지 없는지 여부 판단
	 * @param ftp
	 * @param dirPath
	 * @return
	 * @throws IOException
	 */
	boolean checkDirectoryExists(FTPClient ftp, String dirPath) throws IOException {
		ftp.changeWorkingDirectory(dirPath);
		int returnCode = ftp.getReplyCode();
		if (returnCode == 550) {
			return false;
		}
		return true;
	}


	/**
	 * jsxl로 엑셀 다운로드 하는 기능
	 * @param response
	 * @param resultMap
	 * @param fileName
	 * @param remoteFilePath
	 * @throws Exception
	 */
	public void excelDownload(HttpServletResponse response, Map<String, Object> resultMap , String fileName, String remoteFilePath) throws Exception {

		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("DOWNLOAD_STATUS");
		cg.addCookie(response, "COMPLETE");

		FTPClient ftp = new FTPClient();
		File file = null;
		int result = 0;

		String title = "excelFile";
		String header = String.valueOf(resultMap.get("header"));

		try {

			//객체 생성
			ftp.setControlEncoding("UTF-8"); // 문자 코드를 UTF-8로 인코딩
			ftp.connect(ftpIp, Integer.parseInt(ftpPort)); // 서버접속 " "안에 서버 주소 입력 또는 "서버주소", 포트번호
			String decryptFtpPwd = AriaCrypt.decrypt(ftpPwd);

			ftp.login(ftpId, decryptFtpPwd); // FTP 로그인 ID, PASSWORLD 입력
			ftp.enterLocalPassiveMode(); // Passive Mode 접속일때
			ftp.changeWorkingDirectory(remoteFilePath); // 작업 디렉토리 변경
			ftp.setFileType(FTP.BINARY_FILE_TYPE); // 업로드 파일 타입 셋팅


			if(ftp.listFiles(fileName) != null && ftp.listFiles(fileName).length!=0)
			{
				InputStream inputStream = ftp.retrieveFileStream(fileName);


				byte fileByte[] = IOUtils.toByteArray(inputStream);
				inputStream.close();

				ByteArrayInputStream bis = new ByteArrayInputStream(fileByte);
				File tempFolder = new File("C:/Temp/download");
				if(!tempFolder.mkdir()) {
					tempFolder.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream("C:/Temp/download/"+fileName);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				int readCount = 0;

				byte[] outBuffer = new byte[65536];

				while((readCount = bis.read(outBuffer))>0) {
					bos.write(outBuffer,0,readCount);
				}

				file = new File("C:/Temp/download/"+fileName);

				InputStream is = new BufferedInputStream(new FileInputStream("C:/Temp/download/"+fileName));

				XLSTransformer xls = new XLSTransformer();
				Workbook workbook = xls.transformXLS(is, resultMap);


				if(resultMap.containsKey("title")){
					title = resultMap.get("title").toString();
				}

				if(header.contains("MSIE") || header.contains("Trident")) {
					title = URLEncoder.encode(title,"UTF-8").replaceAll("\\+", "%20");
					response.setHeader("Content-Disposition", "attachment; filename="+title+".xlsx;");
				}else {
					title = new String(title.getBytes("UTF-8"),"ISO-8859-1");

					response.setHeader("Content-Disposition", "attachment; filename=\""+title+".xlsx");
				}

				response.setHeader("Content-Disposition", "attachment; filename=\""+title+".xlsx");

				OutputStream os = response.getOutputStream();

				workbook.write(os);

				file.delete();

			}

		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
		} finally {
			try {

				if (ftp.isConnected()) {
					ftp.logout();
					ftp.disconnect();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	public MultipartFile generateThumbnail(MultipartFile source) {

		File file = new File(new StringBuffer(tempLocation).append(source.getOriginalFilename()).toString());
		BufferedImage thumbnail = null;
		MultipartFile result = null;
		InputStream inputStream = null;

		try{

			source.transferTo(file);

			Picture picture = FrameGrab.getFrameFromFile(file, 0);
			thumbnail = AWTUtil.toBufferedImage(picture);

/*            inputStream = source.getInputStream();

            //원본 동영상 파일 읽기
            BufferedImage videoImage = ImageIO.read(inputStream);

            //썸네일 이미지 생성
            BufferedImage thumbnailImage = videoImage.getSubimage(0,0,videoImage.getWidth(), videoImage.getHeight());*/

			//썸네일 이미지를 임시 파일 객체로 변환
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(thumbnail, "jpg", baos);
			byte[] imageData = baos.toByteArray();

			String originalFileName = source.getOriginalFilename();
			int delimeterIdx = originalFileName.lastIndexOf(".");
			String fileName = originalFileName.substring(0,delimeterIdx);

			String thumbnailName = new StringBuffer(fileName).append("_thumbnail").toString();
			String originalFilename = new StringBuffer(thumbnailName).append(".").append("jpg").toString();

			/*result = new org.springframework.mock.web.MockMultipartFile(originalFilename, imageData);*/

			//임시 파일 객체를 반환
			result = new MultipartFile() {
				@Override
				public String getName() {
					return thumbnailName;
				}

				@Override
				public String getOriginalFilename() {
					return originalFilename;
				}

				@Override
				public String getContentType() {
					return "image/jpeg";
				}

				@Override
				public boolean isEmpty() {
					return false;
				}

				@Override
				public long getSize() {
					return imageData.length;
				}

				@Override
				public byte[] getBytes() throws IOException {
					return imageData;
				}

				@Override
				public InputStream getInputStream() throws IOException {
					return new ByteArrayInputStream(imageData);
				}

				@Override
				public void transferTo(File dest) throws IOException, IllegalStateException {
					source.transferTo(dest);
				}
			};

		} catch (Exception e){
			e.printStackTrace();
		}

		//TODO 기본 썸네일 이미지 세팅 필요!!!
		return result;
	}

	public void sendFileBySftp(ChannelSftp sftp, MultipartFile source, String typePath, String thumbYn, List<HashMap<String,Object>> uploadFileList){

		HashMap<String,Object> resultMap = new HashMap<>();
		InputStream sourceInputStream = null;
		try {

			String originalFilename = source.getOriginalFilename();
			String filename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
			// 확장자명 추출
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			String convFileName = "";

			if("Y".equals(thumbYn)){
				HashMap<String, Object> videoFile = uploadFileList.get(uploadFileList.size()-1);
				String videoName = (String)videoFile.get("FILE_CONV_NM");
				String videoConvName = videoName.substring(0, videoName.lastIndexOf("."));
				convFileName = new StringBuffer(videoConvName).append(".").append(extension).toString();

			}else{
				convFileName = new StringBuffer(String.valueOf(System.currentTimeMillis())).append(UUID.randomUUID()).append(".").append(extension).toString();
			}

			String subPath = "";

			if(CommonUtil.isNull(extension)){
				logger.error("파일 확장자 없음.");
				throw new Exception();
			}else{
				if(("jpg".equals(extension) || "png".equals(extension)) && "N".equals(thumbYn)){
					subPath = "/photo/";
				}else if("mp4".equals(extension) || "Y".equals(thumbYn)) {
					subPath = "/video/";
				}else{
					logger.error("불특정 확장자 >>>>>>>[{}]<<<<<<<", extension);
					throw new Exception();
				}
			}


			StringBuffer pathSb = new StringBuffer("/").append(typePath).append(subPath).append(CommonUtil.getDate("yyyyMMdd"));
			if("Y".equals(thumbYn)){
				pathSb.append("/thumbnail");
			}

			String path = new StringBuffer(rootDir).append(pathSb).toString();

			String curPath = "";
			for(String rPath : path.split("/")){
				try{
					if(!rPath.isEmpty()) {
						curPath += "/"+rPath;
						sftp.cd(curPath);
					}
				}catch(Exception e){
					sftp.mkdir(curPath);
				}
			}

			String fullPath = new StringBuffer(path).append("/").append(convFileName).toString();

			logger.info("fullPath :::: "+fullPath);
			sourceInputStream = source.getInputStream();
			sftp.put(sourceInputStream, fullPath);

			resultMap.put("FIELD_NAME"  , source.getName());
			resultMap.put("FILE_NM"     , originalFilename);
			resultMap.put("FILE_CONV_NM", convFileName);
			resultMap.put("FILE_PATH"   , pathSb.toString());
			resultMap.put("FILE_SIZE"   , source.getSize());
			resultMap.put("FILE_EXT"    , extension);
			resultMap.put("thumbYn"     , thumbYn);
			resultMap.put("contentType" , source.getContentType());

			uploadFileList.add(resultMap);
			if("mp4".equals(extension)){
				MultipartFile thumbnailFile = this.generateThumbnail(source);
				this.sendFileBySftp(sftp, thumbnailFile, typePath, "Y", uploadFileList);
			}


		} catch (Exception e){
			CommonUtil.extracePrintLog(e);
		} finally {
			if(sourceInputStream != null){
				try {
					sourceInputStream.close();
				} catch (IOException e) {
					CommonUtil.extracePrintLog(e);
				}
			}
		}

	}
}
