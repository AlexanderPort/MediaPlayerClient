package com.example.freeplayandroidclient;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Base {
    private API api;
    private TrackRecyclerView trackRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new API(getBaseContext());
        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        trackRecyclerView.addTracks(response);
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }
        };
        api.getRandomTracks(10, jsonObjectListener, errorListener);
        trackRecyclerView = findViewById(R.id.trackRecyclerView);
        onPrepared(mediaPlayer);
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