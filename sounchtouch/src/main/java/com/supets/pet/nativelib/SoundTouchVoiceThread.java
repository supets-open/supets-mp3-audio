package com.supets.pet.nativelib;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SoundTouchVoiceThread extends Thread {

    private BlockingQueue<short[]> recordQueue;
    private Handler handler;
    private static final long TIME_WAIT_RECORDING = 100;
    private volatile boolean setToStopped = false;
    private JNISoundTouch soundtouch = new JNISoundTouch();
    private LinkedList<byte[]> wavDatas = new LinkedList<byte[]>();
    SampleVoice mSampleVoice;

    public SoundTouchVoiceThread(SampleVoice mSampleVoice, Handler handler, BlockingQueue<short[]> recordQueue) {
        this.handler = handler;
        this.recordQueue = recordQueue;
        this.mSampleVoice = mSampleVoice;
    }

    public void stopSoundTounch() {
        setToStopped = true;
    }


    @Override
    public void run() {
        soundtouch.setSampleRate(mSampleVoice.sampleRate);
        soundtouch.setChannels(mSampleVoice.channels);
        soundtouch.setPitchSemiTones(mSampleVoice.pitchSemiTones);
        soundtouch.setRateChange(mSampleVoice.rateChange);
        soundtouch.setTempoChange(mSampleVoice.tempoChange);

        wavDatas.clear();
        //MP3 init
        FileOutputStream output = null;
        try {
            File file = new File(Settings.recordingPath);
            if (!file.exists()) {
                file.mkdir();
            }

            output = new FileOutputStream(new File(Settings.recordingMp3Path));
        } catch (FileNotFoundException e) {
            return;
        }
        com.supets.pet.nativelib.MP3Recorder.init(16000, 1, 16000, 32);
        //MP3 init
        short[] recordingData;
        while (true) {

            try {
                recordingData = recordQueue.poll(TIME_WAIT_RECORDING, TimeUnit.MILLISECONDS);
                if (recordingData != null) {

                    soundtouch.putSamples(recordingData, recordingData.length);

                    short[] buffer;
                    do {

                        buffer = soundtouch.receiveSamples();
                        byte[] bytes = Utils.shortToByteSmall(buffer);
                        wavDatas.add(bytes);
                        if (buffer.length > 0) {
                            //MP3 init
                            byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
                            int encResult = com.supets.pet.nativelib.MP3Recorder.encode(buffer,
                                    buffer, buffer.length, mp3buffer);
                            if (encResult < 0) {
                                break;
                            }
                            if (encResult != 0) {
                                try {
                                    output.write(mp3buffer, 0, encResult);
                                } catch (IOException e) {
                                    break;
                                }
                            }
                        }
                        //MP3 init

                    } while (buffer.length > 0);


                }

                if (setToStopped && recordQueue.size() == 0) {
                    break;
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(Settings.CHANGEVOICE_FAIL);
            }

        }

        int fileLength = 0;
        for (byte[] bytes : wavDatas) {
            fileLength += bytes.length;
        }

        try {
            WaveHeader header = new WaveHeader(fileLength);
            byte[] headers = header.getHeader();
            // 保存文件
            FileOutputStream out = new FileOutputStream(Settings.recordingVoicePath);
            out.write(headers);
            for (byte[] bytes : wavDatas) {
                out.write(bytes);
            }
            out.close();
            Message msg = handler.obtainMessage(Settings.CHANGVOICE_SUCCESS, Settings.recordingVoicePath);
            handler.sendMessage(msg);
        } catch (Exception e) {
            handler.sendEmptyMessage(Settings.CHANGEVOICE_FAIL);
        } finally {
            //MP3 init
            try {
                output.flush();
                output.close();
            } catch (IOException e) {
            }
            com.supets.pet.nativelib.MP3Recorder.close();
            //MP3 init
        }

    }
}
