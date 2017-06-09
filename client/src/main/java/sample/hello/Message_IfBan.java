package sample.hello;

/**
 * Created by Bella on 6/9/2017.
 */
public class Message_IfBan extends Message {
    private String userName;
    private String roomName;
    private boolean isBan;

    public Message getMsg() {
        return msg;
    }

    private Message msg;

    public Message_IfBan(String userName, String roomName, Boolean isBan, Message msg) {
        this.userName = userName;
        this.roomName = roomName;
        this.isBan = isBan;
        this.msg = msg;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    public boolean isBan() {
        return isBan;
    }

}
