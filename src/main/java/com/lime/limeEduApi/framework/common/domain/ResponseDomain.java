package com.lime.limeEduApi.framework.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDomain<T> {
    @ApiModelProperty(notes = "응답코드", example = "200")
    private String code;
    @ApiModelProperty(notes = "결과", example = "success")
    private String message;
    @ApiModelProperty(notes = "결과 목록")
    private List<T> list;
    @ApiModelProperty(notes = "결과수")
    private int totalCnt;
    @ApiModelProperty(notes = "결과")
    private T data;
}
