package Responses;

import constants.Constant;
import utility.SongInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class SongSearchResponse extends Response implements Serializable {
    public ArrayList<SongInfo> searchedSongs;

    public SongSearchResponse(ArrayList<SongInfo> searchList) {
        this.respType = (Constant.SONG_SEARCH);
        this.searchedSongs = searchList;
    }

    @Override
    public Constant getRespType() {
        return respType;
    }
}
