package com.supets.pet.nativelib;

public class SampleVoice {
	public int sampleRate=16000;
	public int channels=1;
	public int pitchSemiTones=0;
	public float rateChange=0;
	public float tempoChange=0;
	
	public SampleVoice() {
	}
	
	public SampleVoice(int pitchSemiTones,
					   float rateChange, float tempoChange) {
		this.pitchSemiTones=pitchSemiTones;
		this.rateChange=rateChange;
		this.tempoChange=tempoChange;
	}
}
