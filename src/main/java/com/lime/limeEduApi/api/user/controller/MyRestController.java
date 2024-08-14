package com.lime.limeEduApi.api.user.controller;

import com.lime.limeEduApi.api.user.domain.UserChangePasswordReq;
import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.api.user.service.UserService;
import com.lime.limeEduApi.framework.common.annotaion.User;
import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.domain.ResponseDomain;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping(Const.API.MY)
@RestController
@RequiredArgsConstructor
@Api(tags = {"마이페이지 API"})  // Swagger 최상단 Controller 명칭
public class MyRestController {

    private final UserService userService;
    private final MessageSource messageSource;
    @PostMapping("/changePassword")
    @ApiOperation(value = "비밀번호 수정", notes = "비밀번호 수정")
    public ResponseDomain<Boolean> changePassword(@RequestBody UserChangePasswordReq userChangePasswordReq, @User @ApiIgnore UserDto userDto){
        int result = userService.changePassword(UserDto.builder()
                .seq(userDto.getSeq())
                .password(userChangePasswordReq.getPassword())
                .modSeq(userDto.getSeq()).build());
        if(result == 1) {
            return ResponseDomain.<Boolean>builder()
                    .data(true)
                    .code(ResponseCode.UPDATE_SUCCESS.getCode())
                    .message(messageSource.getMessage(ResponseCode.UPDATE_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                    .build();
        } else {
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
    }
}
