// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DateUtil.java

package ssm.zmh.webchat.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{

    public DateUtil()
    {
    }

    public static String convertDateToString(Date date)
    {
        return getSimpleDateFormat().format(date);
    }

    public static SimpleDateFormat getSimpleDateFormat()
    {
        if(simpleDateFormat == null)
            synchronized(SimpleDateFormat.class)
            {
                if(simpleDateFormat == null)
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        return simpleDateFormat;
    }

    private static SimpleDateFormat simpleDateFormat;
}
