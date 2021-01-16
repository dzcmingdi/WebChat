// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MessageItem.java

package ssm.zmh.webchat.model.message;

import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

public class MessageItem
{

    public String getMessageItemId()
    {
        return messageItemId;
    }

    public void setMessageItemId(String messageItemId)
    {
        this.messageItemId = messageItemId;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public int getNewMessageNum()
    {
        return newMessageNum;
    }

    public void setNewMessageNum(int newMessageNum)
    {
        this.newMessageNum = newMessageNum;
    }

    public MessageItem(String messageItemId, String roomType, String name, String message, Date messageTime, int newMessageNum)
    {
        this.messageItemId = messageItemId;
        this.roomType = roomType;
        this.name = name;
        this.message = message;
        this.messageTime = messageTime;
        this.newMessageNum = newMessageNum;
    }

    public MessageItem()
    {
    }

    @MongoId
    private String messageItemId;
    private String roomType;
    private String name;
    private String message;
    private Date messageTime;
    private int newMessageNum;
}
