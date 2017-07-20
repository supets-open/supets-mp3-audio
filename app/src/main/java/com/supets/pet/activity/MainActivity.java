
package com.supets.pet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.supets.pet.R;
import com.supets.pet.libvoice.dialog.MYAudio;
import com.supets.pet.libvoice.dialog.OnSendVoiceListenr;
import com.supets.pet.libvoice.dialog.RecordPlayDialog;
import com.supets.pet.libvoice.dialog.VoiceDialog;
import com.supets.pet.nativelib.Settings;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.wavButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                RecordPlayDialog dialog = new RecordPlayDialog(MainActivity.this);
                dialog.setOnSendVoiceListenr(new OnSendVoiceListenr() {
                    @Override
                    public void onSend(MYAudio audio) {
                        Toast.makeText(MainActivity.this, "wav文件路径:" + audio.audio_url,
                                Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });

        findViewById(R.id.trnsferButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                VoiceDialog dialog = new VoiceDialog(MainActivity.this);
                dialog.setOnSendVoiceListenr(new OnSendVoiceListenr() {
                    @Override
                    public void onSend(MYAudio audio) {
                        Toast.makeText(MainActivity.this, "wav文件路径:" + audio.audio_url, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });
    }

}
