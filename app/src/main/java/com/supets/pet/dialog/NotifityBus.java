package com.supets.pet.dialog;

import android.os.Bundle;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class NotifityBus {

    private static final Set<BaseNotifityEvent> listeners = new CopyOnWriteArraySet<>();
    private static PlayButton mPlayButton;

    public static void addNotifityEventListener(BaseNotifityEvent listener) {
        listeners.add(listener);
    }

    public static void removeNotifityEventListener(BaseNotifityEvent listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(String action, PlayButton playButton) {

        if (mPlayButton != null) {
            mPlayButton.stopPlay();
        }
        mPlayButton = playButton;

        for (BaseNotifityEvent listener : listeners) {
            try {
                listener.onReceive(action, null);
            } catch (RuntimeException unexpected) {
            }
        }
        clear();
    }


    public static void clear() {
        listeners.clear();
    }


    public static void stopPlaying(){
        try {
            if (mPlayButton != null) {
                 mPlayButton.stopPlay();
                mPlayButton=null;
            }
        } catch (RuntimeException unexpected) {
        }

    }

    public interface BaseNotifityEvent {
        void onReceive(String action, Bundle savedInstanceState);
    }

}