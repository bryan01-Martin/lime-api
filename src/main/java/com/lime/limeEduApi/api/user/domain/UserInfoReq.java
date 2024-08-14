package com.lime.limeEduApi.api.user.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoReq {
    @ApiModelProperty(notes = "유저번호", example = "1", required = true)
    private int seq;
}