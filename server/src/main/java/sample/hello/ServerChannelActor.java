package sample.hello;

import akka.actor.*;
import akka.routing.BroadcastRoutingLogic;

import akka.routing.Router;

import java.util.ArrayList;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerChannelActor extends AbstractActor {
    private String roomName;
    Router router;
    private String roomTitle;

    public ServerChannelActor(String roomName) {
        this.roomName = roomName;
        router = new Router(new BroadcastRoutingLogic());
    }



    @Override
    public Receive createReceive() {


        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("Got message from " + getSender());
                    if (router.routees().isEmpty()) {
                        Message_BecomeOwnerChannel type = new Message_BecomeOwnerChannel();
                        type.channelName = m.getChannel();
                        getSender().tell(type, self());
                    }
                    router = router.addRoutee(getSender());
                    String message = "[" + m.getTimeStamp() + "]*** joins: " + m.getUsername();
                    router.route(message, m.getActorClient());
                    //users.add(m.getUsername());
                    Message_JoinApproval respond = new Message_JoinApproval();
                    respond.roomName = roomName;
                    respond.channelActorRef = self();
                    ArrayList<String> users=null;
                    respond.userList = users;
                    sender().tell(respond, self());
                    broadcastMessage("User " + m.getUsername() + " Has joined!");

                })
                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Got message from " + getSender());
                    router = router.removeRoutee(getSender());

                    if (router.routees().isEmpty()) {
                        getContext().stop(self());
                    } else {
                        if (router.routees().size() == 1) {
                            Message_BecomeOwnerChannel msg = new Message_BecomeOwnerChannel();
                            msg.channelName = m.channel;
                            router.route(msg, self());
                        }
                        String message = "[" + m.timeStamp + "]*** parts: " + m.username;
                        router.route(message, m.client);
                    }
                })
                .build();
    }

    private void broadcastMessage(String message) {
        Message_Broadcast brod = new Message_Broadcast();
        brod.content = "< " + roomName + " > " + message;

        router.route(brod, self());


    }
}
