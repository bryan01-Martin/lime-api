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
public class FileDto extends DefaultTableRow{
	@ApiModelProperty(notes = "원본 이름")
	private String originalFileName;
	@ApiModelProperty(notes = "변환 이름")
	private String convertFileName;
	@ApiModelProperty(notes = "파일 크기")
	private String fileSize;
	@ApiModelProperty(notes = "파일 번호")
	private int contentSeq;
}
