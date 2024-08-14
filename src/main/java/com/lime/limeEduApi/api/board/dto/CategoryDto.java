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
public class CategoryDto extends DefaultTableRow{
	@ApiModelProperty(notes = "카테고리 제목")
	private String name;
	@ApiModelProperty(notes = "카테고리 타입")
	private String type;
}
