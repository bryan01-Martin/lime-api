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
public class UserModReq {
    @ApiModelProperty(notes = "유저 번호", required = true)
    private int seq;
    @ApiModelProperty(notes = "유저 아이디",example = "userid", required = true)
    private String id;
    @ApiModelProperty(notes = "유저 이름", example = "유저1", required = true)
    private String name;
    @ApiModelProperty(notes = "유저 유형(1- 일반유저,2-관리자)", example = "1", required = true)
    private int type;
    @ApiModelProperty(notes = "사용 여부(0-미사용 1-사용)", example = "1", required = true)
    private int useYn;
    @ApiModelProperty(notes = "수정자 번호", hidden = true, required = true)
    private int modSeq;
}
