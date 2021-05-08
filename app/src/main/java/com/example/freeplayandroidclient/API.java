package com.example.freeplayandroidclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Size;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class API {
    private final RequestQueue requestQueue;
    //public static final String API = "http://127.0.0.1:8000";
    private static final String API = "http://10.0.2.2:8000";
    public API(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
    }
    public void getTrackData(String id,
            Response.Listener<byte[]> listener,
            Response.ErrorListener errorListener) {
        ByteArrayRequest byteArrayRequest = new ByteArrayRequest(
                Request.Method.GET, getTrackDataURL(id),
                listener, errorListener);
        requestQueue.add(byteArrayRequest);
    }
    public void getRandomTracks(int k, Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                getRandomTracksURL(k), null, listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
    public void getThumbnail(String id, int maxWidth, int maxHeight,
                             Response.Listener<Bitmap> listener) {
        ImageRequest imageRequest = new ImageRequest(getThumbnailURL(id),
                listener, maxWidth, maxHeight, null, null);
        requestQueue.add(imageRequest);
    }
    public static String getThumbnailURL(String id) {
        return String.format("%s/api/tracks/thumbnail/%s", API, id);
    }
    public static String getTrackDataURL(String id) {
        return String.format("%s/api/tracks/data/%s", API, id);
    }
    public static String getRandomTracksURL(int k) {
        return String.format("%s/api/tracks/random/%s", API, k);
    }
    public void searchTrackByName(String key,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        String url = String.format("%s/api/search/tracks/%s", API, key);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url, null, listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
    public void searchAlbumByName(String key,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
        String url = String.format("%s/api/search/albums/%s", API, key);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url, null, listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }
    public void searchArtistByName(String key,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        String url = String.format("%s/api/search/artists/%s", API, key);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url, null, listener, errorListener);
        requestQueue.add(jsonObjectRequest);
    }

}
