package com.supets.pet.dialog;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.supets.pet.R;

public class PlayLayoutView extends FrameLayout implements PlayButton.OnPlayCallBackListener,
		View.OnClickListener {
	private PlayButton mRecordButton;
	private TextView mTime;
	private View leftAnim;
	private View rightAnim;

	private int length = 1;

	private Button mReLoad,mReLoad2;
	private Button mSend;
	private View mreloadLayout;

	public PlayLayoutView(@NonNull Context context) {
		super(context);
		init();
	}

	public PlayLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		 LayoutInflater.from(getContext()).inflate(R.layout.record_widget_play_sub_item, this);
		mRecordButton = (PlayButton) findViewById(R.id.playbtn);
		mTime = (TextView) findViewById(R.id.time2);
		leftAnim = findViewById(R.id.leftAnim2);
		rightAnim = findViewById(R.id.rightAnim2);
		hideAnim();
		mreloadLayout=findViewById(R.id.reloadLayout);

		mRecordButton.setPlayer(new Player());
		mRecordButton.setOnPlayCallBackListener(this);

		mReLoad = (Button) findViewById(R.id.reLoad);
		mReLoad2 = (Button) findViewById(R.id.reLoad2);
		mSend = (Button) findViewById(R.id.send);
		mReLoad.setOnClickListener(this);
		mSend.setOnClickListener(this);
		mReLoad2.setOnClickListener(this);
		mReLoad2.setVisibility(View.GONE);
		mreloadLayout.setVisibility(View.VISIBLE);
	}

	public  void setAudio(String path,int time){
		mRecordButton.setPlayerPath(path);
		mTime.setText(TimeDateUtils.formatRecordTime(time));
	}

	private void showAnim() {
		leftAnim.setVisibility(View.VISIBLE);
		rightAnim.setVisibility(View.VISIBLE);
		Animatable anim = (Animatable) leftAnim.getBackground();
		anim.start();
		Animatable ranim = (Animatable) rightAnim.getBackground();
		ranim.start();
	}

	public void setHideSendBtn(){
		mReLoad2.setVisibility(View.VISIBLE);
		mreloadLayout.setVisibility(View.GONE);
	}

	private void hideAnim() {
		leftAnim.setVisibility(View.GONE);
		rightAnim.setVisibility(View.GONE);
		Animatable anim = (Animatable) leftAnim.getBackground();
		anim.stop();
		Animatable ranim = (Animatable) rightAnim.getBackground();
		ranim.stop();
	}

	@Override
	public void isShowAnim(boolean isShow) {
		if (isShow) {
			showAnim();
		} else {
			hideAnim();
		}
	}

	@Override
	public void onClick(View v) {

		if (v == mSend) {
			NotifityBus.broadcast("stop", null);
			if (mOnSendCallBackListener!=null) {
				mOnSendCallBackListener.send();
			}
		}
		if (v == mReLoad) {
			NotifityBus.broadcast("stop", null);
			if (mOnSendCallBackListener!=null) {
				mOnSendCallBackListener.reload();
			}
		}

		if (v == mReLoad2) {
			NotifityBus.broadcast("stop", null);
			if (mOnSendCallBackListener!=null) {
				mOnSendCallBackListener.reload();
			}
		}
	}
	
	
	OnSendCallBackListener mOnSendCallBackListener;
	public void setOnSendCallBackListener(
			OnSendCallBackListener mOnSendCallBackListener) {
		this.mOnSendCallBackListener = mOnSendCallBackListener;
	}
	public interface OnSendCallBackListener{
		void send();
		void reload();
	}
}