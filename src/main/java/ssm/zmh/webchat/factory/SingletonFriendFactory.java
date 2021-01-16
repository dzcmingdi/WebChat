// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonFriendFactory.java

package ssm.zmh.webchat.factory;

import com.google.gson.Gson;
import ssm.zmh.webchat.builder.model.FriendBuilder;

public class SingletonFriendFactory
{

    public SingletonFriendFactory()
    {
    }

    public static FriendBuilder getFriendBuilder()
    {
        if(friendBuilder == null)
            synchronized(FriendBuilder.class)
            {
                if(friendBuilder == null)
                    friendBuilder = new FriendBuilder();
            }
        return friendBuilder;
    }

    private static FriendBuilder friendBuilder;
}
