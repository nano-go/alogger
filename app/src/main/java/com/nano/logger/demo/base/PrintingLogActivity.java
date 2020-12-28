package com.nano.logger.demo.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.core.widget.NestedScrollView;
import butterknife.BindColor;
import butterknife.BindView;
import com.nano.logger.Logger;
import com.nano.logger.demo.R;
import com.nano.logger.listener.LoggerListener;

public abstract class PrintingLogActivity extends BaseActivity implements LoggerListener{
	@BindView(R.id.log_scroll_layout) ViewGroup mLogLayout ;
	
	@BindColor(R.color.log_default) @ColorInt 
	int mLogColorDefault ;
	
	@BindColor(R.color.log_info) @ColorInt 
	int mLogColorInfo ;
	
	@BindColor(R.color.log_warn) @ColorInt 
	int mLogColorWarn ;
	
	@BindColor(R.color.log_error) @ColorInt 
	int mLogColorError ;
	
	@Override
	public int getLayoutResId() {
		return R.layout.activity_printing_log;
	}
	
	@Override
	public void initView() {
		Logger.getConfiguation().registerListener(this, this) ;
		mToolBar.setNavigationIcon(R.drawable.ic_arrow_left) ;
		mToolBar.setNavigationOnClickListener((v) -> onBackPressed()) ;
		initData() ;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Logger.getConfiguation().hasAddedLoggerListener(this)) {
			throw new IllegalStateException("Not remove self!") ;
		}
	}
	
	@Override
	public void onOutput(int level, String tag, String log) {
		addLogText(level, tag, log) ;
	}

	private void addLogText(int level, String tag, String log) {
		LayoutInflater.from(this).inflate(R.layout.item_log, mLogLayout, true) ;
		TextView logView = (TextView) mLogLayout.getChildAt(mLogLayout.getChildCount() - 1) ;
		switch(level) {
			case android.util.Log.INFO:
				logView.setTextColor(mLogColorInfo) ;
				break ;
			case android.util.Log.WARN:
				logView.setTextColor(mLogColorWarn) ;
				break ;
			case android.util.Log.ERROR:
				logView.setTextColor(mLogColorError) ;
				break ;
			default: logView.setTextColor(mLogColorDefault) ;
		}
		logView.setText(log) ;
		if(mLogLayout.getChildCount() == 50) {
			mLogLayout.removeViewAt(0) ;
		}
	}
	
	protected void clearLog() {
		mLogLayout.removeAllViews();
	}
	
	public abstract void initData() ;
}
