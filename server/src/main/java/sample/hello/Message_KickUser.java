package sample.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_KickUser extends  Message{
    private String kicker;

    public String getKicker() {
        return kicker;
    }

    public String getRoomName() {
        return roomNAme;
    }

    public String getUserName() {
        return userName;
    }

    public String getToKick() {
        return ToKick;
    }

    private String roomNAme;
    private String userName;
    private String ToKick;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public boolean isHandeled() {
        return handeled;
    }

    private boolean handeled;

    public String getTimeStamp() {
        return timeStamp;
    }

    private String timeStamp = dateFormat.format(new Date());

    public Message_KickUser(String kicker, String roomNAme, String userName, String toKick,boolean handeled) {
        this.kicker = kicker;
        this.roomNAme = roomNAme;
        this.userName = userName;
        ToKick = toKick;
        this.handeled=handeled;

    }
}
