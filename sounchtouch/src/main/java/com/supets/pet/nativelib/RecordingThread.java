package com.supets.pet.nativelib;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

import java.util.concurrent.BlockingQueue;


public class RecordingThread extends Thread {
	
	private static int FREQUENCY = 16000;
	private static int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	private static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	

	private volatile boolean setToStopped = false;
	private Handler handler;
	
	
	private static int bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
	private BlockingQueue<short[]> recordQueue;
	
	public RecordingThread(Handler handler, BlockingQueue<short[]> recordQueue){
		this.handler = handler;
		this.recordQueue = recordQueue;
	}
	
	public void stopRecording(){
		this.setToStopped = true;
	}
	
	
	
	@Override
	public void run(){
		
			AudioRecord audioRecord = null;
			
			try {
					
				short[] buffer = new short[bufferSize];
				audioRecord = new AudioRecord(
						MediaRecorder.AudioSource.MIC, FREQUENCY,
						CHANNEL, ENCODING, bufferSize);
				
				int state = audioRecord.getState();				
				if (state == AudioRecord.STATE_INITIALIZED) {
					
					audioRecord.startRecording();			
					//handler.obtainMessage(Settings.MSG_RECORDING_START).sendToTarget();										
					boolean flag = true;					
					while (!setToStopped) {						
						int len = audioRecord.read(buffer, 0, buffer.length);							
						// 去掉全0数据
						if(flag){							
							double sum = 0.0;
							for(int i = 0; i < len; i++){
								sum += buffer[i];
							}
							if(sum == 0.0){
								continue;								
							}else{														
								//handler.obtainMessage(Settings.MSG_RECORDING_START).sendToTarget();
								flag = false;												
							}
						}												
						short[] data = new short[len]; 
						System.arraycopy(buffer, 0, data, 0, len);							
						recordQueue.add(data);												
					}									
					audioRecord.stop();
					handler.sendEmptyMessage(Settings.MSG_RECORDING_SUCCESS);
				} else {					
					handler.sendEmptyMessage(Settings.MSG_RECORDING_EXCEPTION);
				}
			} catch (Exception e) {				
				handler.sendEmptyMessage(Settings.MSG_RECORDING_EXCEPTION);					
			} finally {							
				try {								
					audioRecord.release();
					audioRecord = null;					
				} catch (Exception e) {
					
				}
			}
			
		}
		
		
	
	
}
