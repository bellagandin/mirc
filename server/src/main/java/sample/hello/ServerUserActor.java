package sample.hello;

import akka.actor.*;

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
                    System.out.println("ServerUserActor: Message_JoinClient: Got a message from UserMainActor from " + m.getUserName());
                    String actorName = m.getUserName() + "&" + m.getRoomName();
                    ActorRef userChannelActor = getContext().actorOf(Props.create(ServerUserChannelActor.class, getSender(), self()), actorName);
                    System.out.println("ServerUserActor: Message_JoinClient: Done creating");
                    table.put(m.getRoomName(), userChannelActor);
                    System.out.println("ServerUserActor: Message_JoinClient: Done putting");
                    userChannelActor.tell(m, self());


                    //// sending the Client his type
                    //Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUserName(), UserMode.OWNER);
                    //connectdClient.tell(userMode, self());

                })
                .match(Message_LeaveChannel.class, msg -> {
                    System.out.println("UserActor : Message_LeaveChannel: Got leave message from " + msg.getUserName());
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    table.remove(msg.getRoomName());
                    ref.tell(msg, self());
                })
                //receive from the Channel
                .match(Message_JoinApproval.class, msg -> {
                    System.out.println("UserActor :Message_JoinApproval :Got message to sent to client :" + msg);
                    connectdClient.tell(msg, self());

                })
                .match(Message_changeRole.class, msg -> {
                    System.out.println("UserActor :Message_changeRole :Got message to sent to client :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .match(Message_removeOp.class, msg -> {
                    System.out.println("UserActor :Message_removeOp :Got message to sent to client :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .match(Message_removeVoice.class, msg -> {
                    System.out.println("UserActor :Message_removeVoice :Got message to sent to client :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .match(Message_changeRole.class, msg -> {
                    System.out.println("UserActor :Message_changeRole :Got message to sent to client :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .match(Message_CloseChannel.class, msg -> {
                    System.out.println("UserActor : Message_CloseChannel : Got message to sent to client :" + msg);
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
                .match(Message_updateRole.class, msg -> {
                    System.out.println("Got message to sent to client <Message_updateRole> :" + msg);
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
                .match(Message_gotKicked.class, msg -> {
                    System.out.println("Message_gotKicked: Got message to sent to client :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());


                })
                .match(Message_KickUser.class, msg -> {

                    System.out.println("UserActor: Got message to kick to client <Message_kickuser> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .match(Message_ChangeUserMode.class, msg -> {
                    System.out.println("UserActor: Got change userMode message to sent to client <Message_ChangeUserMode> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    if (ref != null) {
                        ref.tell(msg, self());
                    }
                })
                .match(Message_AddToBandList.class, msg -> {
                    System.out.println("UserActor: Message_AddToBandList : Got change userMode message to sent to client <Message_ChangeUserMode> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    if (ref != null) {
                        ref.tell(msg, self());
                    }
                })
                .match(Message_ReceiveMessage.class, m -> {
                    System.out.println(m);
                    connectdClient.tell(m, self());

                })
                .match(Message_Disband.class, msg -> {
                    System.out.println("UserActor: Got message to kick to client <Message_disband> :" + msg);
                    ActorRef ref = (ActorRef) table.get(msg.getRoomName());
                    ref.tell(msg, self());
                })
                .build();
    }
}
