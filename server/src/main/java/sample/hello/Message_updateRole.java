package sample.hello;

/**
 * Created by Roy on 07/06/2017.
 */
public class Message_updateRole extends Message {
    private UserMode Role;
    private String userName;
    private String roomNAme;

    public Message_updateRole(UserMode role, String userName, String roomNAme) {
        Role = role;
        this.userName = userName;
        this.roomNAme = roomNAme;
    }

    public UserMode getRole() {
        return Role;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomNAme() {
        return roomNAme;
    }
}
