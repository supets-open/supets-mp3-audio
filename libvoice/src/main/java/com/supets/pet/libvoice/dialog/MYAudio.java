package com.supets.pet.libvoice.dialog;



import java.io.Serializable;

public class MYAudio  implements Serializable {
    public String audio_url;//语音路径
    public int length;//语音时长(单位s)

    public MYAudio(String path, int length) {
        this.audio_url=path;
        this.length=length;
    }

    public int getSoundsLength() {
        return length;
    }

    public String getSounds() {
        return audio_url;
    }

    public boolean isNetUrl() {
        return audio_url!=null&&audio_url.toLowerCase().startsWith("http");
    }
}
