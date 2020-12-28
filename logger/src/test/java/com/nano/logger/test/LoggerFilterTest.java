package com.nano.logger.test;

import com.nano.logger.Logger;
import com.nano.logger.config.LoggerConfiguration;
import com.nano.logger.filter.LevelFilter;
import com.nano.logger.listener.LoggerListener;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.nano.logger.filter.LevelFilter.* ;

public class LoggerFilterTest {

	@Test public void testFilter() {
		LoggerConfiguration config = Logger.getConfiguation() ;
		TestOutputListener test = new TestOutputListener() ;
		config.registerListener(test) ;

		LevelFilter filter = new LevelFilter(LevelFilter.RELEASE_VERSION) ;
		config.setLogFilter(filter) ;

		shouldAcceptLevels(DEBUG_VERSION, "debug version", filter, test) ;
		shouldAcceptLevels(RELEASE_VERSION, "release version", filter, test) ;

		shouldAcceptLevels(LEVEL_DEBUG, "level_debug", filter, test) ;
		shouldAcceptLevels(LEVEL_VERBOSE, "level_verbose", filter, test) ;
		shouldAcceptLevels(LEVEL_INFO, "level_info", filter, test) ;
		shouldAcceptLevels(LEVEL_WARN, "level_warn", filter, test) ;
		shouldAcceptLevels(LEVEL_ERROR, "level_error", filter, test) ;
		
		config.setLogFilter(null) ;
	}

	private static void shouldAcceptLevels(int levelMask, String message, LevelFilter filter, TestOutputListener test) {
		filter.setPriority(levelMask) ;
		Logger.d(message) ;
		test.assertAccepted((levelMask & LEVEL_DEBUG) != 0) ;
		Logger.dJson(message) ;
		test.assertAccepted((levelMask & LEVEL_DEBUG) != 0) ;
		Logger.dXml(message) ;
		test.assertAccepted((levelMask & LEVEL_DEBUG) != 0) ;


		Logger.v(message) ;
		test.assertAccepted((levelMask & LEVEL_VERBOSE) != 0) ;
		Logger.vJson(message) ;
		test.assertAccepted((levelMask & LEVEL_VERBOSE) != 0) ;
		Logger.vXml(message) ;
		test.assertAccepted((levelMask & LEVEL_VERBOSE) != 0) ;

		Logger.i(message) ;
		test.assertAccepted((levelMask & LEVEL_INFO) != 0) ;
		Logger.json(message) ;
		test.assertAccepted((levelMask & LEVEL_INFO) != 0) ;
		Logger.xml(message) ;
		test.assertAccepted((levelMask & LEVEL_INFO) != 0) ;

		Logger.w(message) ;
		test.assertAccepted((levelMask & LEVEL_WARN) != 0) ;
		Logger.wJson(message) ;
		test.assertAccepted((levelMask & LEVEL_WARN) != 0) ;
		Logger.wXml(message) ;
		test.assertAccepted((levelMask & LEVEL_WARN) != 0) ;

		Logger.e(message) ;
		test.assertAccepted((levelMask & LEVEL_ERROR) != 0) ;
		Logger.eJson(message) ;
		test.assertAccepted((levelMask & LEVEL_ERROR) != 0) ;
		Logger.eXml(message) ;
		test.assertAccepted((levelMask & LEVEL_ERROR) != 0) ;
	}

	public static class TestOutputListener implements LoggerListener {
		public int shouldLevel = -1 ;
		public String shouldTag ;
		public String shouldLog ;

		public boolean isAccepted ;

		@Override
		public void onOutput(int level, String tag, String log) {
			isAccepted = true ;
			if (shouldTag != null) {
				assertEquals(shouldTag, tag) ;
			}
			if (shouldLog != null) {
				assertEquals(shouldLog, log) ;
			}
			if (shouldLevel != -1) {
				assertEquals(shouldLevel, level) ;
			}
		}

		public void assertAccepted(boolean shouldAccept) {
			assertEquals(shouldAccept, isAccepted) ;
			isAccepted = false ;
		}
	}
}
