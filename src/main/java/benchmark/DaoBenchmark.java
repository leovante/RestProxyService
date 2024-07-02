package benchmark;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.server.binding.RequestArgumentSatisfier;
import io.micronaut.runtime.Micronaut;
import io.micronaut.web.router.Router;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.openjdk.jmh.annotations.*;
import ru.vtb.stub.MicronautApp;
import ru.vtb.stub.controller.RequestController;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DaoBenchmark extends AbstractBenchmark {

    private RequestController requestController;
    private ApplicationContext context;
    private RequestArgumentSatisfier requestArgumentSatisfier;
    private Router router;

    @Setup
    public void init() {
        context = Micronaut.build("")
                .eagerInitSingletons(true)
                .mainClass(MicronautApp.class)
                .start();
        requestArgumentSatisfier = context.getBean(RequestArgumentSatisfier.class);
        router = context.getBean(Router.class);
        requestController = context.getBean(RequestController.class);
    }

    @TearDown
    public void closeContext() {
        context.close();
    }

    @Benchmark
    public void benchmarkSave() {
        var headers = new Header("name", "value");
        var resp = new Response(200, List.of(headers), null, null, false);
        requestController.putData(new StubData("local", "/passport/oauth2/token", HttpMethod.POST, 100, resp, List.of(resp)));
    }

    @Benchmark
    public void benchmarkFindAll() {
        requestController.getData(new GetDataBaseRequest("local", "/passport/oauth2/token", "POST"));
    }

}
