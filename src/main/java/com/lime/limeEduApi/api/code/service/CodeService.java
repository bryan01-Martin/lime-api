package com.lime.limeEduApi.api.code.service;


import com.lime.limeEduApi.api.code.domain.CodeListByDepthReqDomain;
import com.lime.limeEduApi.api.code.domain.CodeListReqDomain;
import com.lime.limeEduApi.api.code.dto.CodeDto;

import java.util.List;

public interface CodeService {
    public List<CodeDto> getCodeList(CodeListReqDomain codeListReqDomain);
    public List<CodeDto> getCodeListByDepth(CodeListByDepthReqDomain codeListReqDomain);
}
