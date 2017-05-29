package sample.hello;

import akka.actor.AbstractActor;


/**
 * Created by Bella on 5/26/2017.
 */
public class ServerClient extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .match(String.class, m -> {
            System.out.println(m);
            //ActorSelection channels = getContext().actorSelection("/user/Server/Channels");
           getSender().tell(m, self());
            //getContext().stop(self());
        })
                .build();
    }
}
