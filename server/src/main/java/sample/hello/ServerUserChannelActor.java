package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;

/**
 * Created by Bella on 6/5/2017.
 */
public class ServerUserChannelActor extends AbstractActor {
    private AbstractActor.Receive User;
    private AbstractActor.Receive Voice;
    private AbstractActor.Receive Operator;
    private AbstractActor.Receive Banned;
    private AbstractActor.Receive Owner;

    private ActorRef connectorClient;
    private ActorRef parentUserActor;
    private ExtensionFunction helper;

    public ServerUserChannelActor(ActorRef client, ActorRef act) {
        this.connectorClient = client;
        this.parentUserActor = act;
        this.helper = new ExtensionFunction();

        Owner = receiveBuilder()
                .match(Message_ChangeUserMode.class, msg ->
                {
                    switch (msg.getMode()) {
                        case USER:
                            getContext().become(User);
                            break;
                        case VOICE:
                            break;
                        case OPERATOR:
                            break;
                        case OWNER:
                            getContext().become(Owner);
                            break;
                        case BANNED:
                            break;
                    }
                })
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    // send Client his mode
                    // Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                    //connectorClient.tell(userMode, self());
                    ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
                    channelMainActor.tell(m, self());

                })
                .match(Message_LeaveChannel.class, msg -> {
                    System.out.println("Got leave message from " + msg.getUsername());
                    //table.remove(msg.getChannel());
                    parentUserActor.tell(msg, self());
                    ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getChannel());
                    channelActor.tell(msg, self());

                })
                .match(Message_JoinApproval.class, msg -> {
                    System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectorClient.tell(msg, self());

                })
                .match(Message_CloseChannel.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectorClient.tell(msg, self());
                })
                .match(Message_UpdateList.class, msg -> {
                    System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectorClient.tell(msg, self());
                })
                .match(Message_PrivateMessage.class, msg -> {
                    System.out.println("UserChannelActor:<owner> Got message to sent to client :" + msg.getRoomName());
                    ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getSpecificUserName());
                    String message = "[" + msg.getTimeStamp() + "] < " + msg.getSender() + "> " + msg.getText();
                    Message_ReceiveMessage rec = new Message_ReceiveMessage(
                            msg.getRoomName(), msg.getSender(), msg.getSpecificUserName(), message);
                    toSend.tell(rec, self());
                    connectorClient.tell(rec, self());
                })
                .match(Message_PublicMessage.class, msg -> {
                    System.out.println("Got message to sent to client <Message_PublicMessage> :" + msg);
                    ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                    channelActor.tell(msg, getSender());

                })
                .match(Message_PermissionToChangeTitle.class, msg -> {
                    System.out.println("UserChannel: Owner: Got message to sent to client <Message_PermissionToChangeTitle> :" + msg);

                    ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                    channelActor.tell(msg, getSender());

                })
                .match(Message_ChangeTitle.class, msg -> {

                    System.out.println(" UserChannel: Owner: Got message to sent to client <Message_ChangeTitle> :" + msg);
                    //ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                    connectorClient.tell(msg, getSender());

                })
                .match(Message_ReceiveMessage.class, m -> {
                    System.out.println(m);
                    connectorClient.tell(m, self());

                })
                .match(Message_AddUserToChannel.class, msg -> {
                    System.out.println("UserChannelActor:<owner> Got message to sent <Message_AddUserToChannel> in room:" + msg.getRoomName());
                    Message_ChangeUserMode rec = new Message_ChangeUserMode(msg.getUserName(), msg.getRoomName(), msg.getListType());
                    ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                    channelActor.tell(rec, getSender());
                })
                .build();
        //end Owner
        Voice =
                receiveBuilder()
                        .match(Message_ChangeUserMode.class, msg ->
                        {
                            switch (msg.getMode()) {
                                case USER:
                                    getContext().become(User);
                                    break;
                                case VOICE:
                                    break;
                                case OPERATOR:
                                    break;
                                case OWNER:
                                    getContext().become(Owner);
                                    break;
                                case BANNED:
                                    break;
                            }
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            // send Client his mode
                            // Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                            //connectorClient.tell(userMode, self());
                            ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
                            channelMainActor.tell(m, self());

                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            System.out.println("Got leave message from " + msg.getUsername());
                            //table.remove(msg.getChannel());
                            parentUserActor.tell(msg, self());
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getChannel());
                            channelActor.tell(msg, self());

                        })
                        .match(Message_JoinApproval.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());

                        })
                        .match(Message_CloseChannel.class, msg -> {
                            System.out.println("Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_UpdateList.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_PrivateMessage.class, msg -> {
                            System.out.println("UserChannelActor: Got message to sent to client :" + msg.getRoomName());
                            ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getSpecificUserName());
                            String message = "[" + msg.getTimeStamp() + "] < " + msg.getSpecificUserName() + "> " + msg.getText();
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(
                                    msg.getRoomName(), "", msg.getSpecificUserName(), message);
                            toSend.tell(rec, self());
                            connectorClient.tell(rec, self());
                        })
                        .match(Message_PublicMessage.class, msg -> {
                            System.out.println("Got message to sent to client <Message_PublicMessage> :" + msg);
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            channelActor.tell(msg, getSender());

                        })
                        .match(Message_PermissionToChangeTitle.class, msg -> {
                            System.out.println("UserChannel: Owner: Got message to sent to client <Message_PermissionToChangeTitle> :" + msg);

                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            channelActor.tell(msg, getSender());

                        })
                        .match(Message_ChangeTitle.class, msg -> {

                            System.out.println("Got message to sent to client <Message_ChangeTitle> :" + msg);
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            channelActor.tell(msg, getSender());

                        })
                        .match(Message_ReceiveMessage.class, m -> {
                            System.out.println(m);
                            connectorClient.tell(m, self());

                        }).build();
        User =
                receiveBuilder()
                        .match(Message_ChangeUserMode.class, msg ->
                        {
                            switch (msg.getMode()) {
                                case USER:
                                    getContext().become(User);
                                    break;
                                case VOICE:
                                    break;
                                case OPERATOR:
                                    break;
                                case OWNER:
                                    getContext().become(Owner);
                                    break;
                                case BANNED:
                                    break;
                            }
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            // send Client his mode
                            // Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                            //connectorClient.tell(userMode, self());
                            ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
                            channelMainActor.tell(m, self());

                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            System.out.println("Got leave message from " + msg.getUsername());
                            //table.remove(msg.getChannel());
                            parentUserActor.tell(msg, self());
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getChannel());
                            channelActor.tell(msg, self());

                        })
                        .match(Message_JoinApproval.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());

                        })
                        .match(Message_CloseChannel.class, msg -> {
                            System.out.println("Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_UpdateList.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_PrivateMessage.class, msg -> {
                            System.out.println("UserChannelActor: Got message to sent to client :" + msg.getRoomName());
                            ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getSpecificUserName());
                            String message = "[" + msg.getTimeStamp() + "] < " + msg.getSpecificUserName() + "> " + msg.getText();
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(
                                    msg.getRoomName(), "", msg.getSpecificUserName(), message);
                            toSend.tell(rec, self());
                            connectorClient.tell(rec, self());
                        })
                        .match(Message_PublicMessage.class, msg -> {
                            System.out.println("Got message to sent to client <Message_PublicMessage> :" + msg);
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            channelActor.tell(msg, getSender());

                        })
                        .match(Message_PermissionToChangeTitle.class, msg -> {
                            String message = "You don't have the right permission";
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(msg.getRoomName(), "System", msg.getUserName(), message);
                            connectorClient.tell(rec, getSender());

                        })
                        .match(Message_ChangeTitle.class, msg -> {


                        })
                        .match(Message_ReceiveMessage.class, m -> {
                            System.out.println(m);
                            connectorClient.tell(m, self());
                        })
                        .match(Message_AddUserToChannel.class, msg -> {

                        })

                        .build();
        Operator =
                receiveBuilder()
                        .match(Message_ChangeUserMode.class, msg ->
                        {
                            switch (msg.getMode()) {
                                case USER:
                                    getContext().become(User);
                                    break;
                                case VOICE:
                                    break;
                                case OPERATOR:
                                    break;
                                case OWNER:
                                    getContext().become(Owner);
                                    break;
                                case BANNED:
                                    break;
                            }
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            // send Client his mode
                            // Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                            //connectorClient.tell(userMode, self());
                            ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
                            channelMainActor.tell(m, self());

                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            System.out.println("Got leave message from " + msg.getUsername());
                            //table.remove(msg.getChannel());
                            parentUserActor.tell(msg, self());
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getChannel());
                            channelActor.tell(msg, self());

                        })
                        .match(Message_JoinApproval.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());

                        })
                        .match(Message_CloseChannel.class, msg -> {
                            System.out.println("Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_UpdateList.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_PrivateMessage.class, msg -> {
                            System.out.println("UserChannelActor: Got message to sent to client :" + msg.getRoomName());
                            ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getSpecificUserName());
                            String message = "[" + msg.getTimeStamp() + "] < " + msg.getSpecificUserName() + "> " + msg.getText();
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(
                                    msg.getRoomName(), "", msg.getSpecificUserName(), message);
                            toSend.tell(rec, self());
                            connectorClient.tell(rec, self());
                        })
                        .match(Message_PublicMessage.class, msg -> {
                            System.out.println("Got message to sent to client <Message_PublicMessage> :" + msg);
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            channelActor.tell(msg, getSender());

                        })
                        .match(Message_PermissionToChangeTitle.class, msg -> {
                            System.out.println("UserChannel: Owner: Got message to sent to client <Message_PermissionToChangeTitle> :" + msg);

                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            channelActor.tell(msg, getSender());

                        })
                        .match(Message_ChangeTitle.class, msg -> {

                            System.out.println(" UserChannel: Owner: Got message to sent to client <Message_ChangeTitle> :" + msg);
                            //ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            connectorClient.tell(msg, getSender());

                        })
                        .match(Message_ReceiveMessage.class, m -> {
                            System.out.println(m);
                            connectorClient.tell(m, self());

                        }).build();
        Banned =
                receiveBuilder()
                        .match(Message_ChangeUserMode.class, msg ->
                        {
                            switch (msg.getMode()) {
                                case USER:
                                    getContext().become(User);
                                    break;
                                case VOICE:
                                    break;
                                case OPERATOR:
                                    break;
                                case OWNER:
                                    getContext().become(Owner);
                                    break;
                                case BANNED:
                                    break;
                            }
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            // send Client his mode
                            // Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                            //connectorClient.tell(userMode, self());
                            ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
                            channelMainActor.tell(m, self());

                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            System.out.println("Got leave message from " + msg.getUsername());
                            //table.remove(msg.getChannel());
                            parentUserActor.tell(msg, self());
                            ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getChannel());
                            channelActor.tell(msg, self());

                        })
                        .match(Message_JoinApproval.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());

                        })
                        .match(Message_CloseChannel.class, msg -> {
                            System.out.println("Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_UpdateList.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());
                        })
                        .match(Message_PrivateMessage.class, msg -> {
                            String message = "You don't have the right permission";
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(msg.getRoomName(), "System", msg.getSender(), message);
                            connectorClient.tell(rec, getSender());
                        })
                        .match(Message_PublicMessage.class, msg -> {
                            String message = "You don't have the right permission";
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(msg.getRoomName(), "System", msg.getUserName(), message);
                            connectorClient.tell(rec, getSender());

                        })
                        .match(Message_PermissionToChangeTitle.class, msg -> {
                            String message = "You don't have the right permission";
                            Message_ReceiveMessage rec = new Message_ReceiveMessage(msg.getRoomName(), "System", msg.getUserName(), message);
                            connectorClient.tell(rec, getSender());

                        })
                        .match(Message_ReceiveMessage.class, m -> {
                            System.out.println(m);
                            connectorClient.tell(m, self());

                        }).build();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_ChangeUserMode.class, msg ->
                {
                    switch (msg.getMode()) {
                        case USER:
                            getContext().become(User);
                            break;
                        case VOICE:
                            break;
                        case OPERATOR:
                            break;
                        case OWNER:
                            getContext().become(Owner);
                            break;
                        case BANNED:
                            break;
                    }
                })
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    // send Client his mode
                    // Message_ChangeUserMode userMode = new Message_ChangeUserMode(m.getUsername(), UserMode.OWNER);
                    //connectorClient.tell(userMode, self());
                    ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
                    channelMainActor.tell(m, self());

                })
                .match(Message_JoinApproval.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectorClient.tell(msg, self());

                })
//                .match(Message_ReceiveMessage.class, m -> {
//                    System.out.println(m);
//                    connectorClient.tell(m, self());
//
//                })
                .build();

    }

    public Receive getUser() {
        return User;
    }

    public Receive getVOICE() {
        return Voice;
    }

    public Receive getOPERATOR() {
        return Operator;
    }

    public Receive getBANNED() {
        return Banned;
    }

    public Receive getOwner() {
        return Owner;
    }
}
