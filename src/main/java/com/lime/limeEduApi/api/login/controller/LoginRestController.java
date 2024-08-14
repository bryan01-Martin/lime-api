package com.lime.limeEduApi.api.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lime.limeEduApi.api.login.dto.LoginDto;
import com.lime.limeEduApi.api.login.service.LoginService;
import com.lime.limeEduApi.api.user.domain.UserLoginReq;
import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.controller.BaseController;
import com.lime.limeEduApi.framework.common.domain.ResponseDomain;
import com.lime.limeEduApi.framework.common.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
@RequestMapping(Const.API.LOGIN)
@RestController
@RequiredArgsConstructor
@Api(tags = {"로그인 API"})  // Swagger 최상단 Controller 명칭
public class LoginRestController extends BaseController {

    private final LoginService loginService;
    private final MessageSource messageSource;

    @PostMapping("/authenticate")
    @ApiOperation(value = "로그인", notes = "로그인")
    public ResponseDomain<Boolean> getUserInfo(@RequestBody UserLoginReq UserLoginReq, HttpServletRequest request){
        HttpSession session = request.getSession();
        LoginDto loginDto = loginService.getUserInfo(UserLoginReq);
        session.setAttribute(Const.SESSION.LOGIN_MEMBER, loginDto);

        return ResponseDomain.<Boolean>builder()
                    .data(true)
                    .code(ResponseCode.LOGIN_SUCCESS.getCode())
                    .message(messageSource.getMessage(ResponseCode.LOGIN_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }

}