package benchmark;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.SpringBootApp;
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
@Profile("jmh")
public class DaoBenchmark extends AbstractBenchmark {

    private static RequestController requestController;

    private ConfigurableApplicationContext context;

    @Setup
    public void init() {
        context = SpringApplication.run(SpringBootApp.class);
        context.registerShutdownHook();

        requestController = context.getBean(RequestController.class);
    }

    @TearDown
    public void closeContext() {
        context.close();
    }

    @Benchmark
    public void benchmarkFindAll() {
        DaoBenchmark.requestController.getData(new GetDataBaseRequest("local", "/passport/oauth2/token", "POST"));
    }

    @Benchmark
    public void benchmarkSave() {
        var headers = new Header("name", "value");
        var resp = new Response(200, List.of(headers), "body", null, 0);
        DaoBenchmark.requestController.putData(new StubData("team", "path", RequestMethod.GET, 100, resp, List.of(resp)));
    }

}
