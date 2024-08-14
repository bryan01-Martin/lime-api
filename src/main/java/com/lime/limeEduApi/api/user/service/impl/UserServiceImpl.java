package com.lime.limeEduApi.api.user.service.impl;

import com.lime.limeEduApi.api.user.dao.UserDao;
import com.lime.limeEduApi.api.user.domain.*;
import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.api.user.service.UserService;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final String DEFAULT_PASSWORD = "a12345678";
    @Override
    public UserDto getUserInfo(UserInfoReq userInfoReq){
        return userDao.getUserInfo(userInfoReq);
    }
    
    @Override
    public List<UserDto> getUserInfoList(UserInfoListReq userInfoListReq){
        return userDao.getUserInfoList(userInfoListReq);
    }
    
    @Override
    public int getUserInfoListCnt(UserInfoListReq userInfoListReq){
        return userDao.getUserInfoListCnt(userInfoListReq);
    }
    
    @Override
    public int regUser(UserAddReq userAddReq){
        userAddReq.setPassword(encoder.encode(DEFAULT_PASSWORD));
        int result = userDao.regUser(userAddReq);
        if(result != 1){
            throw new CustomException(ResponseCode.INSERT_FAILED);
        }
        return 1;
    }
    
    @Override
    public int modUser(UserModReq userModReq){
        int result = userDao.modUser(userModReq);
        if(result != 1){
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
        return 1;
    }
    
    @Override
    public int changePassword(UserDto userDto){
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        int result = userDao.changePassword(userDto);
        if(result != 1){
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
        return 1;
    }
    
    @Override
    public int resetPassword(UserDto userDto){
        userDto.setPassword(encoder.encode(DEFAULT_PASSWORD));
        int result = userDao.changePassword(userDto);
        if(result != 1){
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
        return 1;
    }

    @Override
    public boolean checkUserId(CheckUserIdReq checkUserIdReq) {
        return userDao.checkUserId(checkUserIdReq).get() == 0;
    }


}
