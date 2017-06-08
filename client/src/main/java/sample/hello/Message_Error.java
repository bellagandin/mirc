package sample.hello;

/**
 * Created by Bella on 6/8/2017.
 */
public class Message_Error {
    private String UserName;
    private String roomName;
    private String text;

    public Message_Error(String userName, String roomName, String text) {
        UserName = userName;
        this.roomName = roomName;
        this.text = text;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getText() {
        return text;
    }
}
