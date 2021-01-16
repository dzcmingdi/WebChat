// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonGsonFactory.java

package ssm.zmh.webchat.factory;

import com.google.gson.Gson;

public class SingletonGsonBuilderFactory
{


    public static Gson getGson()
    {
        if(gson == null)
            synchronized(Gson.class)
            {
                if(gson == null)
                    gson = new Gson();
            }
        return gson;
    }

    private static Gson gson;
}
