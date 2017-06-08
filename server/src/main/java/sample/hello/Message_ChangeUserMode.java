package sample.hello;

import java.io.Serializable;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_ChangeUserMode extends Message {
    private String userName;
    private UserMode mode;
    private String rootName;


    public Message_ChangeUserMode(String userName, String rootName, UserMode mode) {
        this.userName = userName;
        this.mode = mode;
        this.rootName = rootName;
    }

    public UserMode getMode() {
        return mode;
    }

    public String getRoomName() {
        return rootName;
    }

    public String getUserName() {
        return userName;
    }

}
