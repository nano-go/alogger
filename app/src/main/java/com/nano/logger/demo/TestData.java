package com.nano.logger.demo;
import java.lang.reflect.Array;
import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;

public class TestData {
	
	public static final String[][] LANGUAGES = {
		{"Java", "Python", "JavaScript", "Kotlin"},
		{"C", "C ++", "Go"}
	} ;
	
	public static final String JSON_DATA = 
		"{song_list:[" + 
			"{title:\"Something Just Like this\", duration:25555, album:{id:0, name:\"Unknown\"}}," + 
			"{title:\"See you again\", duration:23555, album:{id:1, name:\"Unknown\"}}" +
		"]}" ;

	public static final String XML_DATA = 
		"<root>" +
			"<configPath>/root/usr/config/config.json</configPath>" +
			"<layout height=\"50dp\" width=\"50dp\">" +
				"<img height=\"20dp\" width=\"auto\" center=\"true\">Icon</img>" +
				"<text>Candy</text>" +
			"</layout>" +
		"</root>";

	public static TreeMap<String, String> randomTreeMap() {
		TreeMap<String, String> treeMap = new TreeMap<>() ;
		int length = (int)(5 + Math.random() * 10) ;
		for(int i = 0; i < length; i ++) {
			treeMap.put(randomString(), randomString()) ;
		}
		return treeMap ;
	}

	public static List<String> randomList() {
		ArrayList<String> list = new ArrayList<>() ;
		int length = (int)(5 + Math.random() * 10) ;
		for(int i = 0; i < length; i ++) {
			list.add(randomString()) ;
		}
		return list ;
	}
	
	public static String randomString() {
		int length = (int)(2 + Math.random() * 10) ;
		StringBuilder str = new StringBuilder() ;
		for(int i = 0; i < length; i ++) {
			str.append((char)('a' + (char)(Math.random() * 26))) ;
		}
		return str.toString() ;
	}
	
	public static int[] randomIntArray(int length, int from, int to) {
		int[] randomDataArray = new int[length] ;
		for(int i = 0; i < randomDataArray.length; i ++){
			randomDataArray[i] = (int)(from + Math.random() * (to - from)) ;
		}
		return randomDataArray ;
	}
	
	public static Object randomMultiDimensionalIntArray(int dimensions, int length, int rangeFrom, int rangeTo) {
		if(dimensions <= 1) {
			return randomIntArray(length, rangeFrom, rangeTo) ;
		}
		
		int[] dimensionsArr = new int[dimensions] ;
		for(int i = 0;i < dimensionsArr.length;i ++) {
			dimensionsArr[i] = length ;
		}
		
		Object arr = Array.newInstance(int.class, dimensionsArr) ;
		
		return assignRandomIntInfo(arr, length, rangeFrom, rangeTo) ;
	}

	private static Object assignRandomIntInfo(Object arrObject, int length, int rangeFrom, int rangeTo) {
		if(arrObject instanceof int[]) {
			return randomIntArray(length, rangeFrom, rangeTo) ;
		}
		Object[] arr = (Object[]) arrObject ;
		for(int i = 0; i < arr.length; i ++) {
			arr[i] = assignRandomIntInfo(arr[i], length, rangeFrom, rangeTo) ;	
		}
		return arr ;
	}
	
}
