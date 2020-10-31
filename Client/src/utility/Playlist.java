package utility;


import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    private String playlistID;
    private String name;
    private String owner;
    private boolean visibility;
    public String currViewer;
    private ArrayList<String> songID = new ArrayList<String>();
    public ArrayList<String> songNames = new ArrayList<String>();

    //Used to create a temporary playlist that can later be created permanently
    public Playlist(String pOwner) {
        this.owner = pOwner;
    }

    //Used to pass information for an existing playlist
    public Playlist(String pID, String pName, String pOwner, String sIdsIncluded, int visible, String viewedBy) {
        this.playlistID = pID;
        this.name = pName;
        this.owner = pOwner;
        this.currViewer = viewedBy;

        if(visible==1){ visibility = true; }
        else { visibility = false; }

        if(sIdsIncluded!=null) {
            for (String temp : sIdsIncluded.split("-")) {
                this.songID.add(temp);
            }
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

