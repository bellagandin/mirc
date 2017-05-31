package sample.hello;

import akka.actor.*;

import java.io.Serializable;

enum Msg implements Serializable {
    DONE;
}

public class Server extends AbstractActor {
    private final ActorRef serverClients = getContext().actorOf(Props.create(ServerUserMainActor.class), "Clients");
    private final ActorRef Channels = getContext().actorOf(Props.create(ServerChannelMainActor.class), "ServerChannelMainActor");



    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("Got a message from : "+ m.getUsername());
                    System.out.println("Sending to ServerUserActor the name of the Client to create new serverClient");
                    serverClients.tell(m,getSender()); //It send a message to the serverClients with the user to create child actor
                    //ServerChannelMainActor.tell(m,getSender());

                    //sender().tell(Msg.DONE, self());//send the sender that we are done
                        }
                )

                .matchEquals(Msg.DONE, m -> {
                    System.out.println("Client Disconnected!");
                })
                .build();

    }


}
