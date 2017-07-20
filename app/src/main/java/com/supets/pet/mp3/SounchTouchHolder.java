package com.supets.pet.mp3;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.supets.pet.R;
import com.supets.pet.dialog.NotifityBus;

public class SounchTouchHolder implements View.OnClickListener {

    private View mWholeView;

    private View recordLayout;
    private View voicelayout;
    private View playLayout;


    private Button mOk;
    private Button mCancel;


    private OnSoundTouchListener mListener;

    private int type = 0;
    private int length = 0;

    public SounchTouchHolder(View view) {
        this.mWholeView = view;
        init();
    }


    private PlaySoundTouchWidget[] plays = new PlaySoundTouchWidget[6];
    private TextView[] text = new TextView[6];
    private View[] effects = new View[6];

    private String[] effect=null;
    private void init() {
        effect= mWholeView.getResources().getStringArray(R.array.effect);

        recordLayout = mWholeView.findViewById(R.id.recordLayout);
        voicelayout = mWholeView.findViewById(R.id.voicelayout);
        playLayout = mWholeView.findViewById(R.id.playLayout);

        mCancel = (Button) mWholeView.findViewById(R.id.cancel);
        mOk = (Button) mWholeView.findViewById(R.id.ok);
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        for (int i = 0; i < 6; i++) {
            final int index=i;
            int id = getIdByName("effect" + (index + 1));
            effects[index] = mWholeView.findViewById(id);
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



    public  int getIdByName(String name) {
        return mWholeView.getResources().getIdentifier(name, "id", mWholeView.getContext().getPackageName());
    }


    private void setSelect() {
        for (int i = 0; i < 6; i++) {
            text[i].setSelected(type == i);
        }
    }


    private void ok() {
        if (playLayout != null) {
            playLayout.setVisibility(View.VISIBLE);
        }
        if (recordLayout != null) {
            recordLayout.setVisibility(View.GONE);
        }
        if (voicelayout != null) {
            voicelayout.setVisibility(View.GONE);
        }
    }

    private void cancel() {
        if (recordLayout != null) {
            recordLayout.setVisibility(View.GONE);
        }
        if (voicelayout != null) {
            voicelayout.setVisibility(View.GONE);
        }
        if (playLayout != null) {
            playLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mOk) {
            NotifityBus.broadcast("stop", null);
            ok();
            if (mListener != null) {
                mListener.onConfirm(type);
            }
        }

        if (v == mCancel) {
            NotifityBus.broadcast("stop", null);
            cancel();
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
