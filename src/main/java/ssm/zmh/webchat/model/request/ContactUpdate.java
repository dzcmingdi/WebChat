package ssm.zmh.webchat.model.request;

public class ContactUpdate {
    String userName;
    String password;
    String targetType;
    String groupName;
    String groupMaxMembers;
    String groupUUID;
    String userUUID;

    @Override
    public String toString() {
        return "ContactUpdate{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", targetType='" + targetType + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupMaxMembers='" + groupMaxMembers + '\'' +
                ", groupUUID='" + groupUUID + '\'' +
                ", userUUID='" + userUUID + '\'' +
                '}';
    }

    public String getGroupUUID() {
        return groupUUID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupMaxMembers() {
        return groupMaxMembers;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupMaxMembers(String groupMaxMembers) {
        this.groupMaxMembers = groupMaxMembers;
    }

    public void setGroupUUID(String groupUUID) {
        this.groupUUID = groupUUID;
    }
}
