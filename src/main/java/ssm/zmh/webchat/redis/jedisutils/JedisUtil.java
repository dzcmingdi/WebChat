// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JedisUtil.java

package ssm.zmh.webchat.redis.jedisutils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Set;
@Component
public interface JedisUtil {
    void set(String var1, Object var2);

    Object get(String var1, Type var2);

    void sAdd(String var1, String var2);

    Set<String> sMembers(String var1);

}