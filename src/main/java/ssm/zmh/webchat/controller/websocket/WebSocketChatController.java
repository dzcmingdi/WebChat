//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.controller.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import ssm.zmh.webchat.builder.model.MessageFactory;
import ssm.zmh.webchat.dao.mapper.user.GroupMapper;
import ssm.zmh.webchat.builder.MapBuilder;
import ssm.zmh.webchat.factory.SingletonGsonBuilderFactory;
import ssm.zmh.webchat.factory.SingletonJsonObjectBuilderFactory;
import ssm.zmh.webchat.model.message.Message;
import ssm.zmh.webchat.model.socket.MessageInBound;
import ssm.zmh.webchat.model.user.User;
import ssm.zmh.webchat.service.FileService;
import ssm.zmh.webchat.service.MessageService;
import ssm.zmh.webchat.utils.date.DateUtil;

@Controller
public class WebSocketChatController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    MessageService messageService;
    @Autowired
    FileService fileService;
    @Autowired
    GroupMapper groupMapper;

    public WebSocketChatController() {
    }

    @MessageMapping({"/webchat/user/{toUserUUID}/chat"})
    public void privateChat(@DestinationVariable String toUserUUID, String message, StompHeaderAccessor headerAccessor) {
        String fromUserUUID;
        String fromUserName;
        String messageType;
        String messageLocIndex;
        String messageLocKey;
        MessageInBound messageInBound;
        try {
            fromUserUUID = (Objects.requireNonNull(headerAccessor.getNativeHeader("fromUserUUID"))).get(0);
            fromUserName = (Objects.requireNonNull(headerAccessor.getNativeHeader("fromUserName"))).get(0);
            messageType = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageType"))).get(0);
            messageLocIndex = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageLocIndex"))).get(0);
            messageLocKey = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageLocKey"))).get(0);
            messageInBound = SingletonGsonBuilderFactory.getGson().fromJson(Objects.requireNonNull(message),MessageInBound.class);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }


        try {

            Message insertMessage = MessageFactory.creatMessage(fromUserUUID, toUserUUID, null, "private", fromUserName, null, messageType);
            String convertedDateTime = DateUtil.convertDateToString(insertMessage.getMessageTime());
            JsonObject broadcastJsonObject = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();
            JsonObject ackJsonObject = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();
            broadcastJsonObject.addProperty("fromUserUUID", fromUserUUID);
            broadcastJsonObject.addProperty("fromUserName", fromUserName);
            broadcastJsonObject.addProperty("messageType", messageType);
            broadcastJsonObject.addProperty("messageTime", convertedDateTime);
            broadcastJsonObject.addProperty("messageId", insertMessage.getMessageId());
            ackJsonObject.addProperty("messageTime", convertedDateTime);
            ackJsonObject.addProperty("messageId", insertMessage.getMessageId());
            switch (messageType) {
                case "text":
                    insertMessage.setMessage(messageInBound.getMessage());
                    broadcastJsonObject.addProperty("message", messageInBound.getMessage());
                    break;
                case "byte":
                    if (messageInBound.getSrc() == null) {
                        throw new Exception("未找到图片");
                    }
                    insertMessage.setSrc(messageInBound.getSrc());
                    broadcastJsonObject.addProperty("src", messageInBound.getSrc());
                    break;
                case "add_contact":
                    String messageStatus = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageStatus"))).get(0);
                    insertMessage.setMessage(messageInBound.getMessage());
                    insertMessage.setMessageStatus(messageStatus);
                    broadcastJsonObject.addProperty("message", messageInBound.getMessage());
                    broadcastJsonObject.addProperty("messageStatus", messageStatus);
                    break;

                default:
                    break;
            }
            Map<String,Object> ackMap = MapBuilder.createMap(new AbstractMap.SimpleEntry<>("messageLocKey", messageLocKey),
                    new AbstractMap.SimpleEntry<>("messageLocIndex", messageLocIndex)
            );

            List<String> userIdList = new ArrayList<>();
            userIdList.add(insertMessage.getFromUserUUID());
            userIdList.add(insertMessage.getToUserUUID());
            this.messageService.handleInComingMessage(insertMessage, userIdList);
            this.messageService.handleUnAcceptedPrivateMessage(insertMessage);
            this.simpMessagingTemplate.convertAndSendToUser(toUserUUID, "/private/chat", broadcastJsonObject.toString());
            this.simpMessagingTemplate.convertAndSendToUser(fromUserUUID, "/message/ack", ackJsonObject.toString(), ackMap);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @MessageMapping({"/webchat/group/{groupUUID}/chat"})
    public void groupChat(@DestinationVariable String groupUUID, String message, StompHeaderAccessor headerAccessor) {
        String fromUserUUID;
        String fromUserName;
        String groupName;
        String messageType;
        String messageLocIndex;
        String messageLocKey;
        MessageInBound messageInBound;

        try {
            fromUserUUID = (Objects.requireNonNull(headerAccessor.getNativeHeader("fromUserUUID"))).get(0);
            fromUserName = (Objects.requireNonNull(headerAccessor.getNativeHeader("fromUserName"))).get(0);
            groupName = (Objects.requireNonNull(headerAccessor.getNativeHeader("groupName"))).get(0);
            messageType = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageType"))).get(0);
            messageLocIndex = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageLocIndex"))).get(0);
            messageLocKey = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageLocKey"))).get(0);
            messageInBound = SingletonGsonBuilderFactory.getGson().fromJson(Objects.requireNonNull(message),MessageInBound.class);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
        try {

            Message insertMessage = MessageFactory.creatMessage(fromUserUUID,  null, groupUUID, "group", fromUserName, groupName, messageType);
            String convertedDateTime = DateUtil.convertDateToString(insertMessage.getMessageTime());
            JsonObject broadcastJsonObject = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();
            JsonObject ackBody = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();
            broadcastJsonObject.addProperty("fromUserUUID", fromUserUUID);
            broadcastJsonObject.addProperty("fromUserName", fromUserName);
            broadcastJsonObject.addProperty("messageType", messageType);
            broadcastJsonObject.addProperty("messageTime", convertedDateTime);
            broadcastJsonObject.addProperty("groupUUID", groupUUID);
            broadcastJsonObject.addProperty("groupName", groupName);
            broadcastJsonObject.addProperty("messageId", insertMessage.getMessageId());
            ackBody.addProperty("messageId", insertMessage.getMessageId());
            ackBody.addProperty("messageTime", convertedDateTime);
            String destination;
            List<String> groupMemberUserUUIDList;
            switch (messageType) {
                case "text":
                    groupMemberUserUUIDList = this.groupMapper.getGroupMemberUserUUID(insertMessage.getGroupUUID());
                    destination = "/subscribe/user/" + groupUUID + "/group/chat";
                    insertMessage.setMessage(messageInBound.getMessage());
                    broadcastJsonObject.addProperty("message",messageInBound.getMessage() );
                    break;
                case "byte":
                    if (messageInBound.getSrc() == null){
                        throw new Exception("未找到图片");
                    }
                    destination = "/subscribe/user/" + groupUUID + "/group/chat";
                    groupMemberUserUUIDList = this.groupMapper.getGroupMemberUserUUID(insertMessage.getGroupUUID());
                    insertMessage.setSrc(messageInBound.getSrc());
                    broadcastJsonObject.addProperty("src", messageInBound.getSrc());
                    break;
                case "add_contact":
                    String messageStatus = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageStatus"))).get(0);
                    groupMemberUserUUIDList = new ArrayList<>();
                    User groupHostUser = this.groupMapper.selectGroupRoleUserUUID(groupUUID, "host");
                    groupMemberUserUUIDList.add(groupHostUser.getUserUUID());
                    groupMemberUserUUIDList.add(fromUserUUID);
                    destination = "/subscribe/user/" + groupUUID + "/group/host/chat";
                    insertMessage.setMessage(messageInBound.getMessage());
                    insertMessage.setMessageStatus(messageStatus);
                    broadcastJsonObject.addProperty("messageStatus", messageStatus);
                    broadcastJsonObject.addProperty("message", messageInBound.getMessage());
                    break;
                default:
                    groupMemberUserUUIDList = new ArrayList<>();
                    destination = "subscribe/user/" + groupUUID + "/group/chat";

            }

            Map<String, Object> ackMap = MapBuilder.createMap(new AbstractMap.SimpleEntry<>("messageLocKey", messageLocKey),
                    new AbstractMap.SimpleEntry<>("messageLocIndex", messageLocIndex));

            this.messageService.handleInComingMessage(insertMessage, groupMemberUserUUIDList);
            this.messageService.handleUnAcceptedGroupMessage(groupMemberUserUUIDList, insertMessage);
            this.simpMessagingTemplate.convertAndSend(destination, broadcastJsonObject.toString());
            this.simpMessagingTemplate.convertAndSendToUser(fromUserUUID, "/message/ack", ackBody.toString(), ackMap);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @MessageMapping({"/webchat/contact/{toUserUUID}/add/confirm"})
    public void contactsAddAck(@DestinationVariable String toUserUUID, String message, StompHeaderAccessor headerAccessor) {
        String roomType = (Objects.requireNonNull(headerAccessor.getNativeHeader("roomType"))).get(0);
        String messageId = (Objects.requireNonNull(headerAccessor.getNativeHeader("messageId"))).get(0);
        JsonObject ackJsonObject = SingletonJsonObjectBuilderFactory.getJsonObjectBuilder().build();
        switch(roomType) {
            case "private":
                String fromUserUUID = (Objects.requireNonNull(headerAccessor.getNativeHeader("fromUserUUID"))).get(0);
                ackJsonObject.addProperty("fromUserUUID", fromUserUUID);
                break;
            case "group":
                String groupUUID = (Objects.requireNonNull(headerAccessor.getNativeHeader("groupUUID"))).get(0);
                ackJsonObject.addProperty("groupUUID", groupUUID);
                break;
        }
        Message updateMessage = MessageFactory.updateMessage(messageId, "accept");
        this.messageService.updateMessageStatus(updateMessage);
        ackJsonObject.addProperty("messageId", messageId);
        ackJsonObject.addProperty("message", message);
        ackJsonObject.addProperty("roomType", roomType);
        this.simpMessagingTemplate.convertAndSendToUser(toUserUUID, "/contact/add/confirm", ackJsonObject.toString());
    }
}
