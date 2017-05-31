package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;

import java.util.Hashtable;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerUserActor extends AbstractActor {
    private Hashtable table = new Hashtable();


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, m -> {
                    System.out.println("Got message to sent to cleint :" + m);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMainActor");
                    getSender().tell(m, self());
                    //getContext().stop(self());
                })

                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    table.put(m.getChannel(), UserMode.OWNER);
                    Message_ChangeUserMode usermode = new Message_ChangeUserMode(UserMode.OWNER);
                    m.getActorClient().tell(usermode, self());
                    //m.getActorClient().tell(self(),self());

                })
                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Got leave message from " + m.getUsername());
                    table.remove(m.getChannel());
                    ActorSelection channel = getContext().actorSelection("/user/Server/ServerChannelMainActor" + m.getChannel());
                    m.setActorClient(getSender());
                    channel.tell(m, self());

                })
                .match(Message_SendMessage.class, mgs -> {
                    ActorSelection channel = getContext().actorSelection("/user/Server/ServerChannelMainActor" + mgs.getChannel());


                })
                .match(Message_BecomeOwnerChannel.class, msg -> table.put(msg.channelName, UserMode.OWNER))
                .build();
    }
}
