package com.lime.limeEduApi.framework.security.dao;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.user.dto.UserDto;

@Mapper
public interface LoginDao {

    public Optional<UserDto> selectUser(UserDto userDto);

}
