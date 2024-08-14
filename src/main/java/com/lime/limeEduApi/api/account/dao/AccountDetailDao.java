package com.lime.limeEduApi.api.account.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;

@Mapper
public interface AccountDetailDao {
	public List<AccountDto> getAccountDetailInfo(AccountInfoReq accountInfoReq);
	public int regAccountDetail(AccountDto accountDto);
	public int modAccountDetail (AccountDto accountDto);
	public int delAccountDetail (AccountDto accountDto);
}
