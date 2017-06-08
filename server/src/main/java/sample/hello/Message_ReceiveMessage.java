package sample.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 6/3/2017.
 */
public class Message_ReceiveMessage extends Message {
    private String roomName;
    private String fromUser;
    private String toUser;
    private String text;
    private String timeStamp;

    public Message_ReceiveMessage(String roomName, String fromUser, String toUser, String text) {
        this.roomName = roomName;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.text = text;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeStamp = dateFormat.format(new Date());
    }

    @Override
    public String getUserName() {
        return null;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getText() {
        return text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
