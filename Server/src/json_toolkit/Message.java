package json_toolkit;

import java.util.List;

public class Message {
    private String messageType;
    private List<Object> messageData;

    public Message() {}

    public Message(String messageType, List<Object> messageData) {
        this.messageType = messageType;
        this.messageData = messageData;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<Object> getMessageData() {
        return messageData;
    }

    public void setMessageData(List<Object> messageData) {
        this.messageData = messageData;
    }
}
