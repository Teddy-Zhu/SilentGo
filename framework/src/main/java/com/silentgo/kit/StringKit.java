package com.silentgo.kit;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class StringKit {

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

}
