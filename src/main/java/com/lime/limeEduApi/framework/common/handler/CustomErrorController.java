package com.lime.limeEduApi.framework.common.handler;

import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@ApiIgnore
public class CustomErrorController implements ErrorController {
    private final MessageSource messageSource;

    @RequestMapping("/api/error/403")
    @ResponseBody
    public ResponseEntity error403(HttpServletRequest request){
        Map map = new HashMap();
        map.put("errorCode", ResponseCode.INTERNAL_SERVER_ERROR.getCode());
        map.put("message", messageSource.getMessage(ResponseCode.INTERNAL_SERVER_ERROR.getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

    @RequestMapping("/api/unauthorized")
    protected ResponseEntity unauthorizedException(HttpServletRequest request) {
        Map map = new HashMap<String,Object>();
        CustomException ex = new CustomException(ResponseCode.UNAUTHORIZED);
        CommonUtil.extracePrintLog(ex);
        map.put("code", ex.getResponseCode().getCode());
        map.put("message", messageSource.getMessage(ex.getResponseCode().getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

    @RequestMapping("/api/noRoles")
    protected ResponseEntity noRolesException(HttpServletRequest request) {
        Map map = new HashMap<String,Object>();
        CustomException ex = new CustomException(ResponseCode.NO_ROLES);
        CommonUtil.extracePrintLog(ex);
        map.put("code", ex.getResponseCode().getCode());
        map.put("message", messageSource.getMessage(ex.getResponseCode().getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }

    @RequestMapping("/api/filterError")
    protected ResponseEntity handleFilterException(HttpServletRequest request) {
        Map map = new HashMap<String,Object>();
        String errorCode = (String) request.getAttribute("errorCode");


        CustomException ex = new CustomException(ResponseCode.DISPLAY_NOT_FOUND);
        if(errorCode != null)
        switch (errorCode){
            case "1":
                ex = new CustomException(ResponseCode.LOGIN_TOKEN_EXPIRED);
                break;
            case "2":
                ex = new CustomException(ResponseCode.LOGIN_NEED);
                break;
        }

        CommonUtil.extracePrintLog(ex);
        map.put("code", ex.getResponseCode().getCode());
        map.put("message", messageSource.getMessage(ex.getResponseCode().getKey(), null , CommonUtil.getLocale()));
        return new ResponseEntity(map, null, HttpStatus.OK);
    }
}
