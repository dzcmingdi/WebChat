//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.mongodb;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ssm.zmh.webchat.factory.SingletonGsonBuilderFactory;
import ssm.zmh.webchat.model.message.Message;
import ssm.zmh.webchat.model.message.MessageItem;
import ssm.zmh.webchat.model.message.MessageUser;
import ssm.zmh.webchat.model.response.UnAckMessage;
import ssm.zmh.webchat.model.response.UserMessageReload;

@Repository
public class MongoService {
    @Autowired
    MongoOperations mongoOperations;

    public MongoService() {
    }

    public MongoService setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        return this;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void updateMessageUser(String userUUID, Message message) {
        MessageUser messageStorageUser = this.mongoOperations.findById(userUUID, MessageUser.class);

        assert messageStorageUser != null;

        List<String> messagesId = messageStorageUser.getMessagesId();
        messagesId.add(message.getMessageId());
        this.mongoOperations.save(messageStorageUser);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void uploadMessageToMongo(Message message) {
        this.mongoOperations.insert(message);
    }

    public void uploadMessageListToMongo(MessageUser messageUser) {
        MessageUser messageStorageUser = (MessageUser)this.mongoOperations.findById(messageUser.getUserUUID(), MessageUser.class);

        try {
            if (messageStorageUser == null) {
                throw new NullPointerException();
            }

            messageStorageUser.setMessageList(messageUser.getMessageList());
            this.mongoOperations.save(messageStorageUser);
        } catch (NullPointerException var4) {
            var4.printStackTrace();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public JsonObject loadMessageFromMongo(String userUUID, int load1, int load2) {
        MessageUser messageStorageUser = (MessageUser)this.mongoOperations.findById(userUUID, MessageUser.class);

        assert messageStorageUser != null;

        List<String> messagesId = messageStorageUser.getMessagesId();
        JsonObject jsonObject = new JsonObject();
        if (load1 == 1) {
            Query query = new Query(Criteria.where("messageId").in(messagesId));
            Sort sort = Sort.by(Direction.ASC, "messageTime");
            this.mongoOperations.updateFirst(new Query(Criteria.where("userUUID").is(messageStorageUser.getUserUUID())),new Update().set("unAcceptedMessagesId",new ArrayList<>()),MessageUser.class);
            jsonObject.add("messages", SingletonGsonBuilderFactory.getGson().toJsonTree(this.mongoOperations.find(query.with(sort), Message.class)));
        }

        if (load2 == 1) {
            jsonObject.add("messageList", SingletonGsonBuilderFactory.getGson().toJsonTree(messageStorageUser.getMessageList()));
        }

        return jsonObject;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void uploadUnAcceptedMessagesToMongo(String userUUID, Message message) {
        Query query = new Query(Criteria.where("userUUID").is(userUUID).and("messageList").elemMatch(Criteria.where("messageItemId").is(message.getFromUserUUID())));
        if (!"add_contact".equals(message.getMessageType())) {
            if (!this.mongoOperations.exists(query, MessageItem.class)) {
                MessageItem messageItem = new MessageItem("private".equals(message.getRoomType()) ? message.getFromUserUUID() : message.getGroupUUID(), message.getRoomType(), "private".equals(message.getRoomType()) ? message.getFromUserName() : message.getGroupName(), message.getMessage(), message.getMessageTime(), 1);
                this.mongoOperations.updateFirst(new Query(Criteria.where("userUUID").is(userUUID)), (new Update()).push("messageList", messageItem), MessageUser.class);
            } else {
                Update update = (new Update()).inc("messageList.$.newMessageNum", 1).set("messageList.$.message", message.getMessage()).set("messageList.$.messageTime", message.getMessageTime()).set("messageList.$.messageType", message.getMessageType());
//                .set("messageList.$.changeStatus", true)
                this.mongoOperations.updateFirst(query, update, MessageUser.class);
            }
        }

        this.mongoOperations.updateFirst(query, (new Update()).push("unAcceptedMessagesId", message.getMessageId()), MessageUser.class);
    }


//    private List<MessageItem> filterUnChangedMessageItem(List<MessageItem> messageList) {
//        List<MessageItem> newMessageList = new ArrayList<>();
//        for (MessageItem messageItem: messageList) {
//            if (messageItem.isChangeStatus()) {
//                messageItem.setChangeStatus(false);
//                newMessageList.add(messageItem);
//            }
//        }
//        return newMessageList;
//    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public UnAckMessage loadUnAcceptedMessage(String userUUID) {
        MessageUser messageUser = (MessageUser)this.mongoOperations.findById(userUUID, MessageUser.class);

        assert messageUser != null;

//        List<MessageItem> messageList = this.filterUnChangedMessageItem(messageUser.getMessageList());
        Sort sort = Sort.by(Direction.ASC, "messageTime");
        List<Message> unAcceptedMessages = this.mongoOperations.find((new Query(Criteria.where("messageId").in(messageUser.getUnAcceptedMessagesId()))).with(sort), Message.class);
        messageUser.setUnAcceptedMessagesId(new ArrayList<>());
        this.mongoOperations.save(messageUser);
        UnAckMessage unAckMessage = new UnAckMessage();
        unAckMessage.setMessageList(messageUser.getMessageList());
        unAckMessage.setMessages(unAcceptedMessages);
        return unAckMessage;
    }

    public void updateMessageStatus(Message message) {
        this.mongoOperations.updateFirst(new Query(Criteria.where("messageId").is(message.getMessageId())), (new Update()).set("messageStatus", message.getMessageStatus()), Message.class);
    }

    public void initUserStorage(String userUUID) {
        MessageUser messageUser = new MessageUser();
        messageUser.setUserUUID(userUUID);
        messageUser.setMessageList(new ArrayList<>());
        messageUser.setUnAcceptedMessagesId(new ArrayList<>());
        messageUser.setMessagesId(new ArrayList<>());
        this.mongoOperations.insert(messageUser);
    }
}
