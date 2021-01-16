// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FriendBuilder.java

package ssm.zmh.webchat.builder.model;

import ssm.zmh.webchat.builder.interfaces.WebChat2020Builder;
import ssm.zmh.webchat.model.user.Friend;

public class FriendBuilder
    implements WebChat2020Builder<Friend>
{

    public FriendBuilder()
    {
    }

    public Friend build(Object ...args)
    {
        return new Friend(((Integer)args[0]), ((Integer)args[1]));
    }

}
