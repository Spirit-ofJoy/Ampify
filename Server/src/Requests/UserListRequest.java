package Requests;

import constants.Constant;

public class UserListRequest extends Request{

    public UserListRequest() {
        this.reqType = (Constant.USERS_LIST);
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }
}
