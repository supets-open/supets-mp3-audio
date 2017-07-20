package com.supets.pet.libvoice.widget;

import java.io.File;

import com.supets.pet.nativelib.Settings;
import com.supets.pet.nativelib.SoundTouchClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class RecordButton extends Button implements View.OnTouchListener {

	private SoundTouchClient soundTouchClient;
	private boolean passed = false;
	private boolean isCancel = false;
	private OnRecordListener mListener;
	private static final int MIN_INTERVAL_TIME = 1000;
	private static final int MAX_INTERVAL_TIME = 15000;
	private ObtainDecibelThread mThread;
	private long mStartTime = 0;

	public RecordButton(Context context) {
		super(context);
		init();
	}

	@SuppressLint("NewApi")
	public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void init() {
		soundTouchClient = new SoundTouchClient(mHandler);
		setOnTouchListener(this);
	}

	public void setOnRecordListener(OnRecordListener mListener) {
		this.mListener = mListener;
	}

	private class ObtainDecibelThread extends Thread {
		private volatile boolean running = true;

		public void exit() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(7777);
				if (System.currentTimeMillis() - mStartTime >= MAX_INTERVAL_TIME) {
					mHandler.sendEmptyMessage(-9999);
					exit();
					break;
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Settings.CHANGVOICE_SUCCESS:
					String path = (String) msg.obj;
					if (isCancel) {
						mListener.cancelRecord();
					} else {

						if (TextUtils.isEmpty(path)) {
							mListener.recordFail();
						} else {
							long intervalTime = System.currentTimeMillis()
									- mStartTime;
							if (intervalTime < MIN_INTERVAL_TIME) {
								mListener.recordLengthShort();
								File file = new File(path);
								file.delete();
							} else {
								mListener.recordSuccess(path, (int)(intervalTime/1000));
							}
						}
					}
					break;
				case Settings.CHANGEVOICE_FAIL:
					if (isCancel) {
						mListener.cancelRecord();
					} else {
						mListener.recordFail();
					}
					break;

				case -9999://时间超了
					stopRecording();
					break;
				case -8888:
					mListener.recordStart();
					break;
				case 7777:
					int time = (int) ((System.currentTimeMillis() - mStartTime) / 1000);
					mListener.recordTime(time);
					break;
				default:
					break;
			}
		}

	};

	public SoundTouchClient getSoundTouchClient() {
		return soundTouchClient;
	}

	protected void stopRecording() {
		soundTouchClient.stop();
		if (mThread != null) {
			mThread.exit();
			mThread = null;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (!passed) {

					startRecording();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_UP:
				if (passed) {
					stopRecording(event);
				}
				passed = false;
				break;
			default:
		}
		return false;
	}

	private void stopRecording(MotionEvent event) {
		if (event.getY() < -50) {
			isCancel = true;
		} else {
			isCancel = false;
		}
		soundTouchClient.stop();
		if (mThread != null) {
			mThread.exit();
			mThread = null;
		}
	}

	private void startRecording() {
		mHandler.sendEmptyMessage(-8888);
		soundTouchClient.start();
		passed = true;
		isCancel = false;
		mStartTime = System.currentTimeMillis();
		mThread = new ObtainDecibelThread();
		mThread.start();
	}

	public interface OnRecordListener {
		void recordStart();

		void recordTime(long time);

		void recordFail();

		void recordSuccess(String path, int length);

		void cancelRecord();

		void recordLengthShort();
	}

}