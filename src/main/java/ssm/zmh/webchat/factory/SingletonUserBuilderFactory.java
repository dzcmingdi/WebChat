// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonUserFactory.java

package ssm.zmh.webchat.factory;

import com.google.gson.Gson;
import ssm.zmh.webchat.builder.model.UserBuilder;

public class SingletonUserBuilderFactory
{

    public SingletonUserBuilderFactory()
    {
    }

    public static UserBuilder getUserBuilder()
    {
        if(userBuilder == null)
            synchronized(UserBuilder.class)
            {
                if(userBuilder == null)
                    userBuilder = new UserBuilder();
            }
        return userBuilder;
    }

    private static UserBuilder userBuilder;
}
