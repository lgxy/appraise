package com.appraise.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoPlayer extends VideoView {

	private int width = 0;
	private int height = 0;
	public VideoPlayer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VideoPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VideoPlayer(Context context) {
		super(context);
	}
	
	public void init(int width,int height){
		this.width = width ;
		this.height = height ;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(width != 0 && height !=0){
			setMeasuredDimension(width, height);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
