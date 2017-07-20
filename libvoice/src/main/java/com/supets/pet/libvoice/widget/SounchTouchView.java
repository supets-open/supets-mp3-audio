package com.supets.pet.libvoice.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.supets.pet.libvoice.R;
import com.supets.pet.libvoice.core.NotifityBus;

public class SounchTouchView extends FrameLayout implements View.OnClickListener {

    private Button mOk;
    private Button mCancel;

    private OnSoundTouchListener mListener;

    private int type = 0;
    private int length = 0;


    public SounchTouchView(@NonNull Context context) {
        super(context);
        init();
    }

    public SounchTouchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private PlaySoundTouchWidget[] plays = new PlaySoundTouchWidget[6];
    private TextView[] text = new TextView[6];
    private View[] effects = new View[6];

    private String[] effect = null;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.record_widget_soundtouch, this);

        effect = getResources().getStringArray(R.array.effect);
        mCancel = (Button) findViewById(R.id.cancel);
        mOk = (Button) findViewById(R.id.ok);
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        for (int i = 0; i < 6; i++) {
            final int index = i;
            int id = getIdByName("effect" + (index + 1));
            effects[index] = findViewById(id);
            plays[index] = (PlaySoundTouchWidget) effects[index].findViewById(R.id.playsound);
            plays[index].setEfffctType(index);
            text[index] = (TextView) effects[index].findViewById(R.id.effectText);
            text[index].setText(effect[index]);

            effects[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plays[index].startPlay();
                    type = index;
                    setSelect();
                }
            });
        }

        text[0].setSelected(true);
    }


    public int getIdByName(String name) {
        return getResources().getIdentifier(name, "id", getContext().getPackageName());
    }


    private void setSelect() {
        for (int i = 0; i < 6; i++) {
            text[i].setSelected(type == i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mOk) {
            NotifityBus.broadcast("stop", null);
            if (mListener != null) {
                mListener.onConfirm(type);
            }
        }

        if (v == mCancel) {
            NotifityBus.broadcast("stop", null);
            if (mListener != null) {
                mListener.onCancel(type);
            }
        }
    }


    public void setListener(OnSoundTouchListener mListener) {
        this.mListener = mListener;
    }

    public interface OnSoundTouchListener {
        void onConfirm(int type);

        void onCancel(int type);
    }

    public void setAudioLength(int length) {
        this.length = length;
        for (int i = 0; i < 6; i++) {
            plays[i].setTime(length);
        }
    }
}
