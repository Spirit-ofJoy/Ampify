package Requests;

import constants.Constant;

import java.io.Serializable;

public class BrowseRequest extends Request implements Serializable {

    public BrowseRequest() {
        this.reqType = (Constant.SONG_BROWSE);
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }
}
