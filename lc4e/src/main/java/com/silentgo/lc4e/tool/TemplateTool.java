package com.silentgo.lc4e.tool;


import com.silentgo.utils.StringKit;

import java.util.Date;
import java.util.List;

/**
 * Created by teddy on 2015/9/5.
 */
public class TemplateTool {
    public static Long getTime() {
        return System.currentTimeMillis();
    }

    public static String formatTime(Date date, Date now) {
        return RelativeDate.format(date, now);
    }

    public static boolean isEmpty(String string) {
        return StringKit.isBlank(string);
    }

    public static boolean isEmpty(List<String> string) {
        return string == null || string.size() == 0;
    }
}
