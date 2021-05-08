package com.example.freeplayandroidclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackRecyclerView extends RecyclerView implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    private API api;
    private TrackAdapter trackAdapter;
    private List<TrackAdapter.Track> tracks;
    private void init() {
        tracks = new ArrayList<>();
        api = new API(getContext());
        setAdapter(new TrackAdapter());
        setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public TrackRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }
    public TrackRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public TrackRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void setAdapter(TrackAdapter trackAdapter) {
        super.setAdapter(trackAdapter);
        this.trackAdapter = trackAdapter;
    }
    public void addTrack(String trackId, String trackName,
                          String albumId, String albumName,
                          String artistId, String artistName) {
        TrackAdapter.Track.OnClickListener onClickListener =
                new TrackAdapter.Track.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = getContext();
                        if (view.getId() == R.id.play) {
                            if (context instanceof Base) {
                                ((Base) context).setTrackArtist(trackName, artistName);
                            }
                            MediaPlayer mediaPlayer = Base.getMediaPlayer();
                            if (mediaPlayer == null) return; mediaPlayer.reset();
                            try {
                                File file = new File(String.format("%s/tracks/%s.mp3",
                                        context.getFilesDir(), trackId));
                                if (file.exists()) {
                                    mediaPlayer.setDataSource(file.getPath());
                                } else {
                                    mediaPlayer.setDataSource(API.getTrackDataURL(trackId));
                                }
                                mediaPlayer.prepare();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        } else if (view.getId() == R.id.actions) {
                            Intent intent = new Intent(context, ActionsActivity.class);
                            intent.putExtra("trackId", trackId);
                            intent.putExtra("trackName", trackName);
                            intent.putExtra("albumId", albumId);
                            intent.putExtra("albumName", albumName);
                            intent.putExtra("artistId", artistId);
                            intent.putExtra("artistName", artistName);
                            getContext().startActivity(intent);
                        }
                    }
                };
        TrackAdapter.Track track = new TrackAdapter.Track(
                trackId, trackName, albumId, albumName, artistId, artistName);
        track.setOnClickListener(onClickListener);
        File file = new File(String.format("%s/images/%s.jpg",
                getContext().getFilesDir(), trackId));
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    200, 200, false);
            track.setThumbnail(bitmap);
        } else {
            Response.Listener<Bitmap> imageListener = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    response = Bitmap.createScaledBitmap(response,
                            200, 200, false);
                    track.setThumbnail(response);
                }
            };
            api.getThumbnail(trackId, 1000, 1000, imageListener);
        }
        tracks.add(track);
    }
    public void addTrack(Track track) {
        addTrack(track.getTrackId(), track.getTrackName(),
                track.getAlbumId(), track.getAlbumName(),
                track.getArtistId(), track.getArtistName());
    }
    public void addTracks(List<Track> tracks) {
        for (Track track : tracks) addTrack(track);
        trackAdapter.setItems(this.tracks);
        trackAdapter.notifyDataSetChanged();
    }
    public void addTracks(JSONObject response) throws JSONException {
        JSONArray tracks = response.getJSONArray("tracks");
        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = (JSONObject) tracks.get(i);
            String trackId = track.getString("trackId");
            String trackName = track.getString("trackName");
            String artistId = track.getString("artistId");
            String artistName = track.getString("artistName");
            String albumId = track.getString("albumId");
            String albumName = track.getString("albumName");
            addTrack(trackId, trackName,
                    albumId, albumName,
                    artistId, artistName);
        }
        trackAdapter.setItems(this.tracks);
        trackAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        if (tracks.size() > 0) {
            try {
                TrackAdapter.Track track = trackAdapter.getNextTrack();
                String trackId = track.getTrackId();
                Context context = getContext();
                if (context instanceof Base) {
                    ((Base) context).setTrackArtist(
                            track.getTrackName(), track.getArtistName());
                    trackAdapter.increaseNextTrackIndex();
                }
                String filename = String.format("%s/tracks/%s.mp3",
                        getContext().getFilesDir(), trackId);
                if (new File(filename).exists()) {
                    mp.setDataSource(filename);
                } else {
                    mp.setDataSource(API.getTrackDataURL(trackId));
                }
                mp.prepare();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public void clear() {
        tracks.clear();
        trackAdapter.clearItems();
    }
}

