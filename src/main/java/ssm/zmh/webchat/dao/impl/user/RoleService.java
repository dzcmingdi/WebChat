// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RoleService.java

package ssm.zmh.webchat.dao.impl.user;

import java.util.List;

import org.springframework.stereotype.Service;
import ssm.zmh.webchat.dao.mapper.user.RoleMapper;
import ssm.zmh.webchat.model.user.Role;


@Service
public class RoleService
{

    public RoleService()
    {
    }

    public List<Role> selectAllRoleByMail(String mail)
    {
        return roleMapper.selectAllRoleByMail(mail);
    }

    RoleMapper roleMapper;
}
