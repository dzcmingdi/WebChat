// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileUtil.java

package ssm.zmh.webchat.utils.file.save;

import ssm.zmh.webchat.utils.file.save.FileSave;

import java.io.File;
import java.io.IOException;

// Referenced classes of package ssm.zmh.webchat.utils:
//            FileSave

public class FileUtil
{
    private static final String NGINX_ROOT = "D:/nginx-1.16.1";

    public FileUtil()
    {
    }

    public static boolean createRoomDir(String path)
    {
        File file = new File(NGINX_ROOT + "/msgimage/static/message/image/" + path);
        if(!file.exists()) {
            return file.mkdirs();
        }
        else
            return true;
    }

    public static void saveMessageImg(String room, String path, byte[] bytes)
        throws IOException
    {
        FileSave fileSave = new FileSave(NGINX_ROOT + "/msgimage/static/message/image/" + room + '/' + path, bytes);
        fileSave.save();
    }

    public static File getImageFile(String room, String fileName){
        return new File(NGINX_ROOT + "/msgimage/static/message/image/" + room + '/' + fileName);
    }
    public static String getImageSrc(String fileName){
        return "/static/message/image/" + fileName;
    }

    private static final String ABSOLUTE_PATH = NGINX_ROOT + "/msgimage/static/message/image";
}
