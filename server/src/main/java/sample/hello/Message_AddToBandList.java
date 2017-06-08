package sample.hello;

/**
 * Created by Bella on 6/8/2017.
 */
public class Message_AddToBandList extends Message {
    private String userName;
    private String roomName;

    public Message_AddToBandList(String userName, String roomName) {
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
