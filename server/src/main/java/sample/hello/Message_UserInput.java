package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_UserInput extends  Message {

    private String userName;
    private String roomName;
    private String text;

    public Message_UserInput( String userName, String roomName,String text) {
        this.text = text;
        this.userName = userName;
        this.roomName = roomName;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }
}
