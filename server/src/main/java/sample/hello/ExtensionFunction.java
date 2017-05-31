package sample.hello;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

/**
 * Created by Bella on 5/31/2017.
 */
public class ExtensionFunction {

    public ActorRef GetActorByName(ActorSelection sel, String Channel) {

        Timeout t = new Timeout(5, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> fut = asker.ask(Channel, t);
        ActorRef ref = null;

        try {

            ref = (ActorRef) Await.result(fut, t.duration());
        } catch (Exception e) {
            System.out.println("here");
        }

        return ref;
    }

}
