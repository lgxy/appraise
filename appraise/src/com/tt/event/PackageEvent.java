package com.tt.event;



import com.tt.core.Location;
import com.tt.core.TtEvent;

public class PackageEvent extends TtEvent {

	private String packageName = null;
	private State state = null;

	public PackageEvent(Location from, Location to, String packageName, State st) {
		super(from, to);
		this.packageName = packageName;
		this.state = st;
	}

	public String getPackageName() {
		return packageName;
	}

	public State getState() {
		return state;
	}

	public enum State {
		added, removed, replaced
	}
}
