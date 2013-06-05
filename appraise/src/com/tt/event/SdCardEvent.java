package com.tt.event;

import com.tt.core.Location;
import com.tt.core.TtEvent;

public class SdCardEvent extends TtEvent {

	public SdCardEvent(Location from, Location to,SdCardState st) {
		super(from, to);
		this.state = st;
	}
	SdCardState state;
	boolean mounted;

	public SdCardState getState() {
		return state;
	}
	public static enum SdCardState {
		mounted, unmounted;
	}
}
