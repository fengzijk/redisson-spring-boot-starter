package com.fengzijk.redisson.redlock;

/**
 * -------------------------------------------------
 *
 * @ProjectName : redisson-spring-boot-starter
 * @Descprition : String工具类
 * @Author : fengzijk
 * @email: guozhifengvip@163.com
 * @Time : 2019/4/3 14:40
 * --------------------------------------------------
 */
public class StringUtil {
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}
