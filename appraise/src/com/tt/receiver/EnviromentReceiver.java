package com.tt.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tt.core.BaseReceiver;
import com.tt.core.Location;
import com.tt.core.LogUtil;
import com.tt.core.TtEvent;
import com.tt.event.PackageEvent;
import com.tt.event.SdCardEvent;
import com.tt.event.SdCardEvent.SdCardState;


public class EnviromentReceiver extends BaseReceiver {

	private final String tag = this.getClass().getSimpleName();

	@Override
	protected void doReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String action = intent.getAction();
		TtEvent ttEvent = null;
		// sd卡状态
		if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
			SdCardEvent e = new SdCardEvent(getLocation(),Location.any,SdCardState.mounted);
			ttEvent = e;
		} else if (action.equals(Intent.ACTION_MEDIA_EJECT) || action.equals(Intent.ACTION_MEDIA_REMOVED)) {
			SdCardEvent e = new SdCardEvent(getLocation(),Location.any,SdCardState.unmounted);
			ttEvent = e;
		}
		// 软件安装卸载状态
		else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
			int n = intent.getDataString().indexOf(":")+1;
			String packageName = intent.getDataString().substring(n).trim();
			LogUtil.d(tag ,"有应用被添加 :" + packageName);
			PackageEvent e = new PackageEvent(getLocation(),Location.any,packageName,PackageEvent.State.added);
			ttEvent = e;
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
			int n = intent.getDataString().indexOf(":")+1;
			String packageName = intent.getDataString().substring(n).trim();
			LogUtil.d(tag ,"有应用被删除 : " + packageName);
			PackageEvent e = new PackageEvent(getLocation(),Location.any,packageName,PackageEvent.State.removed);
			ttEvent = e;
		} else if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
			int n = intent.getDataString().indexOf(":")+1;
			String packageName = intent.getDataString().substring(n).trim();
			LogUtil.d(tag ,"有应用被替换 :" + packageName);
			PackageEvent e = new PackageEvent(getLocation(),Location.any,packageName,PackageEvent.State.replaced);
			ttEvent = e;
		}
		if(ttEvent != null){
			sendEvent(ttEvent);
		}
	}
}
