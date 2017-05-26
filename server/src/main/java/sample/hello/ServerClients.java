package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * Created by Bella on 5/24/2017.
 */
public class ServerClients extends AbstractActor {
    ServerClients (String s)
    {

    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( Message_NewClient.class, (Message_NewClient m) -> {
                   //It creates child actor under: /User/Server/ServerClients/"username"
                    System.out.println("Creating new ServerClient for "+m.getUsername());
                    ActorRef child = this.getContext().actorOf(Props.create(ServerClient.class, m.getUsername()));
                        }
                ) .build();
    }
    }

