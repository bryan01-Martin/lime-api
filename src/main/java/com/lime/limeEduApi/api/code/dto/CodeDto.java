package com.lime.limeEduApi.api.code.dto;

import com.lime.limeEduApi.framework.common.domain.DefaultTableRow;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto extends DefaultTableRow {
    @ApiModelProperty(notes = "코드")
    private String code;
    @ApiModelProperty(notes = "상위코드")
    private String category;
    @ApiModelProperty(notes = "코드명")
    private String comKor;
    @ApiModelProperty(notes = "추가사항")
    private String subCom;
    @ApiModelProperty(notes = "코드깊이")
    private int depth;
}
