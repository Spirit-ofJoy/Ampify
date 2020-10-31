package Requests;

import constants.Constant;

public class LoadGroupRequest extends Request{
    private String grpId;

    public LoadGroupRequest(String id) {
        this.reqType = String.valueOf(Constant.LOAD_GROUP);
        this.grpId = id;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getGrpId() {
        return grpId;
    }
}
