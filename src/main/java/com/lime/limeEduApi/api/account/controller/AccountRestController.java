package com.lime.limeEduApi.api.account.controller;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lime.limeEduApi.api.account.dto.AccountDto;
import com.lime.limeEduApi.api.account.dto.AccountInfoReq;
import com.lime.limeEduApi.api.account.service.AccountDetailService;
import com.lime.limeEduApi.api.account.service.AccountService;
import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.annotaion.User;
import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.domain.ResponseDomain;
import com.lime.limeEduApi.framework.common.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping(Const.API.ACCOUNT)
@RestController
@RequiredArgsConstructor
@Api(tags = {"회계 API"})  // Swagger 최상단 Controller 명칭
public class AccountRestController {

	private final AccountService accountService;
	private final AccountDetailService accountDetailService;
	private final MessageSource messageSource;
	
	@PostMapping("/getAccountInfo")
    @ApiOperation(value = "회계 정보 조회", notes = "회계 정보 조회")
    public ResponseDomain<AccountDto> getAccountInfo(@RequestBody AccountInfoReq accountInfoReq){
        return ResponseDomain.<AccountDto>builder()
                    .data(accountService.getAccountInfo(accountInfoReq))
                    .list(accountDetailService.getAccountDetailInfo(accountInfoReq))
                    .code(ResponseCode.SEARCH_SUCCESS.getCode())
                    .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }
	
    @PostMapping("/getAccountList")
    @ApiOperation(value = "회계 목록 조회", notes = "회계 목록 조회")
    public ResponseDomain<AccountDto> getAccountList(@RequestBody AccountDto accountDto){
        return ResponseDomain.<AccountDto>builder()
                .list(accountService.getAccountList(accountDto))
                .totalCnt(accountService.getAccountListCnt(accountDto))
                .code(ResponseCode.SEARCH_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
            .build();
    }
    
    @PostMapping("/regAccount")
    @ApiOperation(value = "회계 등록", notes = "회계 등록")
    public ResponseDomain<Integer> regAccount(@RequestBody AccountDto accountDto, @User@ApiIgnore UserDto userDto){
    	accountDto.setRegSeq(userDto.getSeq());
    	accountDto.setModSeq(userDto.getSeq());
    	accountService.regAccount(accountDto);
        return ResponseDomain.<Integer>builder()
                    .data(accountDto.getSeq())
                    .code(ResponseCode.INSERT_SUCCESS.getCode())
                    .message(messageSource.getMessage(ResponseCode.INSERT_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }
    
    @PostMapping("/modAccount")
    @ApiOperation(value = "회계 수정", notes = "회계 수정")
    public ResponseDomain<Integer> modAccount(@RequestBody AccountDto accountDto, @User@ApiIgnore UserDto userDto){
    	accountDto.setModSeq(userDto.getSeq());
    	accountService.modAccount(accountDto);
    	return ResponseDomain.<Integer>builder()
    			.data(accountDto.getSeq())
    			.code(ResponseCode.INSERT_SUCCESS.getCode())
    			.message(messageSource.getMessage(ResponseCode.INSERT_SUCCESS.getKey(), null, CommonUtil.getLocale()))
    			.build();
    }

}
