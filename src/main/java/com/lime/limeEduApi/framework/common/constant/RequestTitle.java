package com.lime.limeEduApi.framework.common.constant;

import lombok.Getter;

@Getter
public enum RequestTitle {

    REQUEST_MODIFY("0000", "REQUEST_TITLE.REQUEST_MODIFY")
    ;
//    //500 INTERNAL SERVER ERROR

    private String code;
    private String key;

    RequestTitle(String code, String key) {
        this.code = code;
        this.key = key;
    }
}
