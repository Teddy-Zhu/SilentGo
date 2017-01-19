package com.silentgo.utils.log.dialect.log4j;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

/**
 * <a href="http://logging.apache.org/log4j/1.2/index.html">Apache Log4J</a> log.<br>
 *
 * @author Looly
 */
public class Log4jLogFactory extends LogFactory {

    public Log4jLogFactory() {
        super("Log4j");
    }

    @Override
    public Log getLog(String name) {
        return new Log4jLog(name);
    }

    @Override
    public Log getLog(Class<?> clazz) {
        return new Log4jLog(clazz);
    }

}
