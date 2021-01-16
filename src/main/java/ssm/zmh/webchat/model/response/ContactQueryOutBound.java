// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ContactQueryOutBound.java

package ssm.zmh.webchat.model.response;

import ssm.zmh.webchat.model.user.Group;
import ssm.zmh.webchat.model.user.User;

public class ContactQueryOutBound
{

    public ContactQueryOutBound(User user, Group group)
    {
        this.user = user;
        this.group = group;
    }

    public User getUser()
    {
        return user;
    }

    public Group getGroup()
    {
        return group;
    }

    User user;
    Group group;
}
