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
public class UserAddReq {
    @ApiModelProperty(notes = "유저 번호", hidden = true)
    private int seq;
    @ApiModelProperty(notes = "유저 아이디",example = "userid")
    private String userId;
    @ApiModelProperty(notes = "유저 비밀번호",example = "userid", hidden = true)
    private String password;
    @ApiModelProperty(notes = "유저 이름", example = "유저1")
    private String name;
    @ApiModelProperty(notes = "유저 유형(1- 일반유저,2-관리자)", example = "1")
    private int type;
    @ApiModelProperty(notes = "등록자 번호", hidden = true)
    private int regSeq;
}
