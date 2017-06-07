package sample.hello;

/**
 * Created by Roy on 07/06/2017.
 */
public class Message_removeOp extends Message implements remove {
    private String userName;
    private String roomName;

    public Message_removeOp(String userName, String roomName) {
        this.userName = userName;
        this.roomName = roomName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }
}
