package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;


import java.io.Serializable;

enum Msg implements Serializable {
    DONE;
}

public class Client extends AbstractActor {
    ActorSelection greeter = null;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, m -> {
                    System.out.println(m);
                    Message_LeaveChannel l = new Message_LeaveChannel("bella", "1");
                    // greeter =  getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server/Clients/bella");
                    //greeter.tell(l, self());
                    sender().tell(Msg.DONE, self());
                    getContext().stop(self());

                })

                .match(Message_ChangeUserMode.class, mgs -> {
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

        greeter = getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server");
        Message_JoinClient m = new Message_JoinClient("bella", "1");

        greeter.tell(m, self());
    }
}
