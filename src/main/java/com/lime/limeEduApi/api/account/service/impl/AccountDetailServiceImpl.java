package com.lime.limeEduApi.api.account.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lime.limeEduApi.api.account.dao.AccountDetailDao;
import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;
import com.lime.limeEduApi.api.account.service.AccountDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDetailServiceImpl implements AccountDetailService {
	private final AccountDetailDao accountDetailDao;
	
	public List<AccountDto> getAccountDetailInfo(AccountInfoReq accountInfoReq) {
		return accountDetailDao.getAccountDetailInfo(accountInfoReq);
	}
	
	public int regAccountDetail(AccountDto accountDto) {
		return accountDetailDao.regAccountDetail(accountDto);
	}
	
	public int modAccountDetail (AccountDto accountDto) {
		return accountDetailDao.modAccountDetail(accountDto);
	}
	
	public int delAccountDetail (AccountDto accountDto) {
		return accountDetailDao.delAccountDetail(accountDto);
	}
}
