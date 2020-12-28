package com.nano.logger.demo.base;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.google.android.material.appbar.MaterialToolbar;
import com.nano.logger.demo.R;

public abstract class BaseActivity extends AppCompatActivity {
	
	protected MaterialToolbar mToolBar ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId()) ;
		mToolBar = findViewById(R.id.topAppBar) ;
		mToolBar.setTitle(getToolbarTitle()) ;
		ButterKnife.bind(this) ;
		initView() ;
	}
	
	public String getToolbarTitle() {
		return "Logger" ;
	}
	
	public void toast(String format, Object... args) {
		Toast.makeText(this, String.format(format, args), 0).show() ;
	}
	
	public abstract void initView() ;
	@LayoutRes
    public abstract int getLayoutResId() ;
}
