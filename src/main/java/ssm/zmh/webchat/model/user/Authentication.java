// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Authentication.java

package ssm.zmh.webchat.model.user;

import java.security.Principal;

public class Authentication
    implements Principal
{

    public Authentication(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    String name;
}
