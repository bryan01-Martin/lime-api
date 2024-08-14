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
public class UserLoginReq {
    @ApiModelProperty(notes = "유저 아이디",example = "userId")
    private String userId;
    @ApiModelProperty(notes = "유저 비밀번호",example = "password", hidden = true)
    private String password;
}
