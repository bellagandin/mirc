package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_banUser extends  Message{
    private String roomNAme;
    private String userName;
    private String banUser;

    public Message_banUser(String roomNAme, String userName, String banUser) {
        this.roomNAme = roomNAme;
        this.userName = userName;
        this.banUser = banUser;
    }

    public String getRoomNAme() {
        return roomNAme;
    }

    public String getUserName() {
        return userName;
    }

    public String getBanUser() {
        return banUser;
    }
}
