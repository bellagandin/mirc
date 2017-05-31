package sample.hello;

import akka.actor.ActorRef;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_LeaveChannel {
    private String username;
    private String channel;
    private String timeStamp;
    private ActorRef client;

    Message_LeaveChannel(String usename, String channel) {
        this.username = usename;
        this.channel = channel;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeStamp = dateFormat.format(new Date());
    }

    public String getChannel() {
        return this.channel;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public String getUsername() {
        return this.username;
    }

    public void setActorClient(ActorRef actor) {
        this.client = actor;
    }

    public ActorRef getActorClient() {
        return this.client;
    }
}
