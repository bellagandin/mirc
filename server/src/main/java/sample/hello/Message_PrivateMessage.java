package sample.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Roy on 01/06/2017.
 */
public class Message_PrivateMessage extends Message {
    private String text;
    private String roomName;
    private String specificUserName;
    private String sender;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String timeStamp = dateFormat.format(new Date());

    public Message_PrivateMessage(String text, String roomName, String sender, String userName) {
        this.text = text;
        this.roomName = roomName;
        this.specificUserName = userName;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public String getRoomName() {
        return roomName;
    }


    public String getTimeStamp() {
        return timeStamp;
    }

    public String getSpecificUserName() {
        return specificUserName;
    }

    public String getSender() {
        return sender;
    }
}
