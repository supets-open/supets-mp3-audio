package com.supets.pet.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Player extends OnPlayMusicInterface implements
		OnCompletionListener, MediaPlayer.OnPreparedListener, OnErrorListener {
	public MediaPlayer mediaPlayer;
	private Context context;

	private void init() {
		mediaPlayer = getMediaPlayer(context);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == 0x1111) {
				if (listener != null) {
					listener.onPlayFail();
					isStart = false;
				}
			}

			if (msg.what == 0x1112) {
				if (listener != null) {
					listener.onPlayPrepare();
				}
			}
		};
	};

	public void play() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.start();
				if (listener != null) {
					listener.onPlayStart();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (listener != null) {
				listener.onPlayFail();
				isStart = false;
			}
		}

	}

	boolean isStart = false;

	public void play(final String videoUrl) {
		if (isStart) {
			return;
		}
		isStart = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// ceshi
				//String videoUrl = "http://data8.5sing.kgimg.com/T1_SCQBydT1R47IVrK.mp3";

				handleProgress.sendEmptyMessage(0x1112);
				try {
					init();
					mediaPlayer.reset();
					mediaPlayer.setDataSource(videoUrl);
					mediaPlayer.prepare();
				} catch (Exception e) {
					e.printStackTrace();
					handleProgress.sendEmptyMessage(0x1111);
				}
			}
		}).start();

	}

	@Override
	/**  
	 * 通过onPrepared播放  
	 */
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		if (listener != null) {
			listener.onPlayStart();
		}
		Log.e("mediaPlayer", "onPrepared");
	}

	public void pause() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				if (listener != null) {
					listener.onPlayPause();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (listener != null) {
				listener.onPlayFail();
				isStart = false;
			}
		}
	}

	public  void stop() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.setOnCompletionListener(null);
				mediaPlayer.setOnPreparedListener(null);
				mediaPlayer.setOnErrorListener(null);
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		} catch (Exception e) {
		}
		if (listener != null) {
			listener.onPlayStop();
			isStart = false;
		}

	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.e("mediaPlayer", "onCompletion");
		if (listener != null) {
			listener.onPlayComplete();
		}
		mp.release();
		mp = null;
		isStart = false;
	}

	public MediaPlayer getMediaPlayer(Context context) {

		MediaPlayer mediaplayer = new MediaPlayer();

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
			return mediaplayer;
		}

		try {
			Class<?> cMediaTimeProvider = Class
					.forName("android.media.MediaTimeProvider");
			Class<?> cSubtitleController = Class
					.forName("android.media.SubtitleController");
			Class<?> iSubtitleControllerAnchor = Class
					.forName("android.media.SubtitleController$Anchor");
			Class<?> iSubtitleControllerListener = Class
					.forName("android.media.SubtitleController$Listener");

			Constructor<?> constructor = cSubtitleController
					.getConstructor(new Class[] { Context.class,
							cMediaTimeProvider, iSubtitleControllerListener });

			Object subtitleInstance = constructor.newInstance(context, null,
					null);

			Field f = cSubtitleController.getDeclaredField("mHandler");

			f.setAccessible(true);
			try {
				f.set(subtitleInstance, new Handler());
			} catch (IllegalAccessException e) {
				return mediaplayer;
			} finally {
				f.setAccessible(false);
			}
			Method setsubtitleanchor = mediaplayer.getClass().getMethod(
					"setSubtitleAnchor", cSubtitleController,
					iSubtitleControllerAnchor);

			setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
		} catch (Exception e) {
		}

		return mediaplayer;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (listener != null) {
			listener.onPlayFail();
		}
		return false;
	}

}