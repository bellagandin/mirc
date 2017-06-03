package sample.hello;

import akka.actor.ActorRef;

import java.util.ArrayList;

/**
 * Created by Roy on 01/06/2017.
 */
public class Message_JoinApproval extends Message {
    private String roomName;
    private String userROle;
    private ActorRef channelActorRef;
    private ArrayList<String> userList;
    private String roomTitle;

    public Message_JoinApproval(String roomName, String userROle, ActorRef channelActorRef, ArrayList<String> userList, String roomTitle) {
        this.roomName = roomName;
        this.userROle = userROle;
        this.channelActorRef = channelActorRef;
        this.userList = userList;
        this.roomTitle = roomTitle;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getUserROle() {
        return userROle;
    }

    public ActorRef getChannelActorRef() {
        return channelActorRef;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public String getRoomTitle() {
        return roomTitle;
    }
}
