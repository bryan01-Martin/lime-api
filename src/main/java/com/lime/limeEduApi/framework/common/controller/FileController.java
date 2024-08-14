package com.lime.limeEduApi.framework.common.controller;

import com.lime.limeEduApi.framework.common.service.FileService;
import com.lime.limeEduApi.framework.common.util.FtpFileTransfer;
import com.lime.limeEduApi.framework.common.view.DownloadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@ApiIgnore // TODO 완성 후 제거
public class FileController {
	@Autowired
	DownloadView downloadView;

	@Autowired
	FileService fileService;

	@Autowired
	private FtpFileTransfer ftpFileTransfer;

	/**
	 * 파일 다운로드
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/downloadFile/{FILE_SEQ}")
	public ModelAndView selectFile(@PathVariable String FILE_SEQ, @RequestParam HashMap<String,Object> params) throws Exception {
		params.put("FILE_SEQ", FILE_SEQ);
		return new ModelAndView(downloadView, "file" , fileService.selectFile(params));
	}


	/**
	 * CKEditor로 이미지 업로드 하기
	 * @param req
	 * @param res
	 * @param multiFile
	 * @param upload
	 * @throws Exception
	 */
	@RequestMapping(value="/api/{basePath}/imageUpload")
	public void imageUpload (@PathVariable(value="basePath") String basePath, HttpServletRequest req , HttpServletResponse res,
							 MultipartHttpServletRequest multiFile, @RequestParam MultipartFile upload) throws Exception{

		OutputStream out = null;
		PrintWriter printWriter = null;

		res.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8");


		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date time = new Date();
			String today = format.format(time);
			String baseDir = getPath(basePath);
			List<HashMap<String, Object>> fileList = ftpFileTransfer.ftpFileUpload(req, baseDir + "/editor/"+today);
			String path = (String) fileList.get(0).get("FILE_PATH");
			String orgFileName = (String) fileList.get(0).get("FILE_NM");
			String fileName = (String) fileList.get(0).get("FILE_CONV_NM");

			printWriter = res.getWriter();
			String fileUrl = "/api/ckImgSubmit?path="+path+"&fileName=" + fileName + "&orgFileName=" + orgFileName; // 작성화면

			printWriter.println("{\"filename\" : \""+fileName+"\", \"uploaded\" : 1, \"url\":\""+fileUrl+"\"}");
			printWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null) {
					out.close();
				}
				if(printWriter != null) {
					printWriter.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * CKEditor 서버로 전송된 이미지 뿌려주기
	 * @param orgFileName
	 * @param path
	 * @param fileName
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value="/api/ckImgSubmit")
	public void ckSubmit(
			@RequestParam(value="orgFileName") String orgFileName
			, @RequestParam(value="path") String path
			, @RequestParam(value="fileName") String fileName
			, HttpServletRequest req, HttpServletResponse res) throws Exception {

		File file = ftpFileTransfer.getFile(fileName,orgFileName,path);
		String sDirPath =  file.getPath();

		File imgFile = new File(sDirPath);

		if(imgFile.isFile()) {
			byte[] buf = new byte[1024];
			int readByte = 0;
			int length = 0;
			byte[] imgBuf = null;

			FileInputStream fileInputStream = null;
			ByteArrayOutputStream outputStream = null;
			ServletOutputStream out = null;

			try {
				fileInputStream = new FileInputStream(imgFile);
				outputStream = new ByteArrayOutputStream();
				out = res.getOutputStream();

				while((readByte = fileInputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, readByte);
				}

				imgBuf = outputStream.toByteArray();
				length = imgBuf.length;
				out.write(imgBuf,0,length);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				outputStream.close();
				fileInputStream.close();
				out.close();
			}
		}
	}

	private String getPath(String basePath){
		String baseDir = "";
		return baseDir;
	}
}
	
