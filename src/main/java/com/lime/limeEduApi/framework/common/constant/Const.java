package com.lime.limeEduApi.framework.common.constant;

public interface Const {
    public interface API{
        String CODE = "/api/code";
        String USER = "/api/user";
        String MY = "/api/my";
        String LOGIN = "/api/login";
        String BOARD = "/api/board";
        String BOARD_FILE = "/api/board/file";
        String BOARD_IMAGE = "/api/board/image";
        String ACCOUNT = "/api/account";
    }
    //URL 상수 interface
    interface URL {
        String TEST = "/test";
        String USER = "/user";
        String LOGIN = "/login";
        String BOARD = "/board";
        String BOARD_FILE = "/board/file";
        String BOARD_IMAGE = "/board/image";
        String ACCOUNT = "/account";
        String MY = "/my";
    }
    
    public interface SESSION {
        String LOGIN_MEMBER = "UserDto";
        String BOARD_SEQUENCE = "BoardDto";
    }

}
