package com.appraise;

import java.io.FileNotFoundException;

import android.media.MediaPlayer;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tt.core.BaseActivity;
import com.tt.core.LogUtil;
import com.tt.core.TtEvent;
import com.tt.utils.IOUtils;

public class TestMediaPlayerActivity extends BaseActivity implements SurfaceHolder.Callback{

	Button btnPauseOrResume = null;
	Button btnStop = null;
	Button btnNext = null;
	private final int WHAT_INIT = 0XAA;
	private LinearLayout viewContent = null;
	MediaPlayer mp = null;
	SurfaceView surfaceView = null;
	SurfaceHolder surfaceHolder = null;
	
	private void initView(){
		LogUtil.d(tag, "initView()");
		btnPauseOrResume = (Button)findViewById(R.id.test_pasue_resume);
		btnPauseOrResume.setOnClickListener(this);
		btnStop = (Button)findViewById(R.id.test_stop);
		btnStop.setOnClickListener(this);
		btnNext = (Button)findViewById(R.id.test_next);
		btnNext.setOnClickListener(this);
		
		viewContent = (LinearLayout)findViewById(R.id.tset_video_content);
		
		surfaceView = (SurfaceView)findViewById(R.id.test_surface_view);		
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		surfaceHolder.setKeepScreenOn(true);
	}
	
	private void initMediaPlayer(String path){
		LogUtil.d(tag, "initMediaPlayer()");
		if(mp != null){
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();
		mp.setDisplay(surfaceHolder);
		try {
			mp.setDataSource(path);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void destoryMediaPlayer(){
		LogUtil.d(tag, "destoryMediaPlayer()");
		if(mp != null ){
			mp.stop();
			mp.reset();
			mp.release();
			mp = null;
		}
	}
	private void play(){
		try {
			mp.prepare();
			mp.start();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void pause(){
		mp.pause();
	}
	private void resume(){
		mp.start();
	}
	private void stop(){
		mp.stop();
		try {
			mp.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected int getContentView() {
		return R.layout.activity_test;
	}

	@Override
	protected void doCreate() {
		sendEmptyMessage(WHAT_INIT);
	}
	
	@Override
	protected void doClick(View v) {
		if(v == btnPauseOrResume){
			if(mp.isPlaying()){
				pause();
			}else{
				resume();
			}
		}else if(v == btnStop){
			stop();
		}else if(v == btnNext){
			next();
		}
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
		if(msg.what == WHAT_INIT){
			initView();
		}
	}

	private void next() {
		LogUtil.d(tag, "next()");
		try {
			String path = IOUtils.getExternalStoragePath()+"test2.mp4";
			initMediaPlayer(path);
			play();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doExecuteEvent(TtEvent evt) {

	}

	int position = -1;
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		LogUtil.d(tag, "surfaceChanged()");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		LogUtil.d(tag, "surfaceCreated()");
		next();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		LogUtil.d(tag, "surfaceDestroyed()");
		destoryMediaPlayer();
	}

}
