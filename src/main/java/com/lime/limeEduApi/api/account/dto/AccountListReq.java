package com.lime.limeEduApi.api.account.dto;

import java.util.List;

import com.lime.limeEduApi.framework.common.domain.PagingDomain;

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
public class AccountListReq extends PagingDomain{
	private AccountDto dto;
	private List<AccountDto> dtoList;
}
