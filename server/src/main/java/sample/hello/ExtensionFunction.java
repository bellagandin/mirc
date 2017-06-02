package sample.hello;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Bella on 5/31/2017.
 */
public class ExtensionFunction {

    public ActorRef g(ActorSelection sel) {

        AskableActorSelection asker = new AskableActorSelection(sel);

        Timeout timeOut = new Timeout(Duration.create(1, TimeUnit.SECONDS));
        Future<Object> future = asker.ask(new Identify(1), timeOut);
        ActorIdentity identity;

        try {
            identity = (ActorIdentity) Await.result(future, timeOut.duration());
            if (identity.ref().isDefined()) {
                return identity.ref().get();
            }
        } catch (Exception e) {
            // timeout passed
        }
        return null; // not alive anymore
    }

}
