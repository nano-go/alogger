package com.nano.logger.listener;

public interface LoggerListener {
	public void onOutput(int level, String tag, String log) ;
}
