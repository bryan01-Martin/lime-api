package com.lime.limeEduApi.framework.config;

import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.annotaion.User;
import com.lime.limeEduApi.framework.security.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserSessionArgumentResolver implements HandlerMethodArgumentResolver, Ordered {

    private final LoginService loginService;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;  // 우선 순위를 가장 낮게 설정
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserDto.class) && parameter.hasParameterAnnotation(User.class);

    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return loginService.getUserInfo();
    }

}