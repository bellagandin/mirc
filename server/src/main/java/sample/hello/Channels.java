package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.Router;

/**
 * Created by Bella on 5/24/2017.
 */
public class Channels extends AbstractActor {

    Channels (String s)
    {

    }
    Router router;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match( Message_NewClient.class, (Message_NewClient m) -> {
                            ActorRef r = this.getContext().actorOf(Props.create(Channels.class, m.getChannel()));
                            getContext().watch(r);
                            String message = m.getTimeStamp()+ "*** joins: "+ m.getUsername();
                    router.route(message, getSender());
                        }
                ) .build();
    }
}
