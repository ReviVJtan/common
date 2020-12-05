package cn.modoumama.common.utils;

import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author Administrator
 */
public class RegexUtil {

    /**
     * 检测 手机号 的正则
     */
    public static final String PHONE_REGEX = "^1(3[0-9]|5[0-35-9]|7[0-9]|8[0-9]|14[57])[0-9]{8}$";

    /**
     * @param param
     * @return
     * @Description 判断字符串是否为空
     */
    public static boolean isNull(String param) {

        return (param == null || "".equals(param.trim()));
    }

    /**
     * @param phone
     * @return
     * @Description 判断正则表达式
     */
    public static boolean checkRegexWithStrict(String param, String regex) {
        return !isNull(param) && Pattern.compile(regex).matcher(param).matches();
    }

    /**
     * @param phone
     * @return
     * @Description 检测手机号
     */
    public static boolean checkPhone(String phone) {
        return checkRegexWithStrict(phone, PHONE_REGEX);
    }
}
