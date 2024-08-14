package com.lime.limeEduApi.framework.security.provider;

import com.lime.limeEduApi.api.user.dto.UserDto;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;
import com.lime.limeEduApi.framework.common.util.CommonUtil;
import com.lime.limeEduApi.framework.common.util.CookieUtil;
import com.lime.limeEduApi.framework.security.domain.ResponseLogin;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class TokenProvider {

    protected final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    protected static final String AUTHORITIES_KEY = "auth";

    protected final String secret;
    public final long tokenValidityInMilliseconds;
    public final long refreshTokenValidityInMilliseconds;

    protected Key key;
    public static final String AUTHORIZATION_ACCESS = "accessToken";
    public static final String AUTHORIZATION_REFRESH = "refreshToken";
    public static final String AUTHORITY = "role";
    public static final String USER_SEQ = "userSeq";
    public static final String ADDITIONAL_STR = "refreshToken";
    public static final String ACCESS_KEY = "accessKey";
    public static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/test/**",
            "/api/test/**",
            "/api/error/**",
            "/login/**",
            "/api/login/**",
            "/api/authenticate",
            "/favicon.ico",
            "/api/filterError",
            "/api/code",
            "/api/code/**",
            "/api/join",
            "/api/join/**",
            "/api/question/insert",
            "/api/policy/list",
            "/api/policy/savePolicyAgreement",
            "/api/policy/**",
            "/api/policy/detail",
            "/api/user/checkUserInfo",
            "/api/findId",
            "/api/reIssue",
            "/api/logout"
    };

    public TokenProvider(String secret, long tokenValidityInSeconds, long refreshTokenValidityInMilliseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
        //시크릿 값을 decode해서 키 변수에 할당
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public String createToken(String id, Collection<GrantedAuthority> authoritiesList, boolean isRefresh) {
        String authorities = authoritiesList.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        if(isRefresh){
            validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        }

        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // 토큰 생성
    public String createToken(Authentication authentication,boolean isRefresh) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        if(isRefresh){
            validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 리프레시 토큰정보로 토큰 재발행
     * @param isRefresh
     * @return
     */
    public String createTokenByRefreshToken(Boolean isRefresh){
        String refreshToken = resolveToken().getRefreshToken();
        Authentication authentication = getAuthentication(refreshToken);
        return createToken(authentication, isRefresh);
    }

    // 토큰을 받아 클레임을 만들고 권한정보를 빼서 시큐리티 유저객체를 만들어 Authentication 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 디비를 거치지 않고 토큰에서 값을 꺼내 바로 시큐리티 유저 객체를 만들어 Authentication을 만들어 반환하기에 유저네임, 권한 외 정보는 알 수 없다.
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    public String getUserIdInToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.get("sub").toString();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return null;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    // 헤더에서 토큰 정보를 꺼내온다.
    public ResponseLogin resolveToken() {
        HttpServletRequest request = CommonUtil.getRequest();
        if(request == null){
            return new ResponseLogin();
        }
        Cookie accessCookie = CookieUtil.getCookie(request, AUTHORIZATION_ACCESS).orElse(null);
        Cookie refreshCookie = CookieUtil.getCookie(request, AUTHORIZATION_REFRESH).orElse(null);

        String accessToken = accessCookie != null ? URLDecoder.decode(accessCookie.getValue()) : null;
        String refreshToken = refreshCookie != null ? URLDecoder.decode(refreshCookie.getValue()) : null;


        if (accessToken == null) {
            logger.info("cookie :::: accessToken is null!!!");
            accessToken = request.getHeader(AUTHORIZATION_ACCESS);
            logger.info("accessToken ::::"+accessToken);

        }
        if (refreshToken == null) {
            logger.info("cookie :::: refreshToken is null!!!");
            refreshToken = request.getHeader(AUTHORIZATION_REFRESH);
            logger.info("refreshToken ::::"+refreshToken);
        }

        if(accessToken == null || refreshToken == null){
            throw new CustomException(ResponseCode.LOGIN_NEED);
        }

        if (StringUtils.hasText(accessToken) && (accessToken.startsWith("Bearer ")||accessToken.startsWith("Bearer+"))) {
            accessToken =  accessToken.substring(7);
        }
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")||refreshToken.startsWith("Bearer+")) {
            refreshToken =  refreshToken.substring(7);
        }

        return ResponseLogin.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
        .build();
    }

    public String getRole(UserDto userDto){
        String role = null;
        switch(userDto.getType()){ /* 1-일반직원 2-관리자 */
            case 1:
                role = "ROLE_USER";
                break;
            case 2:
                role = "ROLE_ADMIN";
                break;
        }
        return role;
    }
}
