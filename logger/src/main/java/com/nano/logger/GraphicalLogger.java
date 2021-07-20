package com.nano.logger;
import android.annotation.NonNull;
import com.nano.logger.config.LoggerConfiguration;
import com.nano.logger.format.MessageFormater;
import com.nano.logger.util.PrettyTable;
import com.nano.logger.util.PrettyTable.Section;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Grahical decoration and additional information for log.
 */
public class GraphicalLogger extends ILogger {
	
	private static final StackTraceElement[] EMPTY_STACK_ELEMENT = new StackTraceElement[0] ;
	
	private static final String[] IGNORE_CLASS_OF_TRACE_ELEMENT = {
		GraphicalLogger.class.getName(),
		Logger.class.getName(),
		Printer.class.getName(),
	} ;
	
	private LoggerConfiguration config ;
	
	@Override
	public void log(int level, String tag, String message) {
		config = LoggerConfiguration.getInstance() ;
		logger.log(level, tag, wrap(level, tag, message)) ;
	}
	
	private String wrap(int level,String tag,String message) {
		PrettyTable prettyTable = new PrettyTable() ;
		if(config.isPrintHeader()){
			writeHeader(prettyTable.addNewSection(), tag, level) ;
		}
		
		if(config.isPrintMethodStackInfo()){
			writeMethodStackInfo(prettyTable.addNewSection()) ;
		}
		
		prettyTable.addNewSection().append(message) ;
		
		return prettyTable.toString(true) ;
	}

	private void writeHeader(Section out, String tag, int level) {
		for(String line : getHeaderLines(tag, level)){
			out.appendNewLine(line) ;
		}
	}
	
	private void writeMethodStackInfo(Section out) {
		out.appendNewLine(config.getMessageFormater()
			.formatMethodStackInfo(getStackTraceElements())
		) ;
	}
	
	private List<String> getHeaderLines(String tag, int level) {
		ArrayList<String> headerLines = new ArrayList<>() ;
		MessageFormater formater = config.getMessageFormater() ;
		if (config.isPrintThreadInfo()) {
			headerLines.add(formater.formatThreadInfo(Thread.currentThread())) ;
		}
		if (config.isPrintTagAndLevel()){
			headerLines.add(formater.formatTagAndLevel(tag, level)) ;
		}
		return headerLines ;
	}
	
	@NonNull
	private StackTraceElement[] getStackTraceElements(){
		StackTraceElement[] elements = Thread.currentThread().getStackTrace() ;
		if(elements == null) {
			return EMPTY_STACK_ELEMENT ;
		}
		
		return Arrays.<StackTraceElement>stream(elements)
			.skip(2)
			.filter(element -> isFilterTraceElement(element))
			.limit(config.getStackTraceInfoDeep())
			.toArray(StackTraceElement[] :: new) ;
	}
	
	public boolean isFilterTraceElement(StackTraceElement element) {
		return Arrays.binarySearch(IGNORE_CLASS_OF_TRACE_ELEMENT, element.getClassName()) < 0 ;
	}
}
