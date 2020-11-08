package Responses;

import constants.Constant;

public class LoadGroupChatResponse extends Response{
    private String messages;

    public LoadGroupChatResponse(String chat) {
        respType = (Constant.LOAD_GROUP_CHATS);
        this.messages = chat;
    }

    @Override
    public Constant getRespType() {
        return respType;
    }

    public String getMessages() {
        return messages;
    }
}
