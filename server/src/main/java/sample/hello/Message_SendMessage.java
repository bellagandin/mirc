package sample.hello;

import akka.actor.ActorRef;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_SendMessage {
    private String message;
    private String username;
    private String channelname;
    private ActorRef Client;
    private String timeStamp;

    Message_SendMessage(String username, String channelname) {
        this.username = username;
        this.channelname = channelname;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timeStamp = dateFormat.format(new Date());
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public String getChannel() {
        return this.channelname;
    }

    public void setActorClient(ActorRef Client) {
        this.Client = Client;
    }

    public ActorRef getActorClient() {
        return this.Client;
    }

    public String getUsername() {
        return this.username;
    }

    public String getMessage() {
        return this.message;
    }
}
