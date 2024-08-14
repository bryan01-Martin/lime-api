package com.lime.limeEduApi.api.user.service;


import com.lime.limeEduApi.api.user.domain.*;
import com.lime.limeEduApi.api.user.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto getUserInfo(UserInfoReq userInfoReq);

    public List<UserDto> getUserInfoList(UserInfoListReq userInfoListReq);

    public int getUserInfoListCnt(UserInfoListReq userInfoListReq);

    public int regUser(UserAddReq userAddReq);

    public int modUser(UserModReq userModReq);

    public int changePassword(UserDto userDto);
    public int resetPassword(UserDto userDto);
    public boolean checkUserId(CheckUserIdReq checkUserIdReq);
}
