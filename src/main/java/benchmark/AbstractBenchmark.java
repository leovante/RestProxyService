package benchmark;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

public class AbstractBenchmark {
    private final static Integer MEASUREMENT_ITERATIONS = 10;
    private final static Integer WARMUP_ITERATIONS = 3;
    @Value("${jmh.result.path:/}")
    private String path;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // set the class name regex for benchmarks to search for to the current class
                .include("\\.*" + DaoBenchmark.class.getSimpleName() + "\\.*")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                .timeUnit(TimeUnit.MILLISECONDS)
                // do not use forking or the benchmark methods will not see references stored within its class
                .forks(0)
                // do not use multiple threads
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
//                .result(path)
                .result("src/main/resources/result.json") // set this to a valid filename if you want reports
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }

}
