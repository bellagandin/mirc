package sample.hello;

/**
 * Created by Roy on 01/06/2017.
 */
public class Message_Broadcast extends Message {
    private String roomName;
    private String content;

    public Message_Broadcast(String roomName, String content) {
        this.roomName = roomName;
        this.content = content;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getContent() {
        return content;
    }
}
