// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Message.java

package ssm.zmh.webchat.model.message;

import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

public class Message
{

    public String getFromUserName()
    {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName)
    {
        this.fromUserName = fromUserName;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Message(String fromUserUUID, String toUserUUID, String groupUUID, String roomType, String fromUserName, String groupName, String messageType)
    {
        messageId = UUID.randomUUID().toString().replace("-", "");
        this.fromUserUUID = fromUserUUID;
        this.toUserUUID = toUserUUID;
        this.groupUUID = groupUUID;
        this.roomType = roomType;
        messageTime = Calendar.getInstance().getTime();
        this.fromUserName = fromUserName;
        this.groupName = groupName;
        this.messageType = messageType;
    }

    public Message()
    {
    }

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public String getSrc()
    {
        return src;
    }

    public void setSrc(String src)
    {
        this.src = src;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Date getMessageTime()
    {
        return messageTime;
    }

    public void setMessageTime(Date messageTime)
    {
        this.messageTime = messageTime;
    }

    public String getFromUserUUID()
    {
        return fromUserUUID;
    }

    public void setFromUserUUID(String fromUserUUID)
    {
        this.fromUserUUID = fromUserUUID;
    }

    public String getGroupUUID()
    {
        return groupUUID;
    }

    public void setGroupUUID(String groupUUID)
    {
        this.groupUUID = groupUUID;
    }

    public String getToUserUUID()
    {
        return toUserUUID;
    }

    public void setToUserUUID(String toUserUUID)
    {
        this.toUserUUID = toUserUUID;
    }

    public String getMessageStatus()
    {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus)
    {
        this.messageStatus = messageStatus;
    }

    @MongoId
    private String messageId;
    private String fromUserUUID;
    private String fromUserName;
    private String groupUUID;
    private String roomType;
    private String message;
    private String src;
    private String messageType;
    private String messageStatus;
    private Date messageTime;
    private String groupName;
    private String toUserUUID;
}
