package ssm.zmh.webchat.model.message;

import java.util.List;

/**
 * @author Administrator
 */
public class UnAckMessage {
    private List<MessageItem> messageList;
    private List<Message> privateMessages;
    private List<Message> groupMessages;

    public List<MessageItem> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageItem> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getPrivateMessages() {
        return privateMessages;
    }

    public void setPrivateMessages(List<Message> privateMessages) {
        this.privateMessages = privateMessages;
    }

    public List<Message> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(List<Message> groupMessages) {
        this.groupMessages = groupMessages;
    }
}
