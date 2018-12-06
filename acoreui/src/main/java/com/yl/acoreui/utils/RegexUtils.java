package com.yl.acoreui.utils;

/**
 * 正则表达式工具类，提供一些常用的正则表达式</h2>
 */
public class RegexUtils {

    /**
     * 匹配手机号码的正则表达式
     */
    public static boolean isMobilePhoneNumber(String string) {
        String s = string.trim();
        return isInteger(s, 11) && s.startsWith("1");
        //return string.matches("^1{1}(3{1}\\d{1}|5{1}[012356789]{1}|7{1}[012356789]{1}|8{1}[012356789]{1})\\d{8}$");
    }

    /**
     * 是否是指定长度的纯数字
     */
    public static boolean isInteger(String string, int length) {
        return string.matches("^\\d{" + length + "}$");
    }

    /**
     * 密码规则
     */
    public static boolean isMatchesPwd(String string) {
        return string.matches("^[a-z0-9A-Z]{6,16}+$");
    }

    /**
     * 身份证
     */
    public static boolean isIdCardPwd(String string) {
        return string.matches("^(\\d{18}|\\d{17}x)$");
    }
}
