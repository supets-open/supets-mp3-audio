package com.supets.pet.dialog;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.supets.pet.R;
import com.supets.pet.nativelib.Settings;

public class RecordLayoutView extends FrameLayout implements RecordButton.OnRecordListener {

    private RecordButton mRecordButton;
    private TextView mTime;
    private View leftAnim;
    private View rightAnim;

    public RecordLayoutView(@NonNull Context context) {
        super(context);
        init();
    }

    public RecordLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
       LayoutInflater.from(getContext()).inflate(R.layout.record_widget_record_item, this);
        mRecordButton = (RecordButton) findViewById(R.id.record);
        mTime = (TextView) findViewById(R.id.time);
        leftAnim = findViewById(R.id.leftAnim);
        rightAnim = findViewById(R.id.rightAnim);
        mRecordButton.setOnRecordListener(this);
        hideAnim();
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
//        showRecordLay();
    }

    @Override
    public void recordFail() {
        hideAnim();
        Toast.makeText(getContext(), "录音失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recordSuccess(String path, int length) {
        hideAnim();
        if (mListener != null) {
            mListener.onRecordTime(Settings.recordingOriginPath, length);
        }
    }

    @Override
    public void cancelRecord() {
        hideAnim();
        Toast.makeText(getContext(), "取消录音", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recordLengthShort() {
        hideAnim();
        Toast.makeText(getContext(), "录音太短", Toast.LENGTH_SHORT).show();
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