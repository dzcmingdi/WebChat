// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MessageService.java

package ssm.zmh.webchat.service;

import com.google.gson.JsonObject;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.zmh.webchat.dao.mapper.user.GroupMapper;
import ssm.zmh.webchat.model.message.Message;
import ssm.zmh.webchat.model.message.MessageUser;
import ssm.zmh.webchat.model.response.UnAckMessage;
import ssm.zmh.webchat.mongodb.MongoService;
import ssm.zmh.webchat.websocket.WebSocketModel;

@Service
public class MessageService
{

    @Autowired
    MongoService mongoService;

    public void handleInComingMessage(Message message, List<String> list)
    {
        String userUUID;
        for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); mongoService.updateMessageUser(userUUID, message))
            userUUID = (String)iterator.next();

        mongoService.uploadMessageToMongo(message);
    }

    public void handleUnAcceptedPrivateMessage(Message message)
    {
        if(!WebSocketModel.userSocketSessionsSet.contains(message.getToUserUUID()))
            mongoService.uploadUnAcceptedMessagesToMongo(message.getToUserUUID(), message);
    }

    public void handleUnAcceptedGroupMessage(List<String> groupMemberUserUUID, Message message)
    {
        for (String memberUserUUID : groupMemberUserUUID) {
            if (!memberUserUUID.equals(message.getFromUserUUID()) && !WebSocketModel.userSocketSessionsSet.contains(memberUserUUID))
                mongoService.uploadUnAcceptedMessagesToMongo(memberUserUUID, message);
        }
    }

    public UnAckMessage loadUnAcceptedPrivateMessage(String userUUID)
    {
        return mongoService.loadUnAcceptedMessage(userUUID);
    }

    public void uploadMessageListToMongo(MessageUser messageUser)
    {
        mongoService.uploadMessageListToMongo(messageUser);
    }

    public JsonObject loadMessagesFromMongo(String userUUID, int load1, int load2)
    {
        return mongoService.loadMessageFromMongo(userUUID, load1, load2);
    }

    public void updateMessageStatus(Message message)
    {
        mongoService.updateMessageStatus(message);
    }


}
