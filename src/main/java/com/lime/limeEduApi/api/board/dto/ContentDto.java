package com.lime.limeEduApi.api.board.dto;

import com.lime.limeEduApi.framework.common.domain.DefaultTableRow;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto extends DefaultTableRow {
	@ApiModelProperty(notes = "게시판 제목")
	private String title;
	@ApiModelProperty(notes = "게시판 내용")
	private String content;
	@ApiModelProperty(notes = "게시판 번호")
	private int boardSeq;
	@ApiModelProperty(notes = "게시판 타입")
	private int type;
	@ApiModelProperty(notes = "파일 이름")
	private String fileName;
	@ApiModelProperty(notes = "파일 번호")
	private int fileSeq;
}
