package com.tt.core;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.tt.event.ServiceStateEvent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

public abstract class BaseService extends Service implements TtListener,ISendListener {

	protected final String tag = this.getClass().getSimpleName()+"::";
	private BaseApplication app = null;
	private final Location from = new Location(this.getClass().getName());
	private final Location  to = new Location(this.getClass().getName());
	private final ConcurrentLinkedQueue<DoWhat> doWhatQueue = new ConcurrentLinkedQueue<DoWhat>();
	private Thread3 t3 = null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (BaseApplication) getApplication();
		app.registerTtListener(this);
		t3 = new Thread3("T3-"+this.getClass().getSimpleName());
		t3.start();
		try {
			app.sendEvent(new ServiceStateEvent(from, to, ServiceStateEvent.State.create));
		} catch (TtException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			app.sendEvent(new ServiceStateEvent(from, to, ServiceStateEvent.State.destroy));
		} catch (TtException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executeEvent(TtEvent evt) {
		if (evt == null) {
			LogUtil.e(tag, "evt is null");
			return;
		}
		if (evt instanceof ServiceStateEvent) {
			ServiceStateEvent tes = (ServiceStateEvent) evt;
			if (ServiceStateEvent.State.create == tes.getState()) {
				doCreate();
				app.notifyAllMain();
				return;
			} else if (ServiceStateEvent.State.destroy == tes.getState()) {
				app.unRegisterTtListener(this);
				t3.stopRunning();
				doDestroy();
				return;
			}
		}
		doExecuteEvent(evt);
	}

	@Override
	public void executeMessage(Message msg) {
		doExecuteMessage(msg);
	}

	public void sendEvent(TtEvent evt) {
		try {
			app.sendEvent(evt);
		} catch (TtException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Message msg) {
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessage(tmsg);
	}

	public void sendEmptyMessage(int what) {
		Message msg = Message.obtain();
		msg.what = what;
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessage(tmsg);
	}

	public void sendMessageDelayed(Message msg, long delayMillis) {
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessageDelayed(tmsg, delayMillis);
	}

	public void sendEmptyMessageDelayed(int what, long delayMillis) {
		Message msg = Message.obtain();
		msg.what = what;
		TtMessage tmsg = new TtMessage(from, msg);
		app.sendMessageDelayed(tmsg, delayMillis);
	}
	
	protected void submitDoWhat(DoWhat doWhat){
		if(doWhat == null)
			return ;
		doWhatQueue.add(doWhat);
		notifyAllT3();
	}
	
	protected void checkRunOnUI(){
		app.checkRunOnUI();
	}
	
	protected void checkRunOnMain(){
		app.checkRunOnMain();
	}
	
	protected void checkRunOnT3(){
		if(Thread.currentThread() != t3)
			throw new IllegalStateException("not run on T3 Thread");
	}
	
	protected Location getLocation(){
		return new Location(this.getClass().getName());
	}
	
	protected Location findLocation(Class<?> cls){
		return new Location(cls.getName());
	}

	protected abstract void doCreate();

	protected abstract void doDestroy();

	protected abstract void doExecuteMessage(Message msg);

	protected abstract void doExecuteEvent(TtEvent evt);
	
	protected void doExecuteDoWhat(DoWhat doWhat){}
	
	private void doWhat(DoWhat doWhat){
		checkRunOnT3();
		doExecuteDoWhat(doWhat);
	}
	
	private final Object lockT3 = new Object();
	private void waitT3() {
		synchronized (lockT3) {
			try {
				lockT3.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void notifyAllT3() {
		synchronized (lockT3) {
			try {
				lockT3.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Thread3 extends Thread{		
		public Thread3(String threadName) {
			super(threadName);
		}
		@Override
		public void run() {
			while (isRunning()) {
				if (doWhatQueue.isEmpty()) {
					waitT3();
				}
				DoWhat dw = doWhatQueue.remove();
				doWhat(dw); 
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
			notifyAllT3();
		}
	}

}
