package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;


import java.io.Serializable;
import java.util.Scanner;


public class Client extends AbstractActor {
    ActorSelection greeter = null;
    String username;
    Scanner sc;



    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, m -> {
                    System.out.println(m);
                    //Message_LeaveChannel l = new Message_LeaveChannel("bella", "1");
                    // greeter =  getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server/Clients/bella");
                    //greeter.tell(l, self());
                    getContext().stop(self());

                })
                .match(Message_JoinApproval.class, m ->{
                    System.out.println(username+" Seccessfully joined channel"+m.roomName);
                    String msg=sc.next();



                })
                .match(Message_Broadcast.class, mgs -> {
                    System.out.println(mgs.content);
                })

                .build();
    }


    @Override
    public void preStart() {
        sc=new Scanner(System.in);
        username="Roy";
        greeter = getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server");
        Message_JoinClient m = new Message_JoinClient(username, "room_1");

        greeter.tell(m, self());
    }
}
