package com.lime.limeEduApi.api.board.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.lime.limeEduApi.api.board.dao.FileDao;
import com.lime.limeEduApi.api.board.dto.ContentDto;
import com.lime.limeEduApi.api.board.dto.FileDto;
import com.lime.limeEduApi.api.board.service.FileService;
import com.lime.limeEduApi.framework.common.util.CommonUtil;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileDao fileDao;
	private final String ROOT_PATH = "D:/FTP/";

	@Override
	public FileDto selectFile(int seq) {
		return fileDao.selectFile(seq);
	}

	@Override
	public int insertFile(FileDto fileDto) {
		return fileDao.insertFile(fileDto);
	}

	@Override
	public int deleteFile(FileDto fileDto) {
		return fileDao.deleteFile(fileDto);
	}

	@Override
	public int updateFile(ContentDto contentDto, MultipartFile mfile) throws IOException {

		Files.createDirectories(Path.of(ROOT_PATH));
		String original = mfile.getOriginalFilename();
		String ext = original.substring(original.lastIndexOf("."));
		String convert = UUID.randomUUID().toString() + ext;
		File file = new File(ROOT_PATH + convert);
		mfile.transferTo(file);

		insertFile(FileDto.builder()
				.contentSeq(contentDto.getSeq())
				.originalFileName(original)
				.convertFileName(convert)
				.fileSize(mfile.getSize() + "")
				.regSeq(contentDto.getRegSeq())
				.modSeq(contentDto.getRegSeq())
			.build());

		return 0;
	}

	@Override
	public int downloadFile(int seq) throws IOException {
		FileDto fileDto = selectFile(seq);
		File file = new File(ROOT_PATH + fileDto.getConvertFileName());
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		response.setHeader("Access-Control-Expose-Headers", "*");
		response.setContentType("application/octet-stream");
		response.setContentLength(Integer.parseInt(fileDto.getFileSize()));
		response.setHeader("Content-Disposition",
				"attachment; fileName=\"" + URLEncoder.encode(fileDto.getOriginalFileName(), "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("filename", URLEncoder.encode(fileDto.getOriginalFileName(), "UTF-8"));
		FileUtils.copyFile(file, response.getOutputStream());

		return 0;
	}
}
