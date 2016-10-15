package com.silentgo.utils.logger;

/**
 * Project : silentgo
 * com.silentgo.kit.logger
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by  on 16/7/15.
 */
public enum Level {
    TRACE(100), DEBUG(200), INFO(300), WARN(400), ERROR(500), FATAL(600);

    private int value = 0;

    Level(int value) {
        this.value = value;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 100:
                return TRACE;
            case 200:
                return DEBUG;
            case 300:
                return INFO;
            case 400:
                return WARN;
            case 500:
                return ERROR;
            case 600:
                return FATAL;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}
