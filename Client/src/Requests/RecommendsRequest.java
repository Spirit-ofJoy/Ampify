package Requests;

import constants.Constant;

import java.io.Serializable;
import java.util.TreeSet;

//Requests to give personalized recommendations
public class RecommendsRequest extends Request implements Serializable {

    private String userID;
    private TreeSet likedSongs;

    public RecommendsRequest(String uid, TreeSet like) {
        this.reqType = String.valueOf(Constant.PERSONAL_RECOMMENDS);
        this.userID = uid;
        this.likedSongs = like;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getUserID() {
        return userID;
    }

    public TreeSet getLikedSongs() {
        return likedSongs;
    }
}
