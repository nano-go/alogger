package android.util;

/**
 * see https://stackoverflow.com/questions/36787449/how-to-mock-method-e-in-log
 */ 
public class Log {
	
	public static final int VERBOSE = 0 ;
	public static final int DEBUG = 1 ;
	public static final int INFO = 2 ;
	public static final int WARN = 3 ;
	public static final int ERROR = 4 ;
	
	public static final int ASSERT = 5 ;
	
    public static int println(int level, String tag, String msg) {
		System.out.println(msg) ;
		return 0 ;
	}
	
	public static int e(String tag, String msg) {
		System.out.println(msg) ;
		return 0 ;
	}
}
