package com.lime.limeEduApi.framework.common.domain;

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
public class DefaultTableRow {
    @ApiModelProperty(notes = "식별번호", example = "1")
    private int seq;
    @ApiModelProperty(notes = "등록자 유저번호", example = "1")
    private int regSeq;
    @ApiModelProperty(notes = "등록자 유저명", example = "관리자")
    private String regNm;
    @ApiModelProperty(notes = "수정자 유저번호", example = "1")
    private int modSeq;
    @ApiModelProperty(notes = "수정자 유저명", example = "관리자")
    private String modNm;
    @ApiModelProperty(notes = "등록일", example = "2024-01-10 17:54:59.0")
    private String regDate;
    @ApiModelProperty(notes = "수정일", example = "2024-01-10 17:54:59.0")
    private String modDate;
    @ApiModelProperty(notes = "사용여부 (1-사용 0-미사용)", example = "1")
    private int useYn;
}
