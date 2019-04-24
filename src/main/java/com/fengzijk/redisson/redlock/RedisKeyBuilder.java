/*
 *   All rights Reserved, Designed By ZTE-ITS
 *   Copyright:    Copyright(C) 2019-2025
 *   Company       FENGZIJK LTD.
 *   @Author:    fengzijk
 *   @Email: guozhifengvip@163.com
 *   @Version    V1.0
 *   @Date:   2019年04月24日 15时01分
 *   Modification       History:
 *   ------------------------------------------------------------------------------------
 *   Date                  Author        Version        Discription
 *   -----------------------------------------------------------------------------------
 *  2019-04-24 15:01:35    fengzijk         1.0         Why & What is modified: <修改原因描述>
 */

package com.fengzijk.redisson.redlock;

/**
 * -------------------------------------------------
 * @Descprition : RedisKeyBuilder 统一自定义生成redisKey
 * @Author : fengzijk
 * @email: guozhifengvip@163.com
 * @Time : 2019/3/23 13:41
 * --------------------------------------------------
 */
public class RedisKeyBuilder {
    /**
     * 主数据系统标识
     */
    private static final String KEY_PREFIX = "sale";
    /**
     * 分割字符，默认[:]，使用:可用于rdm分组查看
     */
    private static final String KEY_SPLIT_CHAR = ":";

    /**
     * redis的key键规则定义
     *
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    public static String keyBuilder(String module, String func, String... args) {

        return keyBuilder(null, module, func, args);
    }

    /**
     * redis的key键规则定义
     *
     * @param module 模块名称
     * @param func   方法名称
     * @param objStr 对象.toString()
     * @return key
     */
    public static String keyBuilder(String module, String func, String objStr) {
        return keyBuilder(null, module, func, new String[]{objStr});
    }

    /**
     * redis的key键规则定义
     *
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param objStr 对象.toString()
     * @return key
     */
    public static String keyBuilder(String prefix, String module, String func, String objStr) {
        return keyBuilder(prefix, module, func, new String[]{objStr});
    }

    /**
     * redis的key键规则定义
     *
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    private static String keyBuilder(String prefix, String module, String func, String... args) {
        // 项目前缀
        if (prefix == null) {
            prefix = KEY_PREFIX;
        }
        StringBuilder key = new StringBuilder(prefix);
        // KEY_SPLIT_CHAR 为分割字符
        key.append(KEY_SPLIT_CHAR).append(module).append(KEY_SPLIT_CHAR).append(func);
        for (String arg : args) {
            key.append(KEY_SPLIT_CHAR).append(arg);
        }
        return key.toString();
    }
}
