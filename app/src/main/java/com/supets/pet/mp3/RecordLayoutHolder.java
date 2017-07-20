package com.supets.pet.mp3;

import android.graphics.drawable.Animatable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.supets.pet.R;
import com.supets.pet.dialog.TimeDateUtils;
import com.supets.pet.mp3.RecordButton.OnRecordListener;
import com.supets.pet.nativelib.Settings;

public class RecordLayoutHolder implements OnRecordListener {
    private RecordButton mRecordButton;
    private TextView mTime;
    private View leftAnim;
    private View rightAnim;
    private View mWholeView;

    private View recordLayout;
    private View voicelayout;
    private View playLayout;


    public RecordLayoutHolder(View view) {
        this.mWholeView = view;
        init();
    }

    private void init() {
        mRecordButton = (RecordButton) mWholeView.findViewById(R.id.record);
        mTime = (TextView) mWholeView.findViewById(R.id.time);
        leftAnim = mWholeView.findViewById(R.id.leftAnim);
        rightAnim = mWholeView.findViewById(R.id.rightAnim);
        mRecordButton.setOnRecordListener(this);
        hideAnim();
        recordLayout = mWholeView.findViewById(R.id.recordLayout);
        voicelayout = mWholeView.findViewById(R.id.voicelayout);
        playLayout = mWholeView.findViewById(R.id.playLayout);

        showRecordLay();
    }

    private void showAnim() {
        leftAnim.setVisibility(View.VISIBLE);
        rightAnim.setVisibility(View.VISIBLE);
        Animatable anim = (Animatable) leftAnim.getBackground();
        anim.start();
        Animatable ranim = (Animatable) rightAnim.getBackground();
        ranim.start();
        mTime.setText("0:00");
    }

    public void hideAnim() {
        mTime.setText("按住录音");
        leftAnim.setVisibility(View.GONE);
        rightAnim.setVisibility(View.GONE);
        Animatable anim = (Animatable) leftAnim.getBackground();
        anim.stop();
        Animatable ranim = (Animatable) rightAnim.getBackground();
        ranim.stop();
    }

    @Override
    public void recordStart() {
        showAnim();
        showRecordLay();
    }

    public void showVoiceLay() {
        if (recordLayout != null) {
            recordLayout.setVisibility(View.GONE);
        }
        if (voicelayout != null) {
            voicelayout.setVisibility(View.VISIBLE);
        }
        if (playLayout != null) {
            playLayout.setVisibility(View.GONE);
        }
    }

    public void showRecordLay() {
        if (recordLayout != null) {
            recordLayout.setVisibility(View.VISIBLE);
        }
        if (voicelayout != null) {
            voicelayout.setVisibility(View.GONE);
        }
        if (playLayout != null) {
            playLayout.setVisibility(View.GONE);
        }
    }

    public void showPlayLay() {
        if (playLayout != null) {
            playLayout.setVisibility(View.VISIBLE);
        }
        if (voicelayout != null) {
            voicelayout.setVisibility(View.GONE);
        }
        if (recordLayout != null) {
            recordLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void recordFail() {
        hideAnim();
        Toast.makeText(mWholeView.getContext(), "录音失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recordSuccess(String path, int length) {
        hideAnim();
        showVoiceLay();
        if (mListener != null) {
            mListener.onRecordTime(Settings.recordingOriginPath, length);
        }
    }

    @Override
    public void cancelRecord() {
        hideAnim();
        Toast.makeText(mWholeView.getContext(), "取消录音", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recordLengthShort() {
        hideAnim();
        Toast.makeText(mWholeView.getContext(), "录音太短", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recordTime(long time) {
        if (leftAnim.getVisibility() == View.GONE) {
            mTime.setText("按住录音");
        } else {
            mTime.setText(TimeDateUtils.formatRecordTime((int) time));

        }
    }

    private OnRecordCALLBackListener mListener;

    public void setListener(OnRecordCALLBackListener mListenerl) {
        this.mListener = mListenerl;
    }

    public interface OnRecordCALLBackListener {
        void onRecordTime(String path, int length);
    }

}