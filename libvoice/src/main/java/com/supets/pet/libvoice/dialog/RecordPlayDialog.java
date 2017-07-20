package com.supets.pet.libvoice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.supets.pet.libvoice.R;
import com.supets.pet.libvoice.core.NotifityBus;
import com.supets.pet.nativelib.Settings;
import com.supets.pet.libvoice.widget.PlayLayoutView;
import com.supets.pet.libvoice.widget.RecordLayoutView;

public class RecordPlayDialog extends Dialog {

    private PlayLayoutView mPlayView;
    private RecordLayoutView mRecordView;

    private MYAudio audio;

    public RecordPlayDialog(Context context) {
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

        mPlayView.setOnSendCallBackListener(new PlayLayoutView.OnSendCallBackListener() {
            @Override
            public void send() {
                dismiss();
                if (mListener != null) {
                    audio.audio_url = Settings.recordingOriginPath;
                    mListener.onSend(audio);
                }
            }

            @Override
            public void reload() {
                mRecordView.hideAnim();
                showRecordLay();
            }
        });
        mRecordView.setListener(new RecordLayoutView.OnRecordCALLBackListener() {
            @Override
            public void onRecordTime(String path, int length) {
                audio = new MYAudio(path, length);
                //显示播放层
                showPlayLay();
                mPlayView.setAudio(Settings.recordingOriginPath, audio.length);
            }
        });

        showRecordLay();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                NotifityBus.broadcast("stop", null);
            }
        });
    }


    private OnSendVoiceListenr mListener;

    public void setOnSendVoiceListenr(OnSendVoiceListenr mListener) {
        this.mListener = mListener;
    }

    public void showRecordLay() {
        mRecordView.setVisibility(View.VISIBLE);
        mPlayView.setVisibility(View.GONE);
    }

    public void showPlayLay() {
        mPlayView.setVisibility(View.VISIBLE);
        mRecordView.setVisibility(View.GONE);
    }
}
