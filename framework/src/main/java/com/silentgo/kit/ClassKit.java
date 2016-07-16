package com.silentgo.kit;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by  on 16/7/15.
 */
public class ClassKit {


    public static boolean isAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
