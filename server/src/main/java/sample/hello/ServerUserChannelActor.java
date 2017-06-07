package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.PoisonPill;

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
    private UserMode Mode;

    public ServerUserChannelActor(ActorRef client, ActorRef act) {
        this.connectorClient = client;
        this.parentUserActor = act;
        this.helper = new ExtensionFunction();

        Owner = receiveBuilder()
                .match(Message_ChangeUserMode.class, msg ->
                {
                    Mode=msg.getMode();
                    ChangeMode(msg);
                })
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {

                    JoinMessage(m);

                })
                .match(Message_LeaveChannel.class, msg -> {
                    LeaveMessage(msg);

                })
                .match(Message_JoinApproval.class, msg -> {
                    System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectorClient.tell(msg, getContext().getParent());

                })
                .match(Message_PromoteToOperator.class, msg -> {
                    System.out.println("ServerUSerChannelActor: Got Message_PromoteToOperator to sent to promote :" + msg.getUsername());
                   promote(msg);
                })
                .match(Message_PromoteToVoice.class, msg -> {
                    System.out.println(" ServerUSerChannelActor:Got Message_PromoteToVoice to sent to promote :" + msg.getUsername());
                    promote(msg);
                })
                .match(Message_removeFromVoice.class, msg -> {
                    System.out.println("ServerUSerChannelActor: Got Message_removeFromVoice to sent to remove :" + msg.getUsername());
                    demote(msg);
                })
                .match(Message_removeFromOp.class, msg -> {
                    System.out.println("ServerUSerChannelActor: Got Message_removeFromOp to sent to promote :" + msg.getUsername());
                    demote(msg);
                })

                .match(Message_CloseChannel.class, msg -> {
                    System.out.println("Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    connectorClient.tell(msg, self());
                })
                .match(Message_gotKicked.class, msg -> {
                    System.out.println("Message_gotKicked: Got message to sent to client :" + msg);
                    //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                    System.out.println("passing to client");
                    connectorClient.tell(msg, self());

                })
                .match(Message_UpdateList.class, msg -> {
                    System.out.println("Message_UpdateList: Got message to sent to client :" + msg);
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
                .match(Message_KickUser.class, msg -> {
                    System.out.println("server got kick message");
                    ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getToKick());
                    Message_gotKicked handle =new Message_gotKicked(msg.getKicker(),
                            msg.getRoomNAme(),msg.getToKick());
                    toSend.tell(handle,self());
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
                .build();
        //end Owner
        Voice =
                receiveBuilder()
                        .match(Message_ChangeUserMode.class, msg ->
                        {
                            ChangeMode(msg);
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            JoinMessage(m);

                        })
                        .match(Message_removeVoice.class,m -> {
                            downgradeUser(m,UserMode.VOICE);

                        })

                        .match(Message_updateRole.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_updateRole to sent to promote :" + msg.getUserName());
                            Message_ChangeUserMode ums=new Message_ChangeUserMode(msg.getUserName()
                                    ,msg.getRoomNAme(),UserMode.VOICE);
                            ActorSelection ChannelActor = getContext()
                                    .actorSelection("/user/Server/ServerChannelMain/"+msg.getRoomNAme());
                            ChannelActor.tell(ums, self());


                        })


                        .match(Message_LeaveChannel.class, msg -> {
                            LeaveMessage(msg);

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
                        .match(Message_gotKicked.class, msg -> {
                            System.out.println("Message_gotKicked: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            System.out.println("passing to client");
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
        User =
                receiveBuilder()
                        .match(Message_ChangeUserMode.class, msg ->
                        {
                            ChangeMode(msg);
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            JoinMessage(m);

                        })
                        .match(Message_updateRole.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_updateRole to sent to promote :" + msg.getUserName());
                            Message_ChangeUserMode ums=new Message_ChangeUserMode(msg.getUserName()
                                    ,msg.getRoomNAme(),UserMode.USER);
                            ActorSelection ChannelActor = getContext()
                                    .actorSelection("/user/Server/ServerChannelMain/"+msg.getRoomNAme());
                            ChannelActor.tell(ums, self());


                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            LeaveMessage(msg);

                        })
                        .match(Message_JoinApproval.class, msg -> {
                            System.out.println("Message_JoinApproval: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            connectorClient.tell(msg, self());

                        })
                        .match(Message_gotKicked.class, msg -> {
                            System.out.println("Message_gotKicked: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            System.out.println("passing to client");
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
                            System.out.println(" UserChannel: Owner: Got message to sent to client <Message_ChangeTitle> :" + msg);
                            //ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            connectorClient.tell(msg, getSender());

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
                            ChangeMode(msg);
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            JoinMessage(m);

                        })
                        .match(Message_updateRole.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_updateRole to sent to promote :" + msg.getUserName());
                            Message_ChangeUserMode ums=new Message_ChangeUserMode(msg.getUserName()
                            ,msg.getRoomNAme(),UserMode.OPERATOR);
                            ActorSelection ChannelActor = getContext()
                                    .actorSelection("/user/Server/ServerChannelMain/"+msg.getRoomNAme());
                            ChannelActor.tell(ums, self());


                        })

                        .match(Message_PromoteToOperator.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_PromoteToOperator to sent to promote :" + msg.getUsername());
                            promote(msg);
                        })
                        .match(Message_PromoteToVoice.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_PromoteToOperator to sent to promote :" + msg.getUsername());
                            promote(msg);
                        })
                        .match(Message_removeFromVoice.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_removeFromVoice to sent to promote :" + msg.getUsername());
                            demote(msg);
                        })
                        .match(Message_removeFromOp.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Got Message_removeFromOp to sent to promote :" + msg.getUsername());
                            demote(msg);
                        })
                        .match(Message_removeOp.class, msg -> {
                            System.out.println("ServerUSerChannelActor: Operator Got dwograge to sent to promote :" + msg.getUserName());
                            downgradeUser(msg,UserMode.OPERATOR);
                        })

                        .match(Message_gotKicked.class, msg -> {
                            System.out.println("Message_gotKicked: Got message to sent to client :" + msg);
                            //ActorSelection channels = getContext().actorSelection("/user/Server/ServerChannelMain");
                            System.out.println("passing to client");
                            connectorClient.tell(msg, self());

                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            LeaveMessage(msg);

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
                        .match(Message_KickUser.class, msg -> {

                                System.out.println("server got kick message");
                                ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getToKick());
                                Message_gotKicked handle =new Message_gotKicked(msg.getKicker(),
                                        msg.getRoomNAme(),msg.getToKick());
                                toSend.tell(handle,self());


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
                            ChangeMode(msg);
                        })
                        .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                            JoinMessage(m);

                        })
                        .match(Message_LeaveChannel.class, msg -> {
                            LeaveMessage(msg);

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

                        .match(Message_ChangeTitle.class , msg ->
                        {
                            System.out.println(" UserChannel: Owner: Got message to sent to client <Message_ChangeTitle> :" + msg);
                            //ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getRoomName());
                            connectorClient.tell(msg, getSender());
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
                    ChangeMode(msg);

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

    public  void promote(Message_changeRole msg){
        System.out.println("ServerUserChannelActor got Promote Message from "+getSender());

        Message_ChangeUserMode change=new Message_ChangeUserMode(msg.getUsername(),
                msg.getRoomname(),msg.getChangeTo());

        ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getUsername());
        toSend.tell(change,self());

    }
    public  void demote(Message_changeRole msg){
        System.out.println("ServerUserChannelActor got demote Message from "+getSender());
        ActorSelection toSend = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getUsername());

        if(msg.getClass()==Message_removeFromOp.class){
            Message_removeOp rm=new Message_removeOp(msg.getUsername(),msg.getRoomname());
            toSend.tell(rm,self());

        }
        else{
            Message_removeVoice rm=new Message_removeVoice(msg.getUsername(),msg.getRoomname());
            toSend.tell(rm,self());
        }


    }


    public void ChangeMode(Message_ChangeUserMode msg)
    {
        switch (msg.getMode()) {
            case USER:
                getContext().become(User);

                break;
            case VOICE:
                getContext().become(Voice);
                break;
            case OPERATOR:
                getContext().become(Operator);
                break;
            case OWNER:
                getContext().become(Owner);
                break;
            case BANNED:
                getContext().become(Banned);
                break;
        }
        Message_updateRole updt=new Message_updateRole(msg.getMode(),msg.getUserName(),msg.getRootName());
        self().tell(updt,self());
    }

    public void   downgradeUser(remove msg,UserMode mode)
    {
        System.out.println("rached DowngradeFunction");
        if(mode==UserMode.OPERATOR)
            getContext().become(Voice);
        else
            getContext().become(User);

        Message_updateRole updt=new Message_updateRole(null,msg.getUserName(),msg.getRoomName());
        self().tell(updt,self());
    }





    public void JoinMessage(Message_JoinClient m){
        ActorSelection channelMainActor = getContext().actorSelection("/user/Server/ServerChannelMain/");
        channelMainActor.tell(m, self());
    }

    public void LeaveMessage(Message_LeaveChannel msg)
    {
        System.out.println("Got leave message from " + msg.getUsername());

        ActorSelection channelActor = getContext().actorSelection("/user/Server/ServerChannelMain/" + msg.getChannel());
        channelActor.tell(msg, self());
        self().tell(PoisonPill.
                getInstance(),self());
    }
}
