package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_AddUserToChannel extends  Message{
    private String roomNAme;
    private String userName;
    private String addedUser;
    private String listType;

    public Message_AddUserToChannel(String roomNAme, String userName, String addedUser, String listType) {
        this.roomNAme = roomNAme;
        this.userName = userName;
        this.addedUser = addedUser;
        this.listType = listType;
    }

    public String getRoomNAme() {
        return roomNAme;
    }
}
