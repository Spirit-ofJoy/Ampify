package Requests;

import constants.Constant;

public class SongPlayedRequest extends Request{
    private String songId;
    private String historyString;
    private String userId;

    public SongPlayedRequest(String uid, String history, String song) {
        reqType = String.valueOf(Constant.SONG_PLAYED);
        this.songId = song;
        this.historyString = history;
        this.userId = uid;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getHistoryString() {
        return historyString;
    }

    public String getSongId() {
        return songId;
    }

    public String getUserId() {
        return userId;
    }
}
