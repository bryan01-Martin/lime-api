package com.lime.limeEduApi.framework.security.filter;

import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.security.domain.ResponseLogin;
import com.lime.limeEduApi.framework.security.provider.TokenProvider;
import com.lime.limeEduApi.framework.security.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private TokenProvider tokenProvider;

    private LoginService loginService;

    public JwtFilter(TokenProvider tokenProvider, LoginService loginService) {
        this.tokenProvider = tokenProvider;
        this.loginService = loginService;
    }

    // 실제 필터링 로직은 doFilter 안에 들어가게 된다. GenericFilterBean을 받아 구현
    // Dofilter는 토큰의 인증정보를 SecurityContext 안에 저장하는 역할 수행
    // 현재는 jwtFilter 통과 시 loadUserByUsername을 호출하여 디비를 거치지 않으므로 시큐리티 컨텍스트에는 엔티티 정보를 온전히 가지지 않는다
    // 즉 loadUserByUsername을 호출하는 인증 API를 제외하고는 유저네임, 권한만 가지고 있으므로 Account 정보가 필요하다면 디비에서 꺼내와야함
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException, CustomException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpServletRequest.getRequestURI();
        logger.info("requestURI :::: "+requestURI);
        boolean shouldIgnore = Arrays.stream(tokenProvider.PERMIT_URL_ARRAY)
                .anyMatch(ignoreUrl -> requestURI.startsWith(ignoreUrl));

        // If the URL should be ignored, simply forward the request without applying the JWT filter logic
        if (shouldIgnore) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            ResponseLogin responseLogin = tokenProvider.resolveToken();
            String jwt = responseLogin.getAccessToken();
            // 토큰에서 유저네임, 권한을 뽑아 스프링 시큐리티 유저를 만들어 Authentication 반환
            Authentication authentication = tokenProvider.getAuthentication(responseLogin.getAccessToken());
            // 해당 스프링 시큐리티 유저를 시큐리티 건텍스트에 저장, 즉 디비를 거치지 않음
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }catch (Exception e){
            if(e instanceof CustomException){
                logger.debug(((CustomException) e).getResponseCode().getKey());
                servletRequest.setAttribute("errorCode", "2");
            }else {
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                servletRequest.setAttribute("errorCode", "1");
            }
            ((HttpServletResponse) servletResponse).sendRedirect("/api/filterError");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}