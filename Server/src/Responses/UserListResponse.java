package Responses;

import constants.Constant;

import java.util.ArrayList;

public class UserListResponse extends Response{

    public ArrayList<String> usernamesList;
    public ArrayList<String> useridList = new ArrayList<String>();

    public UserListResponse( ArrayList<String> unames,  ArrayList<String> uids){
        this.useridList = uids;
        this.usernamesList = unames;
        this.respType = String.valueOf(Constant.USERS_LIST);
    }

    @Override
    public String getRespType() {
        return respType;
    }
}
