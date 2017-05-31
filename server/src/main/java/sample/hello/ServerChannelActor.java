package sample.hello;

import akka.actor.*;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerChannelActor extends AbstractActor {

    List<Routee> routees = new ArrayList<Routee>();
    Router router = new Router(new RoundRobinRoutingLogic(), routees);


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("Got message from " + getSender());

                    router = router.addRoutee(getSender());
                    String message = "[" + m.getTimeStamp() + "]*** joins: " + m.getUsername();
                    router.route(message, m.getActorClient());

                })
                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Got message from " + getSender());
                    router = router.removeRoutee(getSender());
                    String message = "[" + m.getTimeStamp() + "]*** parts: " + m.getUsername();
                    router.route(message, m.getActorClient());
                })
                .build();


    }
}
