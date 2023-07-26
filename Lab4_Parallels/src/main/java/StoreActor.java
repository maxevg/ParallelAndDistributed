import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActor extends AbstractActor {
    private Map<Integer, ArrayList<Test>> store = new HashMap<Integer, ArrayList<Test>>();

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreMessage.class, m -> {
                    if (store.containsKey(m.getPackageId())) {
                        ArrayList<Test> current_tests = store.get(m.getPackageId());
                        current_tests.addAll(m.getTest());
                        store.replace(m.getPackageId(), current_tests);
                    } else {
                        store.put(m.getPackageId(), m.getTest());
                    }
                })
                .match(GetMessage.class, req ->{
                    sender().tell(
                            new StoreMessage(req.getPackageId(), store.get(req.getPackageId())), self()
                    );
                })
                .build();
    }
}
