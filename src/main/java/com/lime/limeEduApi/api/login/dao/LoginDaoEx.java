package com.lime.limeEduApi.api.login.dao;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.login.dto.LoginDto;
import com.lime.limeEduApi.api.user.domain.UserLoginReq;

@Mapper
public interface LoginDaoEx {
    public LoginDto getUserInfo(UserLoginReq UserLoginReq);
}
