package com.lime.limeEduApi.api.login.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lime.limeEduApi.api.login.dao.LoginDaoEx;
import com.lime.limeEduApi.api.login.dto.LoginDto;
import com.lime.limeEduApi.api.login.service.LoginService;
import com.lime.limeEduApi.api.user.domain.UserLoginReq;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginDaoEx LoginDao;
    private final PasswordEncoder encoder;

    @Override
    public LoginDto getUserInfo(UserLoginReq userLoginReq){
        LoginDto loginDto = LoginDao.getUserInfo(userLoginReq);
        if(loginDto != null){
            if(encoder.matches(userLoginReq.getPassword(), loginDto.getPassword())){
                loginDto.setPassword(null);
                return loginDto;
            } else {
                throw new CustomException(ResponseCode.LOGIN_NOT_MATCHED);
            }
        } else {
            throw new CustomException(ResponseCode.LOGIN_NOT_MATCHED);
        }
    }
}
