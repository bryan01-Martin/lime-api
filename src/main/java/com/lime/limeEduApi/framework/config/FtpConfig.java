package com.lime.limeEduApi.framework.config;
import java.io.BufferedInputStream;
/**
 * 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FtpConfig {

		private String host;
		private Integer port;
		private String rootPath;
		private String user;
		private String password;


	private FTPClient client;
	    
		private static Logger logger = LoggerFactory.getLogger(FtpConfig.class);

		private FTPClient createClient() throws Exception {
			client = new FTPClient();
			try {
				// connection 환경에서 UTF-8의 인코딩 타입을 사용한다.
				client.setControlEncoding("UTF-8");
				// ftp://localhost에 접속한다.
				client.connect(host, port);
				// 접속을 확인한다.
				int resultCode = client.getReplyCode();
				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.enterLocalPassiveMode();
				
				// 접속시 에러가 나오면 콘솔에 에러 메시지를 표시하고 프로그램을 종료한다.
				if (!FTPReply.isPositiveCompletion(resultCode)) {
					System.out.println("FTP server refused connection.!");
					return null;
				} else {
					// 파일 전송간 접속 딜레이 설정 (1ms 단위기 때문에 1000이면 1초)
					client.setSoTimeout(1000);
					// 로그인을 한다.
					if (!client.login(user, password)) {
						// 로그인을 실패하면 프로그램을 종료한다.
						System.out.println("Login Error!");
						return null;
					}
				}
			} catch (Throwable e) {
				disconnect();
			}
			return client;	
		}
		
		private void disconnect() throws Exception{
			// ftp를 로그아웃한다.
			try {
				client.logout();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// ftp 커넥션이 연결되어 있으면 종료한다.
				try {
					if (client.isConnected()) {
						client.disconnect();
					}
				} catch (Exception e) {
					throw e;
				}
			}
		}
	    
	    public File getFile(String targetPath, String fileName) throws Exception {
	    	if(fileName.indexOf("/") != -1) {
		    	targetPath += "/"+fileName.split("/")[0];
		    	fileName = fileName.split("/")[1];
	    	}
	    	return this.downloadFile(targetPath, fileName);
	    }

	    private File downloadFile(String targetPath, String fileName) throws Exception {
	        try {
	        	this.createClient();
    			client.changeWorkingDirectory(rootPath+targetPath); //change directory
    			InputStream input = client.retrieveFileStream(fileName);
	            return this.convertInputStreamToFile(input);
	        } catch (Exception e) {
	            throw new Exception("Download File failure");
	        } finally {
	            this.disconnect();
	        }
	    }
	    
	    private File convertInputStreamToFile(InputStream in) throws Exception {
	        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
	        tempFile.deleteOnExit(); // 임시파일 VM종료시 파일삭제
	        FileUtils.copyInputStreamToFile(in, tempFile);
	        return tempFile;
	    }
	    
	    private File convertToFile(MultipartFile mfile,String fileName) throws IOException {
			File convertFile = File.createTempFile(String.valueOf(hashCode()), fileName);
			convertFile.deleteOnExit(); // 임시파일 VM종료시 파일삭제
	    	mfile.transferTo(convertFile);
	    	return convertFile;
	    }
	    
	    public boolean uploadFile(String targetPath, String fileName, MultipartFile file) throws Exception {
	    	if(fileName.indexOf("/") != -1) {
		    	targetPath += fileName.split("/")[0];
		    	fileName = fileName.split("/")[1];
	    	}
	        return this.uploadFile(targetPath, fileName, this.convertToFile(file, fileName));
	    }
	    
	    private boolean uploadFile(String targetPath, String fileName, File file) throws Exception {
	    	boolean isSuccess = false;
	    	BufferedInputStream fi = null;
	    	try {
	    		this.createClient();
	    		System.out.println(file.length());
    			fi = new BufferedInputStream(new FileInputStream(file));
    			makeDir(targetPath);
				// FTPClient의 staoreFile함수로 보내면 업로드가 이루어 진다.
	    		client.changeWorkingDirectory(rootPath+targetPath);
	    		client.setFileType(FTPClient.BINARY_FILE_TYPE);
				//storeFile Method는 파일 송신결과를 boolean값으로 리턴합니다
	    		isSuccess = client.storeFile(fileName, fi);
				if(isSuccess) {
					logger.debug("업로드 성공");
				} else {
					logger.debug("업로드 실패");
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				file.delete(); // 임시파일 삭제
				if(fi!=null) {
					try {
						fi.close();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				disconnect();
			}
	    	return isSuccess;
	    }
	    
	    public boolean delteFile(String filePath, String fileName) throws Exception{
	    	if(fileName.indexOf("/") != -1) {
		    	filePath += fileName.split("/")[0];
		    	fileName = fileName.split("/")[1];
	    	}
	    	return this.deleteFile(filePath, fileName);
	    }
	    
	    private boolean deleteFile(String filePath, String fileName) throws Exception {
	    	this.createClient();
	    	try {
	    		client.changeWorkingDirectory(rootPath+filePath);
	    		client.deleteFile(fileName);
	    		return true;
	    	} catch (Exception e) {
	    		throw new Exception("Delete File failure");
	    	} finally {
	    		this.disconnect();
	    	}
	    }
	    
	    private void makeDir(String filePath) throws IOException {
	    	String[] filePaths = filePath.split("/");
	    	String dirs = rootPath;
	    	client.changeWorkingDirectory(rootPath);
	    	for(int i = 0; i < filePaths.length; i++) {
	    		String dir = filePaths[i];
	    		if(!dir.equals("")) {
		    		client.makeDirectory(dir);
		    		if(i != 0 ) {
		    			dirs += "/";
		    		}
		    		dirs += dir;
		    		client.changeWorkingDirectory(dir);
	    		}
	    	}
	    }
	    
}

