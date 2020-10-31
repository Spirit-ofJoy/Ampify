package Requests;

import constants.Constant;

public class UserListRequest extends Request{

    public UserListRequest() {
        this.reqType = String.valueOf(Constant.USERS_LIST);
    }

    @Override
    public String getReqType() {
        return reqType;
    }
}
