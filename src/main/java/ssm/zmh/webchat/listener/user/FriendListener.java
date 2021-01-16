// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FriendListener.java

package ssm.zmh.webchat.listener.user;


import org.springframework.stereotype.Component;

@Component
public interface FriendListener
{

    public  void beforeFriendInsert();

    public  void afterFriendInsert(String s, String s1);
}
