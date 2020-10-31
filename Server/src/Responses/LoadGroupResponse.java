package Responses;

import constants.Constant;
import utility.Group;

public class LoadGroupResponse extends Response{
    private Group groupFound;

    public LoadGroupResponse(Group grp) {
        this.respType = String.valueOf(Constant.LOAD_GROUP);
        this.groupFound = grp;
    }

    @Override
    public String getRespType() {
        return respType;
    }

    public Group getGroupFound() {
        return groupFound;
    }
}
