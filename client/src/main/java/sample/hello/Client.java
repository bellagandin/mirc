package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Client extends AbstractActor {
    ActorRef connectorActor;
    String username;
    UserMode UserType;
    String roomName;
    TabbedChat window;
    Map<String, JPanel> rooms;
    int roomsIn;


    public Client(String name, TabbedChat ch) {
        window = ch;
        username = name;
        rooms = new HashMap<String, JPanel>();
        roomsIn = 0;
        ch.c=this;
    }

    public void dispatchAll() {
        Message_dispatch ds=new Message_dispatch(username,"hi",
                self(),false,true);
        connectorActor.tell(ds,self());
        self().tell(PoisonPill.getInstance(),self());

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message_ReceiveMessage.class, m -> {
                    System.out.println(m.getText());
                    chatRoomPanel chat = (chatRoomPanel) rooms.get(m.getRoomName());
                    if (chat != null) {
                        chat.printInMessageArea(m.getText());
                    }

                })
                .match(Message_JoinApproval.class, m -> {
                    connectorActor = sender();
                    System.out.println(username + " Seccessfully joined channel" + m.getRoomName());
                    chatRoomPanel newChatChannel = new chatRoomPanel(this.getSelf(), m.getRoomName(), username, this);
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


                .match(Message_LeaveChannel.class, mgs -> {
                    System.out.println("leave got Back");
                    chatRoomPanel chat = (chatRoomPanel) rooms.get(mgs.getRoomName());
                    chat.clearList();
                    String roomToClose = mgs.getRoomName();
                    rooms.remove(roomToClose);
                    roomsIn--;
                    window.closeChannel(roomToClose);


                })
                .match(Message_gotKicked.class, mgs -> {
                    System.out.println("got Kick Message from server");
                    String text = mgs.getToKick()+" got kicked by "+mgs.getKicker();
                    Message_PublicMessage kickAnounce=new Message_PublicMessage(mgs.getToKick(),mgs.getRoomName(),text);
                    connectorActor.tell(kickAnounce,self());
                    chatRoomPanel c=(chatRoomPanel) rooms.get(mgs.getRoomName());
                    Message_LeaveChannel lev=new Message_LeaveChannel(mgs.getToKick(),mgs.getRoomName(),c.client,true,false);
                    connectorActor.tell(lev,self());

                })


                .match(Message_UpdateList.class, mgs -> {
                    System.out.println("Hello");
                    chatRoomPanel chat = (chatRoomPanel) rooms.get(mgs.getRoomName());
                    chat.clearList();
                    List<String> userL = mgs.getUserList();
                    for (int i = 0; i < userL.size(); i++) {
                        String temp=userL.get(i);
                        if(temp.charAt(0)=='@'){
                            temp="<i>"+temp+"</i>";
                        }
                        chat.addTolist(userL.get(i));
                    }

                })
                .match(Message_Error.class,msg->
                {
                    System.out.println(msg.getText());
                    chatRoomPanel chat = (chatRoomPanel) rooms.get(msg.getRoomName());
                    if (chat != null) {
                        chat.printInMessageArea(msg.getText());
                    }
                })
                .match(Message_ChangeTitle.class, msg -> {
                    chatRoomPanel chat = (chatRoomPanel) rooms.get(msg.getRoomName());
                    chat.changeTitle(msg.getNewTitleName());

                })
                .match(Message_ChangeUserMode.class, msg ->
                {
                    this.UserType = msg.getMode();
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
                                Message_PrivateMessage sen = new Message_PrivateMessage(details[1], msg.getRoomName(), msg.getUserName(), toWhomToSend);
                                connectorActor.tell(sen, self());
                            }
                        } else if (type.equals("/leave")) {
                            Message_LeaveChannel sen = new Message_LeaveChannel(username,msg.getRoomName(),
                                    self(),false,false);
                            chatRoomPanel chat = (chatRoomPanel) rooms.get(msg.getRoomName());
                            chat.clearList();
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/title")) {
                            String details[] = theRest.split(" ", 2);
                            String channelName = details[0];
                            Message_PermissionToChangeTitle sen = new Message_PermissionToChangeTitle(channelName, msg.getUserName(), details[1]);
                            System.out.println("sending the server to change the title");
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/kick")) {
                            System.out.println("Got kick message passing to server");
                            String kicked[] = theRest.split(" ", 2);
                            Message_KickUser sen = new Message_KickUser(username,msg.getRoomName(),username,kicked[0],false);
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/ban")) {
                            System.out.println("Got ban message passing to server");
                            String kicked[] = theRest.split(" ", 2);
                            Message_KickUser sen = new Message_KickUser(username,msg.getRoomName(),username,kicked[0],false);
                            connectorActor.tell(sen, self());
                            Message_AddToBandList sendMsg = new Message_AddToBandList(kicked[0],username,msg.getRoomName());
                            connectorActor.tell(sendMsg, self());

                        } else if (type.equals("/add")) {
                            String res[] = theRest.split(" ", 3);
                            UserMode mode;
                            if (res[1].equals ("v")) {
                                mode = UserMode.VOICE;
                                Message_PromoteToVoice sen=new Message_PromoteToVoice(res[2],res[0],mode);
                                connectorActor.tell(sen,self());
                            } else {
                                mode = UserMode.OPERATOR;
                                Message_PromoteToOperator sen=new Message_PromoteToOperator(res[2],res[0],mode);
                                connectorActor.tell(sen,self());
                            }
                            //Message_AddUserToChannel sen = new Message_AddUserToChannel(res[0], msg.getUserName(),UserType, res[2], mode, false);
                            //connectorActor.tell(sen, self());
                        } else if (type.equals("/remove")) {
                            String res[] = theRest.split(" ", 3);
                            UserMode mode;
                            if (res[1].equals ("v")) {
                                mode = UserMode.VOICE;
                                Message_removeFromVoice sen=new Message_removeFromVoice(res[2],res[0],mode);
                                connectorActor.tell(sen,self());
                            } else {
                                mode = UserMode.OPERATOR;
                                Message_removeFromOp sen=new Message_removeFromOp(res[2],res[0],mode);
                                connectorActor.tell(sen,self());
                            }
                        }

                    } else {
                        if (type.equals("/disband")) {
                            chatRoomPanel chat = (chatRoomPanel) rooms.get(msg.getRoomName());
                            chat.clearList();
                            Message_Disband sen = new Message_Disband(msg.getUserName(), msg.getRoomName());
                            connectorActor.tell(sen, self());
                        } else {
                            Message_PublicMessage sen = new Message_PublicMessage(username, msg.getRoomName(), msg.getText());
                            connectorActor.tell(sen, self());
                        }
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
