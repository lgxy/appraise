package com.tt.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class BaseReceiver extends BroadcastReceiver {

	protected final String tag = this.getClass().getSimpleName();
	private BaseApplication app = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.app = (BaseApplication) context.getApplicationContext();
		try {
			doReceive(context, intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void doReceive(Context context, Intent intent);

	protected void sendEvent(TtEvent evt) {
		try {
			app.sendEvent(evt);
		} catch (TtException e) {
			e.printStackTrace();
		}
	}
	
	protected Location getLocation(){
		return new Location(this.getClass().getName());
	}
	
	protected Location findLocation(Class<?> cls){
		return new Location(cls.getName());
	}

}
