package com.lime.limeEduApi.api.code.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeListByDepthReqDomain {
    @ApiModelProperty(notes = "코드 깊이", example = "1")
    private int depth;
}
