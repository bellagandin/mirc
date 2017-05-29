package sample.hello;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bella on 5/26/2017.
 */
public class My_Channel extends AbstractActor{

    List<Routee> routees = new ArrayList<Routee>();
    Router router = new Router(new RoundRobinRoutingLogic(),routees );


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( Message_NewClient.class, (Message_NewClient m) -> {
                    System.out.println(getSender());

                    router=router.addRoutee(m.getActorClient());
                    String message ="["+ m.getTimeStamp()+ "]*** joins: "+ m.getUsername();
                    router.route(message, getSender());

    })
            .build();


    }
}
