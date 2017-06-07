package sample.hello;

/**
 * Created by Roy on 06/06/2017.
 */
public class Message_changeRole extends Message {
    private String username;
    private String Roomname;
    private UserMode changeTo;


    public Message_changeRole(String username, String roomname, UserMode changeRoleTo) {
        this.username = username;
        Roomname = roomname;
        this.changeTo=changeRoleTo;

    }

    public String getUsername() {
        return username;
    }

    public String getRoomname() {
        return Roomname;
    }

    public UserMode getChangeTo() {
        return changeTo;
    }
}
