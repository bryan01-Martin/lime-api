package com.lime.limeEduApi.api.user.dto;

import com.lime.limeEduApi.framework.common.domain.DefaultTableRow;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends DefaultTableRow {
    @ApiModelProperty(notes = "유저 ID", example = "MASTER")
    private String userId;
    @ApiModelProperty(notes = "비밀번호", example = "1234", hidden = true)
    private String password;
    @ApiModelProperty(notes = "유저 이름", example = "관리자")
    private String name;
    @ApiModelProperty(notes = "권한(1-일반유저, 2-관리자)", example = "1")
    private int type;
}
