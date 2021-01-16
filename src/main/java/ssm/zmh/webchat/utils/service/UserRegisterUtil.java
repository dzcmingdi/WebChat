// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserRegisterUtil.java

package ssm.zmh.webchat.utils.service;

import javax.mail.MessagingException;
import ssm.zmh.webchat.model.user.User;
import ssm.zmh.webchat.utils.spring.SpringMailServiceImpl;

// Referenced classes of package ssm.zmh.webchat.utils:
//            SpringMailServiceImpl

public class UserRegisterUtil
{

    public UserRegisterUtil()
    {
    }

    public void registerActivateMailSend(User user)
        throws MessagingException
    {
        springMailService.sendHtmlMail(user.getMail(), "Account Activation", "http://localhost:8080/user/register/confirm?token=" + user.getUserToken() + "&mail=" + user.getMail());
    }

    SpringMailServiceImpl springMailService;
}
