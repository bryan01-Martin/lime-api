package com.lime.limeEduApi.framework.security.domain;

import com.lime.limeEduApi.api.user.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends User {
    private UserDto userDto;

    public UserAdapter(UserDto userDto) {
        super(userDto.getUserId().toString(), userDto.getPassword(), authorities(userDto.getType()));
        this.userDto = userDto;
    }

    public UserDto getAccount() {
        return this.userDto;
    }

    private static List<GrantedAuthority> authorities(int type) {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        String role = "";
        switch(type){ /* 1-유저 2-관리자 */
            case 1:
                role = "ROLE_USER";
                break;
            case 2:
                role = "ROLE_ADMIN";
                break;
        }
        list.add(new SimpleGrantedAuthority(role));
        return list;
    }
}