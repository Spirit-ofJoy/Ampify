package Requests;

import constants.Constant;

import java.io.Serializable;

public class BrowseRequest extends Request implements Serializable {

    public BrowseRequest() {
        this.reqType = String.valueOf(Constant.SONG_BROWSE);
    }

    @Override
    public String getReqType() {
        return reqType;
    }
}
