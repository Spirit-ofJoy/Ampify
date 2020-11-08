package Requests;

import constants.Constant;

import java.io.Serializable;

//Requests for the current New Releases (in terms of Upload Time)
public class NewReleasesRequest extends Request implements Serializable {

    public NewReleasesRequest() {
        this.reqType = (Constant.NEW_RELEASES_LIST);
    }

    @Override
    public Constant getReqType() {
        return this.reqType;
    }


}
