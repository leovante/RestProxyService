package ru.vtb.stub;

import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtb.stub.controller.RequestController;
import ru.vtb.stub.dto.GetDataBaseRequest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(SpringExtension.class)
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
    public void closeContext(){
        context.close();
    }

    @Benchmark
    public void benchmarkFindAll() {
        DaoBenchmark.requestController.getData(new GetDataBaseRequest("local", "/passport/oauth2/token", "POST"));
    }

}
