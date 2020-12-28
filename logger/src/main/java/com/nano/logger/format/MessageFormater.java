package com.nano.logger.format;

import androidx.annotation.NonNull;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * This class is for formatting log messages.
 */
public interface MessageFormater {

	public String formatTagAndLevel(String tag, int level) ;
	public String formatArray(Object array) ;
	public String formatThrowable(Throwable throwable) ;
	public String formatDate(Date date) ;
	public String formatTemporal(Temporal temporal)  ;
	public String formatMap(Map map) ;
	public String formatCollection(Collection collection) ;
	public String formatJson(String json) ;
	public String formatXml(String xml) ;

	public String formatUnkownType(Object other) ;

	public String formatThreadInfo(Thread thread) ;
	public String formatMethodStackInfo(@NonNull StackTraceElement[] elements) ;
}
