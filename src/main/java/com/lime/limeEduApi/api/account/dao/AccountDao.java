package com.lime.limeEduApi.api.account.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;

@Mapper
public interface AccountDao {
	public List<AccountDto> getAccountList(AccountDto accountDto);
	public int getAccountListCnt(AccountDto accountDto);
	public AccountDto getAccountInfo(AccountInfoReq accountInfoReq);
	public int regAccount(AccountDto accountDto);
	public int modAccount(AccountDto accountDto);
}
