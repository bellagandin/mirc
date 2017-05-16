package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import java.io.Serializable;

enum Msg implements Serializable {
    GREET, DONE;
}

public class RegularUser extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Msg.DONE, m -> {
                    // when the greeter is done, stop this actor and with it the application
                    sender().tell(Msg.DONE, self());
                    getContext().stop(self());
                })
                .build();
    }

    @Override
    public void preStart() {
        ActorSelection greeter = getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:21/user/Greeter");
        greeter.tell(Msg.GREET, self());
    }
}
