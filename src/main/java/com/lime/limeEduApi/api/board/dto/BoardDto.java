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
public class BoardDto extends DefaultTableRow {
	@ApiModelProperty(notes = "게시판 이름")
	private String name;
	@ApiModelProperty(notes = "게시판 타입")
	private int type;
}
