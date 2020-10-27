package Responses;


import constants.Constant;
import utility.Playlist;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonalPlaylistsResponse extends Response implements Serializable {
    public ArrayList<Playlist> personalPlaylists;

    public PersonalPlaylistsResponse(ArrayList<Playlist> playlistsResult) {
        this.respType = String.valueOf(Constant.PERSONAL_PLAYLISTS_SET);
        this.personalPlaylists = playlistsResult;
    }

    @Override
    public String getRespType() {
        return respType;
    }
}
