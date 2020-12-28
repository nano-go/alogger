package com.nano.logger.demo;

import android.app.Application;
import com.nano.logger.Logger;
import com.nano.logger.filter.LevelFilter;
import com.nano.logger.listener.DiskOutputLogger;
import com.nano.logger.listener.LoggerListener;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.init(this) ;
		
		Logger.getConfiguation()
			.setTag("Default Tag")
			// Sets a log filter used for only accepting the error and warn log.
			.setLogFilter(new LevelFilter(LevelFilter.LEVEL_ERROR | LevelFilter.LEVEL_WARN))
			// Don't print the header of message which contains Thread-Info, Tag and Level.
			.setIsPrintHeader(false)
			// Don't print the tag and the level of the log.
			.setIsPrintTagAndLevel(false)
			// Don't print the thread info of printing that log.
			.setIsPrintThreadInfo(false)
			// Don't print the information of the method stack.
			.setIsPrintMethodStackInfo(false)
			// Sets the maximum deep of showing the trace stack.
			// This value at most 9
			.setStackTraceInfoDeep(4)
			// Sets the maximum length of the message each printing.
			.setLogMaxLength(5000)
			.registerListener(new LoggerListener(){
				@Override
				public void onOutput(int level, String tag, String log) {
					// to handle
				}
			}) ;
			
		Logger.getConfiguation()
			.getDiskOutputLogger()
			.setOutputDirectory(getExternalFilesDir("log"))
			.setEnable(true)
			// This filter is used for deleting log files and calculating the
			// size of log files.
			.setLogFileFilter(null)
			// Returns the total size of the files "outputDirectory.list(LogFileFilter)" 
			// .logFilesSize()
			.setBufferSize(1024 * 8)
			.clearBuffer()
			.deleteLogFilesAsync()
			.setLogFileNameGenerator(new DiskOutputLogger.FileNameGenerator(){
				int i = 0 ;
				@Override
				public String generateFileName(String tag, String message) {
					return "LogFile" + (i ++); 
				}
			}) ;
			
	}
}
