// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SingletonFactory.java

package ssm.zmh.webchat.factory;

import ssm.zmh.webchat.builder.model.GroupMemberBuilder;

public class SingletonGroupMemberBuilderFactory
{
    private static GroupMemberBuilder groupMemberBuilder;

    public static GroupMemberBuilder getGroupMemberBuilder()
    {
        if(groupMemberBuilder == null)
            synchronized(GroupMemberBuilder.class)
            {
                if(groupMemberBuilder == null)
                    groupMemberBuilder = new GroupMemberBuilder();
            }
        return groupMemberBuilder;
    }

}
