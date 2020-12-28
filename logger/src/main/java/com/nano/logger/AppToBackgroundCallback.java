package com.nano.logger;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.nano.logger.config.LoggerConfiguration;

public abstract class AppToBackgroundCallback implements Application.ActivityLifecycleCallbacks {
	
	private int visiableActivityCount;
    
	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

	@Override
	public void onActivityStarted(Activity activity) {
		visiableActivityCount ++ ;
	}

	@Override
	public void onActivityResumed(Activity activity) {}

	@Override
	public void onActivityPaused(Activity activity) {}

	@Override
	public void onActivityStopped(Activity activity) {
		visiableActivityCount -- ;
		if(visiableActivityCount <= 0){
			onToBackground() ;
		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	}

	@Override
	public void onActivityDestroyed(Activity activity){}
	
	public abstract void onToBackground() ;
	
}
