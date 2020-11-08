package Requests;

import constants.Constant;

public class SendChatRequest extends Request{
    private String chatMsg;
    private String groupId;

    public SendChatRequest(String chat, String group) {
        reqType = (Constant.CHAT_SEND);
        this.chatMsg = chat;
        this.groupId = group;
    }

    @Override
    public Constant getReqType() {
        return reqType;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public String getGroupId() {
        return groupId;
    }
}
