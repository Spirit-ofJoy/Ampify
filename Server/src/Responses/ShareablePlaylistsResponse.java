package Responses;


import constants.Constant;
import utility.Playlist;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareablePlaylistsResponse extends Response implements Serializable {
    public ArrayList<Playlist> sharePlaylists;

    public ShareablePlaylistsResponse(ArrayList<Playlist> playlistsResult) {
        this.respType = (Constant.SHARE_PLAYLISTS_SET);
        this.sharePlaylists = playlistsResult;
    }

    @Override
    public Constant getRespType() {
        return respType;
    }
}
