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
public class CodeListReqDomain {
    @ApiModelProperty(notes = "상위코드",example = "")
    private String category;
}
