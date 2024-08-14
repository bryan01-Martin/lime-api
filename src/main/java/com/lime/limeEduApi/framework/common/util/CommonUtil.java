package com.lime.limeEduApi.framework.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lime.limeEduApi.api.login.dto.LoginDto;
import com.lime.limeEduApi.framework.common.constant.Const;
import com.lime.limeEduApi.framework.common.constant.CustomException;
import com.lime.limeEduApi.framework.common.constant.ResponseCode;

/***********************************************
 *
 * CommonUtil - 공통 유틸리티
 *
 * 작성자 - 임용휘
 * 작성일 - 2020/05/22
 *
 * LIME_COMPANY 개발팀
 ***********************************************/
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);


    public static final String IS_MOBILE = "MOBILE";
    public static final String IS_PHONE = "PHONE";
    public static final String IS_TABLET = "TABLET";
    public static final String IS_PC = "PC";

    /**
     * 파일 UUID 가져오기
     * @return uuid nm
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 날짜 가져오기
     * 기본값 년도-월-일
     * @return
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }
    /**
     * 날짜 가져오기
     * 포맷형식 지정 yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String getDate(String formatStr) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 모바일,타블렛,PC구분
     * @param request
     * @return
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toUpperCase();
        if(userAgent.indexOf(IS_MOBILE) > -1) {
            if(userAgent.indexOf(IS_PHONE) == -1)
                return IS_PHONE;
            else
                return IS_TABLET;
        } else
            return IS_PC;
    }



    /**
     * Object 의 null 여부를 반환한다.
     *
     * @since 2017. 8. 07.
     * @author ngins
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        return object == null ? true : false;
    }

    public static Object isNull(Object object, String replacement) {
        return object == null ? replacement : object;
    }

    // public static void main(String[] args) {
    // }

    /**
     * String 의 null 여부를 반환한다. " " 은 null 널로 판단하지 않음.
     *
     * @param string
     * @return
     */
    public static boolean isNull(String string) {
        return ("".equals(string) || "null".equals(string.toLowerCase()) || 0 == string.length() || string == null)
                ? true : false;
    }

    /**
     * string 이 널일때, replacement 를 반환한다.
     *
     * @param string
     * @param replacement
     * @return
     */
    public static String isNull(String string, String replacement) {
        if (isNull(string)) {
            return replacement;
        } else {
            return string;
        }
    }

    /**
     * <PRE>
     * name : isEmpty
     * description : Empty checking
     * </PRE>
     *
     * @since 2016. 6. 11.
     * @category choose [none]
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        /** Business Logic */
        // Object가 없거나, 공백, null문자일 경우
        if (str == null || str.trim().equals("") || str.trim().toLowerCase().equals("null")) {
            return true;
        }

        /** Return */
        return false;
    }

    /**
     * @MethodName isNotEmpty
     * @MethodDescription Not Empty checking
     * @Date 2016. 9. 25.
     * @author JCH
     * @Modifiers
     * @param str
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        /** Return */
        return !isEmpty(str);
    }

    /**
     * <PRE>
     * name : isEmpty
     * description : Map에 값이 있는지 확인
     * </PRE>
     *
     * @since 2016. 7. 4.
     * @category choose none
     * @param inputMap
     * @param key
     * @return boolean
     */
    public static boolean isEmpty(Map<String, Object> inputMap, String key) {
        /** Business Logic */
        // Map 없을 경우
        if (inputMap == null) {
            return true;
        }
        // Map만 체크할 경우
        if (isEmpty(key)) {
            return false;
        }
        // Map 해당 Key에 값이 없을 경우
        if (inputMap.get(key) == null || isEmpty(inputMap.get(key).toString())) {
            return true;
        }

        /** Return */
        return false;
    }

    /**
     * <PRE>
     * name : isNotEmpty
     * description : Map에 값이 있는지 확인
     * </PRE>
     *
     * @since 2016. 7. 4.
     * @category choose |none
     * @param inputMap
     * @param key
     * @return boolean
     */
    public static boolean isNotEmpty(Map<String, Object> inputMap, String key) {
        /** Return */
        return !isEmpty(inputMap, key);
    }

    /**
     * <PRE>
     * name : isEmpty
     * description : Map에 값이 있는지 확인
     * </PRE>
     *
     * @since 2016. 7. 4.
     * @category choose none
     * @param inputMap
     * @return boolean
     */
    public static boolean isEmpty(Map<String, Object> inputMap) {
        return isEmpty(inputMap, null);
    }


    public static void extracePrintLog(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        logger.info(exceptionAsString);
    }

    /**
     * ContextHolder에서 Request객체 꺼내오기
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes == null){
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;
    }

    /**
     * ContextHolder에서 response객체 꺼내오기
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes == null){
            return null;
        }
        HttpServletResponse response = servletRequestAttributes.getResponse();
        return response;
    }


    /**
     * session 에서 lang 꺼내오기
     * @return lang
     */
    public static String getLang(){
        return getLocale().getLanguage();
    }

    /**
     * 언어로 locale 객체리턴
     * @return
     */
    public static Locale getLocale(){
        try {
            if (getRequest() != null && getRequest().getLocale() != null) {
                return getRequest().getLocale();
            }
        }catch(Exception e){}
        return new Locale("ko");
    }
    
    public static int getLoginUserSeq(){
        return getLoginUserInfo().getSeq();
    }
    public static String getLoginUserName(){
        return getLoginUserInfo().getName();
    }
    public static LoginDto getLoginUserInfo(){
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        if(req.getSession() == null){
            throw new CustomException(ResponseCode.LOGIN_NEED);
        }
        Object user = req.getSession().getAttribute(Const.SESSION.LOGIN_MEMBER);
        if(user == null){
            throw new CustomException(ResponseCode.LOGIN_NEED);
        }
        return (LoginDto) user;
    }

}
