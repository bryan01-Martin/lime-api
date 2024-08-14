package com.lime.limeEduApi.api.code.controller;

import com.lime.limeEduApi.api.code.domain.CodeListByDepthReqDomain;
import com.lime.limeEduApi.api.code.domain.CodeListReqDomain;
import com.lime.limeEduApi.api.code.dto.CodeDto;
import com.lime.limeEduApi.api.code.service.CodeService;
import com.lime.limeEduApi.framework.common.constant.Const;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(Const.API.CODE)
@Api(tags = {"코드 API"})  // Swagger 최상단 Controller 명칭
public class CodeRestController {
    private final CodeService codeService;
    private final MessageSource messageSource;

    @PostMapping("/getCodeList")
    @ApiOperation(value = "하위코드 조회", notes = "하위코드조회")
    public ResponseDomain<CodeDto> getCodeList(@RequestBody CodeListReqDomain codeListReqDomain) {
        return ResponseDomain.<CodeDto>builder()
                .code(ResponseCode.SEARCH_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .list(codeService.getCodeList(codeListReqDomain))
            .build();
    }

    @PostMapping("/getCodeListByDepth")
    @ApiOperation(value = "레벨별 코드목록 조회", notes = "레벨별 코드목록 조회")
    public ResponseDomain<CodeDto> getCodeListByDepth(CodeListByDepthReqDomain codeListReqDomain) {
        return ResponseDomain.<CodeDto>builder()
                .code(ResponseCode.SEARCH_SUCCESS.getCode())
                .message(messageSource.getMessage(ResponseCode.SEARCH_SUCCESS.getKey(), null, CommonUtil.getLocale()))
                .list(codeService.getCodeListByDepth(codeListReqDomain))
                .build();
    }
}
