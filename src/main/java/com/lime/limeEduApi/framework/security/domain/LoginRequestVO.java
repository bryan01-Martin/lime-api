package com.lime.limeEduApi.framework.security.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestVO {
    @Schema(description = "유저아이디", example = "test", required = true)
    private String userId;
    @Schema(description = "비밀번호", example = "123123", required = true)
    private String password;
}