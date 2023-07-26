import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;

public class TestPackageActor extends AbstractActor {
    private ActorSelection testPerformerRouter = getContext().actorSelection("/user/testPerformerActor");

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestPackageMessage.class, m -> {
                    for (Test test: m.getTests()) {
                        testPerformerRouter
                                .tell(new TestMessage(m.getPackageId(), m.getJsScript(), m.getFunctionName(), test), self());
                    }
                })
                .build();
    }
}
