package com.lime.limeEduApi.framework.security.config;

import com.lime.limeEduApi.framework.security.filter.JwtFilter;
import com.lime.limeEduApi.framework.security.provider.TokenProvider;
import com.lime.limeEduApi.framework.security.service.LoginService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;
    private LoginService loginService;

    public JwtSecurityConfig(TokenProvider tokenProvider, LoginService loginService) {
        this.tokenProvider = tokenProvider;
        this.loginService = loginService;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider, loginService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}