// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupMemberBuilder.java

package ssm.zmh.webchat.builder.model;

import ssm.zmh.webchat.builder.interfaces.WebChat2020Builder;
import ssm.zmh.webchat.model.user.GroupMember;

public class GroupMemberBuilder implements WebChat2020Builder<GroupMember> {
    public GroupMemberBuilder() {
    }

    public GroupMember build(Object... args) {
        GroupMember groupMember = new GroupMember();
        groupMember.setUserUUID((String)args[0]);
        groupMember.setGroupUUID((String)args[1]);
        groupMember.setGroupRole((String)args[2]);
        return groupMember;
    }
}

