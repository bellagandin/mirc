package sample.hello;

/**
 * Created by Bella on 6/3/2017.
 */
public class Message_ChangeTitle extends Message {
    private String roomName;
    private String userName;
    private String newTitleName;

    public Message_ChangeTitle(String roomName, String userName, String newTitleName) {
        this.roomName = roomName;
        this.userName = userName;
        this.newTitleName = newTitleName;
    }


    public String getRoomName() {
        return roomName;
    }

    public String getUserName() {
        return userName;
    }

    public String getNewTitleName() {
        return newTitleName;
    }
}
