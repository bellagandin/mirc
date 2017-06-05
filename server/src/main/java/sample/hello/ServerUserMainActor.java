package sample.hello;

import akka.actor.*;

/**
 * Created by Bella on 5/24/2017.
 */
public class ServerUserMainActor extends AbstractActor {
    private ExtensionFunction helper = new ExtensionFunction();


    @Override
    public Receive createReceive() {


        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    //It creates child actor under: /User/Server/ServerUsersMain/"username"


                    //find the userActor in exists
                    ActorSelection sel = context().actorSelection("akka://HelloWorldSystem/user/Server/ServerUsersMain/" + m.getUsername());
                    ActorRef res = helper.GetActorByName(sel);
                    if (res == null) {
                        ActorRef child = this.getContext().actorOf(Props.create(ServerUserActor.class, getSender()), m.getUsername());
                        System.out.println("Creating new ServerUserActor for " + m.getUsername());
                        System.out.println("send to child message newClient");
                        // ActorChannelMain.tell(m, child);
                        child.tell(m, getSender());
                    } else {
                        //ActorChannelMain.tell(m, res);
                        res.tell(m, getSender());
                    }

                        }
                ) .build();
    }


//    private ActorRef FindActor(String actor)
//    {
//        ActorSelection sel = context().actorSelection("akka://HelloWorldSystem/user/Server/");
//
//        Timeout t = new Timeout(5, TimeUnit.SECONDS);
//        AskableActorSelection asker = new AskableActorSelection(sel);
//        Future<Object> fut = asker.ask(actor, t);
//        ActorRef ref= null;
//
//        try {
//
//            ref = (ActorRef) Await.result(fut, t.duration());
//        }
//        catch (Exception e)
//        {
//            System.out.println("here");
//        }
//
//        return ref;
//    }
    }

