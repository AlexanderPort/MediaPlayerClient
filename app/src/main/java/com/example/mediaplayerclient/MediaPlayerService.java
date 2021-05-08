package com.example.mediaplayerclient;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MediaPlayerService implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private OnPreparedListener onPreparedListener;
    private OnCompletionListener onCompletionListener;

    public MediaPlayerService() {
        releaseMediaPlayer();
        createMediaPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (onCompletionListener == null) return;
        try {
            onCompletionListener.onCompletion(mp);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (onPreparedListener == null) return;
        try {
            onPreparedListener.onPrepared(mp);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }
    interface OnPreparedListener {
        void onPrepared(MediaPlayer mp) throws Exception;
    }
    interface OnCompletionListener {
        void onCompletion(MediaPlayer mp) throws Exception;
    }
    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }
    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }
}
