package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_PermissionToChangeTitle extends Message {
    private String roomName;
    private String userName;
    private String newTitleName;

    public Message_PermissionToChangeTitle(String roomName, String userName, String newTitleName) {
        this.roomName = roomName;
        this.userName = userName;
        this.newTitleName = newTitleName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getNewTitleName() {
        return newTitleName;
    }
}
