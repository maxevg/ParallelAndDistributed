import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class Server {
    private static final String SERVER = "localhost";
    private static final Integer PORT = 8080;
    private static final String TEST_URL = "testUrl";
    private static final String COUNT = "count";
    private static final int MAP_ASYNC = 1;
    private static final Integer TIME_OUT_MILLIS = 5000;

    public static void main(String[] args) throws IOException {
        System.out.println("Start!");
        ActorSystem system = ActorSystem.create("routes");
        ActorRef actor = system.actorOf(Props.create(ActorRec.class));
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createFlow(http, system, materializer, actor);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(SERVER, PORT),
                materializer
        );
    }

    private static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(Http http, ActorSystem system, ActorMaterializer materializer, ActorRef actor) {
        return  Flow.of(HttpRequest.class)
                .map((req) -> {
                    Query q = req.getUri().query();
                    String url = q.get(TEST_URL).get();
                    int count = Integer.parseInt(q.get(COUNT).get());
                    System.out.println(url + " " + count);
                    return new Pair<String, Integer>(url, count);
                })
                .mapAsync(MAP_ASYNC, req -> {
                    CompletionStage<Object> stage = Patterns.ask(actor, new GetMessage(req.first()), Duration.ofSeconds(TIME_OUT_MILLIS));
                    return stage.thenCompose(res -> {
                        if ((Integer) res >= 0) {
                            return CompletableFuture.completedFuture(new Pair<>(req.first(), (Integer) res));
                        }
                        Flow<Pair<String, Integer>, Integer, NotUsed> flow =
                                Flow.<Pair<String, Integer>>create()
                                        .mapConcat(pair -> new ArrayList<>(Collections.nCopies(pair.second(), pair.first())))
                                        .mapAsync(req.second(), url -> {
                                            long start = System.currentTimeMillis();
                                            asyncHttpClient().prepareGet(url).execute();
                                            long finish = System.currentTimeMillis();
                                            return CompletableFuture.completedFuture((int) (finish - start));
                                        });
                        return Source
                                .single(req)
                                .via(flow)
                                .toMat(Sink.fold((int) 0, Integer::sum), Keep.right())
                                .run(materializer)
                                .thenApply(sum -> new Pair<>(req.first(),  (sum / req.second())));
                    });
                })
                .map(req -> {
                    actor.tell(new StorageMessage(req.first(), req.second()), ActorRef.noSender());
                    return HttpResponse.create().withEntity(req.second().toString() + '\n');
                });
    }

}
