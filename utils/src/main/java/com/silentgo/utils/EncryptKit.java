package com.silentgo.utils;


import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/29.
 */
public class EncryptKit {

    private static final Log LOGGER = LogFactory.get();


    public static String getMD5Short(String sourceStr) {
        return getMD5(sourceStr).substring(8, 24);
    }

    public static String getMD5(String sourceStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e, "get md5 error");
        }
        return "";
    }
}
