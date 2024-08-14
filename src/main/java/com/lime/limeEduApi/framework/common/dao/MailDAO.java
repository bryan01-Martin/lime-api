package com.lime.limeEduApi.framework.common.dao;

import com.lime.limeEduApi.framework.common.domain.MailResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MailDAO {

   public int insertMailHistory(MailResultVO mailResultVO);
}
