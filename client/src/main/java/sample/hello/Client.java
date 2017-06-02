package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class Client extends AbstractActor {
    ActorRef connectorActor;
    String username;
    String roomName;
    TabbedChat window;
    Map<String,JPanel> rooms;


    public Client(String name,TabbedChat ch){
        window=ch;
        username=name;
        rooms=new HashMap<String,JPanel>();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, m -> {
                    System.out.println(m);
                    //Message_LeaveChannel l = new Message_LeaveChannel("bella", "1");
                    // greeter =  getContext().actorSelection("akka.tcp://HelloWorldSystem@127.0.0.1:22/user/Server/Clients/bella");
                    //greeter.tell(l, self());
                    getContext().stop(self());

                })
                .match(Message_JoinApproval.class, m ->{
                    connectorActor = sender();
                    System.out.println(username+" Seccessfully joined channel"+m.roomName);
                    chatRoomPanel newChatChannel=new chatRoomPanel(this.getSelf(),m.roomName);
                    rooms.put(m.roomName,newChatChannel);
                    newChatChannel.changeTitle(m.roomTitle);
                    window.openNewChanel(m.roomName,newChatChannel);
                    chatRoomPanel chat=(chatRoomPanel)rooms.get(m.roomName);
                    chat.printInMessageArea(
                            username+" Has joined room: "+m.roomName);
                    for(int i=0;i<m.userList.size();i++){
                        chat.addTolist(m.userList.get(i));
                    }

                })
                .match(Message_Broadcast.class, mgs -> {
                    //chatGui.renderMessage(mgs.content);
                    System.out.println(mgs.content);
                })
                .match(Message_UserInput.class, msg -> {

                    String arr[] = msg.text.split(" ", 2);
                    String type = arr[0];
                    if (arr.length>1) {



                        String theRest = arr[1];

                        if (type.equals("/w")) {
                            Message_ChatMessage sen = new Message_ChatMessage();
                            String details[] =theRest.split(" ", 2);
                            String toWhomToSend = details[0];
                            sen.text = details[1];
                            sen.userName = toWhomToSend;
                            sen.roomNAme = roomName;
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/leave")) {
                            Message_LeaveChannel sen = new Message_LeaveChannel();
                            sen.username = username;
                            sen.channel = roomName;
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/title")) {
                            Message_ChangeTitle sen = new Message_ChangeTitle();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/kick")) {
                            Message_KickUser sen = new Message_KickUser();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            String kicked[] = theRest.split(" ", 2);
                            sen.ToKick = kicked[0];
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/ban")) {
                            Message_banUser sen = new Message_banUser();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            String ban[] = theRest.split(" ", 2);
                            sen.banUser = ban[0];
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/add")) {
                            Message_AddUserToChannel sen = new Message_AddUserToChannel();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            String res[] = theRest.split(" ", 3);
                            sen.addedUser = res[2];
                            sen.listType = res[1];
                            connectorActor.tell(sen, self());
                        } else if (type.equals("/remove ")) {
                            Message_RemoveUser sen = new Message_RemoveUser();
                            sen.userName = username;
                            sen.roomNAme = roomName;
                            String res[] = theRest.split(" ", 3);
                            sen.delUser = res[2];
                            sen.listType = res[1];
                            connectorActor.tell(sen, self());
                        } else {
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
