package com.silentgo.utils.logger;


import com.silentgo.utils.ClassKit;

/**
 * Project : silentgo
 * com.silentgo.kit.logger
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public class LoggerFactory {

    private static final String slf4j = "org.slf4j.Logger";
    private static final String log4j = "org.apache.log4j.Logger";

    private static final boolean log4jAvailable = ClassKit.isAvailable(log4j);
    private static final boolean slf4jAvailable = ClassKit.isAvailable(slf4j);



    public static Logger getLog(Class<?> clazz) {
        if (slf4jAvailable) {
            return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(clazz));
        }
        if (log4jAvailable) {
            return new Log4jLogger(org.apache.log4j.Logger.getLogger(clazz));
        }
        return new SilentGoLogger(clazz);
    }


}
