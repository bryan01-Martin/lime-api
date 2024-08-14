package com.lime.limeEduApi.api.login.service;

import com.lime.limeEduApi.api.login.dto.LoginDto;
import com.lime.limeEduApi.api.user.domain.UserLoginReq;

public interface LoginService {
    public LoginDto getUserInfo(UserLoginReq UserLoginReq);
}
