package com.supets.pet.nativelib;

import android.os.Handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundTouchVoiceClient{

	private BlockingQueue<short[]> recordQueue = new LinkedBlockingQueue<short[]>();
	private SoundTouchVoiceThread soundTouchThread;
	private Handler handler;
    private ReadingThread  mReadingThread;
	private SampleVoice  mSampleVoice;
    public SoundTouchVoiceClient(Handler mainHandler, SampleVoice sampleVoice) {
		this.handler = mainHandler;
		this.mSampleVoice=sampleVoice;
	}

	public void start() {
		try {
			mReadingThread=new ReadingThread(handler, recordQueue);
			mReadingThread.start();
			soundTouchThread = new SoundTouchVoiceThread(mSampleVoice,handler, recordQueue);
			soundTouchThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			soundTouchThread.stopSoundTounch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
