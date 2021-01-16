// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserMapper.java

package ssm.zmh.webchat.dao.mapper.user;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import ssm.zmh.webchat.model.request.ContactUpdate;
import ssm.zmh.webchat.model.user.User;

@Mapper
public interface UserMapper {
    @Insert({"insert into user(mail,userName,password,createDate,userUUID,userTargetNumber) values(#{mail},#{userName},#{password},now(),#{userUUID})"})
    @Options(
            useGeneratedKeys = true,
            keyProperty = "userId"
    )
    void insert(User user);

    @Update({"<script>update user <trim prefix='set' suffixOverrides=','><if test='userName!=null'>userName=#{userName},</if><if test='password!=null'>password=#{password},</if><if test='userTargetNumber!=null'>userTargetNumber=#{userTargetNumber}</if></trim> where <if test='userId != -1'> userId=#{userId}</if> <if test='userUUID != null'>userUUID=#{userUUID}</if></script>"})
    void updateOne(User user);

    @Delete({"delete from user where userId=#{userId}"})
    void delete(User var1);

    @Select({"select mail,userName,password,createDate from user where userId=#{userId}"})
    User selectOneById(@Param("userId") Integer var1);

    @Select({"select userId,mail,userName,password,createDate,userUUID,userTargetNumber from user where mail=#{mail}"})
    User selectOneByMail(@Param("mail") String var1);

    @Select({"select userId,mail,password,createDate from user where userId between #{userIdL} and #{userIdR}"})
    List<User> selectBylr(@Param("userIdL") int var1, @Param("userIdR") int var2);

    @Select({"select userId,mail,password,createDate from user"})
    List<User> selectAll();

    @Select({"select userId,userUUID from user where userUUID = #{userUUID}"})
    User selectOnesId(@Param("userUUID") String var1);

    @Select({"select userName,createDate,userUUID from user where userTargetNumber = #{userTargetNumber}"})
    User selectOneByTargetNumber(@Param("userTargetNumber") String userTargetNumber);

    @Select({"<script>select userUUID from user where userTargetNumber in <foreach begin='(' separator=',' close=')' item='item' index='index' collection='targetNumbers' >#{item}</foreach></script>"})
    List<String> selectManyByTargetNumber(@Param("targetNumbers") List<String> targetNumber);

    @Select({"select userName,createDate,userUUID from user where userUUID = #{userUUID}"})
    User selectOneByUserUUID(@Param("userUUID") String var1);

    @Deprecated
    @Update("<script>update user <trim prefix='set' suffixOverrides=','> <if test='userName!=null'>userName=#{userName},</if> <if test='password!=null'>password=#{password}</if> where userUUID = #{userUUID} </trim> </script>")
    void updateOneByUserUUID(ContactUpdate contactUpdate);



}

