package com.wangyuelin.app.utils;

public class TextUtil {

    /**
     * 清除特殊的字符
     * @param content
     * @param cs
     * @return
     */
    public static String removeSpecialCharacter(String content, String... cs) {
        if (TextUtil.isEmpty(content)) {
            return "";
        }


        for (String character : cs) {
            content = content.replaceAll(character, "");
        }
        return content;
    }

    /***
     * 是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equalsIgnoreCase(str)) {
            return true;
        }
        return false;

    }
}
