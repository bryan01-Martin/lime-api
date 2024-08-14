package com.lime.limeEduApi.api.code.service.impl;

import com.lime.limeEduApi.api.code.dao.CodeDao;
import com.lime.limeEduApi.api.code.domain.CodeListByDepthReqDomain;
import com.lime.limeEduApi.api.code.domain.CodeListReqDomain;
import com.lime.limeEduApi.api.code.dto.CodeDto;
import com.lime.limeEduApi.api.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CodeDao codeDao;
    @Override
    public List<CodeDto> getCodeList(CodeListReqDomain codeListReqDomain) {
        return codeDao.getCodeList(codeListReqDomain);
    }

    @Override
    public List<CodeDto> getCodeListByDepth(CodeListByDepthReqDomain codeListReqDomain) {
        return codeDao.getCodeListByDepth(codeListReqDomain);
    }
}
