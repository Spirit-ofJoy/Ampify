package utility;


import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    private String playlistID;
    private String name;
    private String owner;
    private boolean visibility;
    private ArrayList<String> songID = new ArrayList<String>();
    public ArrayList<String> songNames;

    public Playlist(String pID, String pName, String pOwner, String sIdsIncluded, int visible) {
        this.playlistID = pID;
        this.name = pName;
        this.owner = pOwner;

        if(visible==1){ visibility = true; }
        else { visibility = false; }

        for (String temp: sIdsIncluded.split("-")){
            this.songID.add(temp);
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getSongID() {
        return songID;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public String getOwner() {
        return owner;
    }

    public String getPlaylistID() {
        return playlistID;
    }
}

