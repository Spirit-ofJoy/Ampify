package utility;

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

    public SongInfo(String sid, String sname, String gen, String lang, String artist, String album, String upload) {
        this.songID = sid;
        this.songName = sname;
        this.genre = gen;
        this.language = lang;
        this.artistName = artist;
        this.albumName = album;
        this.uploadTime = upload;
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

    public String getSongDescription() {
        return (this.songName +"\nby " +this.artistName );
    }
}
