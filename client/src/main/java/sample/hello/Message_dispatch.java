package sample.hello;

import akka.actor.ActorRef;

/**
 * Created by Roy on 09/06/2017.
 */
public class Message_dispatch extends Message_LeaveChannel {
    public Message_dispatch(String username, String channel, ActorRef client, boolean kicked, boolean leaveALL) {
        super(username, channel, client, kicked, leaveALL);
    }
}
