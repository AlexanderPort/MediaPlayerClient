package com.example.freeplayandroidclient;

import androidx.annotation.NonNull;

public class Album {
    private final String albumId;
    private final String albumName;
    private final String artistId;

    public Album(String albumId, String albumName, String artistId) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
    }
    public String getAlbumId() {
        return albumId;
    }
    public String getAlbumName() {
        return albumName;
    }
    public String getArtistId() {
        return artistId;
    }
    @NonNull
    @Override
    public String toString() {
        return "Album{" +
                "albumName='" + albumName + '\'' +
                '}';
    }
}
