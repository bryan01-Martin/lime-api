package com.lime.limeEduApi.framework.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import com.lime.limeEduApi.framework.security.dao.LoginDao;
import com.lime.limeEduApi.framework.security.domain.LoginRequestVO;
import com.lime.limeEduApi.framework.security.domain.LoginResponseVO;
import com.lime.limeEduApi.framework.security.domain.ReIssueResponseVO;
import com.lime.limeEduApi.framework.security.provider.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LoginDao loginDao;
    private final String refreshAdditionalStr = "refreshToken_";
    private final ObjectMapper objectMapper;

    public LoginService(TokenProvider tokenProvider,
                        AuthenticationManagerBuilder authenticationManagerBuilder,
                        LoginDao loginDao) {
        this.tokenProvider=tokenProvider;
        this.authenticationManagerBuilder=authenticationManagerBuilder;
        this.loginDao = loginDao;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }



    // username 과 패스워드로 사용자를 인증하여 액세스토큰을 반환한다.
    public LoginResponseVO authenticate(HttpServletRequest request, HttpServletResponse response, LoginRequestVO loginVO) {
        UserDto userDto = UserDto.builder()
                .userId(loginVO.getUserId())
                .password(loginVO.getPassword())
                .build();
        String username = userDto.getUserId();
        String password = userDto.getPassword();
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        userDto = loginDao.selectUser(UserDto.builder().userId(username).build()).orElseThrow(() -> {throw new BadCredentialsException(username + "Invalid id");});

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication, false);
        String refreshToken = tokenProvider.createToken(authentication, true);
        String role = tokenProvider.getRole(userDto);

        userDto.setPassword(null);

        setCookie(request,response, accessToken, refreshToken, String.valueOf(userDto.getSeq()), role);

        response.setHeader(tokenProvider.AUTHORIZATION_ACCESS, "Bearer " + accessToken);
        response.setHeader(tokenProvider.AUTHORIZATION_REFRESH, "Bearer " + refreshToken);
        response.setHeader(tokenProvider.AUTHORITY, tokenProvider.getRole(userDto));

        setCookie(request,response, accessToken, refreshToken, String.valueOf(userDto.getSeq()), tokenProvider.getRole(userDto));

        return LoginResponseVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .userSeq(String.valueOf(userDto.getSeq()))
                .build();
    }

    /**
     * refreshToken 가지고 토큰들 재발행 (시간갱신)
     * @return
     */
    public ReIssueResponseVO reIssue(){
        String accessToken = tokenProvider.createTokenByRefreshToken(false);
        String refreshToken = tokenProvider.createTokenByRefreshToken(true);
        HttpServletRequest request = CommonUtil.getRequest();
        HttpServletResponse response = CommonUtil.getResponse();

        request.getSession().setAttribute(tokenProvider.AUTHORIZATION_ACCESS, URLEncoder.encode("Bearer " + accessToken));
        request.getSession().setAttribute(tokenProvider.AUTHORIZATION_REFRESH, URLEncoder.encode("Bearer " + refreshToken));
        response.setHeader(tokenProvider.AUTHORIZATION_ACCESS, "Bearer " + accessToken);
        response.setHeader(tokenProvider.AUTHORIZATION_REFRESH, "Bearer " + refreshToken);
        Cookie accessCookie = new Cookie(tokenProvider.AUTHORIZATION_ACCESS, URLEncoder.encode("Bearer " + accessToken));
        Cookie refreshCookie = new Cookie(tokenProvider.AUTHORIZATION_REFRESH, URLEncoder.encode("Bearer " + refreshToken));
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ReIssueResponseVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
            .build();
    }


    public void setCookie(
            HttpServletRequest request
            , HttpServletResponse response
            , String accessToken
            , String refreshToken
            , String userSeq
            , String auth){
        request.getSession().setAttribute(tokenProvider.AUTHORIZATION_ACCESS, URLEncoder.encode("Bearer " + accessToken));
        request.getSession().setAttribute(tokenProvider.AUTHORIZATION_REFRESH, URLEncoder.encode("Bearer " + refreshToken));
        request.getSession().setAttribute(tokenProvider.AUTHORITY, URLEncoder.encode(auth));

        logger.error(request.getServerName());
        Cookie accessCookie = new Cookie(tokenProvider.AUTHORIZATION_ACCESS, URLEncoder.encode("Bearer " + accessToken));
        accessCookie.setPath("/");
        Cookie userSeqCookie = new Cookie(tokenProvider.USER_SEQ, URLEncoder.encode(userSeq));
        userSeqCookie.setPath("/");
        Cookie refreshCookie = new Cookie(tokenProvider.AUTHORIZATION_REFRESH, URLEncoder.encode("Bearer " + refreshToken));
        refreshCookie.setPath("/");
        Cookie authorityCookie =new Cookie(tokenProvider.AUTHORITY, URLEncoder.encode(auth));
        authorityCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(userSeqCookie);
        response.addCookie(refreshCookie);
        response.addCookie(authorityCookie);
    }

    public void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(tokenProvider.AUTHORIZATION_ACCESS);
        request.getSession().removeAttribute(tokenProvider.AUTHORIZATION_REFRESH);
        request.getSession().removeAttribute(tokenProvider.AUTHORITY);
        request.getSession().removeAttribute(tokenProvider.ACCESS_KEY);

        Cookie accessCookie = new Cookie(tokenProvider.AUTHORIZATION_ACCESS, "");
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);
        Cookie refreshCookie = new Cookie(tokenProvider.AUTHORIZATION_REFRESH, "");
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);
        Cookie userSeqCookie = new Cookie(tokenProvider.USER_SEQ, "");
        userSeqCookie.setMaxAge(0);
        userSeqCookie.setPath("/");
        response.addCookie(userSeqCookie);
        Cookie authorityCookie = new Cookie(tokenProvider.AUTHORITY, "");
        authorityCookie.setMaxAge(0);
        authorityCookie.setPath("/");

        response.addCookie(authorityCookie);
    }

    public UserDto getUserInfo(){
        return getUserInfo(CommonUtil.getRequest(), CommonUtil.getResponse());
    }
    public UserDto getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = tokenProvider.resolveToken().getAccessToken();
        String refreshToken = tokenProvider.resolveToken().getRefreshToken();
        UserDto userDto = null;
        String userId = null;
        String userInfo = null;

        userId = tokenProvider.getUserIdInToken(accessToken);
        if(userId == null){
            logger.info("토큰만료. refresh 조회");
            userId = tokenProvider.getUserIdInToken(refreshToken);
        }

        if(!tokenProvider.validateToken(accessToken)) {
            throw new CustomException(ResponseCode.LOGIN_TOKEN_EXPIRED);
        }

        userDto = loginDao.selectUser(UserDto.builder().userId(userId).build()).orElseThrow();
        return userDto;
    }

    public Boolean logout(HttpServletRequest request, HttpServletResponse response){
        try {
            clearCookie(request,response);
        }catch (Exception ex){
            CommonUtil.extracePrintLog(ex);
        }
        return true;
    }
}