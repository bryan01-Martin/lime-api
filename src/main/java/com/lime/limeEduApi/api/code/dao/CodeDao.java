package com.lime.limeEduApi.api.code.dao;


import com.lime.limeEduApi.api.code.domain.CodeListByDepthReqDomain;
import com.lime.limeEduApi.api.code.domain.CodeListReqDomain;
import com.lime.limeEduApi.api.code.dto.CodeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeDao {
    public List<CodeDto> getCodeList(CodeListReqDomain codeListReqDomain);
    public List<CodeDto> getCodeListByDepth(CodeListByDepthReqDomain codeListReqDomain);
}
