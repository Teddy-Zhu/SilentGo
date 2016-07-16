package com.silentgo.logger;

/**
 * Project : silentgo
 * com.silentgo.logger
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public interface Logger {

    void debug(String msg);

    void debug(String msg, Object... args);

    void info(String msg);

    void info(String msg, Object... args);

    void warn(String msg);

    void warn(String msg, Object... args);

    void warn(String msg, Throwable exception);

    void error(String msg);

    void error(String msg, Object... args);

    void error(String msg, Throwable t);

}
