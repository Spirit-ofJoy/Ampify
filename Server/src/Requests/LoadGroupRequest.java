package Requests;

import constants.Constant;

public class LoadGroupRequest extends Request{
    private String grpId;

    public LoadGroupRequest(String id) {
        this.reqType = (Constant.LOAD_GROUP);
        this.grpId = id;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getGrpId() {
        return grpId;
    }
}
