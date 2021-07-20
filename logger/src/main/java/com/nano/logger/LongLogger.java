package com.nano.logger;
import com.nano.logger.config.LoggerConfiguration;

/**
 * This class solves the problem due to the size limitation of the log in
 * the Android System.
 *
 * <pre>
 * If the size of each log is greater than (1024 * 4) bits, the log will 
 * be divided into multiple logs to print.
 * 
 * But for performance reasons, the length of each log is still less
 * than {@link LoggerConfiguration#getLogMaxLength()} that the default
 * value is {@link LoggerConfiguration#DEFAULT_LOG_MAX_LENGTH}. If you
 * want to change it, see @{link LoggerConfiguration#setLogMaxLength(int)}.
 * <pre>
 */
public class LongLogger extends ILogger {

	private static final int LOG_MAX_LENGTH = 4000 ;

	@Override
	public void log(int level,String tag,String message) {
		LoggerConfiguration config = LoggerConfiguration.getInstance() ;
		
		// limit the length of each printed log.
		int length = Math.min(
			message.length(),
			config.getLogMaxLength()
		) ;
		
		if(length <= 0){
			logger.log(level, tag, message) ;
			return ;
		}
		int i = 0 ;
		while(i + LOG_MAX_LENGTH < length){
			logger.log(level, tag, message.substring(i, i + LOG_MAX_LENGTH)) ;
			i += LOG_MAX_LENGTH ;
		}
		
		logger.log(level, tag, message.substring(i)) ;
	}
}
