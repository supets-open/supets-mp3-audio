package com.supets.pet.nativelib;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.BlockingQueue;


public class ReadingThread extends Thread {
	
	private static int FREQUENCY = 16000;
	private static int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	private static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private Handler handler;
	
	private static int bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
	private BlockingQueue<short[]> recordQueue;
	
	public ReadingThread(Handler handler, BlockingQueue<short[]> recordQueue){
		this.handler = handler;
		this.recordQueue = recordQueue;
	}
	
	@Override
	public void run(){
			FileInputStream fis = null;
			try {
				File outFile = new File(Settings.recordingOriginPath);
				if(!outFile.exists()) {
					handler.sendEmptyMessage(Settings.MSG_READING_EXCEPTION);
					return;
				}
				fis = new FileInputStream(outFile);	
				byte[] buffer = new byte[bufferSize * 2];
				int len = 0;
				while (len != -1) {
					len = fis.read(buffer, 0, buffer.length);
					recordQueue.add(Utils.getShort(buffer));
				}
				handler.sendEmptyMessage(Settings.MSG_READING_SUCCESS);
			} catch (Exception e) {
				handler.sendEmptyMessage(Settings.MSG_READING_EXCEPTION);	
			}
		}
}
