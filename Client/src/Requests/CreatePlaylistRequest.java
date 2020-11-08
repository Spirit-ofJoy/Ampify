package Requests;

import constants.Constant;

import java.io.Serializable;

public class CreatePlaylistRequest extends Request implements Serializable {
    private String songsList;
    private String ownerID;
    private String playlistName;
    private int visibility;

    public CreatePlaylistRequest(String songs, String uid, String pname, int visible) {
        reqType = (Constant.CREATE_PLAYLIST);
        this.songsList = songs;
        this.ownerID = uid;
        this.playlistName = pname;
        this.visibility = visible;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getSongsList() {
        return songsList;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getPlaylistName() {
        return playlistName;
    }
}
