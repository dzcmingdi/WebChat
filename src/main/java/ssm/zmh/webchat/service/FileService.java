// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileService.java

package ssm.zmh.webchat.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Service;
import ssm.zmh.webchat.utils.file.save.FileUtil;

@Service
public class FileService
{

    public FileService(){}

    public void saveMessageImg(String room, String src, String buffer)
        throws IOException
    {
        FileUtil.saveMessageImg(room, src, Base64.getDecoder().decode(buffer));
    }


}
