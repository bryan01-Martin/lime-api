package com.lime.limeEduApi.framework.common.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmVO {
    private String token;
    private String title;
    private String content;
    private String tabNo;
    private String url;
    private String type;
}
