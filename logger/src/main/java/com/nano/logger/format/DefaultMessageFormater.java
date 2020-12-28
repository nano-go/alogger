package com.nano.logger.format;

import androidx.annotation.NonNull;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DefaultMessageFormater implements MessageFormater {

	private DateTimeFormatter mDateTimeFormat ;
	private DateTimeFormatter mDateFormat ;
	private int mIndentSpaces ;
	
	public DefaultMessageFormater(){
		this.mDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") ;
		this.mDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		this.mIndentSpaces = 4 ;
	}

	@Override
	public String formatTagAndLevel(String tag, int level) {
		return String.format(
			"Level: %s Tag: \"%s\"", 
			levelStr(level).toUpperCase(), tag
		) ;
	}
	
	public static String levelStr(int level) {
		switch(level) {
			case android.util.Log.INFO:
				return "info" ;
			case android.util.Log.WARN:
				return "warn" ;
			case android.util.Log.DEBUG:
				return "debug" ;
			case android.util.Log.ERROR:
				return "error" ;
			case android.util.Log.VERBOSE:
				return "verbose" ;
			case android.util.Log.ASSERT:
				return "assert" ;
		}
		return "unknown";
	}
	
	@Override
	public String formatArray(Object array) {
		if(array instanceof int[]){
			return Arrays.toString((int[])array) ;
		}else if(array instanceof short[]){
			return Arrays.toString((short[])array) ;
		}else if(array instanceof byte[]){
			return Arrays.toString((byte[])array) ;
		}else if(array instanceof long[]){
			return Arrays.toString((long[])array) ;
		}else if(array instanceof double[]){
			return Arrays.toString((double[])array) ;
		}else if(array instanceof float[]){
			return Arrays.toString((float[])array) ;
		}else if(array instanceof boolean[]){
			return Arrays.toString((boolean[])array) ;
		}else if(array instanceof char[]){
			return Arrays.toString((char[])array) ;
		}else {
			return deepToString(array, "") ;
		}
	}

	private String deepToString(Object arrayObject, String indent) {
		
		// the case: call by self.
		if(!(arrayObject instanceof Object[])){
			return formatArray(arrayObject) ;
		}
		
		Object[] array = (Object[]) arrayObject ;
		
		if(array.length == 0) {
			return "[]" ;
		}
		
		Class<?> arrType = array.getClass().getComponentType() ;
		if(arrType == null || !arrType.isArray()) {
			return Arrays.deepToString(array) ;
		}
		
		StringBuilder arrayStr = new StringBuilder(indent).append("[\n") ;
		String nIndent = indent + " " ;
		
		int iMax = array.length - 1 ;
		for(int i = 0; ; i ++){
			arrayStr.append(nIndent).append(
				deepToString(array[i], nIndent)
			) ;
			if(i == iMax) {
				break ;
			}
			arrayStr.append(",\n") ;
		}
		
		arrayStr.append('\n').append(indent).append(']') ;
		return arrayStr.toString() ;
	}

	@Override
	public String formatThrowable(Throwable throwable) {
		StringWriter stringWrite = new StringWriter() ;
		PrintWriter printWrite = new PrintWriter(stringWrite) ;
		throwable.printStackTrace(printWrite) ;
		printWrite.close() ;
		return stringWrite.toString() ;
	}

	@Override
	public String formatDate(Date date) {
		return "Date: " + mDateTimeFormat.format(
			Instant.ofEpochMilli(date.getTime()).atZone(ZoneOffset.ofHours(8))) ;
	}

	@Override
	public String formatTemporal(Temporal temporal) {
		if(temporal instanceof LocalDateTime){
			return "LocalDateTime: " + mDateTimeFormat.format(temporal) ;
		}
		if(temporal instanceof LocalDate){
			return "LocalDate: " + mDateFormat.format(temporal) ;
		}
		return temporal.toString() ;
	}

	@Override
	public String formatMap(Map map) {
		return map.toString();
	}

	@Override
	public String formatCollection(Collection collection) {
		return collection.toString() ;
	}

	@Override
	public String formatJson(String json) {
		if(json == null){
			return "null" ;
		}
		json = json.trim() ;
		try {
			if(json.startsWith("{")){
				JSONObject jsonObject = new JSONObject(json) ;
				return jsonObject.toString(mIndentSpaces) ;
			}
			if(json.startsWith("[")){
				JSONArray jsonArray = new JSONArray(json) ;
				return jsonArray.toString(mIndentSpaces) ;
			}
			return json ;
		} catch(JSONException e) {
			return json ;
		}
	}

	@Override
	public String formatXml(String xml) {
		try {
			if(xml == null){
				return "null" ;
			}
			Source source = new StreamSource(new StringReader(xml)) ;
			StreamResult result = new StreamResult(new StringWriter()) ;
			Transformer transformer = TransformerFactory.newInstance().newTransformer() ;
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount",
				String.valueOf(mIndentSpaces)
			) ;
			transformer.transform(source, result) ;
			String formatedXml = result.getWriter().toString() ;
			return formatedXml.replaceFirst(">", ">\n") ;
		} catch (TransformerException e) {
			return xml ;
		}
	}
	
	@Override
	public String formatUnkownType(Object other) {
		return other.toString();
	}
	
	@Override
	public String formatThreadInfo(Thread thread) {
		return "Thread: " + thread.getName();
	}

	@Override
	public String formatMethodStackInfo(@NonNull StackTraceElement[] elements) {
		if(elements.length == 0){
			return "empty method stack." ;
		}
		final StringBuilder stackInfo = new StringBuilder("┌─") ;
		
		boolean limited = elements.length >= 10 ;
		int iMax = limited ? 9 : elements.length - 1;
		for(int i = 0; ;i ++){
			StackTraceElement element = elements[i] ;
			// format: at com.nano.logger.Logger.i(Logger.java:85)
			stackInfo.append(String.format(
				"at %s.%s(%s:%d)",
				element.getClassName(),
				element.getMethodName(),
				element.getFileName(),
				element.getLineNumber()
			)) ;
			
			if(i == iMax){
				break ;
			}
			stackInfo.append("\n├─") ;
		}
		if(limited) {
			stackInfo.append(String.format(
				"\n%d more informations...",
				elements.length - 9
			)) ;
		}
		return stackInfo.toString() ;
	}

	
}
