// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RoleMapper.java

package ssm.zmh.webchat.dao.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.*;
import ssm.zmh.webchat.model.user.Role;

@Mapper
public interface RoleMapper {
    @Insert({"insert into role(userId,roleName) values(#{userId},#{roleName})"})
    void insert(Role var1);

    @Delete({"delete from role where userId=#{userId} and roleName=#{roleName}"})
    void deleteByName(Role var1);

    @Select({"select roleId,role.userId,roleName from user inner join role on user.userId=role.userId where mail=#{mail}"})
    List<Role> selectAllRoleByMail(@Param("mail") String var1);
}
