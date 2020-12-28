package com.nano.logger.test;
import com.nano.logger.Logger;
import com.nano.logger.listener.DiskOutputLogger;
import com.nano.logger.listener.LoggerListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class DiskOutputTest {

	public static final int BUFFER_SIZE = 1024 * 32 ;
	
	DiskOutputLogger output = Logger.getConfiguation().getDiskOutputLogger() ;

	FilenameFilter testFileNameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File f, String name) {
			return name.matches("TestLog\\d+");
		}
	} ;

	int expectedSize ;
	LoggerListener calculatingSize = new LoggerListener() {
		@Override
		public void onOutput(int level, String tag, String log) {
			expectedSize += (log + "\n").getBytes().length ;
		}
	} ;
	
	@Rule public TemporaryFolder temporaryFolder = new TemporaryFolder() ;

	@Before public void configOutputStrategy() {	
		Logger.getConfiguation().registerListener(calculatingSize) ;
		Logger.getConfiguation().setStackTraceInfoDeep(500) ;
		output.setBufferSize(BUFFER_SIZE) ;
		output.setLogFileFilter(testFileNameFilter) ;	
		output.setLogFileNameGenerator(new DiskOutputLogger.FileNameGenerator(){
			int i = 0 ;
			@Override
			public String generateFileName(String tag, String message) {
				return "TestLog" + (i ++) ;
			}
		}) ;
	}

	@Test public void testDiskOutputStrategy() throws IOException, InterruptedException {
		File tempFolder = temporaryFolder.newFolder("log") ;
		output.setOutputDirectory(tempFolder) ;
		final int printingCount = 100 ;
		assertOutputToDisk(printingCount, tempFolder) ;
	}

	private void assertOutputToDisk(int printingCount, File tempFolder) throws InterruptedException {
		for (int i = 0; i < printingCount; i ++) {
			Logger.i("Hello World") ;
		}

		// async output buffer to disk
		output.writeBufferToDisk() ;
		Thread.sleep(10) ;

		assertEquals(expectedSize, output.logFilesSize()) ;

		for(String name : tempFolder.list()) {
			assertTrue(testFileNameFilter.accept(null, name)) ;
		}

		output.deleteLogFilesSync() ;
		assertTrue(tempFolder.list() == null || tempFolder.list().length == 0) ;
		
		expectedSize = 0 ;
	}
	
	@After public void resetConfig() {
		Logger.getConfiguation().unregisterListener(calculatingSize) ;
		output.setLogFileFilter(null) ;
		output.setLogFileNameGenerator(null) ;
		output.setOutputDirectory(null) ;
	}
}
