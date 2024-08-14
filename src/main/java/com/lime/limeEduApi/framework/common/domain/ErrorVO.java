package com.lime.limeEduApi.framework.common.domain;

import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorVO {
    ResponseCode errorCode;
}
