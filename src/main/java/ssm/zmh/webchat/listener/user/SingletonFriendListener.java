// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonFriendListener.java

package ssm.zmh.webchat.listener.user;

import org.springframework.stereotype.Component;
import ssm.zmh.webchat.dao.impl.user.ContactsService;
import ssm.zmh.webchat.utils.file.save.FileUtil;

// Referenced classes of package ssm.zmh.webchat.listener.user:
//            FriendListener

@Component
public class SingletonFriendListener
    implements FriendListener
{

    public SingletonFriendListener()
    {
    }

    public void beforeFriendInsert()
    {
    }

    public void afterFriendInsert(String fromUserUUID, String toUserUUID)
    {
        FileUtil.createRoomDir(ContactsService.createRoomDirPath(
            fromUserUUID, toUserUUID
        ));
    }
}
