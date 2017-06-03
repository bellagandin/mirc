package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class Client extends AbstractActor {
    ActorRef connectorActor;
    String username;
    String roomName;
    TabbedChat window;
    Map<String, JPanel> rooms;
    int roomsIn;


    public Client(String name, TabbedChat ch) {
        window = ch;
        username = name;
        rooms = new HashMap<String, JPanel>();
        roomsIn = 0;
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_ReceiveMessage.class, m -> {
                    System.out.println(m.getRoomName());
                    chatRoomPanel chat = (chatRoomPanel)rooms.get(m.getRoomName());
                    if (chat !=null) {
                        chat.printInMessageArea(m.getText());
                    }

                })
                .match(Message_JoinApproval.class, m -> {
                    connectorActor = sender();
                    System.out.println(username + " Seccessfully joined channel" + m.getRoomName());
                    chatRoomPanel newChatChannel = new chatRoomPanel(this.getSelf(), m.getRoomName());
                    rooms.put(m.getRoomName(), newChatChannel);

                    if (roomsIn == 0)
                        this.window.ServerActor = sender();
                    newChatChannel.changeTitle(m.getRoomTitle());
                    window.openNewChanel(m.getRoomName(), newChatChannel);
                    chatRoomPanel chat = (chatRoomPanel) rooms.get(m.getRoomName());
//                    chat.printInMessageArea(
//                            username + " Has joined room: " + m.roomName);
                    for (int i = 0; i < m.getUserList().size(); i++) {
                        chat.addTolist(m.getUserList().get(i));
                    }
                    roomsIn++;


                })
                .match(Message_Broadcast.class, mgs -> {
                    //chatGui.renderMessage(mgs.content);
                    System.out.println(mgs.getContent());
                })
                .match(Message_UserInput.class, msg -> {

                    String arr[] = msg.getText().split(" ", 2);
                    String type = arr[0];
                    if (arr.length > 1) {
                        String theRest = arr[1];
                        if (type.equals("/w")) {

                            String details[] = theRest.split(" ", 2);
                            String toWhomToSend = details[0];
                            if (details.length > 1) {
                                Message_PrivateMessage sen = new Message_PrivateMessage(details[1],msg.getRoomName(),msg.getUserName(),toWhomToSend);
                                connectorActor.tell(sen, self());
                            }
                        } else if (type.equals("/leave")) {
                            Message_LeaveChannel sen = new Message_LeaveChannel();
                            sen.username = username;
                            sen.channel = roomName;
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/title")) {
//                            Message_ChangeTitle sen = new Message_ChangeTitle();
//                            sen.userName = username;
//                            sen.roomNAme = roomName;
//                            connectorActor.tell(sen, self());
                        } else if (type.equals("/kick")) {
                            Message_KickUser sen = new Message_KickUser();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            String kicked[] = theRest.split(" ", 2);
                            sen.ToKick = kicked[0];
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/ban")) {
//                            Message_banUser sen = new Message_banUser();
//                            sen.userName = username;
//                            sen.roomNAme = roomName;
//                            String ban[] = theRest.split(" ", 2);
//                            sen.banUser = ban[0];
//                            connectorActor.tell(sen, self());
                        } else if (type.equals("/add")) {
//                            Message_AddUserToChannel sen = new Message_AddUserToChannel();
//                            sen.userName = username;
//                            sen.roomNAme = roomName;
//                            String res[] = theRest.split(" ", 3);
//                            sen.addedUser = res[2];
//                            sen.listType = res[1];
//                            connectorActor.tell(sen, self());
                        } else if (type.equals("/remove ")) {
                            Message_RemoveUser sen = new Message_RemoveUser();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            String res[] = theRest.split(" ", 3);
                            sen.delUser = res[2];
                            sen.listType = res[1];
                            connectorActor.tell(sen, self());
                        }
                    }
                    else{
                        Message_PublicMessage sen = new Message_PublicMessage(username,msg.getRoomName(),msg.getText());
                        connectorActor.tell(sen, self());
                    }

                })

                .build();
    }


    @Override
    public void preStart() {
//        sc=new Scanner(System.in);
//        username="Roy";
//        greeter = getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server");
//        Message_JoinClient m = new Message_JoinClient(username, "room_1");
//
//        greeter.tell(m, self());
    }
}
