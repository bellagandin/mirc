package sample.hello;

import akka.actor.ActorRef;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_LeaveChannel extends Message {
    String username;
    String channel;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String timeStamp = dateFormat.format(new Date());
    ActorRef client;


}
