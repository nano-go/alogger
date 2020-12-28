package com.nano.logger.demo;

import android.content.Intent;
import com.nano.logger.Logger;
import com.nano.logger.demo.base.FunctionListActivity;
import com.nano.logger.listener.DiskOutputLogger;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FunctionListActivity {

	@Override
	public List<String> getFunctions() {
		return Arrays.<String>asList(
			"Show log",
			"Delete all log files"
		) ;
	}

	@Override
	public void onItemClick(String title, int index) {
		switch(title) {
			case "Show log": 
				startActivity(new Intent(this, ShowLogActivity.class)) ;
				break ;
			case "Delete all log files":
				Logger.getConfiguation()
					.getDiskOutputLogger()
					.deleteLogFilesAsync() ;
				toast("Successfully deleted.") ;
				break ;
		}
	}   
}
