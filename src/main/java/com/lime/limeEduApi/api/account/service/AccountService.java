package com.lime.limeEduApi.api.account.service;

import java.util.List;

import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;

public interface AccountService {
	public List<AccountDto> getAccountList(AccountDto accountDto);
	public int getAccountListCnt(AccountDto accountDto);
	public AccountDto getAccountInfo(AccountInfoReq accountInfoReq);
	public int regAccount(AccountDto accountDto);
	public int modAccount(AccountDto accountDto);
}
