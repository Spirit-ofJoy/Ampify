package Responses;

import constants.Constant;
import utility.Group;

public class LoadGroupResponse extends Response{
    private Group groupFound;

    public LoadGroupResponse(Group grp) {
        this.respType = (Constant.LOAD_GROUP);
        this.groupFound = grp;
    }

    @Override
    public Constant getRespType() {
        return respType;
    }

    public Group getGroupFound() {
        return groupFound;
    }
}
