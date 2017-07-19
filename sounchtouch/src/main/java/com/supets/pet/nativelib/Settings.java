package com.supets.pet.nativelib;

import android.os.Environment;

public class Settings {

	public static String sdcardPath = Environment.getExternalStorageDirectory().toString();
	public static String recordingPath = sdcardPath + "/wave_soundtouch/";
	public static String recordingOriginPath = recordingPath+"soundtouch.wav";
	public static String recordingVoicePath = recordingPath+"soundtouch_voice.wav";
	public static String recordingMp3Path = recordingPath+"soundtouch_mp3.mp3";

	public static final int MSG_RECORDING_EXCEPTION = 4;
	public static final int MSG_RECORDING_SUCCESS = 5;

	public static final int CHANGEVOICE_FAIL = 8;
	public static final int CHANGVOICE_SUCCESS = 9;

	public static final int MSG_READING_EXCEPTION = 10;
	public static final int MSG_READING_SUCCESS = 11;
}
