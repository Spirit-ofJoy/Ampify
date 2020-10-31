package Requests;

import constants.Constant;

public class ShareablePlaylistsRequest extends Request{
    private String userID;

    public ShareablePlaylistsRequest(String uid) {
        reqType = String.valueOf(Constant.SHARE_PLAYLISTS_SET);
        this.userID = uid;
    }
    @Override
    public String getReqType() {
        return reqType;
    }

    public String getUserID() {
        return userID;
    }
}
