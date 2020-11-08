package Requests;

import constants.Constant;

public class LoadGroupChatRequest extends Request{
    private String groupId;

    public LoadGroupChatRequest(String gid) {
        reqType = (Constant.LOAD_GROUP_CHATS);
        this.groupId = gid;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getGroupId() {
        return groupId;
    }
}
