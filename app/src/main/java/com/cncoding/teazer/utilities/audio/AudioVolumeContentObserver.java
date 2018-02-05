package com.cncoding.teazer.utilities.audio;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

/**
 *
 * Created by Prem$ on 1/26/2018.
 */

public class AudioVolumeContentObserver extends ContentObserver {

    @NonNull private final OnAudioVolumeChangedListener listener;
    @NonNull private final AudioManager audioManager;
    private final int audioStreamType;
    private int lastVolume;
    private int currentVolume;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    AudioVolumeContentObserver(@NonNull Handler handler, @NonNull AudioManager audioManager,
                               int audioStreamType, @NonNull OnAudioVolumeChangedListener listener) {
        super(handler);
        this.audioManager = audioManager;
        this.audioStreamType = audioStreamType;
        this.listener = listener;
        lastVolume = audioManager.getStreamVolume(audioStreamType);
        listener.initialVolume(lastVolume);
    }

    /**
     * Depending on the handler this method may be executed on the UI thread
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        try {
            int maxVolume = audioManager.getStreamMaxVolume(audioStreamType);
            currentVolume = audioManager.getStreamVolume(audioStreamType);
            if (currentVolume != lastVolume) {
                lastVolume = currentVolume;
                listener.onVolumeChanged(currentVolume, maxVolume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public int getCurrentVolume() {
//        return currentVolume;
//    }

    public interface OnAudioVolumeChangedListener {
        void initialVolume(int currentVolume);
        void onVolumeChanged(int currentVolume, int maxVolume);
    }
}
