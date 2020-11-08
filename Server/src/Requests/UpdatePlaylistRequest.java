package Requests;

import constants.Constant;

public class UpdatePlaylistRequest extends Request{
    private String songsList;
    private String userViewer;
    private String playlistID;

    public UpdatePlaylistRequest(String playlistContents, String playlistViewer, String playlistkey) {
        reqType = (Constant.UPDATE_PLAYLIST);
        this.songsList = playlistContents;
        this.userViewer = playlistViewer;
        this.playlistID = playlistkey;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getPlaylistID() {
        return playlistID;
    }

    public String getSongsList() {
        return songsList;
    }

    public String getUserViewer() {
        return userViewer;
    }
}
