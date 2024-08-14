package com.lime.limeEduApi.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.annotaion.User;
import com.lime.limeEduApi.framework.common.annotaion.UserRequestBody;
import com.lime.limeEduApi.framework.security.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFieldSessionArgumentResolver implements HandlerMethodArgumentResolver, Ordered {

    private final LoginService loginService;

    private final ObjectMapper objectMapper;
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;  // 우선 순위를 가장 낮게 설정
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String requestBody = getRequestBody(servletRequest);
        Class<?> parameterType = parameter.getParameterType();

        // 요청 본문 데이터를 자바 객체로 변환
        Object obj = objectMapper.readValue(requestBody, parameterType);

        UserDto userVO = loginService.getUserInfo();
        // 필드 타입 및 이름에 따라 적절한 로직 수행
        List<Field> resultFields = getFieldFromParameter(parameter);
        for (Field field : resultFields) {
            field.setAccessible(true);
            String setter ="set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
            String getter ="get"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
            try{
                if(Arrays.stream(new String[]{"userSeq", "regSeq", "modSeq"})
                        .anyMatch(match -> field.getName().equals(match))){
                    obj.getClass().getMethod(setter, field.getType()).invoke(obj, userVO.getSeq());
                }else {
                    obj.getClass().getMethod(setter, field.getType()).invoke(obj, userVO.getClass().getMethod(getter).invoke(userVO));
                }
            }catch (TypeMismatchException e){

            }
        }

        return obj;
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream();
             StringWriter writer = new StringWriter()) {
            IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
            return writer.toString();
        }
    }

    private List<Field> getFieldFromParameter(MethodParameter parameter) {
        Class<?> declaringClass = parameter.getParameterType();
        Field[] fields = declaringClass.getDeclaredFields();
        // 부모 클래스의 필드 가져오기
        List<Field> resultFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(User.class)) {
                resultFields.add(field);
            }
        }

        Class<?> superClass = declaringClass.getSuperclass();
        while (superClass != null) {
            Field[] superFields = superClass.getDeclaredFields();
            for (Field field : superFields) {
                if (field.isAnnotationPresent(User.class)) {
                    resultFields.add(field);
                }
            }
            superClass = superClass.getSuperclass();
        }

        return resultFields;
    }
}