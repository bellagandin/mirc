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
    ArrayList<String> users;

    public ServerChannelActor(String roomName) {
        this.roomName = roomName;
        router = new Router(new BroadcastRoutingLogic());
        users = new ArrayList<String>();
        roomTitle = roomName;
    }



    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("Got message from " + getSender());
                    if (router.routees().isEmpty()) {
                        System.out.println("The room is empty.");
                        Message_ChangeUserMode type = new Message_ChangeUserMode();
                        type.rootName = m.getChannel();
                        getSender().tell(type, self());
                        System.out.println("Sending to User change in UserMode.");
                    }

                    String message = "[" + m.getTimeStamp() + "]*** joins: " + m.getUsername();
                   // router.route(message, m.getActorClient());

                    Message_JoinApproval respond = new Message_JoinApproval();
                    respond.roomName = roomName;
                    respond.channelActorRef = self();

                    respond.userList = users;
                    respond.roomTitle = roomName;
                    sender().tell(respond, self());
                    users.add(m.getUsername());

                    router = router.addRoutee(getSender());
                    broadcastMessage(message, m.getActorClient());
                    Message_JoinAnnunce msg = new Message_JoinAnnunce();
                    msg.roomName = m.getChannel();
                    router.route(msg, self());

                })
                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Got message from " + getSender());
                    router = router.removeRoutee(getSender());
                    if (router.routees().isEmpty()) {
                        Message_CloseChannel msg = new Message_CloseChannel();
                        msg.roomName = m.channel;
                        getSender().tell(msg, self());
                        getContext().stop(self());
                    } else {
                        if (router.routees().size() == 1) {
                            Message_ChangeUserMode msg = new Message_ChangeUserMode();
                            msg.rootName = m.channel;
                            router.route(msg, self());
                        }
                        String message = "[" + m.timeStamp + "]*** parts: " + m.username;
                        broadcastMessage(message, m.client);
                    }
                })
                .build();
    }

    private void broadcastMessage(String message,ActorRef sender) {
        Message_Broadcast brod = new Message_Broadcast();
        brod.content = message;
        System.out.println("Sending with broadcast");
        router.route(brod, sender);


    }
}
