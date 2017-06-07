package sample.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Roy on 05/06/2017.
 */
public class Message_gotKicked extends Message {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    private String timeStamp = dateFormat.format(new Date());

    public String getKicker() {
        return kicker;
    }

    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getToKick() {
        return toKick;
    }

    public void setToKick(String toKick) {
        this.toKick = toKick;
    }

    private String kicker;
    private String roomName;

    public Message_gotKicked(String kicker, String roomName, String toKick) {
        this.kicker = kicker;
        this.roomName = roomName;
        this.toKick = toKick;
    }

    private String toKick;


}
