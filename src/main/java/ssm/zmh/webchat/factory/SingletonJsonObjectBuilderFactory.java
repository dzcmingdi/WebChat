// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonJsonObjectFactory.java

package ssm.zmh.webchat.factory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ssm.zmh.webchat.builder.gson.JsonObjectBuilder;

public class SingletonJsonObjectBuilderFactory
{

    public SingletonJsonObjectBuilderFactory()
    {
    }

    public static JsonObjectBuilder getJsonObjectBuilder()
    {
        if(jsonObjectBuilder == null)
            synchronized(JsonObject.class)
            {
                if(jsonObjectBuilder == null)
                    jsonObjectBuilder = new JsonObjectBuilder();
            }
        return jsonObjectBuilder;
    }

    private static JsonObjectBuilder jsonObjectBuilder;
}
