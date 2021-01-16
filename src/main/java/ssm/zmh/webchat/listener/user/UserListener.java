// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserListener.java

package ssm.zmh.webchat.listener.user;

import org.springframework.stereotype.Component;
import ssm.zmh.webchat.model.user.User;

@Component
public interface UserListener
{

    public  void beforeInsert(User user);

    public  void afterInsert(User user);

    public  void beforeRedisSet(User user);

    public  void afterRedisSet();

    public  void beforeRedisGet();

    public  void afterRedisGet(User user);
}
