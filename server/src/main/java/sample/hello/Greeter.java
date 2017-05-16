package sample.hello;

import akka.actor.AbstractActor;
import java.io.Serializable;

enum Msg implements Serializable {
    Message, DONE;
}

public class Greeter extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
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
