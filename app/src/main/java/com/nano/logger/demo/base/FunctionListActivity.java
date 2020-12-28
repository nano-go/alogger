package com.nano.logger.demo.base;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.nano.logger.demo.R;
import java.util.List;

public abstract class FunctionListActivity extends BaseActivity {

	@BindView(R.id.list) RecyclerView mRcyFunctionList ;
	
	protected FunctionListAdapter mAdapter ;
	
	protected List<String> mFunctions ;
	
	@Override
	public int getLayoutResId() {
		return R.layout.activity_function_list;
	}
	
	@Override
	public void initView() {
		mFunctions = getFunctions() ;
		mRcyFunctionList.setLayoutManager(new LinearLayoutManager(this)) ;
		mAdapter = new FunctionListAdapter(mFunctions) ;
		mAdapter.setRecyclerView(mRcyFunctionList) ;
		mAdapter.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int pos) {
				FunctionListActivity.this.onItemClick(mAdapter.getData().get(pos), pos) ;
			}
		}) ;
		mRcyFunctionList.setAdapter(mAdapter) ;
	}
	
	@NonNull
	public abstract List<String> getFunctions() ;
	public abstract void onItemClick(String title, int index) ;
 
	public static class FunctionListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

		public FunctionListAdapter(@NonNull List<String> functions) {
			super(R.layout.item_function, functions) ;
		}
		
		@Override
		protected void convert(BaseViewHolder helper, String item) {
			helper.setText(R.id.title, item) ;
		}
	}
}
