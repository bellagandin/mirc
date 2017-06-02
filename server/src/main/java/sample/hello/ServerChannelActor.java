package sample.hello;

import akka.actor.*;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.*;

import java.util.ArrayList;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerChannelActor extends AbstractActor {
    private String roomName;
    Router router;
    private String roomTitle;
    private ArrayList<String> users;

    public ServerChannelActor(String roomName) {
        this.roomName = roomName;
        router = new Router(new BroadcastRoutingLogic());
        users = new ArrayList<String>();
    }



    @Override
    public Receive createReceive() {


        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("Got message from " + m.getUsername());

                    router = router.addRoutee(new ActorRefRoutee(sender()));
                    String message = "[" + m.getTimeStamp() + "]*** joins: " + m.getUsername();
                    users.add(m.getUsername());
                    Message_JoinApproval respond = new Message_JoinApproval();
                    respond.roomName = roomName;
                    respond.channelActorRef = self();
                    respond.userList = users;
                    sender().tell(respond, self());
                    broadcastMessage("User " + m.getUsername() + " Has joined!");

                })
                .match(Message_ChatMessage.class, msg -> {
                    System.out.println(msg.text);
                    broadcastMessage(msg.userName + ": " + msg.text);
                })
                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Got message from " + m.getUsername());
                    router = router.removeRoutee(getSender());

                    if (router.routees().isEmpty()) {
                        getContext().stop(self());
                    } else {
                        if (router.routees().size() == 1) {
                            Message_BecomeOwnerChannel msg = new Message_BecomeOwnerChannel();
                            msg.channelName = m.getChannel();
                            router.route(msg, self());
                        }
                        String message = "[" + m.getTimeStamp() + "]*** parts: " + m.getUsername();
                        router.route(message, m.getActorClient());
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
