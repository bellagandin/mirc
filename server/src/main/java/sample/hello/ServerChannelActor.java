package sample.hello;

import akka.actor.AbstractActor;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Router;

import java.util.ArrayList;

/**
 * Created by Bella on 5/26/2017.
 */
public class ServerChannelActor extends AbstractActor {
    private String roomName;
    Router router;
    private String roomTitle;
    ArrayList<String> users;

    public ServerChannelActor(String roomName) {
        this.roomName = roomName;
        router = new Router(new BroadcastRoutingLogic());
        users = new ArrayList<String>();
        roomTitle = roomName;
    }



    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(Message_JoinClient.class, (Message_JoinClient m) -> {
                    System.out.println("ServerChannelActor: in room " + m.getRoomName() + " Got message from " + getSender());

                    //find the user mode
                    Message_ChangeUserMode type;
                    String userMode;
                    if (router.routees().isEmpty()) {
                        System.out.println("The room is empty.");
                        type = new Message_ChangeUserMode(m.getUserName(), m.getUserName(), UserMode.OWNER);
                        userMode = "@" + m.getUserName();
                    } else {
                        type = new Message_ChangeUserMode(m.getUserName(), m.getUserName(), UserMode.USER);
                        userMode = m.getUserName();
                    }
                    getSender().tell(type, self());
                    System.out.println("Sending to UserChannel change in UserMode.");

                    //Sending HandShake to the Client
                    Message_JoinApproval respond = new Message_JoinApproval(roomName, self(), users, roomTitle);
                    sender().tell(respond, self());

                    //Add the client to the channel and updates the list
                    router = router.addRoutee(getSender());
                    users.add(userMode);
                    Message_UpdateList msg = new Message_UpdateList(m.getRoomName(), users);
                    router.route(msg, self());

                    //Sends all the Users the join message
                    String message = "[" + m.getTimeStamp() + "]*** joins: " + m.getUserName();
                    System.out.println(message);
                    Message_ReceiveMessage rec = new Message_ReceiveMessage(roomName, m.getUserName(), "", message);
                    router.route(rec, self());
                })
                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Channel Got Leave message from " + m.getUserName());
                    router = router.removeRoutee(getSender());

                        for(int i=0;i<users.size();i++){
                            String temp=users.get(i);
                            if(users.get(i).charAt(0)=='@'||users.get(i).charAt(0)=='+' )
                                temp=temp.substring(1);
                            if (temp.equals(m.getUserName())) {
                                users.remove(i);
                                break;
                            }
                        }
                        router.route(new Message_UpdateList(roomName,users),sender());
                    String message;
                    if(m.isKicked()){
                    message = "[" + m.getTimeStamp() + "]*** parts: " + m.getTimeStamp();
                    router.route(message, m.getClient());}
                        m.getClient().tell(m,self());

                })
                .match(Message_PublicMessage.class, msg -> {
                    System.out.println("channel actor: rec message from" + msg.getUserName());
                    String message = "[" + msg.getTimeStamp() + "] < " + msg.getUserName() + "> " + msg.getText();
                    Message_ReceiveMessage rec = new Message_ReceiveMessage(
                            msg.getRoomName(), msg.getUserName(), "", message);
                    router.route(rec, getSender());
                })
                .match(Message_PermissionToChangeTitle.class, msg -> {
                    System.out.println("Channel actor: rec message from" + msg.getUserName());
                    roomTitle = msg.getNewTitleName();
                    System.out.println("The title of the room was change to " + msg.getNewTitleName());
                    Message_ChangeTitle chan = new Message_ChangeTitle(msg.getRoomName(), msg.getUserName(), msg.getNewTitleName());
                    router.route(chan, getSender());
                    //router.route);

                })
                .match(Message_ChangeTitle.class, msg -> {
                    //Message_PermissionToChangeTitle.
                })
                .match(Message_PromoteToOperator.class, msg -> {
                    System.out.println("ServerActorChannel Got Message_PromoteToOperator to sent to promote :" + msg.getUserName());

                    for(int i=0;i<users.size();i++){
                        String temp=users.get(i);
                        if(users.get(i).charAt(0)=='@'||users.get(i).charAt(0)=='+' )
                            temp=temp.substring(1);
                        if (temp.equals(msg.getUserName())) {
                            users.set(i,"@"+temp);
                            break;
                        }
                    }

                    Message_UpdateList changeLst = new Message_UpdateList(msg.getRoomName(), users);
                    router.route(changeLst,getSender());

                })
                .match(Message_PromoteToVoice.class, msg -> {
                    System.out.println("ServerActorChannel Got Message_PromoteToVoice to sent to promote :" + msg.getUserName());
                    for(int i=0;i<users.size();i++){
                        String temp=users.get(i);
                        if(users.get(i).charAt(0)=='@'||users.get(i).charAt(0)=='+' )
                            temp=temp.substring(1);
                        if (temp.equals(msg.getUserName())) {
                            users.set(i,"+"+temp);
                            break;
                        }
                    }

                    Message_UpdateList changeLst = new Message_UpdateList(msg.getRoomName(), users);
                    router.route(changeLst,self());
                })
                .match(Message_ChangeUserMode.class, msg -> {
                    System.out.println("ServerActorChannel Got Message_ChangeUserMode to sent to promote :" + msg.getUserName());
                    for(int i=0;i<users.size();i++){
                        String temp=users.get(i);
                        if(users.get(i).charAt(0)=='@'||users.get(i).charAt(0)=='+' )
                            temp=temp.substring(1);
                        if(temp.equals(msg.getUserName())){
                            if(msg.getMode()==UserMode.USER)
                                users.set(i,temp);
                            if(msg.getMode()==UserMode.OPERATOR)
                                users.set(i,"@"+temp);
                            if(msg.getMode()==UserMode.VOICE)
                                users.set(i,"+"+temp);

                            break;
                        }
                    }

                    Message_UpdateList changeLst = new Message_UpdateList(msg.getRoomName(), users);
                    router.route(changeLst,self());

                })
                .match(Message_removeFromOp.class, msg -> {
                    System.out.println("ServerActorChannel Got Message_removeFromVoice to sent to promote :" + msg.getUserName());
                    for(int i=0;i<users.size();i++){
                        String temp=users.get(i);
                        if(users.get(i).charAt(0)=='@'||users.get(i).charAt(0)=='+' )
                            temp=temp.substring(1);
                        if (temp.equals(msg.getUserName())) {
                            users.set(i,temp.substring(1));
                            break;
                        }
                    }

                    Message_UpdateList changeLst = new Message_UpdateList(msg.getRoomName(), users);
                    router.route(changeLst,self());
                })
                .match(Message_Disband.class, msg -> {
                    System.out.println("ChannelActor : Got message to sent to client <Message_disband> :" + msg);
                    Message_gotKicked handle = new Message_gotKicked(msg.getUserName(),
                            msg.getRoomName(), "");
                    users.clear();
                    router.route(handle, getSender());
                })
                .match(Message_ReceiveMessage.class, msg -> router.route(msg, getSender()))
                .build();
    }



}
