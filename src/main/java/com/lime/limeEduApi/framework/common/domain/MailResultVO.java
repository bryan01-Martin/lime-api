package com.lime.limeEduApi.framework.common.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MailResultVO extends DefaultTableRow{
    private String sender;
    private String receiver;
    private String subject;
    private String content;
    private String code;
    private int type;
    private int result; //0 - 실패 1 - 성공
    private String message; //결과 메세지
}
