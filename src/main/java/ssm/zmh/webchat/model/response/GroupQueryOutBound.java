// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupQueryOutBound.java

package ssm.zmh.webchat.model.response;


public class GroupQueryOutBound
{

    public GroupQueryOutBound()
    {
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getGroupUUID()
    {
        return groupUUID;
    }

    public void setGroupUUID(String groupUUID)
    {
        this.groupUUID = groupUUID;
    }

    public String getGroupRole()
    {
        return groupRole;
    }

    public void setGroupRole(String groupRole)
    {
        this.groupRole = groupRole;
    }

    public String getGroupTargetNumber() {
        return groupTargetNumber;
    }

    public void setGroupTargetNumber(String groupTargetNumber) {
        this.groupTargetNumber = groupTargetNumber;
    }

    String groupName;
    String createDate;
    String groupUUID;
    String groupTargetNumber;
    String groupRole;
}
