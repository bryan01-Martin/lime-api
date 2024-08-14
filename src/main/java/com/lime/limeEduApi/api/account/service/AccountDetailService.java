package com.lime.limeEduApi.api.account.service;

import java.util.List;

import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;

public interface AccountDetailService {
	public List<AccountDto> getAccountDetailInfo(AccountInfoReq accountInfoReq);
	public int regAccountDetail(AccountDto accountDto);
	public int modAccountDetail (AccountDto accountDto);
	public int delAccountDetail (AccountDto accountDto);
}
