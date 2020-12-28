package com.nano.logger;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import com.nano.logger.config.LoggerConfiguration;
import com.nano.logger.filter.LogFilter;
import com.nano.logger.format.MessageFormater;
import com.nano.logger.listener.DiskOutputLogger;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static android.util.Log.*;

/**
 * Simple and pretty logger.
 *
 * Created by nano1 on 20/5/12
 */
public class Logger {
	
	private static Context context ;
	private Logger() {}

	@NonNull
	public static LoggerConfiguration getConfiguation(){
		return LoggerConfiguration.getInstance() ;
	}
	
	public static void init(Application applicationContext){
		if(context != null){
			throw new IllegalStateException("The Logger has initialized.") ;
		}
		context = applicationContext ;
		DiskOutputLogger.register(applicationContext) ;
	}
	
	public static Context getContext() {
		return context;
	}
	
	public static void v(Object message, Object... args){
		vTag(getConfiguation().getTag(), message, args) ;
	}

	public static void vTag(String tag, Object message, Object... args){
		Printer.print(VERBOSE, tag, Printer.TYPE_OBJECT, message, args) ;
	}
	
	public static void d(Object message, Object... args){
		dTag(getConfiguation().getTag(), message, args) ;
	}

	public static void dTag(String tag, Object message, Object... args){
		Printer.print(DEBUG, tag, Printer.TYPE_OBJECT, message, args) ;
	}

	public static void i(Object message, Object... args){
		iTag(getConfiguation().getTag(), message, args) ;
	}

	public static void iTag(String tag, Object message, Object... args){
		Printer.print(INFO, tag, Printer.TYPE_OBJECT, message, args) ;
	}

	public static void w(Object message, Object... args){
		wTag(getConfiguation().getTag(), message, args) ;
	}

	public static void wTag(String tag, Object message, Object... args){
		Printer.print(WARN, tag, Printer.TYPE_OBJECT, message, args) ;
	}

	public static void e(Object message, Object... args){
		eTag(getConfiguation().getTag(), message, args) ;
	}

	public static void eTag(String tag, Object message, Object... args){
		Printer.print(ERROR, tag, Printer.TYPE_OBJECT, message, args) ;
	}
	
	public static void json(String json){
		json(getConfiguation().getTag(), json) ;
	}
	
	public static void json(String tag, String json){
		json(INFO, tag, json) ;
	}
	
	public static void vJson(String json) {
		vJson(getConfiguation().getTag(), json) ;
	}
	
	public static void vJson(String tag, String json) {
		json(VERBOSE, tag, json) ;
	}
	
	public static void dJson(String json) {
		dJson(getConfiguation().getTag(), json) ;
	}

	public static void dJson(String tag, String json) {
		json(DEBUG, tag, json) ;
	}
	
	public static void wJson(String json) {
		wJson(getConfiguation().getTag(), json) ;
	}

	public static void wJson(String tag, String json) {
		json(WARN, tag, json) ;
	}
	
	public static void eJson(String json) {
		eJson(getConfiguation().getTag(), json) ;
	}

	public static void eJson(String tag, String json) {
		json(ERROR, tag, json) ;
	}
	
	public static void json(int level, String tag, String json){
		Printer.print(level, tag, Printer.TYPE_JSON ,json) ;
	}
	
	public static void xml(String xml){
		xml(getConfiguation().getTag(), xml) ;
	}
	
	public static void xml(String tag, String xml){
		xml(INFO, tag, xml) ;
	}
	
	public static void dXml(String xml){
		dXml(getConfiguation().getTag(), xml) ;
	}

	public static void dXml(String tag, String xml){
		xml(DEBUG, tag, xml) ;
	}
	
	public static void vXml(String xml){
		vXml(getConfiguation().getTag(), xml) ;
	}

	public static void vXml(String tag, String xml){
		xml(VERBOSE, tag, xml) ;
	}
	
	public static void wXml(String xml){
		wXml(getConfiguation().getTag(), xml) ;
	}

	public static void wXml(String tag, String xml){
		xml(WARN, tag, xml) ;
	}
	
	public static void eXml(String xml){
		eXml(getConfiguation().getTag(), xml) ;
	}

	public static void eXml(String tag, String xml){
		xml(ERROR, tag, xml) ;
	}
	
	public static void xml(int level, String tag, String xml){
		Printer.print(level, tag, Printer.TYPE_XML , xml) ;
	}
}
