package com.lime.limeEduApi.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final UserSessionArgumentResolver userSessionArgumentResolver;
    private final UserFieldSessionArgumentResolver userFieldSessionArgumentResolver;

    public MvcConfig(UserSessionArgumentResolver userSessionArgumentResolver, UserFieldSessionArgumentResolver userFieldSessionArgumentResolver) {
        this.userSessionArgumentResolver = userSessionArgumentResolver;
        this.userFieldSessionArgumentResolver = userFieldSessionArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userSessionArgumentResolver);
        resolvers.add(userFieldSessionArgumentResolver);
    }
}