package com.nano.logger.filter;

import static com.nano.logger.Logger.* ;

public class LevelFilter implements LogFilter {

	public static final int RELEASE_VERSION = 0 ;
	public static final int DEBUG_VERSION = 0x0000FFFF ;
	
	public static final int LEVEL_ASSERT = 1 ;
	public static final int LEVEL_DEBUG = 1 << 1 ;
	public static final int LEVEL_VERBOSE = 1 << 2 ;
	public static final int LEVEL_INFO = 1 << 3 ;
	public static final int LEVEL_WARN = 1 << 4 ;
	public static final int LEVEL_ERROR = 1 << 5 ;
	
	private int levelMask = DEBUG_VERSION ;

	public LevelFilter(int levelMask) {
		this.levelMask = levelMask;
	}

	public LevelFilter setPriority(int levelMask) {
		this.levelMask = levelMask;
		return this ;
	}

	public int getLevelMask() {
		return levelMask;
	}
	
	@Override
	public boolean accept(int level, String tag) {
		if(levelMask == 0) {
			return false ;
		} 
		level = replaceToLevelMask(level) ;
		return (level & levelMask) != 0 ;
	}

	private final int replaceToLevelMask(int androidLevel) {
		switch(androidLevel) {
			case android.util.Log.ASSERT : return LEVEL_ASSERT ;
			case android.util.Log.INFO : return LEVEL_INFO ;
			case android.util.Log.DEBUG : return LEVEL_DEBUG ;
			case android.util.Log.VERBOSE : return LEVEL_VERBOSE ;
			case android.util.Log.WARN : return LEVEL_WARN ;
			case android.util.Log.ERROR : return LEVEL_ERROR;
		}
		return LEVEL_INFO;
	}
}
