package com.lime.limeEduApi.api.board.domain;

import com.lime.limeEduApi.framework.common.domain.PagingDomain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardContentListReq extends PagingDomain{
	@ApiModelProperty(notes = "검색 키워드 게시판 제목")
	private String title;
	@ApiModelProperty(notes = "검색 키워드 게시판 내용")
	private String content;
	@ApiModelProperty(notes = "검색 키워드 게시판 제목+내용")
	private String integrated;
	@ApiModelProperty(notes = "검색 키워드 게시판 작성자")
	private String regNm;
	@ApiModelProperty(notes = "검색 키워드 게시판 내용")
	private String keyword;
	@ApiModelProperty(notes = "검색 키워드 게시판 내용")
	private String keyOption;	
	@ApiModelProperty(notes = "게시판 번호")
	private int boardSeq;
}
