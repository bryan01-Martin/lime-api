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
public class UserPasswordResetReq {
    @ApiModelProperty(notes = "대상 유저 번호", example = "1")
    private int seq;
    @ApiModelProperty(notes = "수정자", hidden = true)
    private int modSeq;
}
