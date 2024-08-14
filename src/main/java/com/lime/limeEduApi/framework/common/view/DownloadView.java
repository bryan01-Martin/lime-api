package com.lime.limeEduApi.framework.common.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.lime.limeEduApi.api.board.service.FileService;

import lombok.RequiredArgsConstructor;

//@Component
//public class DownloadView extends AbstractView {
//
//	@Autowired
//    FtpFileTransfer ftpFileTransfer;
//
//	@Override
//	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		HashMap<String,Object> fileInfo = (HashMap<String, Object>) model.get("file");
//        ftpFileTransfer.ftpFileDown( (String) fileInfo.get("FILE_CONV_NM"), (String) fileInfo.get("FILE_NM"), (String) fileInfo.get("FILE_PATH"),response);
//	}
//}
@Component
@RequiredArgsConstructor
public class DownloadView extends AbstractView {
	private final FileService fileService;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int seq = (int) model.get("seq");
		fileService.downloadFile(seq);
	}
}