package Requests;

import constants.Constant;

public class LoadGroupChatRequest extends Request{
    private String groupId;

    public LoadGroupChatRequest(String gid) {
        reqType = String.valueOf(Constant.LOAD_GROUP_CHATS);
        this.groupId = gid;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getGroupId() {
        return groupId;
    }
}
