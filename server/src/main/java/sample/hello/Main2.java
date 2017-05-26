package sample.hello;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main2 {

  public static void main(String[] args) {
    //creating the system
    ActorSystem system = ActorSystem.create("HelloWorldSystem");
    //creating system actors
    system.actorOf(Props.create(Server.class), "Server");

  }

}
