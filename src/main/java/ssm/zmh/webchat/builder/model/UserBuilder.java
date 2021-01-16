// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserBuilder.java

package ssm.zmh.webchat.builder.model;

import org.springframework.util.DigestUtils;
import ssm.zmh.webchat.builder.interfaces.WebChat2020Builder;
import ssm.zmh.webchat.model.user.User;

public class UserBuilder
    implements WebChat2020Builder<User>
{

    public UserBuilder()
    {
    }

    public User build(Object ...args)
    {
        return new User((String)args[0], (String)args[1], (String)args[2], DigestUtils.md5DigestAsHex(((String) args[0] + System.currentTimeMillis()).getBytes()));
    }

}
