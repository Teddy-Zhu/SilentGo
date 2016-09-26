package com.silentgo.utils.logger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SilentGoLogger implements Logger {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static PrintStream outprint = System.out;

    private int level = Level.DEBUG.value();

    protected String path;

    private static final String formatter = "[%s]";

    public SilentGoLogger() {
        this.path = Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public SilentGoLogger(Class<?> type) {
        this.path = type.getName();
    }

    private String getLevel(int level) {
        if (level <= Level.TRACE.value()) {
            return "TRACE";
        }
        if (level <= Level.DEBUG.value()) {
            return "DEBUG";
        }
        if (level <= Level.INFO.value()) {
            return "INFO";
        }
        if (level <= Level.WARN.value()) {
            return "WARN";
        }
        if (level <= Level.ERROR.value()) {
            outprint = System.err;
            return "ERROR";
        }
        if (level <= Level.FATAL.value()) {
            return "FATAL";
        }
        return "DEBUG";
    }

    public void trace(String message) {
        if (level <= Level.TRACE.value())
            log(Level.TRACE.value(), message);
    }

    public void trace(String message, Object... args) {
        if (level <= Level.TRACE.value())
            log(Level.TRACE.value(), message, args);
    }

    public void trace(String message, Throwable t) {
        if (level <= Level.TRACE.value())
            log(Level.TRACE.value(), message, t);
    }


    public void trace(String message, Throwable t, Object... args) {
        if (level <= Level.TRACE.value())
            log(Level.TRACE.value(), message, t, args);
    }


    public void debug(String message) {
        if (level <= Level.DEBUG.value())
            log(Level.DEBUG.value(), message);
    }


    public void debug(String message, Object... args) {
        if (level <= Level.DEBUG.value())
            log(Level.DEBUG.value(), message, args);
    }


    public void debug(String message, Throwable t) {
        if (level <= Level.DEBUG.value())
            log(Level.DEBUG.value(), message, t);
    }


    public void debug(String message, Throwable t, Object... args) {
        if (level <= Level.DEBUG.value())
            log(Level.DEBUG.value(), message, t, args);
    }


    public void info(String message) {
        if (level <= Level.INFO.value())
            log(Level.INFO.value(), message);
    }


    public void info(String message, Object... args) {
        if (level <= Level.INFO.value())
            log(Level.INFO.value(), message, args);
    }


    public void info(String message, Throwable t) {
        if (level <= Level.INFO.value())
            log(Level.INFO.value(), message, t);
    }


    public void info(String message, Throwable t, Object... args) {
        if (level <= Level.INFO.value())
            log(Level.INFO.value(), message, t, args);
    }


    public void warn(String message) {
        if (level <= Level.WARN.value())
            log(Level.WARN.value(), message);
    }


    public void warn(String message, Object... args) {
        if (level <= Level.WARN.value())
            log(Level.WARN.value(), message, args);
    }


    public void warn(String message, Throwable t) {
        if (level <= Level.WARN.value())
            log(Level.WARN.value(), message, t);
    }


    public void warn(String message, Throwable t, Object... args) {
        if (level <= Level.WARN.value())
            log(Level.WARN.value(), message, t, args);
    }


    public void error(String message) {
        if (level <= Level.ERROR.value())
            log(Level.ERROR.value(), message);
    }


    public void error(String message, Object... args) {
        if (level <= Level.ERROR.value())
            log(Level.ERROR.value(), message, args);
    }


    public void error(String message, Throwable t) {
        if (level <= Level.ERROR.value())
            log(Level.ERROR.value(), message, t);
    }


    public void error(String message, Throwable t, Object... args) {
        if (level <= Level.ERROR.value())
            log(Level.ERROR.value(), message, t, args);
    }

    public boolean isDebugEnabled() {
        return level <= Level.DEBUG.value();
    }

    public boolean isErrorEnabled() {
        return level <= Level.ERROR.value();
    }

    public boolean isInfoEnabled() {
        return level <= Level.INFO.value();
    }

    public boolean isWarnEnabled() {
        return level <= Level.WARN.value();
    }

    public void log(int level, String message, Object... args) {
        log(level, message, null, args);
    }

    public void log(int level, String message, Throwable t, Object... args) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(formatter, Level.valueOf(level)));
        sb.append(String.format(formatter, now()));
        sb.append(String.format(formatter, this.path));
        sb.append(filterMessage(message, args));

        outprint.println(sb.toString());
        if (t != null) {
            t.printStackTrace(System.err);
            System.err.flush();
        }
    }

    protected String filterMessage(String message, Object... args) {
        if (message == null) {
            return null;
        }
        if (args == null || args.length == 0) {
            if (message.contains("{}")) {
                message = message.replaceAll("\\{\\}", "");
            }
            return message;
        } else {
            if (message.contains("{}")) {
                message = message.replaceAll("\\{\\}", "%s");
            }
            return String.format(message, args);
        }
    }

    private String now() {
        return sdf.format(new Date());
    }

}