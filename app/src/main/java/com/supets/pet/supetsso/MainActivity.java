
package com.supets.pet.supetsso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.supets.pet.R;
import com.supets.pet.dialog.MYAudio;
import com.supets.pet.dialog.RecordDialog;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.trnsferButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                RecordDialog dialog = new RecordDialog(MainActivity.this);
                dialog.setOnSendVoiceListenr(new RecordDialog.OnSendVoiceListenr() {
                    @Override
                    public void onSend(MYAudio audio) {

                    }
                });
                dialog.show();

            }
        });
    }

}
