package com.nano.logger;

import androidx.annotation.IntDef;
import com.nano.logger.filter.LogFilter;
import com.nano.logger.format.MessageFormater;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class Printer {
	public static final String TAG = "Printer";
	
	public static final int TYPE_OBJECT = 0 ;
	public static final int TYPE_XML = 1 ;
	public static final int TYPE_JSON = 2 ;

	@IntDef({ TYPE_OBJECT, TYPE_XML, TYPE_JSON })
	@Retention(RetentionPolicy.SOURCE)
	public @interface MessageType{}
	
	public static void print(int level, String tag, @MessageType int messageType, Object message, Object... args) {
		if(!filter(level, tag)) {
			return ;
		}
		switch(messageType) {
			case TYPE_JSON: 
				printJson(level, tag, message, args) ;
				break ;
			case TYPE_XML:
				printXml(level, tag, message, args) ;
				break ;
			case TYPE_OBJECT:
				printObject(level, tag, message, args) ;
				break ;
			default:
				throw new IllegalStateException("Message Type:" + messageType) ;
		}
	}

	private static void printJson(int level, String tag, Object message, Object... args) {
		MessageFormater formater = Logger.getConfiguation().getMessageFormater() ;
		String messageStr = String.format((String)message, args) ;
		log(level, tag, formater.formatJson(messageStr)) ;
	}
	
	private static void printXml(int level, String tag, Object message, Object... args) {
		MessageFormater formater = Logger.getConfiguation().getMessageFormater() ;
		String messageStr = String.format((String)message, args) ;
		log(level, tag, formater.formatXml(messageStr)) ;
	}
	
	private static void printObject(int level, String tag, Object message, Object... args) {
		log(level, tag, String.format(formatObject(message), args)) ;
	}
	
	private static String formatObject(Object obj) {
		MessageFormater formater = Logger.getConfiguation().getMessageFormater() ;
		if(obj.getClass().isArray()){
			return formater.formatArray(obj) ;
		}else if(obj instanceof Collection){
			return formater.formatCollection((Collection)obj) ;
		}else if(obj instanceof Map){
			return formater.formatMap((Map)obj) ;
		}else if(obj instanceof Throwable){
			return formater.formatThrowable((Throwable)obj) ;
		}else if(obj instanceof Date){
			return formater.formatDate((Date)obj) ;
		}else if(obj instanceof Temporal){
			return formater.formatTemporal((Temporal)obj) ;
		}else if(obj instanceof String) {
			return (String)obj ;
		}
		return formater.formatUnkownType(obj) ;
	}
	
	private static void log(int level, String tag, String message) {
		Logger.getConfiguation().getLogger().log(level, tag, message) ;
	}

	private static boolean filter(int level, String tag) {
		LogFilter filter = Logger.getConfiguation().getLogFilter() ;
		return filter == null || filter.accept(level, tag) ;
	}
}
