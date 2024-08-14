package com.lime.limeEduApi.framework.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLogin {
	private String accessToken;
	private String refreshToken;
	private String userSeq;
	private String pushToken;
	private String role;
}