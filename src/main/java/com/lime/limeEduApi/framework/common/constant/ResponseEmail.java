package com.lime.limeEduApi.framework.common.constant;

import lombok.Getter;

@Getter
public enum ResponseEmail {
    TITLE("RESPONSE_EMAIL.TITLE"),
    CONT_TITLE("RESPONSE_EMAIL.CONT_TITLE"),
    CONT_SUB_TITLE("RESPONSE_EMAIL.CONT_SUB_TITLE"),
    CONTENT("RESPONSE_EMAIL.CONT"),
    FOOTER("RESPONSE_EMAIL.FOOTER"),
    FOOTER2("RESPONSE_EMAIL.FOOTER2"),
    TITLE_JOIN("RESPONSE_EMAIL.TITLE_JOIN"),
    TITLE_PASSWORD("RESPONSE_EMAIL.TITLE_PASSWORD"),
    CONT_JOIN("RESPONSE_EMAIL.CONT_JOIN"),
    CONT_PASSWORD("RESPONSE_EMAIL.CONT_PASSWORD")
;

    private String value;
    ResponseEmail(String value) {
        this.value = value;
    }

}
