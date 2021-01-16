// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JsonObjectBuilder.java

package ssm.zmh.webchat.builder.gson;

import com.google.gson.JsonObject;
import ssm.zmh.webchat.builder.interfaces.WebChat2020Builder;

public class JsonObjectBuilder
        implements WebChat2020Builder<JsonObject> {

    public JsonObjectBuilder() {
    }


    public JsonObject build(Object ...args) {
        return new JsonObject();
    }

}
