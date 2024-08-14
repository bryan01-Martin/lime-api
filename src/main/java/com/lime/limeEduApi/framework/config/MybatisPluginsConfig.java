package com.lime.limeEduApi.framework.config;

import com.lime.limeEduApi.framework.common.interceptor.PrivacyEncryptInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPluginsConfig {

    @Bean
    public PrivacyEncryptInterceptor PrivacyEncryptInterceptor(){
        return new PrivacyEncryptInterceptor();
    }
}
