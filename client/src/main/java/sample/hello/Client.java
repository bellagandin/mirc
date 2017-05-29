package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import java.io.Serializable;

enum Msg implements Serializable {
    Join,Message,Leave,CreateNewChannel, DONE;
}

public class Client extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                    .match(String.class, m -> {
                        System.out.println(m);
                        sender().tell(Msg.DONE, self());
                        getContext().stop(self());
                    })

                .matchEquals(Msg.DONE, m -> {
                    // when the greeter is done, stop this actor and with it the application
                    sender().tell(Msg.DONE, self());
                    getContext().stop(self());
                })
                .build();
    }

    @Override
    public void preStart() {
        new GuiAppStart();
        ActorSelection greeter = getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server");
        Message_NewClient m =  new Message_NewClient("bella","1");

        greeter.tell(m, self());
    }
}
