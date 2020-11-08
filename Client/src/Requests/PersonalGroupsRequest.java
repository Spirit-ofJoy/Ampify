package Requests;

import constants.Constant;

public class PersonalGroupsRequest extends Request{
    private String userID;

    public PersonalGroupsRequest(String id) {
        this.reqType = (Constant.PERSONAL_GROUPS_SET );
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
