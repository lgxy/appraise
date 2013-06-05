package com.tt.core;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tt.event.ActivityStateEvent;

public abstract class BaseActivity extends Activity implements TtListener, OnClickListener, OnItemClickListener {

	protected String tag = this.getClass().getSimpleName();
	private BaseApplication app = null;
	private final Location from = new Location(this.getClass().getName());
	private final Location to = new Location(this.getClass().getName());
	public final String DATA_SERIALIZABLE = "DATA_SERIALIZABLE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(tag, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(getContentView());
		app = (BaseApplication) getApplication();
		app.registerTtListener(this);
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.create));
		} catch (TtException e) {
			e.printStackTrace();
		}
	}

	protected void onStart() {
		LogUtil.d(tag, "onStart()");
		super.onStart();
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.start));
		} catch (TtException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		LogUtil.d(tag, "onResume()");
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.resume));
		} catch (TtException e) {
			e.printStackTrace();
		}
		super.onResume();
	
	}
	
	@Override
	protected void onPause() {
		LogUtil.d(tag, "onPause()");
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.pause));
		} catch (TtException e) {
			e.printStackTrace();
		}
		super.onPause();
		
	}
	
	@Override
	protected void onStop() {
		LogUtil.d(tag, "onStop()");
		super.onStop();
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.stop));
		} catch (TtException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		LogUtil.d(tag, "onDestroy()");
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.destroy));
		} catch (TtException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	@Override
	protected void onRestart() {
		LogUtil.d(tag, "onRestart()");
		try {
			app.sendEvent(new ActivityStateEvent(from, to, ActivityStateEvent.State.restart));
		} catch (TtException e) {
			e.printStackTrace();
		}
		super.onRestart();
	}

	@Override
	public void executeMessage(Message msg) {
		doExecuteMessage(msg);
	}

	@Override
	public void executeEvent(TtEvent evt) {
		if (evt == null) {
			LogUtil.e(tag, "evt is null");
			return;
		}
		if (evt instanceof ActivityStateEvent) {
			ActivityStateEvent tea = (ActivityStateEvent) evt;

			if (ActivityStateEvent.State.create == tea.getState()) {
				doCreate();
			} else if (ActivityStateEvent.State.destroy == tea.getState()) {
				app.unRegisterTtListener(this);
				doDestroy();
			} else if (ActivityStateEvent.State.start == tea.getState()) {
				doStart();
				app.notifyAllMain();
			} else if (ActivityStateEvent.State.stop == tea.getState()) {
				doStop();
			}else if (ActivityStateEvent.State.resume == tea.getState()) {
				doResume();
			}else if (ActivityStateEvent.State.pause == tea.getState()) {
				doPause();
			}else if (ActivityStateEvent.State.restart == tea.getState()) {
				doRestart();
			}
			return;
		}
		doExecuteEvent(evt);
	}
	
	@Override
	public void onClick(View v) {
		LogUtil.d(tag, "onClick()");
		doClick(v);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LogUtil.d(tag, "onItemClick()");
		doItemClick(arg0, arg1, arg2, arg3);
	}

	protected void sendEvent(TtEvent evt) {
		try {
			app.sendEvent(evt);
		} catch (TtException e) {
			e.printStackTrace();
		}
	}

	protected void sendMessage(Message msg) {
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessage(tmsg);
	}

	protected void sendEmptyMessage(int what) {
		Message msg = Message.obtain();
		msg.what = what;
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessage(tmsg);
	}

	protected void sendMessageDelayed(Message msg, long delayMillis) {
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessageDelayed(tmsg, delayMillis);
	}

	protected void sendEmptyMessageDelayed(int what, long delayMillis) {
		Message msg = Message.obtain();
		msg.what = what;
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessageDelayed(tmsg, delayMillis);
	}

	protected void checkRunOnUI() {
		app.checkRunOnUI();
	}

	protected void checkRunOnMain() {
		app.checkRunOnMain();
	}

	protected Location getLocation() {
		return new Location(this.getClass().getName());
	}

	protected Location findLocation(Class<?> cls) {
		return new Location(cls.getName());
	}

	protected void doClick(View v) { 
		LogUtil.d(tag, "doClick()");
	}
	
	protected void doItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LogUtil.d(tag, "doItemClick()");
	}
	
	
	protected abstract int getContentView();

	protected abstract void doCreate();

	protected abstract void doStart();
	
	protected abstract void doResume();
	
	protected abstract void doPause();

	protected abstract void doStop();

	protected abstract void doDestroy();
	
	protected abstract void doRestart();

	protected abstract void doExecuteMessage(Message msg);

	protected abstract void doExecuteEvent(TtEvent evt);

}
