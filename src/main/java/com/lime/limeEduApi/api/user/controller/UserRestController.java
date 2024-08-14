package com.lime.limeEduApi.api.user.controller;

import com.lime.limeEduApi.api.user.domain.*;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(Const.API.USER)
@Api(tags = {"사용자 API"})  // Swagger 최상단 Controller 명칭
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final MessageSource messageSource;

    @PostMapping("/getUserInfo")
    @ApiOperation(value = "유저 정보 조회", notes = "유저 정보 조회")
    public ResponseDomain<UserDto> getUserInfo(@RequestBody UserInfoReq userInfoReq){
        return ResponseDomain.<UserDto>builder()
                .data(userService.getUserInfo(userInfoReq))
                .code(ResponseCode.SEARCH_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }

    @PostMapping("/getUserInfoList")
    @ApiOperation(value = "유저 목록 조회", notes = "유저 목록 조회")
    public ResponseDomain<UserDto> getUserInfoList(@RequestBody UserInfoListReq userInfoListReq){
        return ResponseDomain.<UserDto>builder()
                .list(userService.getUserInfoList(userInfoListReq))
                .totalCnt(userService.getUserInfoListCnt(userInfoListReq))
                .code(ResponseCode.SEARCH_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }

    @PostMapping("/regUser")
    @ApiOperation(value = "유저 등록", notes = "유저 등록")
    public ResponseDomain<Integer> regUser(@RequestBody UserAddReq userAddReq, @User@ApiIgnore UserDto userDto){
        userAddReq.setRegSeq(userDto.getSeq());
        int result = userService.regUser(userAddReq);
        return ResponseDomain.<Integer>builder()
                .data(userAddReq.getSeq())
                .code(ResponseCode.INSERT_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.INSERT_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }

    @PostMapping("/modUser")
    @ApiOperation(value = "유저 수정", notes = "유저 수정")
    public ResponseDomain<Integer> modUser(@RequestBody UserModReq userModReq, @User@ApiIgnore UserDto userDto){
        userModReq.setModSeq(userDto.getSeq());
        int result = userService.modUser(userModReq);
        if(result == 1){
            return ResponseDomain.<Integer>builder()
                    .data(userModReq.getSeq())
                    .code(ResponseCode.UPDATE_SUCCESS.getCode())
                    .message(messageSource.getMessage(ResponseCode.UPDATE_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                    .build();
        } else {
            throw new CustomException(ResponseCode.UPDATE_FAILED);
        }
    }
    @PostMapping("/resetPassword")
    @ApiOperation(value = "비밀번호 초기화", notes = "비밀번호 초기화")
    public ResponseDomain<Integer> resetPassword(@RequestBody UserPasswordResetReq userResetPassReq, @User@ApiIgnore UserDto userDto){
        userService.resetPassword(UserDto.builder()
                .seq(userResetPassReq.getSeq())
                .modSeq(userDto.getSeq()).build());
        return ResponseDomain.<Integer>builder()
                .data(userResetPassReq.getSeq())
                .code(ResponseCode.UPDATE_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.UPDATE_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }
    @PostMapping("/checkUserId")
    @ApiOperation(value = "유저아이디 중복체크", notes = "유저아이디 중복체크 가능-true / 불가능- false")
    public ResponseDomain<Boolean> checkUserId(@RequestBody CheckUserIdReq checkUserIdReq){
        return ResponseDomain.<Boolean>builder()
                .data(userService.checkUserId(checkUserIdReq))
                .code(ResponseCode.SEARCH_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .build();
    }
}
