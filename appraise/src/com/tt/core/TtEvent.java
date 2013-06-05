package com.tt.core;

public class TtEvent implements Cloneable{
	private Location from = null;
	private Location to = null;
	private Object data = null;
	public TtEvent(Location from, Location to, Object data) {
		this(from,to);
		this.data = data;
	}
	public TtEvent(Location from, Location to) {
		super();
		this.from = from;
		this.to = to;
	}
	public Location getFrom() {
		return from;
	}
	public Location getTo() {
		return to;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public Object clone()  {
		TtEvent e = null;
		try {
			e = (TtEvent) super.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		return e;
	}
}
