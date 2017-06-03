package sample.hello;

import java.io.Serializable;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_ChangeUserMode extends Message {
    public Message_ChangeUserMode(UserMode mode, String rootName) {
        this.mode = mode;
        this.rootName = rootName;
    }

    private UserMode mode;
    private String rootName;


    public UserMode getMode() {
        return mode;
    }

    public String getRootName() {
        return rootName;
    }
}
