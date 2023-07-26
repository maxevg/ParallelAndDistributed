package lab6_zookeeper;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ZooServer implements Watcher {
    final private static String SERVERS = "/servers";
    private ZooKeeper zoo;
    private ActorRef storage;

    public ZooServer(ZooKeeper zoo, ActorRef storage) throws KeeperException, InterruptedException {
        this.zoo = zoo;
        this.storage = storage;
        sendServers();
    }

    private void sendServers() throws KeeperException, InterruptedException {
        List<String> servers = zoo.getChildren(SERVERS, this);
        storage.tell(new StoreServer(servers), ActorRef.noSender());
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString());
        try {
            sendServers();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createServer(String localhost, String port) throws KeeperException, InterruptedException {
        zoo.create(SERVERS + "/" + localhost + ":" + port,
                port.getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL
        );
        storage.tell(new NextServer(localhost + ":" + port), ActorRef.noSender());
    }
}
