package Responses;

import constants.Constant;

public class LoadGroupChatResponse extends Response{
    private String messages;

    public LoadGroupChatResponse(String chat) {
        respType = String.valueOf(Constant.LOAD_GROUP_CHATS);
        this.messages = chat;
    }

    @Override
    public String getRespType() {
        return respType;
    }

    public String getMessages() {
        return messages;
    }
}
