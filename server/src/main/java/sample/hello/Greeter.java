package sample.hello;

import akka.actor.AbstractActor;
import java.io.Serializable;

enum Msg implements Serializable {
    GREET, DONE;
}

public class Greeter extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Msg.GREET, m -> {
                    System.out.println("Hello World!");
                    sender().tell(Msg.DONE, self());
                })
                .matchEquals(Msg.DONE, m -> {
                    System.out.println("Client Disconnected!");
                })
                .build();
    }
}
