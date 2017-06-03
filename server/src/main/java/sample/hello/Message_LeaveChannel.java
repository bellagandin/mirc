package sample.hello;

import akka.actor.ActorRef;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_LeaveChannel extends Message {
    public Message_LeaveChannel(String username, String channel,ActorRef client) {
        this.username = username;
        this.channel = channel;
        this.dateFormat = dateFormat;
        this.timeStamp = timeStamp;
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public String getChannel() {
        return channel;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public ActorRef getClient() {
        return client;
    }

    private  String username;
    private String channel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String timeStamp = dateFormat.format(new Date());
    private ActorRef client;


}
