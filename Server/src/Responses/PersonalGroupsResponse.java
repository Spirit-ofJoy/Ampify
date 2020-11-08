package Responses;

import constants.Constant;

import java.util.ArrayList;

public class PersonalGroupsResponse extends Response{

    public ArrayList<String> grpNamesList = null;;
    public ArrayList<String> grpIdList = null;

    public PersonalGroupsResponse( ArrayList<String> gnames,  ArrayList<String> gids){
        this.grpIdList = gids;
        this.grpNamesList = gnames;
        this.respType = (Constant.PERSONAL_GROUPS_SET);
    }

    @Override
    public Constant getRespType() {
        return respType;
    }
}
