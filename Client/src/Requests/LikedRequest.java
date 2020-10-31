package Requests;

import constants.Constant;

public class LikedRequest extends Request{
    private String user;
    private String likedSongs;

    public LikedRequest(String userID, String likedChange) {
        reqType = String.valueOf(Constant.SONG_LIKED);
        this.user = userID;
        this.likedSongs = likedChange;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getLikedSongs() {
        return likedSongs;
    }

    public String getUser() {
        return user;
    }
}
