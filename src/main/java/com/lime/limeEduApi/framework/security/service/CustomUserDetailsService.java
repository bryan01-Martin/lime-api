package com.lime.limeEduApi.framework.security.service;

import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.security.dao.LoginDao;
import com.lime.limeEduApi.framework.security.domain.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final LoginDao loginDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        UserDto userDto = loginDao.selectUser(com.lime.limeEduApi.api.user.dto.UserDto.builder().userId(username).build()).get();
        if(userDto == null ) throw new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");

        if(userDto.getUseYn() == 0) throw new RuntimeException(userDto.getUserId() + " -> 활성화되어 있지 않습니다.");
        return new UserAdapter(userDto);
    }
}