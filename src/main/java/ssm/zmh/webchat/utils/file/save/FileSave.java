// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileUtil.java

package ssm.zmh.webchat.utils.file.save;

import java.io.*;

public class FileSave
{

    public FileSave(String fileName, byte[] bytes)
        throws FileNotFoundException
    {
        file = new File(fileName);
        fileOutputStream = new FileOutputStream(file);
        this.bytes = bytes;
    }

    public void save()
        throws IOException
    {
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    File file;
    FileOutputStream fileOutputStream;
    byte[] bytes;
}
