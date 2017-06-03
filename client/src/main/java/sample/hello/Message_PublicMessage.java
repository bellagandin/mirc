package sample.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 6/3/2017.
 */
public class Message_PublicMessage extends Message {
    private String userName;
    private String roomName;
    private String text;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String timeStamp = dateFormat.format(new Date());

    public Message_PublicMessage(String userName, String roomName, String text) {
        this.userName = userName;
        this.roomName = roomName;
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getText() {
        return text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
