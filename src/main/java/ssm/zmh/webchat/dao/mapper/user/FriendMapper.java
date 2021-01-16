// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FriendMapper.java

package ssm.zmh.webchat.dao.mapper.user;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ssm.zmh.webchat.model.user.Friend;
import ssm.zmh.webchat.model.user.User;

@Mapper
public interface FriendMapper {
    @Insert({"insert into friends(userAId,userBId,createTime) values(#{userAId},#{userBId},now())"})
    void insert(Friend var1);

    @Select({"select userName,createDate,userUUID,userTargetNumber from user where userId in (select userAId from friends where userBId=#{userId} union select userBId from friends where userAId=#{userId})"})
    List<User> getOnesAllFriends(@Param("userId") int var1);

    @Insert({" insert into friends(userAId,userBId,createDate) select #{userAId},#{userBId},now() from dual where not exists (select * from friends where userAId = #{userAId} and userBId = #{userBId})"})
    @Options(
            useGeneratedKeys = true,
            keyProperty = "friendId"
    )
    void addOnesFriend(Friend var1);

    @Delete({"delete from friends where userAId = #{userAId} and userBId=(select userId from user where userUUID=#{userBUUID})"})
    void deleteFriend(@Param("userAId") int userAId, @Param("userBUUID") String userBUUID);
}
