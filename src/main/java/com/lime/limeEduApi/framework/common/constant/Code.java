package com.lime.limeEduApi.framework.common.constant;

public interface Code {
    interface PayType{
        public String NORMAL        = "PT03001";    /* 일반       */
        public String EXPENDITURE   = "PT03002";    /* 지출결의     */
    }
    interface MSG{
        interface MSG_TYPE{
            public String VACATION       = "휴가전환";
            public String POINT        = "포인트전환";
            public String ORD          = "주문";
        }
        interface MSG_STATAUS{
            public String REGISTER      = "신청";
            public String APPLY         = "승인";
            public String REJECT        = "반려";
            public String CANCEL        = "취소";
        }
    }
}

