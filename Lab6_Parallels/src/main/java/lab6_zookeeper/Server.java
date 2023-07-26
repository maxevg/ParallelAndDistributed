package lab6_zookeeper;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

public class Server {
    final private static String ZOO_HOST = "127.0.0.1:2181";
    final private static int TIME_OUT = 2500;
    final private static String LOCAL_HOST = "localhost";
    final private static String PORT = "8080";
    final private static String URL = "url";
    final private static String COUNT = "COUNT";
    final private static int TIME_OUT_SEC = 5;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        BasicConfigurator.configure();
        ActorSystem system = ActorSystem.create("routes");
        ActorRef storage = system.actorOf(Props.create(StorageActor.class));
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        Watcher empty = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
            }
        };
        ZooKeeper zoo = new ZooKeeper(ZOO_HOST, TIME_OUT, empty);
        final Http http = Http.get(system);
        ZooServer server = new ZooServer(zoo, storage);
        server.createServer(ZOO_HOST, PORT);
        final Flow<HttpRequest, HttpResponse, NotUsed> flow = createRoute(storage, http)
                .flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                flow,
                ConnectHttp.toHost("localhost", Integer.parseInt(PORT)),
                materializer
        );
        System.out.println("Server online at http://" + LOCAL_HOST + ":" + PORT + "/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());

    }

    private static CompletionStage<HttpResponse> singleRequest(Http http, String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    private static Route check(ActorRef storage, final Http http,Request request) {
        if (request.getCount() == 0) {
            return completeWithFuture(singleRequest(http, request.getUrl()));
        } else {
            request.countMinus();
            return completeWithFuture(
                    Patterns.ask(storage, new RandomServer(), Duration.ofSeconds(TIME_OUT_SEC))
                            .thenApply(req -> (String) req)
                            .thenCompose(req -> {
                                String singleRequestURL = "https://" + req + "/?url=" +
                                        request.getUrl() + "&count=" + request.getCount();
                                return singleRequest(http, singleRequestURL);
                            })
            );
        }
    }

    private static Route createRoute(ActorRef storage, final Http http) {
        return route(pathSingleSlash(() ->
                parameter(URL, url ->
                        parameter(COUNT, count ->
                                check(storage, http, new Request(url, count))
                        )
                )
        ));
    }
}
