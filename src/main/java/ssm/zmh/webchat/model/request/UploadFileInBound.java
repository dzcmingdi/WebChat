package ssm.zmh.webchat.model.request;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileInBound {
    String fileType;
    MultipartFile multipartFile;
    String fromUserUUID;
    String toUserUUID;
    String groupUUID;
    String roomType;

    @Override
    public String toString() {
        return "UploadFileInBound{" +
                "fileType='" + fileType + '\'' +
                ", fromUserUUID='" + fromUserUUID + '\'' +
                ", toUserUUID='" + toUserUUID + '\'' +
                ", groupUUID='" + groupUUID + '\'' +
                ", roomType='" + roomType + '\'' +
                '}';
    }

    public String getGroupUUID() {
        return groupUUID;
    }

    public void setGroupUUID(String groupUUID) {
        this.groupUUID = groupUUID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getFromUserUUID() {
        return fromUserUUID;
    }

    public void setFromUserUUID(String fromUserUUID) {
        this.fromUserUUID = fromUserUUID;
    }

    public String getToUserUUID() {
        return toUserUUID;
    }

    public void setToUserUUID(String toUserUUID) {
        this.toUserUUID = toUserUUID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
