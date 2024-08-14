package com.lime.limeEduApi.framework.common.util;

import java.math.BigDecimal;
/***********************************************
 *
 * NumberUtil - 숫자연산 유틸리티
 *
 * 작성자 - 임용휘
 * 작성일 - 2020/05/22
 *
 * LIME_COMPANY 개발팀
 ***********************************************/
public class NumberUtil {

    /**
     * calculate - 연산 공통
     * @param a - 앞 숫자
     * @param mode - +,-,*,/
     * @param b - 뒷 숫자
     * @return
     */
    public static String calculate(Object a,String mode, Object b) {
        String val = "0";
        switch (mode) {
            case "+":
            case "sum":
            case "add":
                val = add(a,b);
                break;
            case "-":
            case "miners":
            case "subtract":
                val = subtract(a,b);
                break;
            case "*":
            case "x":
            case "multiply":
                val = multiply(a,b);
                break;
            case "/":
            case "divide":
                val = divide(a,b);
            break;
        }
        return val;
    }



    /**
     * 덧셈
     * @param a
     * @param b
     * @return
     */
    public static String add(Object a, Object b) {
        BigDecimal n = toDecimal(0);
        try {
            n = toDecimal(a).add(toDecimal(b));
        }catch (Exception e) {}
        return n.toString();
    }

    /**
     * 뺄셈
     * @param a
     * @param b
     * @return
     */
    public static String subtract(Object a, Object b) {
        BigDecimal n = toDecimal(0);
        try {
            n = toDecimal(a).subtract(toDecimal(b));
        }catch (Exception e) {}
        return n.toString();
    }

    /**
     * 곱셈
     * @param a
     * @param b
     * @return
     */
    public static String multiply(Object a, Object b) {
        BigDecimal n = toDecimal(0);
        try {
            n = toDecimal(a).multiply(toDecimal(b));
        }catch (Exception e) {}
        return n.toString();
    }

    /**
     * 나눗셈
     * @param a
     * @param b
     * @return
     */
    public static String divide(Object a, Object b) {
        BigDecimal n = toDecimal(0);
        try {
            n = toDecimal(a).divide(toDecimal(b), 12, BigDecimal.ROUND_CEILING);
        }catch (Exception e) {}
        return n.toString();
    }

    /**
     * bigDecimal 형으로 변환
     * @param a
     * @return
     */
    public static BigDecimal toDecimal(Object a) {
        if(a == null || !isNumberic(a.toString())) {
            return new BigDecimal(0);
        }else {
            return new BigDecimal(a.toString());
        }
    }

    /**
     * 숫자체크
     * @param s
     * @return
     */
    public static boolean isNumberic(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

}
