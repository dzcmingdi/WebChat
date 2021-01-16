// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Friend.java

package ssm.zmh.webchat.model.user;

import java.sql.Date;

public class Friend
{

    public Friend()
    {
    }

    public Friend(int userAId, int userBId)
    {
        this.userAId = userAId;
        this.userBId = userBId;
    }

    public int getFriendId()
    {
        return friendId;
    }

    public String toString()
    {
        return "Friend{friendId=" + friendId + ", userAId=" + userAId + ", userBId=" + userBId + ", createDate=" + createDate + '}';
    }

    public void setFriendId(int friendId)
    {
        this.friendId = friendId;
    }

    public int getUserAId()
    {
        return userAId;
    }

    public void setUserAId(int userAId)
    {
        this.userAId = userAId;
    }

    public int getUserBId()
    {
        return userBId;
    }

    public void setUserBId(int userBId)
    {
        this.userBId = userBId;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createTime)
    {
        createDate = createTime;
    }

    int friendId;
    int userAId;
    int userBId;
    Date createDate;
}
