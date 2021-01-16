package ssm.zmh.webchat.model.user;

public class Setting {

    private boolean settingMessageUpload;
    private long settingNotifyMessageCount;
    private String userUUID;


    public boolean isSettingMessageUpload() {
        return settingMessageUpload;
    }

    public void setSettingMessageUpload(boolean settingMessageUpload) {
        this.settingMessageUpload = settingMessageUpload;
    }

    public long getSettingNotifyMessageCount() {
        return settingNotifyMessageCount;
    }

    public void setSettingNotifyMessageCount(long settingNotifyMessageCount) {
        this.settingNotifyMessageCount = settingNotifyMessageCount;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "settingMessageUpload=" + settingMessageUpload +
                ", settingNotifyMessageCount=" + settingNotifyMessageCount +
                ", userUUID='" + userUUID + '\'' +
                '}';
    }
}
