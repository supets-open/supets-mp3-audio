package com.supets.pet.dialog;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.supets.pet.R;
import com.supets.pet.nativelib.SampleVoice;

public class PlaySoundTouchWidget extends FrameLayout implements
        PlayButton.OnPlayCallBackListener {

    private int[] effectBack = new int[]{
            R.drawable.icon_trnsfer_effect_origin,
            R.drawable.icon_trnsfer_effect_girl,
            R.drawable.icon_trnsfer_effect_father,
            R.drawable.icon_trnsfer_effect_boy,
            R.drawable.icon_trnsfer_effect_yellow,
            R.drawable.icon_trnsfer_effect_baby
    };

    private SampleVoice[] mSampleVoice = new SampleVoice[]{
            new SampleVoice(0, 0, 0),
            new SampleVoice(10, 5, 10),
            new SampleVoice(-10, 10, 5),
            new SampleVoice(15, -60, 45),
            new SampleVoice(5, 12, 10),
            new SampleVoice(-5, -30, 50)};

    private PlayTransferTextView mButton;
    private View mAnimLayout;
    private TextView mAnim;
    private TextView mTime;

    public PlaySoundTouchWidget(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.record_widget_soundtouch_play_item,
                this);

        mButton = (PlayTransferTextView) findViewById(R.id.changebtn);
        mButton.setPlayer(new Player());
        mButton.setOnPlayCallBackListener(this);
        mAnimLayout = findViewById(R.id.layoutAnim);
        mAnim = (TextView) findViewById(R.id.changAnim);
        mTime = (TextView) findViewById(R.id.changeTime);
        mAnimLayout.setVisibility(View.GONE);
    }

    public PlaySoundTouchWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaySoundTouchWidget(Context context) {
        super(context);
        init();
    }


    public void startPlay() {
        mButton.start();
    }

    @Override
    public void isShowAnim(boolean isShow) {
        if (isShow) {
            mAnimLayout.setVisibility(View.VISIBLE);
            Animatable anim = (Animatable) mAnim.getBackground();
            anim.start();
        } else {
            mAnimLayout.setVisibility(View.GONE);
            Animatable anim = (Animatable) mAnim.getBackground();
            anim.stop();
        }
    }

    public void setTime(int time) {
        mTime.setText(TimeDateUtils.formatRecordTime(time));
        mTime.invalidate();
    }

    public void setEfffctType(int type) {
        mButton.setBackgroundResource(effectBack[type]);
        mButton.setSampleVoice(mSampleVoice[type]);
    }
}
