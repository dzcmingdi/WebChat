// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupMapper.java

package ssm.zmh.webchat.dao.mapper.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.*;
import ssm.zmh.webchat.model.request.ContactUpdate;
import ssm.zmh.webchat.model.response.GroupQueryOutBound;
import ssm.zmh.webchat.model.user.Group;
import ssm.zmh.webchat.model.user.GroupMember;
import ssm.zmh.webchat.model.user.User;

@Mapper
public interface GroupMapper {
    @Select({"select groupName,createDate,chatgroups.groupUUID,groupTargetNumber,groupRole from chatgroups inner join groupmember on chatgroups.groupUUID=groupmember.groupUUID where groupmember.userUUID=#{userUUID}"})
    List<GroupQueryOutBound> getSelfGroupList(@Param("userUUID") String userUUID);

    @Select({"select userUUID from groupmember where groupUUID=#{groupUUID}"})
    List<String> getGroupMemberUserUUID(@Param("groupUUID") String groupUUID);

    @Insert({"insert into groupmember(userUUID,groupUUID,groupRole) select #{userUUID},#{groupUUID},#{groupRole} from dual where not exists (select * from groupmember where groupUUID=#{groupUUID} and userUUID=#{userUUID})"})
    @Options(
            useGeneratedKeys = true,
            keyProperty = "groupMemberId"
    )
    void addGroupMember(GroupMember groupMember);

    @Select({"select groupId,groupUUID from chatgroups where groupUUID = #{groupUUID}"})
    Group selectOnesId(@Param("groupUUID") String groupUUID);

    @Select({"select groupName,createDate,groupUUID from chatgroups where groupTargetNumber=#{groupTargetNumber}"})
    Group selectOneByTargetNumber(@Param("groupTargetNumber") String var1);

    @Select({"select groupName,createDate,groupUUID from chatgroups where groupUUID = #{groupUUID}"})
    Group selectOneByGroupUUID(@Param("groupUUID") String var1);

    @Select({"select groupName,groupUUID,groupTargetNumber,createDate from chatgroups where groupId=#{groupId}"})
    Group selectOneByGroupId(@Param("groupId") int groupId);

    @Select({"select userUUID from groupmember inner join user on groupmember.userId=user.userId inner join chatgroups on groupmember.groupId=chatgroups.groupId where groupUUID=#{groupUUID} and groupRole=#{groupRole}"})
    User selectGroupRoleUserUUID(@Param("groupUUID") String groupUUID, @Param("groupRole") String groupRole);

    @Delete({"delete from groupmember where userUUID=#{userUUID} and groupId=(select groupId from chatgroups where groupUUID=#{groupUUID})"})
    void deleteGroupMember(@Param("userUUID") String userUUID, @Param("groupUUID") String groupUUID);

    @Deprecated
    @Update("<script>update chatgroups <trim prefix='set' suffixOverrides=','><if test='groupName!=null'>groupName=#{groupName}</if> where groupUUID=#{groupUUID}</trim></script>")
    void updateOneByGroupUUID(ContactUpdate contactUpdate);

    @Update("<script>update chatgroups <trim prefix='set' suffixOverrides=','><if test='groupName!=null'>groupName=#{groupName},</if> <if test='groupTargetNumber!=null' >groupTargetNumber=#{groupTargetNumber}</if></trim> <where> <if test='groupUUID!=null'>groupUUID=#{groupUUID}</if> <if test='groupId!=-1'>groupId=#{groupId}</if> </where></script>")
    void updateOne(Group group);


    @Insert("insert into chatgroups(groupName,groupMax,groupUUID,createDate) value(#{groupName},#{groupMax},#{groupUUID},now())")
    @Options(useGeneratedKeys = true,keyProperty = "groupId",keyColumn = "groupId")
    void insertOneGroup(Group group);

    @Insert("insert into groupmember(userId,groupId,groupRole) values <foreach item='item' index='index' open='(' separator=',' close=')' collection='groupMembers'>#{item} </foreach> </script>")
    void insertManyGroupMembers(@Param("groupMembers") List<GroupMember> groupMembers);

}
