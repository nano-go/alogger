package com.nano.logger;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import com.nano.logger.config.LoggerConfiguration;
import com.nano.logger.listener.LoggerListener;
import java.util.HashSet;
import java.util.Objects;

public class NotifyListenersLogger extends ILogger {
	
	private HashSet<LoggerListener> mListeners ;
	
	public NotifyListenersLogger() {
		mListeners = new HashSet<>() ;
	}

	@Override
	public void log(int level,String tag,String message) {
		LoggerConfiguration config = LoggerConfiguration.getInstance() ;
		config.getDiskOutputLogger().onOutput(level, tag, message) ;
		for(LoggerListener out : mListeners){
			out.onOutput(level, tag, message) ;
		}
		logger.log(level,tag,message) ;
	}
	
	public boolean hasAddedLoggerListener(@NonNull LoggerListener listener) {
		return mListeners.contains(listener) ;
	}
	
	public void registerListener(@NonNull LoggerListener listener) {
		registerListener(null, listener) ;
	}
	
	public void registerListener(@Nullable LifecycleOwner lifecycleOwner, @NonNull LoggerListener listener) {
		mListeners.add(Objects.requireNonNull(listener)) ;
		if(lifecycleOwner == null) {
			return ;
		}
		lifecycleOwner.getLifecycle().addObserver(new LifecycleObserver(){
			@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
			public void onDestroy(){
				unregisterListener(listener) ;
			}
		}) ;
	}
	
	public void unregisterListener(@NonNull LoggerListener listener) {
		mListeners.remove(listener) ;
	}
}
