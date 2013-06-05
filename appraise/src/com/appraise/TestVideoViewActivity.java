package com.appraise;

import java.io.FileNotFoundException;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.appraise.widget.VideoPlayer;
import com.tt.core.BaseActivity;
import com.tt.core.LogUtil;
import com.tt.core.TtEvent;
import com.tt.utils.IOUtils;

public class TestVideoViewActivity extends BaseActivity implements OnTouchListener{

	private static final int WHAT_INIT = 0X1111;
	VideoPlayer videoPlayer = null;
	Button btnPauseOrResume = null;
	Button btnStop = null;
	Button btnNext = null;
	/** 1:正在播放   2：已经停止  3：已经暂停 */
	private int playStatus = -1;
	private void initView(){
		videoPlayer  = (VideoPlayer)findViewById(R.id.test_video_player);
		videoPlayer.setOnTouchListener(this);
		btnPauseOrResume = (Button)findViewById(R.id.test_pasue_resume);
		btnPauseOrResume.setOnClickListener(this);
//		btnPauseOrResume.setOnTouchListener(this);
		btnStop = (Button)findViewById(R.id.test_stop);
		btnStop.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.test_next);
		btnNext.setOnClickListener(this);
		
	}
	@Override
	protected void doClick(View v) {
		if(v == btnPauseOrResume){
			pauseOrResume();
		}else if(v == btnStop){
			stop();
		}else if(v == btnNext){
			 String path = null ;
		        try {
					path = IOUtils.getExternalStoragePath()+"test2.mp4";
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
		        videoPlayer.setVideoPath(path);
		        videoPlayer.start();
		}
	}
	private void stop(){
		videoPlayer.stopPlayback();
		playStatus = 2;
	}
	private void start(){
		videoPlayer.start();
		playStatus = 1;
	}
	private void pauseOrResume(){
		if(videoPlayer.isPlaying()){
			if(videoPlayer.canPause()){
				videoPlayer.pause();
				playStatus = 3;
			}else{
				LogUtil.e(tag, "videoPlayer can't pause");
			}
		}else{
			start();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		LogUtil.d(tag, "onTouchEvent(),evt.x=["+evt.getX()+"] evt.y=["+evt.getY()+"]");
		return super.onTouchEvent(evt);
	}
	
	@Override
	protected int getContentView() {
		return R.layout.activity_test;
	}

	@Override
	protected void doCreate() {
//		initLock();
//		screenOn();
		sendEmptyMessage(WHAT_INIT);
	}

	

	@Override
	protected void doStart() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doResume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doPause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doStop() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doDestroy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doRestart() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void doExecuteMessage(Message msg) {
		checkRunOnUI();
		if(msg.what == WHAT_INIT){
			initView();
			DisplayMetrics dm = new DisplayMetrics();
	        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
	        videoPlayer.init(dm.widthPixels, dm.heightPixels);
	        String path = null ;
	        try {
				path = IOUtils.getExternalStoragePath()+"test2.mp4";
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	        videoPlayer.setVideoPath(path);
	        videoPlayer.start();
		}
		
	}

	@Override
	protected void doExecuteEvent(TtEvent evt) {

	}
	
	private Context getContext(){
		return this;
	}
	private KeyguardLock keyguardLock = null;
	private WakeLock wakeLock = null;
	private void initLock() {
		try {
			keyguardLock = ((KeyguardManager) getContext().getSystemService("keyguard")).newKeyguardLock("");
			wakeLock = ((PowerManager) getContext().getSystemService("power")).newWakeLock(0x10000006, "bright");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void screenOn() {
		try {
			if (keyguardLock == null || wakeLock == null)
				return;
			keyguardLock.disableKeyguard();
			wakeLock.setReferenceCounted(false);
			wakeLock.acquire();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void screenOff() {
		try {
			if (keyguardLock == null || wakeLock == null)
				return;
			keyguardLock.reenableKeyguard();
			if (wakeLock.isHeld())
				wakeLock.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent evt) {
		LogUtil.d(tag, "onTouch(),evt.x=["+evt.getX()+"] evt.y=["+evt.getY()+"]");
		return false;
	}

}
