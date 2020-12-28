package com.nano.logger;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class ILogger {
	
	protected ILogger logger ;
	
	public ILogger next(ILogger logger){
		this.logger = logger ;
		return logger ;
	}
	
	public abstract void log(int level, String tag, String message) ;
}
