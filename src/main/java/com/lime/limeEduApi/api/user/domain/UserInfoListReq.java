package com.lime.limeEduApi.api.user.domain;

import com.lime.limeEduApi.framework.common.domain.PagingDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoListReq extends PagingDomain {
    @ApiModelProperty(notes = "검색 키워드 유저 아이디")
    private String userId;
    @ApiModelProperty(notes = "검색 키워드 유저 명")
    private String name;
}
