package ru.vtb.stub;

import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    public void setActorDAO(RequestController requestController) {
        DaoBenchmark.requestController = requestController;
    }

    @Benchmark
    public void benchmarkFindAll() {
        DaoBenchmark.requestController.getData(new GetDataBaseRequest("local", "/passport/oauth2/token", "POST"));
    }

}
