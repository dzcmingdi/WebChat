// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MessageFactory.java

package ssm.zmh.webchat.builder.model;

import ssm.zmh.webchat.model.message.Message;

public class MessageFactory
{

    public MessageFactory()
    {
    }

    public static Message creatMessage(String fromUserUUID, String toUserUUID, String groupUUID, String roomType, String fromUserName, String groupName, String messageType)
    {
        Message message = new Message(fromUserUUID, toUserUUID, groupUUID, roomType, fromUserName, groupName, messageType);
        return message;
    }

    public static Message updateMessage(String... updateMessage)
    {
        Message message = new Message();
        message.setMessageId(updateMessage[0]);
        message.setMessageStatus(updateMessage[1]);
        return message;
    }
}
