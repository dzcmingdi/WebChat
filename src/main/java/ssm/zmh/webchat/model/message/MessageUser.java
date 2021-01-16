//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.model.message;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

@Component("MessageUser")
public class MessageUser {
    @MongoId
    private String userUUID;
    private List<MessageItem> messageList;
    private List<String> messagesId;
    private List<String> unAcceptedMessagesId;

    public MessageUser() {
    }

    public List<String> getMessagesId() {
        return this.messagesId;
    }

    public void setMessagesId(List<String> messagesId) {
        this.messagesId = messagesId;
    }

    public List<String> getUnAcceptedMessagesId() {
        return this.unAcceptedMessagesId;
    }

    public void setUnAcceptedMessagesId(List<String> unAcceptedMessagesId) {
        this.unAcceptedMessagesId = unAcceptedMessagesId;
    }

    public List<MessageItem> getMessageList() {
        return this.messageList;
    }

    public void setMessageList(List<MessageItem> messageList) {
        this.messageList = messageList;
    }

    public String getUserUUID() {
        return this.userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
}
