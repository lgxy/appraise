package com.tt.core;

import android.os.Message;

public interface TtListener {
	void executeEvent(TtEvent evt);
	void executeMessage(Message msg);
}
