package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_ChangeTitle extends  Message {
    private String roomNAme;
    private String userName;

    public Message_ChangeTitle(String roomNAme, String userName) {
        this.roomNAme = roomNAme;
        this.userName = userName;
    }

    public String getRoomNAme() {
        return roomNAme;
    }

    public String getUserName() {
        return userName;
    }
}
