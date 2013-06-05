package com.tt.core;

public class TtException extends Throwable{

	private static final long serialVersionUID = 1L;

	public TtException() {
		super();
	}

	public TtException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public TtException(String detailMessage) {
		super(detailMessage);
	}

	public TtException(Throwable throwable) {
		super(throwable);
	}
}
