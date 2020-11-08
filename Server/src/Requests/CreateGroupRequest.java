package Requests;

import constants.Constant;

public class CreateGroupRequest extends Request{
    private String usersList;
    private String grpName;

    public CreateGroupRequest(String users, String name) {
        reqType = (Constant.CREATE_GROUP);
        this.usersList = users;
        this.grpName = name;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getGrpName() {
        return grpName;
    }

    public String getUsersList() {
        return usersList;
    }
}
