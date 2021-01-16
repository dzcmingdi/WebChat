package ssm.zmh.webchat.dao.mapper.user;

import org.apache.ibatis.annotations.*;
import ssm.zmh.webchat.model.user.Setting;

@Mapper
public interface SettingMapper {


    @Results(
            id = "settingResultMap",
            value = {
            @Result(property = "settingMessageUpload", column = "setting_message_upload"),
            @Result(property = "settingNotifyMessageCount", column = "setting_notify_message_count"),
    })
    @Select("select setting_message_upload,setting_notify_message_count from setting where userUUID=#{userUUID}")
    public Setting selectOnesSetting(@Param(value = "userUUID") String userUUID);


    @Update("update setting set  setting_message_upload=#{settingMessageUpload},setting_notify_message_count=#{settingNotifyMessageCount} where userUUID=#{userUUID}")
     void updateOnesSetting(Setting setting);
}
