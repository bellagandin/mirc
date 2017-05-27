package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerClient extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(String.class, m -> {
            System.out.println(m);

           // sender().tell(Msg.DONE, self());
            //getContext().stop(self());
        })
                .build();
    }
}
