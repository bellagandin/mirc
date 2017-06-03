package sample.hello;

import java.util.List;

/**
 * Created by Bella on 6/2/2017.
 */
public class Message_UpdateList extends Message {

    private String roomName;
    private List<String> userList;

    public Message_UpdateList(String roomName, List<String> userList) {
        this.roomName = roomName;
        this.userList = userList;
    }




    public String getRoomName() {
        return roomName;
    }
    public List<String> getUserList() {
        return userList;
    }


}
