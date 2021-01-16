// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupListener.java

package ssm.zmh.webchat.listener.user;


import org.springframework.stereotype.Component;
import ssm.zmh.webchat.exception.group.AddGroupException;

@Component
public interface GroupListener
{

    public  void beforeGroupInsert();

    public  void afterGroupInsert(String s) throws AddGroupException;
}
