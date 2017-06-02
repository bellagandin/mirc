package sample.hello;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class Client extends AbstractActor {
    ActorSelection ServerUserActor;
    String username;
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
                    System.out.println(username+" Seccessfully joined channel"+m.roomName);
                    chatRoomPanel newChatChannel=new chatRoomPanel(this.getSelf(),m.roomName);
                    rooms.put(m.roomName,newChatChannel);
                    newChatChannel.changeTitle(m.roomTitle);
                    for(int i=0;i<m.userList.size();i++){
                        //chatGui.model.addElement(m.userList.get(i));
                    }

                })
                .match(Message_Broadcast.class, mgs -> {
                    //chatGui.renderMessage(mgs.content);
                    System.out.println(mgs.content);
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
