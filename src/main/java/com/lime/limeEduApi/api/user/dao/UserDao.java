package com.lime.limeEduApi.api.user.dao;

import com.lime.limeEduApi.api.user.domain.*;
import com.lime.limeEduApi.api.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserDao {
    public UserDto getUserInfo(UserInfoReq userInfoReq);
    public List<UserDto> getUserInfoList(UserInfoListReq userInfoListReq);
    public int getUserInfoListCnt(UserInfoListReq userInfoListReq);
    public int regUser(UserAddReq userAddReq);
    public int modUser(UserModReq userModReq);
    public int changePassword(UserDto userDto);
    public int resetPassword(UserDto userDto);
    public Optional<Integer> checkUserId(CheckUserIdReq checkUserIdReq);

}
