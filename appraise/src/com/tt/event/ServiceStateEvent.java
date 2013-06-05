package com.tt.event;

import com.tt.core.Location;
import com.tt.core.TtEvent;

public class ServiceStateEvent extends TtEvent {

	private State state = null;
	public ServiceStateEvent(Location from, Location to, Object data,State status) {
		super(from, to, data);
		this.state = status;
	}

	public ServiceStateEvent(Location from, Location to,State status) {
		super(from, to);
		this.state = status;
	}

	public State getState() {
		return this.state;
	}
	public static enum State{
		create,destroy
	}
}
