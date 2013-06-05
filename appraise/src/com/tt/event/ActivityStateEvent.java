package com.tt.event;

import com.tt.core.Location;
import com.tt.core.TtEvent;

public class ActivityStateEvent extends TtEvent {
	private State state = null;
	public ActivityStateEvent(Location from, Location to, Object data,State s) {
		super(from, to, data);
		this.state = s;
	}
	public ActivityStateEvent(Location from, Location to,State s) {
		super(from, to);
		this.state = s;
	}
	public State getState() {
		return this.state;
	}
	public static enum State{
		create,start,stop,destroy,restart,pause,resume
	}
}
