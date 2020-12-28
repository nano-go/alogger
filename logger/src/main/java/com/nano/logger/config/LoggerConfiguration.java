package com.nano.logger.config;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import com.nano.logger.GraphicalLogger;
import com.nano.logger.ILogger;
import com.nano.logger.LongLogger;
import com.nano.logger.NotifyListenersLogger;
import com.nano.logger.PrintLogger;
import com.nano.logger.filter.LogFilter;
import com.nano.logger.format.DefaultMessageFormater;
import com.nano.logger.format.MessageFormater;
import com.nano.logger.listener.DiskOutputLogger;
import com.nano.logger.listener.LoggerListener;

public class LoggerConfiguration {
	
	public static final String DEFAULT_TAG = "Logger" ;
	public static final int DEFAULT_LOG_MAX_LENGTH = 6000 ; 
	
	private static final DiskOutputLogger DISK_OUTPUT_LOGGER_LISTENER = new DiskOutputLogger() ; 
	
	private MessageFormater mMessageFormater ;
	private LogFilter mLogFilter ;
	private String mDefaultTag ;
	private boolean isPrintHeader ;
	private boolean isPrintMethodStackInfo ;
	private boolean isPrintThreadInfo ;
	private boolean isPrintTagAndLevel ;
	private int stackTraceInfoDeep = 5 ;
	
	private ILogger mLogger ;
	private NotifyListenersLogger mNotifyListenerLogger ;
	
	private int mLogMaxLength ;
	
	private LoggerConfiguration(){
		this.mMessageFormater = new DefaultMessageFormater() ;
		this.mDefaultTag = DEFAULT_TAG ;
		this.mLogMaxLength = DEFAULT_LOG_MAX_LENGTH ;
		this.isPrintHeader = true ;
		this.isPrintMethodStackInfo = true ;
		this.isPrintThreadInfo = true ;
		this.isPrintTagAndLevel = true ;
		this.mLogger =  new GraphicalLogger() ;
		this.mNotifyListenerLogger = new NotifyListenersLogger() ;
		
		this.mLogger.next(mNotifyListenerLogger)
			.next(new LongLogger())
			.next(new PrintLogger()) ;
	}

	public LoggerConfiguration setStackTraceInfoDeep(int stackTraceInfoDeep) {
		this.stackTraceInfoDeep = stackTraceInfoDeep <= 0 ? 1 : stackTraceInfoDeep;
		return this ;
	}

	public int getStackTraceInfoDeep() {
		return stackTraceInfoDeep;
	}

	public LoggerConfiguration setIsPrintTagAndLevel(boolean isPrint) {
		this.isPrintTagAndLevel = isPrint ;
		return this ;
	}
	
	public boolean isPrintTagAndLevel() {
		return isPrintTagAndLevel;
	}
	
	public LoggerConfiguration setIsPrintMethodStackInfo(boolean isPrintMethodStackInfo) {
		this.isPrintMethodStackInfo = isPrintMethodStackInfo;
		return this ;
	}

	public boolean isPrintMethodStackInfo() {
		return isPrintMethodStackInfo;
	}

	public LoggerConfiguration setIsPrintThreadInfo(boolean isPrintThreadInfo) {
		this.isPrintThreadInfo = isPrintThreadInfo;
		return this;
	}

	public boolean isPrintThreadInfo() {
		return isPrintThreadInfo;
	}

	public LoggerConfiguration setIsPrintHeader(boolean isPrintHeader) {
		this.isPrintHeader = isPrintHeader;
		return this ;
	}

	public boolean isPrintHeader() {
		return isPrintHeader;
	}
	
	public LoggerConfiguration setLogFilter(@Nullable LogFilter logFilter) {
		this.mLogFilter = logFilter;
		return this ;
	}

	@Nullable
	public LogFilter getLogFilter() {
		return mLogFilter;
	}

	public LoggerConfiguration registerListener(@NonNull LoggerListener listener) {
		mNotifyListenerLogger.registerListener(listener) ;
		return this ;
	}
	
	public LoggerConfiguration registerListener(@Nullable LifecycleOwner lifecycleOwner, @NonNull LoggerListener listener) {
		mNotifyListenerLogger.registerListener(lifecycleOwner, listener) ;
		return this ;
	}
	
	public LoggerConfiguration unregisterListener(@NonNull LoggerListener listener){
		mNotifyListenerLogger.unregisterListener(listener) ;
		return this ;
	}
	
	public boolean hasAddedLoggerListener(@NonNull LoggerListener listener) {
		if(listener == null) return false ;
		return this.mNotifyListenerLogger.hasAddedLoggerListener(listener) ;
	}
	
	@NonNull
	public DiskOutputLogger getDiskOutputLogger(){
		return DISK_OUTPUT_LOGGER_LISTENER ;
	}

	public LoggerConfiguration setLogMaxLength(int logMaxLength) {
		if(logMaxLength <= 0) logMaxLength = DEFAULT_LOG_MAX_LENGTH ;
		this.mLogMaxLength = logMaxLength;
		return this ;
	}

	public int getLogMaxLength() {
		return mLogMaxLength;
	}

	public LoggerConfiguration setTag(String mDefaultTag) {
		this.mDefaultTag = mDefaultTag == null ? "" : mDefaultTag;
		return this ;
	}

	public String getTag() {
		return mDefaultTag;
	}

	public LoggerConfiguration setMessageFormater(@Nullable MessageFormater messageFormater) {	
		this.mMessageFormater = messageFormater == null ? new DefaultMessageFormater() : messageFormater ;
		return this ;
	}
	
	@NonNull
	public MessageFormater getMessageFormater() {
		return mMessageFormater;
	}
	
	public ILogger getLogger(){
		return mLogger ;
	}
	
	public static LoggerConfiguration mConfiguration = new LoggerConfiguration() ;

	public static LoggerConfiguration getInstance(){
		return mConfiguration ;
	}
}
