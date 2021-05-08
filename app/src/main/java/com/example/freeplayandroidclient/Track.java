package com.example.freeplayandroidclient;


public class Track {
    protected final String trackId;
    protected final String trackName;
    protected final String albumId;
    protected final String albumName;
    protected final String artistId;
    protected final String artistName;
    public Track(String trackId, String trackName,
                 String albumId, String albumName,
                 String artistId, String artistName) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
    }
    public Track(Track track) {
        this.trackId = track.getTrackId();
        this.trackName = track.getTrackName();
        this.albumId = track.getAlbumId();
        this.albumName = track.getAlbumName();
        this.artistId = track.getArtistId();
        this.artistName = track.getArtistName();
    }
    public String getTrackId() {
        return trackId;
    }
    public String getTrackName() {
        return trackName;
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
    public String getArtistName() {
        return artistName;
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackName='" + trackName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}

