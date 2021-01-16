// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonUserListener.java

package ssm.zmh.webchat.listener.user;

import javax.mail.MessagingException;

import org.springframework.stereotype.Component;
import ssm.zmh.webchat.model.user.User;
import ssm.zmh.webchat.utils.service.UserRegisterUtil;

// Referenced classes of package ssm.zmh.webchat.listener.user:
//            UserListener

@Component
public class SingletonUserListener
    implements UserListener
{

    public SingletonUserListener()
    {
    }

    public void beforeInsert(User user1)
    {
    }

    public void afterInsert(User user1)
    {
    }

    public void beforeRedisSet(User user)
    {
        try
        {
            userRegisterUtil.registerActivateMailSend(user);
        }
        catch(MessagingException e)
        {
            e.printStackTrace();
        }
    }

    public void afterRedisSet()
    {
    }

    public void beforeRedisGet()
    {
    }

    public void afterRedisGet(User user)
    {
        user.setUserUUID();
    }



    UserRegisterUtil userRegisterUtil;
}
