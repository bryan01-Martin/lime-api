package com.lime.limeEduApi.framework.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lime.limeEduApi.api.login.dto.LoginDto;
import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.security.domain.LoginResponseVO;

public class BaseController {
    public int getLoginUserSeq(){
        return getLoginUserInfo().getSeq();
    }
    public LoginDto getLoginUserInfo(){
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        if(req.getSession() == null){
            throw new CustomException(ResponseCode.LOGIN_NEED);
        }
        System.out.println("LoginDto :::::::::::::" + req);
        System.out.println("LoginDto :::::::::::::" + (LoginResponseVO) req.getSession().getAttribute("loginResponseVO"));
        Object user = req.getSession().getAttribute(Const.SESSION.LOGIN_MEMBER);
        Object user2 = req.getCookies();
        System.out.println(user2);
        if(user == null){
            throw new CustomException(ResponseCode.LOGIN_NEED);
        }
        return (LoginDto) user;
    }
}