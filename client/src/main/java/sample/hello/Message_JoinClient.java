package sample.hello;

import akka.actor.ActorRef;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 5/16/2017.
 */


public class Message_JoinClient extends Message {
    private String username;
    private String roomName;
    private String timeStamp;
    private ActorRef client;
    private boolean isFirst;


    Message_JoinClient(String username, String channel, boolean isFirst) {
        this.username = username;
        this.roomName = channel;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeStamp = dateFormat.format(new Date());
        this.isFirst = isFirst;
    }

    public String getUsername() {
        return this.username;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }


    public void setActorClient(ActorRef actor) {
        this.client = actor;
    }

    public ActorRef getActorClient() {
        return this.client;
    }

    public boolean getIsFirst() {
        return this.isFirst;
    }


}