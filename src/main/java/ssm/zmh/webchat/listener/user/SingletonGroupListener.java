// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonGroupListener.java

package ssm.zmh.webchat.listener.user;

import org.springframework.stereotype.Component;
import ssm.zmh.webchat.dao.impl.user.ContactsService;
import ssm.zmh.webchat.exception.group.AddGroupException;
import ssm.zmh.webchat.utils.file.save.FileUtil;

// Referenced classes of package ssm.zmh.webchat.listener.user:
//            GroupListener

@Component
public class SingletonGroupListener
    implements GroupListener
{

    public SingletonGroupListener()
    {
    }

    public void beforeGroupInsert()
    {
    }

    public void afterGroupInsert(String groupUUID) throws AddGroupException {

        if(!FileUtil.createRoomDir(ContactsService.createRoomDirPath(
            groupUUID
        ))){
            throw new AddGroupException("创建组目录失败");
        }

    }
}
