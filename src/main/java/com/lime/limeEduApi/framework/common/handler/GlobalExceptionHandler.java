package com.lime.limeEduApi.framework.common.handler;

import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorController {
    private final MessageSource messageSource;

    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity handleCustomException(HttpServletRequest request, CustomException ex) {
        Map map = new HashMap<String,Object>();
        CommonUtil.extracePrintLog(ex);
        map.put("code", ex.getResponseCode().getCode());
        map.put("message", messageSource.getMessage(ex.getResponseCode().getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

//    @ExceptionHandler({BadCredentialsException.class })
//    protected ResponseEntity handleServerException(HttpServletRequest request, BadCredentialsException ex) {
//        Map map = new HashMap<String,Object>();
//        CommonUtil.extracePrintLog(ex);
//        map.put("code", ResponseCode.NO_ROLES);
//        map.put("message", messageSource.getMessage(ResponseCode.NO_ROLES.getKey(), null , CommonUtil.getLocale()));
//        return new ResponseEntity(map, null, HttpStatus.OK);
//    }
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class, AuthenticationException.class })
    protected ResponseEntity handleAuthenticationException(HttpServletRequest request, Exception ex) {
        Map map = new HashMap<String,Object>();
        CommonUtil.extracePrintLog(ex);
        map.put("code", ResponseCode.LOGIN_NOT_MATCHED.getCode());
        map.put("message", messageSource.getMessage(ResponseCode.LOGIN_NOT_MATCHED.getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

    @ExceptionHandler({ HttpClientErrorException.BadRequest.class })
    protected ResponseEntity handleBadRequestException(HttpServletRequest request, Exception ex) {
        Map map = new HashMap<String,Object>();
        CommonUtil.extracePrintLog(ex);
        map.put("code", ResponseCode.BAD_REQUEST.getCode());
        map.put("message", messageSource.getMessage(ResponseCode.BAD_REQUEST.getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(HttpServletRequest request, Exception ex) {
        Map map = new HashMap<String,Object>();
        CommonUtil.extracePrintLog(ex);
        map.put("code", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
        map.put("message", messageSource.getMessage(ResponseCode.INTERNAL_SERVER_ERROR.getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

    @ExceptionHandler({ValidationException.class})
    protected  ResponseEntity handleValidationException(HttpServletRequest request, Exception e){
        CommonUtil.extracePrintLog(e);
        CustomException ex = new CustomException(ResponseCode.INVALID_PARAMETER);
        Map map = new HashMap<String,Object>();
        map.put("code", ex.getResponseCode().getCode());
        map.put("message", messageSource.getMessage(ex.getResponseCode().getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);

    }
}
