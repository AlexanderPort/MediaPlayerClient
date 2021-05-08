package com.example.freeplayandroidclient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseHelper {
    private final Context context;
    private static final Table ARTISTS = new Table(
            "Artists",
            new Column("id", "varchar(36)").primaryKey(true).notNull(true),
            new Column("name", "varchar(255)").primaryKey(false).notNull(true)
            );
    private static final Table ALBUMS = new Table(
            "Albums",
            new Column("id", "varchar(36)").primaryKey(true).notNull(true),
            new Column("name", "varchar(255)").primaryKey(false).notNull(true),
            new Column("artist_id", "varchar(36)").foreignKey("Artists").notNull(false)
    );
    private static final Table TRACKS = new Table(
            "Tracks",
            new Column("id", "varchar(36)").primaryKey(true).notNull(true),
            new Column("name", "varchar(255)").primaryKey(false).notNull(true),
            new Column("album_id", "varchar(36)").foreignKey("Albums").notNull(false),
            new Column("artist_id", "varchar(36)").foreignKey("Artists").notNull(false)
    );
    private static final String DATABASE_NAME = "database.db";

    private static class Column {
        private final String name;
        private final String type;
        private boolean notNull;
        private String foreignKey;
        private boolean primaryKey;
        private Column(String name, String type) {
            this.name = name;
            this.type = type;
            this.notNull = false;
            this.foreignKey = null;
            this.primaryKey = false;
        }
        public Column notNull(boolean notNull) {
            this.notNull = notNull; return this;
        }
        public Column foreignKey(String foreignKey) {
            this.foreignKey = foreignKey; return this;
        }
        public Column primaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey; return this;
        }
        public boolean isForeignKey() {
            return foreignKey != null;
        }
        @NonNull
        @Override
        public String toString() {
            String description = "";
            description += name;
            description += " " + type;
            if (notNull) description += " NOT NULL";
            if (primaryKey) description += " PRIMARY KEY";
            return description;
        }
    }
    private static class Table {
        private final String name;
        private final List<Column> columns;
        public Table(String name, List<Column> columns) {
            this.name = name;
            this.columns = columns;
        }
        public Table(String name, Column... columns) {
            this.name = name;
            this.columns = Arrays.asList(columns);
        }
        public String createQuery() {
            StringBuilder stringBuilder1 = new StringBuilder("(");
            StringBuilder stringBuilder2 = new StringBuilder();
            for (Column column : columns) {
                stringBuilder1.append(column).append(", ");
                if (column.isForeignKey()) {
                    stringBuilder2.append(String.format(
                            "FOREIGN KEY (%s) REFERENCES %s(id)",
                            column.name, column.foreignKey
                    )).append(", ");
                }
            }
            stringBuilder1.append(stringBuilder2);
            String description = stringBuilder1.toString()
                    .substring(0, stringBuilder1.length() - 2);
            return String.format("CREATE TABLE IF NOT EXISTS %s %s);", name, description);
        }
    }

    public DatabaseHelper(Context context) {
        this.context = context;
        onCreate();
    }

    public void onCreate() {
        SQLiteDatabase db = getDatabase();
        //db.execSQL("DROP TABLE IF EXISTS Tracks");
        //db.execSQL("DROP TABLE IF EXISTS Albums");
        //db.execSQL("DROP TABLE IF EXISTS Artists");
        db.execSQL(ARTISTS.createQuery());
        db.execSQL(ALBUMS.createQuery());
        db.execSQL(TRACKS.createQuery());
        db.close();
    }
    public void insertArtist(String artistId, String artistName) {
        try {
            SQLiteDatabase db = getDatabase();
            String query = String.format(
                    "INSERT INTO Artists VALUES ('%s', '%s');",
                    artistId, artistName);
            db.execSQL(query);
            db.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    public void insertAlbum(String albumId, String albumName, String artistId) {
        try {
            SQLiteDatabase db = getDatabase();
            String query = String.format(
                    "INSERT INTO Albums VALUES ('%s', '%s', '%s');",
                    albumId, albumName, artistId);
            db.execSQL(query);
            db.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    public void insertTrack(String trackId, String trackName, String albumId, String artistId) {
        try {
            SQLiteDatabase db = getDatabase();
            if (albumId == null) albumId = "NULL";
            if (artistId == null) artistId = "NULL";
            String query = String.format(
                    "INSERT INTO Tracks VALUES ('%s', '%s', '%s', '%s');",
                    trackId, trackName, albumId, artistId);
            db.execSQL(query);
            db.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    /*
    public void onUpgrade(int oldVersion, int newVersion) {
        SQLiteDatabase db = getDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }

     */
    public List<Track> selectAllTracks() {
        SQLiteDatabase db = getDatabase();
        String query = "SELECT Tracks.id, Tracks.name, Albums.id, Albums.name, Artists.id, Artists.name \n" +
                "FROM Tracks  \n" +
                "LEFT JOIN Artists  \n" +
                "ON Tracks.artist_id=Artists.id  \n" +
                "LEFT JOIN Albums  \n" +
                "ON Tracks.album_id = Albums.id;";
        ArrayList<Track> tracks = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String trackId = cursor.getString(0);
            String trackName = cursor.getString(1);
            String albumId = cursor.getString(2);
            String albumName = cursor.getString(3);
            String artistId = cursor.getString(4);
            String artistName = cursor.getString(5);
            tracks.add(new Track(trackId, trackName, albumId, albumName, artistId, artistName));
        }

        cursor.close();
        db.close();
        return tracks;
    }
    public SQLiteDatabase getDatabase() {
        return context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }
}

