// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MessageUpload.java

package ssm.zmh.webchat.model.request;

import ssm.zmh.webchat.model.message.MessageUser;

public class MessageUpload
{

    public MessageUpload()
    {
    }

    public MessageUser getMessageUser()
    {
        return messageUser;
    }

    public void setMessageUser(MessageUser messageUser)
    {
        this.messageUser = messageUser;
    }

    MessageUser messageUser;
}
