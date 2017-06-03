package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import java.util.Hashtable;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerUserActor extends AbstractActor {
    private Hashtable table = new Hashtable();
    private ActorRef  connectdClient;
    private ExtensionFunction helper;

    public ServerUserActor(ActorRef act){
        connectdClient=act;
        helper = new ExtensionFunction();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
//                .match(String.class, m -> {
//                    System.out.println("Got message to sent to cleint :" + m);
//                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
//                    getSender().tell(m, self());
//                    //getContext().stop(self());
//                })

                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    table.put(m.getChannel(), UserMode.OWNER);
                    Message_ChangeUserMode usermode = new Message_ChangeUserMode();
                    usermode.mode = UserMode.OWNER;
                    connectdClient.tell(usermode, self());
                    //m.getActorClient().tell(self(),self());

                })
                .match(Message_LeaveChannel.class, msg -> {
                    System.out.println("Got leave message from " + msg.username);
                    table.remove(msg.channel);
                    ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.channel);
                    msg.client = connectdClient;
                    channelActor.tell(msg, self());

                })
                .match(Message_ChangeUserMode.class, msg -> table.put(msg.rootName, UserMode.OWNER))

                .match(Message_Broadcast.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectdClient.tell(msg, self());

                })

//                .match(Message_Broadcast.class, msg -> {
//                    System.out.println("Got message to sent to client :" + msg);
//                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
//                    connectdClient.tell(msg, self());
//
//                })
                .match(Message_JoinApproval.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectdClient.tell(msg, self());

                })
                .match(Message_CloseChannel.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectdClient.tell(msg, self());
                })
                .match(Message_PrivateMessage.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg.getRoomName());
                    ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getSpecificUserName());
                    String message = "[" + msg.getTimeStamp() + "] < " + msg.getSpecificUserName() + "> " + msg.getText();
                    Message_ReceiveMessage rec = new Message_ReceiveMessage(
                            msg.getRoomName(), "", msg.getSpecificUserName(), msg.getText());
                    toSend.tell(rec, self());
                })
                .match(Message_PublicMessage.class, msg -> {
                    System.out.println("Got message to sent to client <Message_PublicMessage> :" + msg);
                    ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                    channelActor.tell(msg, getSender());

                })
                .match(Message_ReceiveMessage.class, m -> {
                    System.out.println(m);
                    connectdClient.tell(m, self());

                })
                .build();
    }
}
