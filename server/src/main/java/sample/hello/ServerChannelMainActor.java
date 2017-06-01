package sample.hello;

import akka.actor.*;

import java.util.logging.Logger;

/**
 * Created by Bella on 5/24/2017.
 */
public class ServerChannelMainActor extends AbstractActor {


    ExtensionFunction helper = new ExtensionFunction();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {


                    System.out.println("check if the channel exists: if exists add the user to the router. if not creates new router " + m.getChannel());
                    ActorSelection sel = context().actorSelection("akka://HelloWorldSystem/user/Server/ServerChannelMain/" + m.getChannel());
                    ActorRef r = helper.GetActorByName(sel);
                    if (r == null) {
                        System.out.println("there is no channel named:" + m.getChannel());
                        System.out.println("Creating new Channel");
                        r = getContext().actorOf(Props.create(ServerChannelActor.class, m.getChannel()), m.getChannel());

                    }
                    r.tell(m, getSender());


                })

                .build();
    }

//    private ActorRef GetActor(String Channel) {
//
//
//
//        Timeout t = new Timeout(5, TimeUnit.SECONDS);
//        AskableActorSelection asker = new AskableActorSelection(sel);
//        Future<Object> fut = asker.ask(Channel, t);
//        ActorRef ref = null;
//
//        try {
//
//            ref = (ActorRef) Await.result(fut, t.duration());
//        } catch (Exception e) {
//            System.out.println("here");
//        }
//
//        return ref;
//    }
}
