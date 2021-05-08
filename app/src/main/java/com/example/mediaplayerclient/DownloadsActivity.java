package com.example.mediaplayerclient;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadsActivity extends Base {
    private DatabaseHelper databaseHelper;
    private TrackRecyclerView trackRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getBaseContext());
        trackRecyclerView = findViewById(R.id.tracksRecyclerView);
        trackRecyclerView.addTracks(databaseHelper.selectAllTracks());
        onPrepared(mediaPlayer);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        super.onPrepared(mp);
        trackRecyclerView.onPrepared(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        super.onCompletion(mp);
        trackRecyclerView.onCompletion(mp);
    }
}