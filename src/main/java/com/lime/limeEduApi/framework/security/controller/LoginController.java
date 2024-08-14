package com.lime.limeEduApi.framework.security.controller;

import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.domain.ResponseDomain;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import com.lime.limeEduApi.framework.security.domain.LoginRequestVO;
import com.lime.limeEduApi.framework.security.domain.LoginResponseVO;
import com.lime.limeEduApi.framework.security.domain.ReIssueResponseVO;
import com.lime.limeEduApi.framework.security.domain.ResponseLogin;
import com.lime.limeEduApi.framework.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"로그인 API"})  // Swagger 최상단 Controller 명칭
public class LoginController {
    private final LoginService loginService;
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final MessageSource messageSource;

    @ApiOperation(value = "로그인시도", notes = "로그인 시도")
    @PostMapping("/authenticate") // Account 인증 API
    public ResponseDomain<LoginResponseVO> authorize(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     @RequestBody LoginRequestVO loginVO
    ) {
        LoginResponseVO loginResponse = null;
        loginResponse = loginService.authenticate(request, response, loginVO);
        return ResponseDomain.<LoginResponseVO>builder()
                .code(ResponseCode.LOGIN_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.LOGIN_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .data(loginResponse)
            .build();
    }

    @ApiOperation(value = "토큰 재발행", notes = "토큰 재발행")
    @PostMapping("/reIssue")
    public ResponseDomain<ReIssueResponseVO> reIssue(HttpServletRequest request, HttpServletResponse response) {
        ReIssueResponseVO result = loginService.reIssue();
        if(result.getAccessToken() == null || result.getRefreshToken() == null){
            throw new CustomException(ResponseCode.REISSUE_FAILED);
        }
        return ResponseDomain.<ReIssueResponseVO>builder()
                .code(ResponseCode.REISSUE_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.REISSUE_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .data(loginService.reIssue())
                .build();
    }
    @ApiOperation(value = "로그아웃", notes = "로그아웃")
    @PostMapping("/logout")
    public ResponseDomain<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        Boolean result = loginService.logout(request, response);
        return ResponseDomain.<Boolean>builder()
                .code(ResponseCode.LOGOUT_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.LOGOUT_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .data(result)
            .build();
    }
}

