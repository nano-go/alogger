package com.nano.logger.listener;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.nano.logger.AppToBackgroundCallback;
import com.nano.logger.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is a default logger listener for outputing the logs to the disk.
 *
 * If you want to disenable this logger listener, you can use the method
 * {@link DiskOutputLoggerListener#setEnable(false)} to close it.
 */
public class DiskOutputLogger implements LoggerListener {

	protected static final String TAG = "DiskOutput" ;
	
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 8 ;
	private static final int OBJECT_POOL_SIZE = 50 ;

	private static final FileNameGenerator DEFAULT_FILE_NAME_GENERATOR = new FileNameGenerator(){
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") ;
		@NonNull
		public String generateFileName(String tag, String message) {
			return dateFormat.format(LocalDateTime.now()) + ".log" ;
		}
	} ;

	private static final FilenameFilter LOG_FILE_NAME_FILTER = new FilenameFilter(){
		@Override
		public boolean accept(File file, String name) {
			return name.endsWith(".log") ;
		}
	} ;
	
	public static void register(final Application app) {
		app.registerActivityLifecycleCallbacks(
			new AppToBackgroundCallback(){
				@Override
				public void onToBackground() {
					Logger.getConfiguation().getDiskOutputLogger().writeBufferToDisk() ;
				}
			}
		) ;
	}

	private FileNameGenerator fileNameGenerator ;
	private FilenameFilter logFileFilter ;
	private File outputDirectory ;

	/**
	 * The task of outputting the buffer is executed by the {@code ThreadPool}, 
	 * A single thread executor, that ensures a task is executed by a only thread.
	 * so that ensures all the messages in the outputted log file are ordered.
	 */
	private ExecutorService executor ;

	private ConcurrentLinkedQueue<BufferedLog> buffer ;
	private BufferedLogObjectPool bufferedLogObjectPool ;
	private int maxBufferBytes ;
	private int curBufferBytes ;

	private boolean enable ;

	public DiskOutputLogger() {
		this.enable = true ;
		this.executor = Executors.newSingleThreadExecutor() ;
		this.maxBufferBytes = DEFAULT_BUFFER_SIZE ;
		this.fileNameGenerator = DEFAULT_FILE_NAME_GENERATOR ;
		this.logFileFilter = LOG_FILE_NAME_FILTER ;
		this.buffer = new ConcurrentLinkedQueue<>() ;
		this.bufferedLogObjectPool = new BufferedLogObjectPool(OBJECT_POOL_SIZE) ;
		Context context = Logger.getContext() ;
		if (context != null) {
			this.outputDirectory = context.getExternalFilesDir("log") ;
		}
	}

	@Override
	public void onOutput(int level, String tag, String log) {
		if (!enable || executor == null || isInvalidOutputDirectory()) {
			return ;
		}
		buffer.offer(bufferedLogObjectPool.fetchLog(tag, log)) ;
		curBufferBytes += log.length() * 2 ;
		if (curBufferBytes >= maxBufferBytes) {
			writeBufferToDisk(); 
		}
	}

	private boolean isInvalidOutputDirectory() {
		return outputDirectory == null || outputDirectory.isFile() ;
	}

	public boolean writeBufferToDisk() {
		if (buffer.isEmpty()) {
			return false ;
		}
		curBufferBytes = 0 ;
		executor.execute(() -> {
			try {
				while (!buffer.isEmpty()) {
					BufferedLog log = buffer.poll() ;
					writeLog(log.tag, log.message + "\n") ;
					bufferedLogObjectPool.recycle(log) ;
				}
			} catch (Exception e) {
				android.util.Log.e(TAG, e.toString()) ;
			}
		}) ;
		return true ;
	}

	private void writeLog(String tag, String message) throws IOException {
		File logFile = generateLogFile(tag, message) ;
		if (logFile == null) {
			return ;
		}
		BufferedWriter bw = null ;
		try {
			bw = new BufferedWriter(new FileWriter(logFile, true)) ;
			bw.write(message) ;
			bw.flush() ;
		} catch (IOException ex) {
			android.util.Log.e(TAG, ex.toString()) ;
		} finally {
			if (bw != null) {
				bw.close() ;
			}
		}
	}

	@Nullable
	private File generateLogFile(String tag, String message) throws IOException {
		if (outputDirectory == null) {
			return null ;
		}

		if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
			return null ;
		}

		String fileName = fileNameGenerator.generateFileName(tag, message) ;
		File logFile = new File(outputDirectory, fileName) ;

		if (!logFile.exists() && !logFile.createNewFile()) {
			return null ;
		}
		return logFile;
	}

	public DiskOutputLogger setLogFileNameGenerator(@Nullable FileNameGenerator generator) {
		this.fileNameGenerator = generator == null ? DEFAULT_FILE_NAME_GENERATOR : generator ;
		return this ;
	}

	public FileNameGenerator getFileNameGenerator() {
		return this.fileNameGenerator ;
	}
	
	public DiskOutputLogger setOutputDirectory(@Nullable File dir) {
		this.outputDirectory = dir ;
		if (dir == null) {
			clearBuffer() ;
		}
		return this ;
	}

	@Nullable
	public File getOutputDirectory() {
		return this.outputDirectory ;
	}
	
	public DiskOutputLogger setBufferSize(int bufferBytesSize) {
		this.maxBufferBytes = bufferBytesSize ;
		return this ;
	}

	public DiskOutputLogger clearBuffer() {
		while (!buffer.isEmpty()) {
			if (!bufferedLogObjectPool.recycle(buffer.poll())) {
				break ;
			}
		}
		buffer.clear() ;
		return this ;
	}
	
	public DiskOutputLogger setEnable(boolean enable) {
		this.enable = enable ;
		if (!enable) {
			clearBuffer() ;
		}
		return this ;
	}

	/**
	 * @see #deleteLogFilesSync()
	 * @see #logFileSize()
	 */
	public DiskOutputLogger setLogFileFilter(@Nullable FilenameFilter filter) {
		this.logFileFilter = filter == null ? LOG_FILE_NAME_FILTER : filter ;
		return this ;
	}

	@NonNull
	public FilenameFilter getLogFileFilter() {
		return this.logFileFilter ;
	}

	public DiskOutputLogger deleteLogFilesAsync() {
		if (isInvalidOutputDirectory()) {
			return this ;
		}
		executor.execute(() -> deleteLogFilesSync()) ;
		return this ;
	}

	public DiskOutputLogger deleteLogFilesSync() {
		if (isInvalidOutputDirectory()) {
			return this ;
		}
		File[] listfiles = outputDirectory.listFiles(logFileFilter) ;

		if (listfiles == null) {
			return this ;
		}
		for (File f : listfiles) {
			f.delete() ;
		}
		return this ;
	}

	public long logFilesSize() {
		if (isInvalidOutputDirectory()) {
			return 0 ;
		}
		File[] listfiles = outputDirectory.listFiles(logFileFilter) ;
		if (listfiles == null) {
			return 0 ;
		}
		long size = 0 ;
		for (File f : listfiles) {
			size += f.length() ;
		}
		return size ;
	}

	private static class BufferedLog {
		public String message ; 
		public String tag ;
	}

	private static class BufferedLogObjectPool {

		private BufferedLog[] pool ;
		private int curPoolSize ;

		public BufferedLogObjectPool(int size) {
			pool = new BufferedLog[size] ;
		}

		@NonNull
		public BufferedLog fetchLog(String tag, String log) {
			BufferedLog bufferedLog ;
			synchronized (this) {
				if (curPoolSize == 0) {
					bufferedLog = new BufferedLog() ;
				} else {
					curPoolSize -- ;
					bufferedLog = pool[curPoolSize] ;
					pool[curPoolSize] = null ;
				}
			}
			bufferedLog.tag = tag ;
			bufferedLog.message = log ;
			return bufferedLog ;
		}

		public synchronized boolean recycle(BufferedLog log) {
			if (isInPool(log)) {
				throw new IllegalStateException("Already in the pool!") ;
			}
			if (curPoolSize < pool.length) {
				pool[curPoolSize ++] = log ;
				log.tag = null ;
				log.message = null ;
				return true;
			}
			return false ;
		}

		private boolean isInPool(BufferedLog log) {
			for (BufferedLog inPool : pool) {
				if (inPool == log) {
					return true ;
				}
			}
			return false;
		}
	}

	public interface FileNameGenerator {
		@NonNull
		String generateFileName(String tag, String message) ;
	}
}
