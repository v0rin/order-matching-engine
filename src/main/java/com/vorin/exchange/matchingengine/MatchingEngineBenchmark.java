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
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@State(Scope.Benchmark)
/**
 * for ORDER_COUNT = 100
 * Benchmark                           Mode  Cnt      Score     Error  Units
 * MatchingEngineBenchmark.arrayDeque  avgt   20  12720.368 ± 244.916  ns/op
 * MatchingEngineBenchmark.linkedList  avgt   20  13526.286 ± 145.378  ns/op
 *
 * for ORDER_COUNT = 1_000
 * Benchmark                           Mode  Cnt       Score      Error  Units
 * MatchingEngineBenchmark.arrayDeque  avgt   20  132141.872 ± 2424.578  ns/op
 * MatchingEngineBenchmark.linkedList  avgt   20  134021.856 ± 3825.727  ns/op
 *
 * for ORDER_COUNT = 100_000
 * Benchmark                           Mode  Cnt         Score        Error  Units
 * MatchingEngineBenchmark.arrayDeque  avgt   20  13043476.073 ± 205455.449  ns/op
 * MatchingEngineBenchmark.linkedList  avgt   20  13330323.198 ± 232670.517  ns/op
 * @author Adam
 */
public class MatchingEngineBenchmark {

    private static final int ORDER_COUNT = 1_000;

    private IOrder[] orders = new IOrder[ORDER_COUNT];
    private com.vorin.exchange.matchingenginetuned.IOrder[] ordersTuned =
            new com.vorin.exchange.matchingenginetuned.IOrder[ORDER_COUNT];

    private IMatchingEngine matchingEngine1;
    private IMatchingEngine matchingEngine2;
    private com.vorin.exchange.matchingenginetuned.IMatchingEngine matchingEngineTuned;

    @Setup
    public void setUp() {
        matchingEngine1 = new SimpleMatchingEngine(new OrderBookArrayDeque());
        matchingEngine2 = new SimpleMatchingEngine(new OrderBookLinkedList());
        matchingEngineTuned = new com.vorin.exchange.matchingenginetuned.SimpleMatchingEngine(
                            new com.vorin.exchange.matchingenginetuned.OrderBookArrayDeque());
        OrderGenerator orderGenerator = new OrderGenerator();
        com.vorin.exchange.matchingenginetuned.OrderGenerator orderGeneratorTuned =
                new com.vorin.exchange.matchingenginetuned.OrderGenerator();

        for (int i = 0; i < ORDER_COUNT; i++) {
            orders[i] = orderGenerator.getNextRandom();
            ordersTuned[i] = orderGeneratorTuned.getNextRandom();
        }
    }

//    @Benchmark
    public void arrayDequeSimpleMatchingEngine(Blackhole bh) {
        for (int i = 0; i < ORDER_COUNT; i++) {
            bh.consume(matchingEngine1.processOrder(orders[i]));
        }
    }

    @Benchmark
    public void arrayDequeSimpleMatchingEngineTuned(Blackhole bh) {
        for (int i = 0; i < ORDER_COUNT; i++) {
            bh.consume(matchingEngineTuned.processOrder(ordersTuned[i]));
        }
    }

//    @Benchmark
    public void linkedList(Blackhole bh) {
        for (int i = 0; i < ORDER_COUNT; i++) {
            bh.consume(matchingEngine2.processOrder(orders[i]));
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
