package Requests;

import constants.Constant;

public class SendChatRequest extends Request{
    private String chatMsg;
    private String groupId;

    public SendChatRequest(String chat, String group) {
        reqType = String.valueOf(Constant.CHAT_SEND);
        this.chatMsg = chat;
        this.groupId = group;
    }

    @Override
    public String getReqType() {
        return reqType;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public String getGroupId() {
        return groupId;
    }
}
