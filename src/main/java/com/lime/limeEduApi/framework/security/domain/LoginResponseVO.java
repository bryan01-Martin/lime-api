package com.lime.limeEduApi.framework.security.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVO {
    @Schema(description = "액세스 토큰")
    private String accessToken;
    @Schema(description = "리프레시 토큰")
    private String refreshToken;
    @Schema(description = "권한")
    private String role;
    @Schema(description = "유저번호")
    private String userSeq;
}