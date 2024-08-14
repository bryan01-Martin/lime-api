package com.lime.limeEduApi.api.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDto {
    @ApiModelProperty(notes = "유저 번호", example = "1")
    private int seq;
    @ApiModelProperty(notes = "유저 ID", example = "MASTER")
    private String userId;
    @ApiModelProperty(notes = "비밀번호", example = "1234", hidden = true)
    private String password;
    @ApiModelProperty(notes = "유저 이름", example = "관리자")
    private String name;
    @ApiModelProperty(notes = "유저 유형(1-일반유저 2-관리자)", example = "1")
    private int type;
}
