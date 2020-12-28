package com.nano.logger.filter;

/**
 * @see LevelFilter
 */
public interface LogFilter {
	public boolean accept(int level, String tag) ;
}
