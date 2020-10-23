package Requests;

import java.io.Serializable;

public class BrowseRequest extends Request implements Serializable {

    public BrowseRequest() {
        this.reqType = "SONG_BROWSE";
    }

    @Override
    public String getReqType() {
        return reqType;
    }
}
