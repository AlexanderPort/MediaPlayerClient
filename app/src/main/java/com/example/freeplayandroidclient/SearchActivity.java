package com.example.freeplayandroidclient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends Base {
    enum SEARCH_MODE {
        TRACKS,
        ALBUMS,
        ARTISTS
    }
    private API api;
    private Chip tracksChip;
    private Chip albumsChip;
    private Chip artistsChip;
    private EditText searchView;
    private SEARCH_MODE search_mode;
    private TrackRecyclerView trackRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        api = new API(getBaseContext());
        search_mode = SEARCH_MODE.TRACKS;

        tracksChip = (Chip) findViewById(R.id.trackChip);
        albumsChip = (Chip) findViewById(R.id.albumsChip);
        artistsChip = (Chip) findViewById(R.id.artistsChip);
        searchView = (EditText) findViewById(R.id.search);
        trackRecyclerView = findViewById(R.id.trackRecyclerView);

        tracksChip.setAlpha(0.9f);

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        trackRecyclerView.clear();
                        trackRecyclerView.addTracks(response);
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) trackRecyclerView.clear();
                if (search_mode == SEARCH_MODE.TRACKS) {
                    api.searchTrackByName(s.toString(), jsonObjectListener, errorListener);
                } else if (search_mode == SEARCH_MODE.ALBUMS) {
                    api.searchAlbumByName(s.toString(), jsonObjectListener, errorListener);
                } else if (search_mode == SEARCH_MODE.ARTISTS) {
                    api.searchArtistByName(s.toString(), jsonObjectListener, errorListener);
                }
            }
        });
        tracksChip.setOnClickListener(this);
        albumsChip.setOnClickListener(this);
        artistsChip.setOnClickListener(this);

        onPrepared(mediaPlayer);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view.getId() == tracksChip.getId()) {
            tracksChip.setAlpha(0.9f);
            albumsChip.setAlpha(0.5f);
            artistsChip.setAlpha(0.5f);
            search_mode = SEARCH_MODE.TRACKS;
        } else if (view.getId() == albumsChip.getId()) {
            tracksChip.setAlpha(0.5f);
            albumsChip.setAlpha(0.9f);
            artistsChip.setAlpha(0.5f);
            search_mode = SEARCH_MODE.ALBUMS;
        } else if (view.getId() == artistsChip.getId()) {
            tracksChip.setAlpha(0.5f);
            albumsChip.setAlpha(0.5f);
            artistsChip.setAlpha(0.9f);
            search_mode = SEARCH_MODE.ARTISTS;
        }
    }
}