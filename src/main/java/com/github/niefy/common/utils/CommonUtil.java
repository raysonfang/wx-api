package com.github.niefy.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author fanglei
 */
public class CommonUtil {
    /**
     * 字符串转换：四川{1},成都-》四川,成都
     *
     * @param format 字符串表达式  拥有占位符 {1}从1开始
     * @param params 参数
     * @return
     */
    public static String parse(String format, String... params) {
        String s = format;
        for (int i = 0; i < params.length; i++) {
            s = s.replace("{" + (i + 1) + "}", StringUtils.isEmpty(params[i]) ? "" : params[i]);
        }
        return s;
    }
}
