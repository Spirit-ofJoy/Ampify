package Requests;

import constants.Constant;

import java.io.Serializable;

public class PersonalPlaylistsRequest extends Request implements Serializable {
    private String userID;

    public PersonalPlaylistsRequest(String id) {
        this.reqType = (Constant.PERSONAL_PLAYLISTS_SET);
        this.userID = id;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getUserID() {
        return userID;
    }
}


