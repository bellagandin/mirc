package sample.hello;

/**
 * Created by Bella on 6/8/2017.
 */
public class Message_Disband extends Message {
    private String Owner;
    private String roomName;

    public Message_Disband(String owner, String roomName) {
        Owner = owner;
        this.roomName = roomName;
    }

    public String getUserName() {
        return Owner;
    }

    public String getRoomName() {
        return roomName;
    }
}
