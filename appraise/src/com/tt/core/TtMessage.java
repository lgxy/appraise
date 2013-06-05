package com.tt.core;

import android.os.Message;

public class TtMessage {
	private Location from = null;
	private Message msg = null;
	public TtMessage(Location from, Message msg) {
		super();
		this.from = from;
		this.msg = msg;
	}
	public Location getFrom() {
		return from;
	}
	public Message getMsg() {
		return msg;
	}
}
