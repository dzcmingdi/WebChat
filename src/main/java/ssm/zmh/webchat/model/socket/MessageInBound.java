package ssm.zmh.webchat.model.socket;

public class MessageInBound {
    String message;
    String src;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
