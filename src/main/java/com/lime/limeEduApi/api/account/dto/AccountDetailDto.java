package com.lime.limeEduApi.api.account.dto;

import java.util.List;

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
public class AccountDetailDto extends DefaultTableRow {
	@ApiModelProperty(notes = "수익/비용")
	private String profitComkr;
	@ApiModelProperty(notes = "관")
	private String bigComkr;
	@ApiModelProperty(notes = "항")
	private String middleComkr;
	@ApiModelProperty(notes = "목")
	private String smallComkr;
	@ApiModelProperty(notes = "과")
	private String detailComkr;
	@ApiModelProperty(notes = "검색 수익/비용")
	private String profitCost;
	@ApiModelProperty(notes = "검색 관")
	private String bigGroup;
	@ApiModelProperty(notes = "검색 항")
	private String middleGroup;
	@ApiModelProperty(notes = "검색 목")
	private String smallGroup;
	@ApiModelProperty(notes = "검색 과")
	private String detailGroup;
	@ApiModelProperty(notes = "회계 상세번호")
	private int dtlSeq;
	@ApiModelProperty(notes = "회계 번호")
	private int accountSeq;
	@ApiModelProperty(notes = "상세 내용")
	private String comments;
	@ApiModelProperty(notes = "상세 개수")
	private int detailCnt;
	@ApiModelProperty(notes = "비용")
	private double transactionMoney;
	@ApiModelProperty(notes = "비용 합계")
	private double totalMoney;
	@ApiModelProperty(notes = "회계 최소금액")
	private Integer min;
	@ApiModelProperty(notes = "회계 최대금액")
	private Integer max;
	@ApiModelProperty(notes = "상세 최소금액")
	private Integer sumMin;
	@ApiModelProperty(notes = "상세 최대금액")
	private Integer sumMax;
	@ApiModelProperty(notes = "발생일")
	private String transactionDate;
	private String mode;
    
    private List<AccountDetailDto> dtoList;
}