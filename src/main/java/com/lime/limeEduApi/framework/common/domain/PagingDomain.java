package com.lime.limeEduApi.framework.common.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PagingDomain {
    @Schema(description = "시작 ROWNUM", example = "0", required = false, defaultValue = "1")
    private int start;
    @Schema(description = "갯수", example = "10", required = false, defaultValue = "10")
    private int cnt;
    @Schema(description = "정렬 할 컬럼", example = "", required = false, defaultValue = "")
    private String orderKey;
    @Schema(description = "정렬 ASC, DESC", example = "1", required = false, defaultValue = "1")
    private String orderBy;
}
