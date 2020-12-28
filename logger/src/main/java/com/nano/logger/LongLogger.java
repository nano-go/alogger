package com.nano.logger;
import com.nano.logger.config.LoggerConfiguration;

/**
 * This class solves the problem that due to the limitation in tne Android System, 
 * the size of each printed log will be limited.
 *
 * <pre>
 * if the size of each printed log is greater than (1024 * 4) bits, the log contents 
 * will be looped out.
 * 
 * But for performance reasons, the length of each printed log is still less than 
 * {@link LoggerConfiguration#getLogMaxLength()} that the default value is 
 * {@link LoggerConfiguration#DEFAULT_LOG_MAX_LENGTH}. If you want to change it 
 * see @{link LoggerConfiguration#setLogMaxLength(int)}.
 * <pre>
 * 
 * <pre>
 * 		Author: nano1
 *		Date  : 20/5/17
 * </pre>
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
