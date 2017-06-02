package sample.hello;

import akka.actor.*;

import java.io.Serializable;


public class Server extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("Got a message from : "+ m.getUsername());
                    System.out.println("Sending to ServerUserActor the name of the Client to create new serverClient");
                    ActorSelection ServerUsersMain = getContext().actorSelection("/user/Server/ServerChannelMain");
                    ServerUsersMain.tell(m, getSender()); //It send a message to the serverClients with the user to create child actor

                        }
                )
                .build();

    }

    @Override
    public void preStart() {
        getContext().actorOf(Props.create(ServerUserMainActor.class), "ServerUsersMain");
        getContext().actorOf(Props.create(ServerChannelMainActor.class), "ServerChannelMain");

    }


}
