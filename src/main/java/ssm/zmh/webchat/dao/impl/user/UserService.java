//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.dao.impl.user;

import java.util.Calendar;
import java.util.List;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ssm.zmh.webchat.dao.mapper.user.SettingMapper;
import ssm.zmh.webchat.dao.mapper.user.UserMapper;
import ssm.zmh.webchat.exception.user.AddUserException;
import ssm.zmh.webchat.listener.user.UserListener;
import ssm.zmh.webchat.model.user.Setting;
import ssm.zmh.webchat.model.user.User;
import ssm.zmh.webchat.mongodb.MongoService;
import ssm.zmh.webchat.redis.jedisutils.JedisUserRegisterUtil;

@Repository
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MongoService mongoService;
    @Autowired
    UserListener userListener;
    @Autowired
    JedisUserRegisterUtil jedisUserRegisterUtil;
    @Autowired
    SettingMapper settingMapper;

    public UserService() {
    }

    public User getOneByUserId(int userId) {
        return this.userMapper.selectOneById(userId);
    }

    public User getOneByMail(String mail) {
        return this.userMapper.selectOneByMail(mail);
    }

    public List<User> getSomeByUserId(int userIdL, int userIdR) {
        return this.userMapper.selectBylr(userIdL, userIdR);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void insertUser(User user) throws AddUserException {
        this.userListener.beforeInsert(user);
        try {
            this.userMapper.insert(user);
            this.userMapper.updateOne(new User(user.getUserId(),String.valueOf(100000000 + user.getUserId())));
            user.setUserTargetNumber(String.valueOf(100000000 + user.getUserId()));
            user.setCreateDate(Calendar.getInstance().getTime());

            this.mongoService.initUserStorage(user.getUserUUID());
            this.userListener.afterInsert(user);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AddUserException("创建用户失败");
        }

    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void updateUser(User user) {
        this.userMapper.updateOne(user);
    }

    public User getUserFromRedis(String mail) {
        this.userListener.beforeRedisGet();
        User user = (User) this.jedisUserRegisterUtil.get(mail, User.class);
        this.userListener.afterRedisGet(user);
        return user;
    }

    public void setUserToRedis(User user) throws MessagingException {
        this.userListener.beforeRedisSet(user);
        this.jedisUserRegisterUtil.set(user.getMail(), user);
        this.userListener.afterRedisSet();
    }



    public void updateUserSetting(Setting setting) throws Exception{


        try {
            settingMapper.updateOnesSetting(setting);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("用户设置更新失败");
        }

    }

    public Setting getUserSetting(String userUUID) throws Exception{
        try {
            return settingMapper.selectOnesSetting(userUUID);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new Exception("获取用户设置失败");
        }
    }

}
