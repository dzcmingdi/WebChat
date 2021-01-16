// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ContactDeleteInBound.java

package ssm.zmh.webchat.model.request;


public class ContactDeleteInBound
{

    public ContactDeleteInBound()
    {
    }

    public String getUserUUID()
    {
        return userUUID;
    }

    public void setUserUUID(String userUUID)
    {
        this.userUUID = userUUID;
    }

    public String getTargetType()
    {
        return targetType;
    }

    public void setTargetType(String targetType)
    {
        this.targetType = targetType;
    }

    public String getGroupUUID()
    {
        return groupUUID;
    }

    public void setGroupUUID(String groupUUID)
    {
        this.groupUUID = groupUUID;
    }

    private String userUUID;
    private String targetType;
    private String groupUUID;
}
