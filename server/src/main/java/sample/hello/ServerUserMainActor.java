package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

/**
 * Created by Bella on 5/24/2017.
 */
public class ServerUserMainActor extends AbstractActor {



    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    //It creates child actor under: /User/Server/ServerUsersMain/"username"
                    System.out.println("Creating new ServerUserActor for " + m.getUsername());
                    ActorRef child = this.getContext().actorOf(Props.create(ServerUserActor.class), m.getUsername());
                    m.setActorClient(getSender());
                    ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");

                    System.out.println("send to channel message newClient");

                    channels.tell(m, child);

                    //child.tell(m, getSender());
                        }
                ) .build();
    }


//    private ActorRef FindActor(String actor)
//    {
//        ActorSelection sel = context().actorSelection("akka://HelloWorldSystem/user/Server/");
//
//        Timeout t = new Timeout(5, TimeUnit.SECONDS);
//        AskableActorSelection asker = new AskableActorSelection(sel);
//        Future<Object> fut = asker.ask(actor, t);
//        ActorRef ref= null;
//
//        try {
//
//            ref = (ActorRef) Await.result(fut, t.duration());
//        }
//        catch (Exception e)
//        {
//            System.out.println("here");
//        }
//
//        return ref;
//    }
    }

