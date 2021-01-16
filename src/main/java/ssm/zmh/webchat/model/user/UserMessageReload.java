package ssm.zmh.webchat.model.user;

import ssm.zmh.webchat.model.message.Message;
import ssm.zmh.webchat.model.message.MessageItem;

import java.util.List;

/**
 * @author Administrator
 */
public class UserMessageReload {
    private List<Message> groupMessages;

    private List<Message> privateMessages;

    private List<MessageItem> messageList;


    public List<Message> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(List<Message> groupMessages) {
        this.groupMessages = groupMessages;
    }

    public List<Message> getPrivateMessages() {
        return privateMessages;
    }

    public void setPrivateMessages(List<Message> privateMessages) {
        this.privateMessages = privateMessages;
    }

    public List<MessageItem> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageItem> messageList) {
        this.messageList = messageList;
    }
}
