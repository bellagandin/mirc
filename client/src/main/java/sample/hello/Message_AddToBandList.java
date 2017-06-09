package sample.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 6/8/2017.
 */
public class Message_AddToBandList extends Message {
    private String userName;
    private String roomName;

    public String getUserOper() {
        return userOper;
    }

    private String userOper;

    public String getTimeStamp() {
        return timeStamp;
    }

    private String timeStamp;

    public Message_AddToBandList(String userName,String userOper, String roomName) {
        this.userName = userName;
        this.roomName = roomName;
        this.userOper = userOper;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeStamp = dateFormat.format(new Date());
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }
}
