//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.dao.impl.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ssm.zmh.webchat.builder.model.GroupMemberBuilder;
import ssm.zmh.webchat.dao.mapper.user.FriendMapper;
import ssm.zmh.webchat.dao.mapper.user.GroupMapper;
import ssm.zmh.webchat.dao.mapper.user.UserMapper;
import ssm.zmh.webchat.exception.group.AddGroupException;
import ssm.zmh.webchat.exception.group.AddGroupMemberException;
import ssm.zmh.webchat.factory.SingletonGroupMemberBuilderFactory;
import ssm.zmh.webchat.factory.SingletonFriendFactory;
import ssm.zmh.webchat.listener.user.FriendListener;
import ssm.zmh.webchat.listener.user.GroupListener;
import ssm.zmh.webchat.model.request.ContactDeleteInBound;
import ssm.zmh.webchat.model.request.ContactSelectBound;
import ssm.zmh.webchat.model.request.ContactUpdate;
import ssm.zmh.webchat.model.request.GroupAddInBound;
import ssm.zmh.webchat.model.response.ContactQueryOutBound;
import ssm.zmh.webchat.model.response.GroupQueryOutBound;
import ssm.zmh.webchat.model.user.Friend;
import ssm.zmh.webchat.model.user.Group;
import ssm.zmh.webchat.model.user.GroupMember;
import ssm.zmh.webchat.model.user.User;

@Repository
public class ContactsService {
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    FriendListener friendListener;
    @Autowired
    GroupListener groupListener;

    public ContactsService() {
    }

    public List<User> getOnesFriendsList(int userId) {
        return this.friendMapper.getOnesAllFriends(userId);
    }

    public List<GroupQueryOutBound> getSelfGroupList(String userUUID) {
        return this.groupMapper.getSelfGroupList(userUUID);
    }

    public boolean addOnesContact(User userA, ContactSelectBound contactSelectBound) {
        User userB = this.userMapper.selectOnesId(contactSelectBound.getUserUUID());
        String targetType = contactSelectBound.getTargetType();
        boolean addResult;
        switch (targetType) {
            case "private":
                this.friendListener.beforeFriendInsert();
                Friend friend = SingletonFriendFactory.getFriendBuilder().build(userA.getUserId(), userB.getUserId());
                this.friendMapper.addOnesFriend(friend);
                addResult = friend.getFriendId() > 0;
                this.friendListener.afterFriendInsert(userA.getUserUUID(), userB.getUserUUID());
                break;
            case "group":
                GroupMember groupMember = SingletonGroupMemberBuilderFactory.getGroupMemberBuilder().build(userB.getUserUUID(), contactSelectBound.getGroupUUID(), "common");
                this.groupMapper.addGroupMember(groupMember);
                addResult = groupMember.getGroupMemberId() > 0;
                break;
            default:
                addResult = false;
        }

        return addResult;
    }

    public ContactQueryOutBound getContactsByTargetNumber(String targetNumber) {
        User user = this.userMapper.selectOneByTargetNumber(targetNumber);
        Group group = this.groupMapper.selectOneByTargetNumber(targetNumber);
        return new ContactQueryOutBound(user, group);
    }

    public User loadUserInfo(String userUUID) {
        return this.userMapper.selectOneByUserUUID(userUUID);
    }

    public Group loadGroupInfo(String groupUUID) {
        return this.groupMapper.selectOneByGroupUUID(groupUUID);
    }

    public void contactDelete(User userA, ContactDeleteInBound contactDeleteInBound) throws Exception {
        String targetType = contactDeleteInBound.getTargetType();

        switch (targetType) {
            case "private":
                this.friendMapper.deleteFriend(userA.getUserId(), contactDeleteInBound.getUserUUID());
                break;
            case "group":
                this.groupMapper.deleteGroupMember(userA.getUserUUID(), contactDeleteInBound.getGroupUUID());
        }

    }

    public static String createRoomDirPath(String... args) {
        if (args.length == 2) {
            return args[0].compareTo(args[1]) > 0 ? args[1] + "-" + args[0] : args[0] + "-" + args[1];
        } else {
            return args.length == 1 ? args[0] : null;
        }
    }

    public void updateUserInfo(ContactUpdate contactUpdate) throws Exception {
        this.userMapper.updateOneByUserUUID(contactUpdate);
    }

    public void updateGroupInfo(ContactUpdate contactUpdate) throws Exception {
        this.groupMapper.updateOneByGroupUUID(contactUpdate);
    }


    @Deprecated
    @Transactional(rollbackFor = AddGroupException.class)
    public void addGroupWithManyMembers(GroupAddInBound groupAddInBound) throws AddGroupException, AddGroupMemberException {
        Group group;
        try {
            group = new Group(groupAddInBound.getGroupName(), groupAddInBound.getGroupMax());
            this.groupMapper.insertOneGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddGroupException("创建组失败");
        }
        List<GroupMember> groupMembers = new ArrayList<>();
        List<String> userUUIDList = userMapper.selectManyByTargetNumber(groupAddInBound.getUserTargetNumbers());
        String groupHostUUID = userMapper.selectOneByTargetNumber(groupAddInBound.getGroupHost()).getUserUUID();
        for (String userUUID :
                userUUIDList) {
            if (groupHostUUID.equals(userUUID)) {
                groupMembers.add(SingletonGroupMemberBuilderFactory.getGroupMemberBuilder().build(userUUID, group.getGroupUUID(), "host"));
            } else {
                groupMembers.add(SingletonGroupMemberBuilderFactory.getGroupMemberBuilder().build(userUUID, group.getGroupUUID(), "common"));
            }
        }
        try {
            this.groupMapper.insertManyGroupMembers(groupMembers);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddGroupMemberException("添加组成员失败");
        }

    }


    @Transactional(rollbackFor = AddGroupException.class)
    public Group addGroup(GroupAddInBound groupAddInBound) throws AddGroupException {
        Group group;
        try {
            groupListener.beforeGroupInsert();
            group = new Group(groupAddInBound.getGroupName(), groupAddInBound.getGroupMax());
            this.groupMapper.insertOneGroup(group);
            if (group.getGroupId() == -1){
                throw new AddGroupException("创建组失败");
            }
            try {
                this.groupMapper.updateOne(new Group(group.getGroupId(), String.valueOf(100000000 + group.getGroupId())));
                group.setGroupTargetNumber(String.valueOf(100000000+group.getGroupId()));
                group.setCreateDate(Calendar.getInstance().getTime());

            }
            catch (Exception e){
                e.printStackTrace();
                throw new AddGroupException("设置组代码失败");
            }
            try{
                this.groupMapper.addGroupMember(SingletonGroupMemberBuilderFactory.getGroupMemberBuilder().build(groupAddInBound.getGroupHost(),group.getGroupUUID(),"host"));
            }
            catch (Exception e){
                e.printStackTrace();
                throw new AddGroupException("设置组长失败");
            }

            groupListener.afterGroupInsert(group.getGroupUUID());
            return group;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AddGroupException("创建组失败");
        }
    }
}
