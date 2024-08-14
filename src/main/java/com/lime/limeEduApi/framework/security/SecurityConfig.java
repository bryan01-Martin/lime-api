package com.lime.limeEduApi.framework.security;

import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.security.config.JwtSecurityConfig;
import com.lime.limeEduApi.framework.security.filter.JwtAccessDeniedHandler;
import com.lime.limeEduApi.framework.security.filter.JwtAuthenticationEntryPoint;
import com.lime.limeEduApi.framework.security.provider.TokenProvider;
import com.lime.limeEduApi.framework.security.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration // EnableWebSecurity에 들어있지만 인텔리 오류방지..
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션 사용을 위해 선언
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final LoginService loginService;

    // BCryptPasswordEncoder 라는 패스워드 인코더 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/images/**", "/js/**", "/css/**", "/font/**", "/swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()// token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 세션을 사용하지 않기 때문에 STATELESS로 설정
                // api 경로
                .and()
                .authorizeRequests()
                .antMatchers(tokenProvider.PERMIT_URL_ARRAY).permitAll()
                .antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN")
                .anyRequest().authenticated() // 나머지 경로는 jwt 인증 해야함
                .and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .apply(new JwtSecurityConfig(tokenProvider, loginService)); // JwtSecurityConfig 적용
    }
}