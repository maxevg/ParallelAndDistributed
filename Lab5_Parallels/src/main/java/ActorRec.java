import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;

public class ActorRec extends AbstractActor {
    private final HashMap<String, Integer> storage = new HashMap<>();

    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(GetMessage.class, msg -> {
                    getSender().tell(storage.getOrDefault(msg.getUrl(), -1), ActorRef.noSender());
                })
                .match(StorageMessage.class, msg -> {
                    storage.putIfAbsent(msg.getUrl(), msg.getTime());
                })
                .build();
    }
}
