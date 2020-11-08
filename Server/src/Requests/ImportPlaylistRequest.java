package Requests;

import constants.Constant;

public class ImportPlaylistRequest extends Request{
    private String playlistID;
    private String userID;

    public ImportPlaylistRequest(String uid, String pid) {
        reqType = (Constant.IMPORT_PLAYLIST);
        this.userID = uid;
        this.playlistID = pid;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getUserID() {
        return userID;
    }

    public String getPlaylistID() {
        return playlistID;
    }
}
