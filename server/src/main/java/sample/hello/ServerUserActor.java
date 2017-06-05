package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;

import java.util.Hashtable;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerUserActor extends AbstractActor {
    private Hashtable table = new Hashtable();
    private ActorRef connectdClient;
    private ExtensionFunction helper;

    public ServerUserActor(ActorRef act) {
        connectdClient = act;
        helper = new ExtensionFunction();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("ServerUserActor: Got a message from UserMainActor from " + m.getUsername());
                    String actorName = m.getUsername() + "&" + m.getRoomName();
                    ActorRef userChannelActor = getContext().actorOf(Props.create(ServerUserChannelActor.class, getSender(), self()), actorName);
                    System.out.println("ServerUserActor: Done creating");
                    table.put(m.getRoomName(), userChannelActor);
                    System.out.println("ServerUserActor: Done putting");
                    userChannelActor.tell(m, self());
                    //// sending the Client his type
                    //Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                    //connectdClient.tell(userMode, self());

                })
                .match(Message_LeaveChannel.class, msg -> {
                    System.out.println("Got leave message from " + msg.getUsername());
                    table.remove(msg.getChannel());
                    ActorRef ref = (ActorRef) table.get(msg.getChannel());
                    ref.tell(msg, self());
                })
                //receive from the Channel
                .match(Message_JoinApproval.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    connectdClient.tell(msg, self());

                })
                .match(Message_CloseChannel.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    connectdClient.tell(msg, self());
                })
                .match(Message_UpdateList.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectdClient.tell(msg, self());
                })
                .match(Message_PrivateMessage.class, msg -> {
                    System.out.println("UserActor: Got message to sent to client :" + msg.getRoomName());
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .match(Message_PublicMessage.class, msg -> {
                    System.out.println("Got message to sent to client <Message_PublicMessage> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());

                })
                .match(Message_PermissionToChangeTitle.class, msg -> {
                    System.out.println("Got message to sent to client <Message_PermissionToChangeTitle> :" + msg);

                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());


                })
                .match(Message_ChangeTitle.class, msg -> {

                    System.out.println("UserActor: Got message to sent to client <Message_ChangeTitle> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
//                .match(Message_AddUserToChannel.class, msg -> {
//                    System.out.println("UserActor: Got message to sent to client <Message_AddUserToChannel> :" + msg);
//                    ActorRef ref = (ActorRef)table.get(msg.getRoomName());
//                    ref.tell(msg, self());
//
//                })
                .match(Message_ChangeUserMode.class, msg -> {
                    System.out.println("UserActor: Got message to sent to client <Message_ChangeUserMode> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRootName());
                    if (ref != null) {
                        ref.tell(msg, self());
                    }
                })
//                .match(Message_RemoveUser.class, msg -> {
//                    System.out.println("Got message to sent to client <Message_RemoveUser> :" + msg);
//                    Object obj = table.get(msg.getRoomName());
//                    //if (obj != null && (obj !=UserMode.USER|| obj!=UserMode.VOICE) ){
//                    connectdClient.tell(msg, self());
//                    //  }
//                })
                .match(Message_ReceiveMessage.class, m -> {
                    System.out.println(m);
                    connectdClient.tell(m, self());

                })
                .build();
    }
}
