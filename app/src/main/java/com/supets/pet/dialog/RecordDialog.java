package com.supets.pet.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.supets.pet.R;
import com.supets.pet.nativelib.Settings;

public class RecordDialog extends Dialog implements
        PlayLayoutView.OnSendCallBackListener,
        RecordLayoutView.OnRecordCALLBackListener,
        SounchTouchView.OnSoundTouchListener {

    private PlayLayoutView mPlayView;
    private RecordLayoutView mRecordView;
    private SounchTouchView mSoundTouchView;

    private MYAudio audio;

    public RecordDialog(Context context) {
        super(context, R.style.ShareDialog);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawableResource(android.R.color.white);
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_record, null);
        super.setContentView(mView);
        setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;

        mRecordView = (RecordLayoutView) findViewById(R.id.recordLayout);
        mPlayView = (PlayLayoutView) findViewById(R.id.playLayout);
        mSoundTouchView = (SounchTouchView) findViewById(R.id.voicelayout);

        mSoundTouchView.setListener(this);
        mPlayView.setOnSendCallBackListener(this);
        mRecordView.setListener(this);

        showRecordLay();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                NotifityBus.broadcast("stop", null);
            }
        });
    }


    @Override
    public void send() {
        dismiss();
        if (mListener != null) {
            audio.audio_url = type == 0 ? Settings.recordingOriginPath : Settings.recordingVoicePath;
            mListener.onSend(audio);
        }

    }

    @Override
    public void reload() {
        mRecordView.hideAnim();

        showRecordLay();

    }

    @Override
    public void onRecordTime(String path, int length) {

        showVoiceLay();

        audio = new MYAudio(path, length);
        mSoundTouchView.setAudioLength(length);
    }

    private int type = 0;

    @Override
    public void onConfirm(int type) {
        //显示播放层
        mPlayView.setVisibility(View.VISIBLE);
        mSoundTouchView.setVisibility(View.GONE);
        mRecordView.setVisibility(View.GONE);

        this.type = type;
        mPlayView.setAudio(type == 0 ? Settings.recordingOriginPath : Settings.recordingVoicePath, audio.length);
    }

    @Override
    public void onCancel(int type) {
        //隐藏自己
        dismiss();
    }

    OnSendVoiceListenr mListener;

    public void setOnSendVoiceListenr(OnSendVoiceListenr mListener) {
        this.mListener = mListener;
    }

    public interface OnSendVoiceListenr {
        void onSend(MYAudio audio);
    }


    public void showVoiceLay() {
        mRecordView.setVisibility(View.GONE);
        mSoundTouchView.setVisibility(View.VISIBLE);
        mPlayView.setVisibility(View.GONE);
    }

    public void showRecordLay() {
        mRecordView.setVisibility(View.VISIBLE);
        mSoundTouchView.setVisibility(View.GONE);
        mPlayView.setVisibility(View.GONE);
    }

    public void showPlayLay() {
        mPlayView.setVisibility(View.VISIBLE);
        mSoundTouchView.setVisibility(View.GONE);
        mRecordView.setVisibility(View.GONE);
    }
}
