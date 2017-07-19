package com.supets.pet.nativelib;

import android.os.Handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundTouchClient /**implements OnCompletionListener**/{

	private BlockingQueue<short[]> recordQueue = new LinkedBlockingQueue<short[]>();
	private RecordingThread recordingThread;
	private SoundTouchThread soundTouchThread;
    private Handler mainHandler;
	
	public SoundTouchClient(Handler mainHandler){
		this.mainHandler = mainHandler;
	}
	
	public void start(){
		recordingThread = new RecordingThread(mainHandler, recordQueue);
		recordingThread.start();
		
		soundTouchThread = new SoundTouchThread(mainHandler, recordQueue);
		soundTouchThread.start();
	}
	
	public void stop(){
		recordingThread.stopRecording();
		soundTouchThread.stopSoundTounch();
	}
}
