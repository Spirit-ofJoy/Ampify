package Requests;

import constants.Constant;

public class UserListRequest extends Request{
    private String userId;

    public UserListRequest(String uid) {
        this.reqType = String.valueOf(Constant.USERS_LIST);
        this.userId = uid;
    }

    @Override
    public String getReqType() {
        return reqType;
    }
}
