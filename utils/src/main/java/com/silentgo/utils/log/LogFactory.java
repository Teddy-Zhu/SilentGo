package com.silentgo.utils.log;

import com.silentgo.utils.log.dialect.console.ConsoleLogFactory;
import com.silentgo.utils.log.dialect.jdk.JdkLogFactory;
import com.silentgo.utils.log.dialect.log4j.Log4jLogFactory;
import com.silentgo.utils.log.dialect.log4j2.Log4j2LogFactory;
import com.silentgo.utils.log.dialect.slf4j.Slf4jLogFactory;
/**
 * from looly
 * LICENSE : Apache 2.0
 * https://raw.githubusercontent.com/looly/hutool/master/LICENSE.txt
 */

/**
 * 日志工厂类
 *
 * @author Looly
 */
public abstract class LogFactory {

    private String logFramworkName;

    public LogFactory(String logFramworkName) {
        this.logFramworkName = logFramworkName;
    }

    /**
     * 获得日志对象
     *
     * @param name 日志对象名
     * @return 日志对象
     */
    public abstract Log getLog(String name);

    /**
     * 获得日志对象
     *
     * @param clazz 日志对应类
     * @return 日志对象
     */
    public abstract Log getLog(Class<?> clazz);

    //------------------------------------------------------------------------- Static start
    private static volatile LogFactory currentLogFactory;
    private static final Object lock = new Object();

    /**
     * @return 当前使用的日志工厂
     */
    public static LogFactory getCurrentLogFactory() {
        if (null == currentLogFactory) {
            synchronized (lock) {
                if (null == currentLogFactory) {
                    currentLogFactory = detectLogFactory();
                }
            }
        }
        return currentLogFactory;
    }

    /**
     * 自定义日志实现
     *
     * @param logFactory 日志工厂类
     * @return 自定义的日志工厂类
     */
    public static LogFactory setCurrentLogFactory(LogFactory logFactory) {
        logFactory.getLog(LogFactory.class).debug("Custom Use [{}] Logger.", logFactory.logFramworkName);
        currentLogFactory = logFactory;
        return currentLogFactory;
    }

    /**
     * 获得日志对象
     *
     * @param name 日志对象名
     * @return 日志对象
     */
    public static Log get(String name) {
        return getCurrentLogFactory().getLog(name);
    }

    /**
     * 获得日志对象
     *
     * @param clazz 日志对应类
     * @return 日志对象
     */
    public static Log get(Class<?> clazz) {
        return getCurrentLogFactory().getLog(clazz);
    }

    /**
     * @return 获得调用者的日志
     */
    public static Log get() {
        return get(new Exception().getStackTrace()[1].getClassName());
    }

    /**
     * @return 获得调用者的调用者的日志（用于内部辗转调用）
     */
    protected static Log indirectGet() {
        return get(new Exception().getStackTrace()[2].getClassName());
    }

    /**
     * 决定日志实现
     *
     * @return 日志实现类
     */
    private static LogFactory detectLogFactory() {
        LogFactory logFactory;
        try {
            logFactory = new Slf4jLogFactory(true);
            logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
        } catch (Throwable e) {
            try {
                logFactory = new Log4jLogFactory();
                logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
            } catch (Throwable e2) {
                try {
                    logFactory = new Log4j2LogFactory();
                    logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
                } catch (Throwable e3) {
                    try {
                        logFactory = new JdkLogFactory();
                        logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
                    } catch (Throwable e5) {
                        logFactory = new ConsoleLogFactory();
                        logFactory.getLog(LogFactory.class).debug("Use [{}] Logger As Default.", logFactory.logFramworkName);
                    }
                }
            }
        }

        return logFactory;
    }
    //------------------------------------------------------------------------- Static end
}
