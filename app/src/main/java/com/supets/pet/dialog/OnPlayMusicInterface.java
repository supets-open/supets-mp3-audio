package com.supets.pet.dialog;
public class OnPlayMusicInterface  {
	
	   //播放
	   public void play(String filePath){};
	   //继续播放
	   public void play(){};
	   //暂停
	   public void pause(){};
	   //停止
	   public void stop(){};
	   //关闭
	   public void close(){};
	  
	   public OnPlayCompletionListener listener = null;

		public void setOnPlayCompleteListener(OnPlayCompletionListener listener) {
			this.listener = listener;
		}
		
		//播放器播放回调改变布局
		public interface OnPlayCompletionListener {
			//播放完成
			public void onPlayComplete();
			//缓存预处理
			public void onPlayPrepare();
			//开始播放
			public void onPlayStart();
			//播放停止
			public void onPlayStop();
			//播放暂停
			public void onPlayPause();
			//播放失败
			public void onPlayFail();
			//播放失败
			public void onPlayFail2();
		}
		
	}