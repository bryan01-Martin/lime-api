package com.lime.limeEduApi.api.user.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckUserIdReq {
    @ApiModelProperty(notes = "유저 아이디", example = "user")
    private String userId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequestVO {
        @Schema(description = "유저 번호", example = "", required = true)
        private Long seq;
        @Schema(description = "비밀번호", example = "", required = true)
        private String password;
    }
}
