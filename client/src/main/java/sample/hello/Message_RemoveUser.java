package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_RemoveUser extends Message {
    private String roomName;
    private String userName;
    private UserMode userMode;
    private String delUser;
    private UserMode listType;

    public Message_RemoveUser(String roomName, String userName, UserMode userMode, String delUser, UserMode listType) {
        this.roomName = roomName;
        this.userName = userName;
        this.userMode = userMode;
        this.delUser = delUser;
        this.listType = listType;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getUserName() {
        return userName;
    }

    public String getDelUser() {
        return delUser;
    }

    public UserMode getListType() {
        return listType;
    }

    public UserMode getUserMode() {
        return userMode;
    }
}
