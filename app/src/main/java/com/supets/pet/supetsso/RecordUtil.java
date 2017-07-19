package com.supets.pet.supetsso;

import android.util.Log;

public class RecordUtil {

	public static int VoiceAm(int readSize, short[] mBuffer) {
		long v = 0;
		for (int i = 0; i < mBuffer.length; i++) {
			v += mBuffer[i] * mBuffer[i];
		}
		double mean = v / (double) readSize;
		double volume = 10 * Math.log10(mean);
		Log.v("RECORD", volume + "");
		if (volume <= 45) {
			return 2000;
		} else if (volume < 50) {
			return 4000;
		} else if (volume < 60) {
			return 6000;
		} else if (volume < 70) {
			return 8000;
		} else {
			return 10000;
		}
	}

	
}