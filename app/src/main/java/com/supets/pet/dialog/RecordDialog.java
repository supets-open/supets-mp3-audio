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
        PlayLayoutHolder.OnSendCallBackListener,
        RecordLayoutHolder.OnRecordCALLBackListener,
        SounchTouchHolder.OnSoundTouchListener {

    private RecordLayoutHolder mRecordHolder;
    private PlayLayoutHolder mPlayHolder;
    private SounchTouchHolder mSoundTouchHolder;

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

        mRecordHolder = new RecordLayoutHolder(mView);
        mPlayHolder = new PlayLayoutHolder(mView);
        mSoundTouchHolder = new SounchTouchHolder(mView);
        mSoundTouchHolder.setListener(this);
        mPlayHolder.setOnSendCallBackListener(this);
        mRecordHolder.setListener(this);
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
            audio.audio_url=type==0? Settings.recordingOriginPath: Settings.recordingVoicePath;
            mListener.onSend(audio);
        }

    }

    @Override
    public void reload() {
        mRecordHolder.hideAnim();
        mRecordHolder.showRecordLay();
    }

    @Override
    public void onRecordTime(String path, int length) {
        audio = new MYAudio(path, length);
        mSoundTouchHolder.setAudioLength(length);

    }

    private int type = 0;

    @Override
    public void onConfirm(int type) {
        this.type = type;
        mPlayHolder.setAudio(type==0? Settings.recordingOriginPath: Settings.recordingVoicePath,audio.length);
    }

    @Override
    public void onCancel(int type) {
        dismiss();
    }

    OnSendVoiceListenr mListener;

    public void setOnSendVoiceListenr(OnSendVoiceListenr mListener) {
        this.mListener = mListener;
    }

    public  interface OnSendVoiceListenr{
         void onSend(MYAudio audio);
    }
}
