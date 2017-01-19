package com.silentgo.utils.log.dialect.console;


import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

/**
 * 利用System.out.println()打印日志
 * @author Looly
 *
 */
public class ConsoleLogFactory extends LogFactory {
	
	public ConsoleLogFactory() {
		super("Hutool Console Logging");
	}

	@Override
	public Log getLog(String name) {
		return new ConsoleLog(name);
	}

	@Override
	public Log getLog(Class<?> clazz) {
		return new ConsoleLog(clazz);
	}

}
