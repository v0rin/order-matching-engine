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
@Warmup(iterations = 10, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 15, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(5)
@State(Scope.Benchmark)
public class MatchingEngineBenchmark {

    private static final int ORDER_COUNT = 1_000;

    private IMatchingEngine matchingEngine1;
    private IMatchingEngine matchingEngine2;
    private OrderGenerator orderGenerator;

    @Setup
    public void setUp() {
        matchingEngine1 = new SimpleMatchingEngine(new OrderBookArrayDeque());
        matchingEngine2 = new SimpleMatchingEngine(new OrderBookLinkedList());
        orderGenerator = new OrderGenerator();
    }

    @Benchmark
    public void arrayDeque(Blackhole bh) {
        for (int i = 0; i < ORDER_COUNT; i++) {
            bh.consume(matchingEngine1.processOrder(orderGenerator.getNextRandom()));
        }
    }

    @Benchmark
    public void linkedList(Blackhole bh) {
        for (int i = 0; i < ORDER_COUNT; i++) {
            bh.consume(matchingEngine2.processOrder(orderGenerator.getNextRandom()));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(MatchingEngineBenchmark.class.getName() + ".*")
                .shouldDoGC(true)
                .build();

        new Runner(opts).run();
    }

}
