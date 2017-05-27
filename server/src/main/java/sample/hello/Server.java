package sample.hello;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.routing.Router;
import akka.util.Timeout;
import scala.Array;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

enum Msg implements Serializable {
    Message, DONE;
}

public class Server extends AbstractActor {
    private ActorRef serverClients=getContext().actorOf(Props.create(ServerClients.class), "Clients");
    private ActorRef Channels=getContext().actorOf(Props.create(Channels.class), "Channels");


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( Message_NewClient.class, (Message_NewClient m) -> {
                    System.out.println("Got a message from : "+m.getUsername());
                    System.out.println("Sending to ServerClient the name of the Client to create new serverClient");
                    serverClients.tell(m,getSender()); //It send a message to the serverClients with the user to create child actor
                    //Channels.tell(m,getSender());

                    //sender().tell(Msg.DONE, self());//send the sender that we are done
                        }
                )
                .matchEquals(Msg.Message, m -> {
                    System.out.println("Hello World!");
                    sender().tell(Msg.DONE, self());
                })
                .matchEquals(Msg.DONE, m -> {
                    System.out.println("Client Disconnected!");
                })
                .build();

    }


}
