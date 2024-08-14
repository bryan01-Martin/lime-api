package com.lime.limeEduApi.api.account.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lime.limeEduApi.api.account.dao.AccountDao;
import com.lime.limeEduApi.api.account.dao.AccountDetailDao;
import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;
import com.lime.limeEduApi.api.account.service.AccountDetailService;
import com.lime.limeEduApi.api.account.service.AccountService;
import com.lime.limeEduApi.framework.common.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountDao accountDao;
	private final AccountDetailDao accountDetailDao;
	private final AccountDetailService accountDetailService;
	
	@Override
	public List<AccountDto> getAccountList(AccountDto accountDto){
		return accountDao.getAccountList(accountDto);
	}
	
	@Override
	public int getAccountListCnt(AccountDto accountDto) {
		return accountDao.getAccountListCnt(accountDto);
	}
	
	@Override
	public AccountDto getAccountInfo(AccountInfoReq accountInfoReq) {
		return accountDao.getAccountInfo(accountInfoReq);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int regAccount(AccountDto accountDto) {
		accountDao.regAccount(accountDto);
//		System.out.println(accountDto.getDtoList());
//		System.out.println(accountDto);
		if (accountDto.getDtoList() != null) {
			for (AccountDto dto : accountDto.getDtoList()) {
				dto.setAccountSeq(accountDto.getSeq());
				dto.setRegSeq(accountDto.getRegSeq());
				dto.setModSeq(accountDto.getRegSeq());
				accountDetailService.regAccountDetail(dto);
			}
		}
		return 1; 
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int modAccount(AccountDto accountDto) {
		accountDao.modAccount(accountDto);
		if (accountDto.getDtoList() != null) {
			for (AccountDto dto : accountDto.getDtoList()) {
				dto.setModSeq(accountDto.getModSeq());
				if(dto.getMode().equals("I")) {
					dto.setAccountSeq(accountDto.getSeq());
					dto.setRegSeq(accountDto.getRegSeq());
					accountDetailService.regAccountDetail(dto);
				} else if(dto.getMode().equals("U")) {
					accountDetailService.modAccountDetail(dto);
				}else if(dto.getMode().equals("D")) {
					accountDetailService.delAccountDetail(dto);
				}
			}
		}
		return 1;
	}
}

