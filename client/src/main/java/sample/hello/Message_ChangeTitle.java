package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_ChangeTitle extends  Message {
    private String roomName;
    private String userName;

    public Message_ChangeTitle(String roomName, String userName) {
        this.roomName = roomName;
        this.userName = userName;
    }

    public String getRoomNAme() {
        return roomName;
    }

    public String getUserName() {
        return userName;
    }
}
