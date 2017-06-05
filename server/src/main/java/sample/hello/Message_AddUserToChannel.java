package sample.hello;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_AddUserToChannel extends Message {
    private String roomName;
    private String userName;
    private UserMode userMode;
    private String addedUser;
    private UserMode listType;
    private Boolean wasHandled;

    public Message_AddUserToChannel(String roomName, String userName, UserMode userMode, String addedUser, UserMode listType, Boolean wasHandled) {
        this.roomName = roomName;
        this.userName = userName;
        this.userMode = userMode;
        this.addedUser = addedUser;
        this.listType = listType;
        this.wasHandled = wasHandled;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddedUser() {
        return addedUser;
    }

    public Boolean getWasHandled() {
        return wasHandled;
    }

    public UserMode getListType() {
        return listType;
    }

    public UserMode getUserMode() {
        return userMode;
    }
}
