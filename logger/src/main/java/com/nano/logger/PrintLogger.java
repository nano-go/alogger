package com.nano.logger;

public class PrintLogger extends ILogger {
	@Override
	public void log(int level, String tag, String message) {
		android.util.Log.println(level, tag, message) ;
	}
}
