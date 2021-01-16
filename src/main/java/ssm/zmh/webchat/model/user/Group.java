// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Group.java

package ssm.zmh.webchat.model.user;

import java.util.Date;
import java.util.UUID;

public class Group
{

    public Group()
    {
    }
    public Group(String groupName,int groupMax){
        this.groupUUID = UUID.randomUUID().toString().replace("-", "");
        this.groupName = groupName;
        this.groupMax = groupMax;
    }
    public Group(int groupId,String groupTargetNumber){
        this.groupId = groupId;
        this.groupTargetNumber = groupTargetNumber;
    }


    public String getGroupUUID()
    {
        return groupUUID;
    }

    public void setGroupUUID(String groupUUID)
    {
        this.groupUUID = groupUUID;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public int getGroupMax()
    {
        return groupMax;
    }

    public void setGroupMax(int groupMax)
    {
        this.groupMax = groupMax;
    }

    public String getGroupTargetNumber() {
        return groupTargetNumber;
    }

    public void setGroupTargetNumber(String groupTargetNumber) {
        this.groupTargetNumber = groupTargetNumber;
    }

    int groupId = -1;
    String groupName;
    Date createDate;
    int groupMax;
    String groupTargetNumber;
    String groupUUID;

}
