package com.silentgo.utils.log;

import java.io.Serializable;

import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.level.Level;
/**
 * from looly
 * LICENSE : Apache 2.0
 * https://raw.githubusercontent.com/looly/hutool/master/LICENSE.txt
 */

/**
 * 抽象日志类<br>
 * 实现了一些通用的接口
 * 
 * @author Looly
 *
 */
public abstract class AbstractLog implements Log, Serializable{
	private static final long serialVersionUID = -3211115409504005616L;
	
	@Override
	public boolean isEnabled(Level level) {
		switch (level) {
			case TRACE:
				return isTraceEnabled();
			case DEBUG:
				return isDebugEnabled();
			case INFO:
				return isInfoEnabled();
			case WARN:
				return isWarnEnabled();
			case ERROR:
				return isErrorEnabled();
			default:
				throw new Error(StringKit.format("Can not identify level: {}", level));
		}
	}

	@Override
	public void trace(Throwable t) {
		this.trace(t, t.getMessage());
	}
	
	@Override
	public void debug(Throwable t) {
		this.debug(t, t.getMessage());
	}
	
	@Override
	public void info(Throwable t) {
		this.info(t, t.getMessage());
	}
	
	@Override
	public void warn(Throwable t) {
		this.warn(t, t.getMessage());
	}
	
	@Override
	public void error(Throwable t) {
		this.error(t, t.getMessage());
	}
}
