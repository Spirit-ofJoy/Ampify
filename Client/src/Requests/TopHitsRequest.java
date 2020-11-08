package Requests;

import constants.Constant;

import java.io.Serializable;

//Requests for the current Top Hits (in terms of Views)
public class TopHitsRequest extends Request implements Serializable {

    public TopHitsRequest() {
        this.reqType = (Constant.TOP_HITS_LIST);
    }

    @Override
    public Constant getReqType() {
        return this.reqType;
    }


}
