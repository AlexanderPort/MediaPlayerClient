package com.example.freeplayandroidclient;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Base extends AppCompatActivity implements
        View.OnClickListener,
        MediaPlayer.OnPreparedListener,
        SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener {
    protected ImageView next;
    protected SeekBar seekbar;
    protected ImageView control;
    protected LinearLayout track;
    protected LinearLayout mainLayout;
    protected LinearLayout mediaLayout;
    protected LinearLayout searchLayout;
    protected LinearLayout downloadsLayout;
    protected static String currentTrackName = "";
    protected static String currentArtistName = "";
    private TextView TrackArtist;
    protected static Thread thread;
    protected static boolean playing = false;
    protected static Handler handler = new Handler();
    protected static MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        next = (ImageView) findViewById(R.id.next);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        track = (LinearLayout) findViewById(R.id.track);
        control = (ImageView) findViewById(R.id.control);
        TrackArtist = (TextView) findViewById(R.id.TrackArtist);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mediaLayout = (LinearLayout) findViewById(R.id.mediaLayout);
        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        downloadsLayout = (LinearLayout) findViewById(R.id.downloadsLayout);

        next.setOnClickListener(this);
        control.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        mediaLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        downloadsLayout.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(this);

        track.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mainLayout) {
            Intent intent = new Intent(Base.this, MainActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.searchLayout) {
            Intent intent = new Intent(Base.this, SearchActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.mediaLayout) {

        } else if (view.getId() == R.id.downloadsLayout) {
            Intent intent = new Intent(Base.this, DownloadsActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.control) {
            playing = !playing;
            if (playing) {
                ((ImageView)view).setImageResource(R.mipmap.pause);
                if (mediaPlayer != null) mediaPlayer.start();
            } else {
                ((ImageView)view).setImageResource(R.mipmap.play);
                if (mediaPlayer != null) mediaPlayer.pause();
            }
        } else if (view.getId() == R.id.next) {
            onCompletion(mediaPlayer);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
    protected void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
            double time = mediaPlayer.getDuration() * ((double)progress / seekBar.getMax());
            mediaPlayer.seekTo((int)time);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            if (thread != null) thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();

        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!hasTrack()) return;
        playing = true;
        track.setVisibility(View.VISIBLE);
        control.setImageResource(R.mipmap.pause);
        seekbar.setMax(mediaPlayer.getDuration());
        if (!mediaPlayer.isPlaying()) mediaPlayer.start();
        setTrackArtist(currentTrackName, currentArtistName);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer == null) return;
                double time = mediaPlayer.getCurrentPosition();
                seekbar.setProgress((int)time);
                if (time == mediaPlayer.getDuration()) return;
                handler.postDelayed(this, 100);
            }
        });
        handler.postDelayed(thread, 100);
    }

    public void setTrackArtist(String track, String artist) {
        currentTrackName = track;
        currentArtistName = artist;
        String text = track + " by " + artist;
        if (text.length() > 40) text = text.substring(0, 40) + "...";
        TrackArtist.setText(text);
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    public boolean hasTrack() {
        return !currentTrackName.equals("") && !currentArtistName.equals("");
    }
}

