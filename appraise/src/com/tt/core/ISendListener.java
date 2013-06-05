package com.tt.core;

import android.os.Message;

public interface ISendListener {
	
	void sendEvent(TtEvent evt);

	void sendMessage(Message msg);

	void sendEmptyMessage(int what);

	void sendMessageDelayed(Message msg, long delayMillis);

	void sendEmptyMessageDelayed(int what, long delayMillis);
}
