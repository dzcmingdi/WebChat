package ssm.zmh.webchat.model.request;

import java.util.List;

public class GroupAddInBound {
    String groupName;
    int groupMax;
    List<String> userTargetNumbers;
    String groupHost;

    public String getGroupHost() {
        return groupHost;
    }

    public void setGroupHost(String groupHost) {
        this.groupHost = groupHost;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupMax() {
        return groupMax;
    }

    public void setGroupMax(int groupMax) {
        this.groupMax = groupMax;
    }

    public List<String> getUserTargetNumbers() {
        return userTargetNumbers;
    }

    public void setUserTargetNumbers(List<String> userTargetNumbers) {
        this.userTargetNumbers = userTargetNumbers;
    }
}
