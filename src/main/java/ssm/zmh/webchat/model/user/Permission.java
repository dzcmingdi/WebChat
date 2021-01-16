// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Permission.java

package ssm.zmh.webchat.model.user;


public class Permission
{

    public Permission()
    {
    }

    public int getPerId()
    {
        return perId;
    }

    public void setPerId(int perId)
    {
        this.perId = perId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getPerName()
    {
        return perName;
    }

    public void setPerName(String perName)
    {
        this.perName = perName;
    }

    int perId;
    int userId;
    String perName;
}
