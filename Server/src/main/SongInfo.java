package main;

import java.io.Serializable;

//Object to store all relevant info for song display
public class SongInfo implements Serializable {

    private String songID;
    private String songName;
    private String genre;
    private String language;
    private String artistName;
    private String albumName;
    private String uploadTime;

    public SongInfo(String a, String b, String c, String d, String e, String f,String g) {
        this.songID = a;
        this.songName = b;
        this.genre = c;
        this.language = d;
        this.artistName = e;
        this.albumName = f;
        this.uploadTime = g;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public String getSongID() {
        return songID;
    }

    public String getSongName() {
        return songName;
    }

    public String getUploadTime() {
        return uploadTime;
    }
}
