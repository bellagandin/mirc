package sample.hello;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinPool;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Bella on 5/24/2017.
 */
public class Channels extends AbstractActor {



    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_NewClient.class, (Message_NewClient m) -> {

                    //TODO: check if the channel exists: if exists add the user to the router. if not creates new router
                    System.out.println("check if the channel exists: if exists add the user to the router. if not creates new router " + m.getChannel());
                    ActorRef r = GetActor(m.getChannel());
                    if (r == null) {
                        System.out.println("the is no channel named:" + m.getChannel());
                        r = getContext().actorOf(Props.create(My_Channel.class), m.getChannel());

                    }
                      r.tell(m,getSender());


                })

                .match(String.class, m -> {
                            System.out.println(m);
                        }
                )
                .build();
    }

    private ActorRef GetActor(String Channel) {

        ActorSelection sel = context().actorSelection("akka://HelloWorldSystem/user/Server/Channels");

        Timeout t = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> fut = asker.ask(Channel, t);
        ActorRef ref = null;

        try {

            ref = (ActorRef) Await.result(fut, t.duration());
        } catch (Exception e) {
            System.out.println("here");
        }

        return ref;
    }
}
