package com.appraise;

import android.content.res.Configuration;
import android.os.Message;

import com.tt.core.BaseActivity;
import com.tt.core.LogUtil;
import com.tt.core.TtEvent;

public class MainActivity extends BaseActivity {
	

	@Override
	protected int getContentView() {
		super.tag = "--------MainActivity-----------";
		return R.layout.activity_main;
	}

	@Override
	protected void doCreate() {
		LogUtil.d(tag, "doCreate()");
	}

	@Override
	protected void doStart() {
		LogUtil.d(tag, "doStart()");
	}

	@Override
	protected void doResume() {
		LogUtil.d(tag, "doResume()");
	}

	@Override
	protected void doPause() {
		LogUtil.d(tag, "doPause()");
	}

	@Override
	protected void doStop() {
		LogUtil.d(tag, "doStop()");
	}

	@Override
	protected void doDestroy() {
		LogUtil.d(tag, "doDestroy()");
	}

	@Override
	protected void doRestart() {
		LogUtil.d(tag, "doRestart()");
		
	}

	@Override
	protected void doExecuteMessage(Message msg) {
		
	}

	@Override
	protected void doExecuteEvent(TtEvent evt) {
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		LogUtil.d(tag, "onConfigurationChanged()");
//		super.onConfigurationChanged(newConfig);
	}
	
	//TEST1
	
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		LogUtil.d(tag, "onCreate()");
//	}
//	@Override
//	protected void onStart() {
//		LogUtil.d(tag, "onStart()");
//		super.onStart();
//	}
//	@Override
//	protected void onStop() {
//		super.onStop();
//		LogUtil.d(tag, "onStop()");
//	}
//	@Override
//	protected void onRestart() {
//		LogUtil.d(tag, "onRestart()");
//		super.onRestart();
//	}
//	
//	@Override
//	protected void onPause() {
//		LogUtil.d(tag, "onPause()");
//		super.onPause();
//	}
//	@Override
//	protected void onResume() {
//		LogUtil.d(tag, "onResume()");
//		super.onResume();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		LogUtil.d(tag, "onDestroy()");
//	}
}
