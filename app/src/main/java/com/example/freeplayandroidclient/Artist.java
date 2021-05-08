package com.example.freeplayandroidclient;

import androidx.annotation.NonNull;

public class Artist {
    private final String artistId;
    private final String artistName;

    public Artist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }
    public String getArtistId() {
        return artistId;
    }
    public String getArtistName() {
        return artistName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Artist{" +
                "artistName='" + artistName + '\'' +
                '}';
    }
}
