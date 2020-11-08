package Responses;

import constants.Constant;

import java.util.ArrayList;

public class UserListResponse extends Response{

    public ArrayList<String> usernamesList = null;;
    public ArrayList<String> useridList = null;

    public UserListResponse( ArrayList<String> unames,  ArrayList<String> uids){
        this.useridList = uids;
        this.usernamesList = unames;
        this.respType = (Constant.USERS_LIST);
    }

    @Override
    public Constant getRespType() {
        return respType;
    }
}
