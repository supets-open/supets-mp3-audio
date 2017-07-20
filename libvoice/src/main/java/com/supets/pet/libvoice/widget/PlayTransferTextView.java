package com.supets.pet.libvoice.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.supets.pet.libvoice.core.NotifityBus;
import com.supets.pet.libvoice.core.OnPlayMusicInterface;
import com.supets.pet.libvoice.core.PlayButton;
import com.supets.pet.nativelib.SampleVoice;
import com.supets.pet.nativelib.Settings;
import com.supets.pet.nativelib.SoundTouchVoiceClient;

public class PlayTransferTextView extends TextView implements
        OnPlayMusicInterface.OnPlayCompletionListener {
    public static final int PLAY_STATUS_Load = 0;
    public static final int PLAY_STATUS_DOING = 1;
    public static final int PLAY_STATUS_END = 2;
    public static final int PLAY_STATUS_PAUSE = 3;
    public OnPlayMusicInterface mPlayer;
    public String mp3File;

    public int currstatus = PLAY_STATUS_END;

    private SoundTouchVoiceClient soundTouchClient;

    private SampleVoice mSampleVoice = new SampleVoice(0, 0, 0);

    public void start() {
        try {
            soundTouchClient = new SoundTouchVoiceClient(mainHandler,
                    mSampleVoice);
            soundTouchClient.start();
        } catch (Exception e) {
            if (mListener != null) {
                mListener.isShowAnim(false);
            }
        }
    }

    public void stop() {
        try {
            soundTouchClient.stop();
        } catch (Exception e) {
            if (mListener != null) {
                mListener.isShowAnim(false);
            }
        }
    }

    public NotifityBus.BaseNotifityEvent event = new NotifityBus.BaseNotifityEvent() {

        @Override
        public void onReceive(String action, Bundle savedInstanceState) {
            if (action.equals("stop")) {
                try {
                    stopPlay();
                } catch (Exception e) {
                }
                Log.v("PLAY", "STOP");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.v("=====>", msg.what + "");
            switch (msg.what) {
                case Settings.CHANGVOICE_SUCCESS:
                    switch (currstatus) {
                        case PLAY_STATUS_END:
                            NotifityBus.broadcast("stop", null);
                            NotifityBus.addNotifityEventListener(event);
                            mPlayer.play(mp3File);
                            break;
                        case PLAY_STATUS_Load:
                            mPlayer.stop();
                            break;
                        case PLAY_STATUS_DOING:
                            mPlayer.stop();
                            break;
                        case PLAY_STATUS_PAUSE:
                            mPlayer.play();
                            break;
                        default:
                            break;
                    }
                    setBackGround();
                    break;
                case Settings.CHANGEVOICE_FAIL:
                    if (mListener != null) {
                        mListener.isShowAnim(false);
                    }
                    break;
                case Settings.MSG_READING_SUCCESS:
                    stop();
                    break;
                case Settings.MSG_READING_EXCEPTION:
                    if (mListener != null) {
                        mListener.isShowAnim(false);
                    }
                    break;
                default:

                    break;
            }
        }
    };

    public PlayTransferTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mp3File = Settings.recordingVoicePath;
    }

    public PlayTransferTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayTransferTextView(Context context) {
        super(context);
        init(context);
    }

    public void setPlayer(OnPlayMusicInterface player) {
        mPlayer = player;
        mPlayer.setOnPlayCompleteListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.close();
        }
    }

    public void setBackGround() {
        switch (currstatus) {
            case PLAY_STATUS_Load:
                if (mListener != null) {
                    mListener.isShowAnim(false);
                }
                break;
            case PLAY_STATUS_END:
                if (mListener != null) {
                    mListener.isShowAnim(false);
                }
                break;
            case PLAY_STATUS_DOING:
                if (mListener != null) {
                    mListener.isShowAnim(true);
                }
                break;
            case PLAY_STATUS_PAUSE:
                if (mListener != null) {
                    mListener.isShowAnim(true);
                }
                break;
            default:
                break;
        }
    }

    public void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        currstatus = PLAY_STATUS_END;
        setBackGround();
    }

    public void setPlayerPath(String path) {
        mp3File = path;
    }

    @Override
    public void onPlayPrepare() {
        currstatus = PLAY_STATUS_Load;
        Log.v("RECORD", "PLAY_STATUS_load");
        setBackGround();
    }

    @Override
    public void onPlayStart() {
        currstatus = PLAY_STATUS_DOING;
        Log.v("RECORD", "PLAY_STATUS_DOING");
        setBackGround();
    }

    @Override
    public void onPlayStop() {
        currstatus = PLAY_STATUS_END;
        setBackGround();
    }

    @Override
    public void onPlayPause() {
        currstatus = PLAY_STATUS_PAUSE;
        Log.v("RECORD", "PLAY_STATUS_PAUSE");
        setBackGround();
    }

    public void onPlayComplete() {
        currstatus = PLAY_STATUS_END;
        setBackGround();
    }

    @Override
    public void onPlayFail() {
        currstatus = PLAY_STATUS_END;
        setBackGround();
    }

    @Override
    public void onPlayFail2() {
        currstatus = PLAY_STATUS_END;
        setBackGround();
    }

    private PlayButton.OnPlayCallBackListener mListener;

    public void setOnPlayCallBackListener(
            PlayButton.OnPlayCallBackListener playLayoutHolder) {
        this.mListener = playLayoutHolder;
    }

    public void setSampleVoice(SampleVoice sampleVoice) {
        this.mSampleVoice = sampleVoice;
    }
}
