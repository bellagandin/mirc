package sample.hello;

import akka.actor.ActorRef;

import java.util.ArrayList;

/**
 * Created by Roy on 01/06/2017.
 */
public class Message_JoinApproval extends Message {
    public String roomName;
    public String userROle;
    public ActorRef channelActorRef;
    public ArrayList<String> userList;
    public String roomTitle;
}
