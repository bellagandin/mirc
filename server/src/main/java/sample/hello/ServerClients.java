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
public class ServerClients extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( Message_NewClient.class, (Message_NewClient m) -> {
                   //It creates child actor under: /User/Server/ServerClients/"username"
                    System.out.println("Creating new ServerClient for " + m.getUsername());
                    ActorRef child = this.getContext().actorOf(Props.create(ServerClient.class), m.getUsername());
                    m.setActorClient(child);
                    ActorSelection channels = getContext().actorSelection("/user/Server/Channels");
                  //  a1.tell("hello", ActorRef.noSender());
                    // ActorRef channels =  FindActor("Channels");
                    System.out.println("send to channel message newClient");
                    channels.tell(m,getSender());
                        }
                ) .build();
    }


    private ActorRef FindActor(String actor)
    {
        ActorSelection sel = context().actorSelection("akka://HelloWorldSystem/user/Server/");

        Timeout t = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> fut = asker.ask(actor, t);
        ActorRef ref= null;

        try {

            ref = (ActorRef) Await.result(fut, t.duration());
        }
        catch (Exception e)
        {
            System.out.println("here");
        }

        return ref;
    }
    }

