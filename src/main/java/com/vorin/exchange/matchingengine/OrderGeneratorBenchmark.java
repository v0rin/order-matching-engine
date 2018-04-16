package com.vorin.exchange.matchingengine;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 *
 * @author Adam
 */
@SuppressWarnings("checkstyle:all")
@Warmup(iterations = 8, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 15, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(3)
@State(Scope.Benchmark)
public class OrderGeneratorBenchmark {

    private OrderGenerator orderGenerator;

    @Setup
    public void setUp() {
        orderGenerator = new OrderGenerator();
    }

    @Benchmark
    public void randomOrder(Blackhole bh) {
        bh.consume(orderGenerator.getNextRandom());
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(OrderGeneratorBenchmark.class.getName() + ".*")
                .shouldDoGC(true)
                .build();

        new Runner(opts).run();
    }

}
