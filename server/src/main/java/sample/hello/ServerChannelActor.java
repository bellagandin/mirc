package sample.hello;

import akka.actor.*;
import akka.routing.BroadcastRoutingLogic;

import akka.routing.Router;

import java.util.ArrayList;

import static sample.hello.UserMode.OWNER;

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
                        type = new Message_ChangeUserMode(m.getUsername(), m.getUsername(), UserMode.OWNER);
                        userMode = "@" + m.getUsername();
                    } else {
                        type = new Message_ChangeUserMode(m.getUsername(), m.getUsername(), UserMode.USER);
                        userMode = m.getUsername();
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
                    String message = "[" + m.getTimeStamp() + "]*** joins: " + m.getUsername();
                    System.out.println(message);
                    Message_ReceiveMessage rec = new Message_ReceiveMessage(roomName, m.getUsername(), "", message);
                    router.route(rec, self());
                })

                .match(Message_LeaveChannel.class, m -> {
                    System.out.println("Got Leave message from " + m.getUsername());
                    router = router.removeRoutee(getSender());
                    //sender().tell(m,self());
//                    if (router.routees().isEmpty()) {
//                        Message_CloseChannel msg = new Message_CloseChannel();
//                        msg.roomName = m.getChannel();
//                        getSender().tell(msg, self());
//                        getContext().stop(self());
//                        return;
//                    } else {
//                        if (router.routees().size() == 1) {
//                            Message_ChangeUserMode msg = new Message_ChangeUserMode();
//                            msg.rootName = m.getChannel();
//                            msg.mode=UserMode.OWNER;
//                            router.route(msg, self());
//                        }
                        for(int i=0;i<users.size();i++){
                            if(users.get(i).equals(m.getUsername())){
                                users.remove(i);
                                break;
                            }
                        }
                        router.route(new Message_UpdateList(roomName,users),sender());
                        String message = "[" + m.getTimeStamp() + "]*** parts: " + m.getTimeStamp();
                    router.route(message, m.getClient());
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
                .match(Message_ChangeUserMode.class, msg -> {
                    users.remove(msg.getUserName());
                    if (msg.getMode() == UserMode.VOICE) {
                        String user = "+" + msg.getUserName();
                        users.add(user);
                    } else if (msg.getMode() == UserMode.OPERATOR) {
                        String user = "@" + msg.getUserName();
                        users.add(user);
                    }
                    Message_UpdateList rec = new Message_UpdateList(msg.getRootName(), users);
                    router.route(rec, self());
                })
                .match(Message_AddUserToChannel.class, msg -> {
                    System.out.println("Channel actor: rec message <Message_AddUserToChannel> from" + msg.getUserName());
                    if (users.contains(msg.getAddedUser())) {
                        Message_ChangeUserMode mode = new Message_ChangeUserMode(msg.getUserName(), msg.getAddedUser(), msg.getListType());
                        ActorSelection userAddedActor = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getAddedUser());
                        userAddedActor.tell(msg, self());

                    }

                })
                .match(Message_RemoveUser.class, msg -> {
                    System.out.println("Channel actor: rec message <Message_RemoveUser> from" + msg.getUserName());
                    if (users.contains(msg.getDelUser())) {
                        Message_ChangeUserMode mode = new Message_ChangeUserMode(msg.getUserName(), msg.getDelUser(), msg.getListType());
                        ActorSelection userAddedActor = getContext().actorSelection("/user/Server/ServerUsersMain/" + msg.getDelUser());
                        userAddedActor.tell(msg, self());

                    }
                })

                .build();
    }


//    private void broadcastMessage(String message,ActorRef sender) {
//        Message_Broadcast brod = new Message_Broadcast("", message);
//        System.out.println("Sending with broadcast");
//        router.route(brod, sender);


    //  }
}
