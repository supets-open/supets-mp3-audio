package com.supets.pet.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.supets.pet.R;
import com.supets.pet.nativelib.Settings;

/**
 * 图片播放，录音播放
 *
 * @author lihongjiang
 */
@SuppressWarnings("ResourceType")
public class PlayButton extends Button implements View.OnClickListener,
        OnPlayMusicInterface.OnPlayCompletionListener {
    public static final int PLAY_STATUS_Load = 0;
    public static final int PLAY_STATUS_DOING = 1;
    public static final int PLAY_STATUS_END = 2;
    public static final int PLAY_STATUS_PAUSE = 3;
    public OnPlayMusicInterface mPlayer;
    public String mp3File;

    public int currstatus = PLAY_STATUS_END;

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

    public PlayButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void init(Context context) {
        mp3File = Settings.recordingVoicePath;
        setOnClickListener(this);
    }


    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayButton(Context context) {
        super(context);
        init(context);
    }

    public void setPlayer(OnPlayMusicInterface player) {
        mPlayer = player;
        mPlayer.setOnPlayCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (currstatus) {
            case PLAY_STATUS_END:

                NotifityBus.broadcast("stop", this);
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

        if (mPb != null) {
            mPb.setVisibility(View.GONE);
        }

        switch (currstatus) {
            case PLAY_STATUS_Load:

                if (mPb != null) {
                    mPb.setVisibility(View.VISIBLE);
                    setBackgroundResource(resId);
                } else {
                    setPlayIcon();
                }

                if (mListener != null) {
                    mListener.isShowAnim(false);
                }

                if (mControlListener!=null){
                    mControlListener.stop();
                }
                break;
            case PLAY_STATUS_END:
                setPlayIcon();
                if (mListener != null) {
                    mListener.isShowAnim(false);
                }
                if (mControlListener!=null){
                    mControlListener.stop();
                }
                break;
            case PLAY_STATUS_DOING:
                setPauseIcon();
                if (mListener != null) {
                    mListener.isShowAnim(true);
                }
                if (mControlListener!=null){
                    mControlListener.start();
                }
                break;
            case PLAY_STATUS_PAUSE:
                //setEnabled(true);
                setPauseIcon();
                break;
            default:
                break;
        }
    }

    private void setPlayIcon() {
        if (mode == 2) {
            setBackgroundResource(R.drawable.record_play_animate_03);
        } else if (mode == 1) {
            setBackgroundResource(R.drawable.icon_iamge_audio_widget_play);
        } else {
            setBackgroundResource(R.drawable.record_play);
        }
    }

    private void setPauseIcon() {

        if (mode == 2) {
            setBackgroundResource(R.drawable.anim_drawable_endrose_play);
            Animatable animatable = (Animatable) getBackground();
            if (!animatable.isRunning()) {
                animatable.start();
            }
        } else if (mode == 1) {
            setBackgroundResource(R.drawable.icon_iamge_audio_widget_playpause);
        } else {
            setBackgroundResource(R.drawable.record_pause);
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
        Log.v("RECORD", "正在加载数据");
        // MYUtils.showToastMessage("正在缓冲数据");
        currstatus = PLAY_STATUS_Load;
        Log.v("RECORD", "PLAY_STATUS_load");
        setBackGround();
    }

    @Override
    public void onPlayStart() {
        currstatus = PLAY_STATUS_DOING;
        Log.v("RECORD", "PLAY_STATUS_DOING");
        setBackGround();
        if (mControlListener!=null){
            mControlListener.start();
        }
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
        Toast.makeText(getContext(),"播放失败",Toast.LENGTH_SHORT).show();
    }


    OnPlayCallBackListener mListener;

    public interface OnPlayCallBackListener {
        void isShowAnim(boolean isShow);
    }

    public void setOnPlayCallBackListener(OnPlayCallBackListener playLayoutHolder) {
        this.mListener = playLayoutHolder;
    }

    private int mode = 0;

    public void setMode() {
        mode = 1;
    }

    public void setEndorseMode() {
        mode = 2;
    }

    private ProgressBar mPb;

    public void setProcessBar(ProgressBar pb) {
        mPb = pb;
    }

    private int resId = R.drawable.icon_iamge_audio_widget_play_bg;

    public void setBackGround(int id) {
        this.resId = id;
    }

    private ControlPlayListener mControlListener;

    public void setmControlListener(ControlPlayListener mControlListener) {
        this.mControlListener = mControlListener;
    }

    public  interface ControlPlayListener{
        void  start();
        void  stop();
    }
}
