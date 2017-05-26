package sample.hello;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.routing.Router;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

enum Msg implements Serializable {
    Message, DONE;
}

public class Server extends AbstractActor {
    private ActorRef serverClients = getContext().actorOf(Props.create(ServerClients.class, "Clients"));
    private ActorRef Routers = getContext().actorOf(Props.create(Channels.class, "Channels"));
    Router router;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( Message_NewClient.class, (Message_NewClient m) -> {
                    System.out.println("Got a message from : "+m.getUsername());
                    System.out.println("Sending to ServerClient the name of the Client to create new serverClient");
                    serverClients.tell(m,getSender()); //It send a message to the serverClients with the user to create child actor
                   //TODO: check if the channel exists: if exists add the user to the router. if not creates new router
                    System.out.println("check if the channel exists: if exists add the user to the router. if not creates new router");
                    if (ExistActor(m.getChannel())) {

                    }
                    sender().tell(Msg.DONE, self());//send the sender that we are done
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

    private Boolean ExistActor (String Channel) throws Exception {

        ActorSelection sel = context().actorSelection("/user/Server/Channels");

        Timeout t = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> fut = asker.ask(Channel, t);
        try {
            ActorIdentity ident = (ActorIdentity) Await.result(fut, t.duration());

            ActorRef ref = ident.getRef();
        }
        catch (Exception e)
        {
            System.out.println("here");
        }



        return true;
    }
}
