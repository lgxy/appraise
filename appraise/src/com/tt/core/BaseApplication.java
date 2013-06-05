package com.tt.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

public class BaseApplication extends Application {
	protected final String tag = this.getClass().getSimpleName();
	private final MainThread mainThread = new MainThread("T2-MainThread");
	private static final ConcurrentLinkedQueue<TtEvent> eventQueue = new ConcurrentLinkedQueue<TtEvent>();
	private static final ConcurrentHashMap<String, TtListener> ttListenerMap = new ConcurrentHashMap<String, TtListener>();
	/** UI线程ID */
	private static volatile long uiTid = -1;

	private static final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (uiTid <= 0) {
				uiTid = Thread.currentThread().getId();
				Thread.currentThread().setName("T1-UI");
			}
			if (msg == null)
				return;
			if (msg.obj == null || (msg.obj instanceof TtMessage) == false) {
				return;
			}
			TtMessage ttMsg = (TtMessage) msg.obj;
			String from = ttMsg.getFrom().getUri();
			TtListener ttMsgListener = ttListenerMap.get(from);
			if (ttMsgListener == null)
				return;
			ttMsgListener.executeMessage(ttMsg.getMsg());
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mainThread.start();
		init();
	}

	protected void init() {
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		mainThread.stopRunning();
	}

	void sendMessage(TtMessage tmsg) {
		Message msg = handler.obtainMessage();
		msg.obj = tmsg;
		handler.sendMessage(msg);
	}

	void sendMessageDelayed(TtMessage tmsg, long delayMillis) {
		Message msg = handler.obtainMessage();
		msg.obj = tmsg;
		handler.sendMessageDelayed(msg, delayMillis);
	}

	void sendEvent(TtEvent evt) throws TtException {
		if (evt == null)
			throw new TtException("evt is null");
		if (evt.getFrom() == null)
			throw new TtException("evt.from is null");
		if (evt.getTo() == null)
			throw new TtException("evt.to is null");
		// String from = evt.getFrom().getUri().trim();
		// if (!ttListenerMap.containsKey(from)) {
		// throw new TtException("from:" + from + " can't register");
		// }
		String to = evt.getTo().getUri().trim();
		eventQueue.add(evt);
		notifyAllMain();
		if (ttListenerMap.containsKey(to) || Location.any.getUri().equals(to)) {
		} else {
			// throw new TtException("to:" + to + " can't register");
			LogUtil.e(tag, "to:" + to + " can't register");
		}
	}

	void registerTtListener(TtListener tl) {
		ttListenerMap.put(tl.getClass().getName(), tl);
	}

	void unRegisterTtListener(TtListener tl) {
		ttListenerMap.remove(tl.getClass().getName());
	}

	private final Object lockMain = new Object();

	private void waitMain() {
		synchronized (lockMain) {
			try {
				lockMain.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	void notifyAllMain() {
		synchronized (lockMain) {
			try {
				lockMain.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class MainThread extends Thread {
		@Override
		public void run() {
			while (isRunning()) {
				if (eventQueue.isEmpty())
					waitMain();
				TtEvent evt = eventQueue.peek();
				if (evt == null)
					continue;
				// TtEvent evtClone = (TtEvent) evt.clone();
				String to = evt.getTo().getUri();
				if (to == null || to.trim().length() == 0)
					continue;
				if (Location.any.getUri().equals(to)) {
					eventQueue.poll();
					for (TtListener lt : ttListenerMap.values()) {
						lt.executeEvent(evt);
					}
					continue;
				}
				TtListener lt = ttListenerMap.get(to);
				if (lt != null) {
					eventQueue.poll();
					lt.executeEvent(evt);
					continue;
				}
				if (eventQueue.size() > 1) {
					try {
						eventQueue.remove();
						eventQueue.add(evt);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}

		private final Object lockRunning = new Object();
		private boolean running = true;

		private boolean isRunning() {
			synchronized (lockRunning) {
				return this.running;
			}
		}

		private void stopRunning() {
			synchronized (lockRunning) {
				this.running = false;
			}
			notifyAllMain();
		}

		public MainThread(String threadName) {
			super(threadName);
		}
	}

	void checkRunOnUI() {
		if (Thread.currentThread().getId() != uiTid)
			throw new IllegalStateException("not run on UI Thread");
	}

	void checkRunOnMain() {
		if (Thread.currentThread() != mainThread)
			throw new IllegalStateException("not run on Main Thread");
	}

}
