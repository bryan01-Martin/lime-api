package com.lime.limeEduApi.framework.security.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class FindIdRequestVO {

    @NotNull
    @Schema(description = "회원유형(1-근로자 2-농장주 3-직원)", example = "1", required = true)
    private int type;

    @Schema(description = "농장주 이름", example = "가", required = true, hidden = true)
    private String firstName;
    @Schema(description = "농장주 성", example = "다", required = true, hidden = true)
    private String lastName;
    @Schema(description = "농장주 전화번호", example = "01099998888", required = true, hidden = true)
    private String phone;


    @Schema(description = "여권번호", example = "M12348581", required = true)
    private String passport;

}
