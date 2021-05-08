package com.example.freeplayandroidclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActionsActivity extends Base {
    private API api;
    private String trackId;
    private String trackName;
    private String albumId;
    private String albumName;
    private String artistId;
    private String artistName;
    private TextView shareView;
    private TextView downloadView;
    private TextView trackNameView;
    private TextView artistNameView;
    private TextView addInAlbumView;
    private ImageView thumbnailView;
    private TextView goToArtistView;
    private TextView addInFavouritesView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        api = new API(getBaseContext());
        shareView = (TextView) findViewById(R.id.share);
        downloadView = (TextView) findViewById(R.id.download);
        trackNameView = (TextView) findViewById(R.id.trackName);
        thumbnailView = (ImageView) findViewById(R.id.thumbnail);
        artistNameView = (TextView) findViewById(R.id.artistName);
        addInAlbumView = (TextView) findViewById(R.id.addInAlbum);
        goToArtistView = (TextView) findViewById(R.id.goToArtist);
        addInFavouritesView = (TextView) findViewById(R.id.addInFavourites);

        downloadView.setOnClickListener(this);

        Intent intent = getIntent();
        trackId = intent.getStringExtra("trackId");
        trackName = intent.getStringExtra("trackName");
        albumId = intent.getStringExtra("albumId");
        albumName = intent.getStringExtra("albumName");
        artistId = intent.getStringExtra("artistId");
        artistName = intent.getStringExtra("artistName");

        trackNameView.setText(trackName);
        artistNameView.setText(artistName);

        databaseHelper = new DatabaseHelper(getBaseContext());

        File file = new File(String.format("%s/images/%s.jpg", getFilesDir(), trackId));
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            thumbnailView.setImageBitmap(bitmap);
        } else {
            Response.Listener<Bitmap> imageListener = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    thumbnailView.setImageBitmap(response);
                }
            };
            api.getThumbnail(trackId, 600, 600, imageListener);
        }
        onPrepared(mediaPlayer);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.download) {
            Response.Listener<byte[]> listener = new Response.Listener<byte[]>() {
                @Override
                public void onResponse(byte[] response) {
                    saveTrack(response, trackId);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            };
            api.getTrackData(trackId, listener, errorListener);
            saveThumbnail(trackId, 600, 600);
        }
    }
    public void saveTrack(byte[] bytes, String filename) {
        databaseHelper.insertArtist(artistId, artistName);
        databaseHelper.insertAlbum(albumId, albumName, artistId);
        databaseHelper.insertTrack(trackId, trackName, albumId, artistId);
        File directory = new File(getFilesDir() + "/tracks/");
        if (!directory.exists()) { boolean status = directory.mkdir(); }
        filename = getFilesDir() + "/tracks/" + filename + ".mp3";
        try {
            File file = new File(filename);
            if (!file.exists()) { boolean status = file.createNewFile(); }
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytes); fos.flush(); fos.close();
            Toast.makeText(this, "Track successfully saved", Toast.LENGTH_LONG).show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    public void saveThumbnail(String trackId, int maxWidth, int maxHeight) {
        Response.Listener<Bitmap> imageListener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                File directory = new File(getFilesDir() + "/images/");
                if (!directory.exists()) { boolean status = directory.mkdir(); }
                String filename = String.format("%s/images/%s.jpg", getFilesDir(), trackId);
                try (FileOutputStream out = new FileOutputStream(filename)) {
                    response.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        };
        api.getThumbnail(trackId, maxWidth, maxHeight, imageListener);
    }
}