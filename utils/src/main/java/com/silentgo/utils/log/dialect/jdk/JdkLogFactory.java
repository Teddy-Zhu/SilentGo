package com.silentgo.utils.log.dialect.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

/**
 * JDK日志工厂类
 * <a href="http://java.sun.com/javase/6/docs/technotes/guides/logging/index.html">java.util.logging</a> log.
 *
 * @author Looly
 */
public class JdkLogFactory extends LogFactory {

    public JdkLogFactory() {
        super("JDK Logging");
        readConfig();
    }

    @Override
    public Log getLog(String name) {
        return new JdkLog(name);
    }

    @Override
    public Log getLog(Class<?> clazz) {
        return new JdkLog(clazz);
    }

    /**
     * 读取ClassPath下的logging.properties配置文件
     */
    private void readConfig() {
        //避免循环引用，Log初始化的时候不使用相关工具类
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties");
        if (null == in) {
            System.err.println("not found logging.properties, use internal config!");
            return;
        }

        try {
            LogManager.getLogManager().readConfiguration(in);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                LogManager.getLogManager().readConfiguration();
            } catch (Exception e1) {
                e.printStackTrace();
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
