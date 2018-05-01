package com.vorin.exchange.matchingenginetuned;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

/**
 * @author Adam
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        loadTest();
    }

    private static void loadTest() throws InterruptedException {
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();

        final int orderCount = 1000_000_000;
        OrderGenerator orderGenerator = new OrderGenerator();

        Stopwatch sw = Stopwatch.createStarted();
        for (int i = 0; i < orderCount; i++) {
            matchingEngine.processOrder(orderGenerator.getNextRandom());
            Thread.sleep(10);
            if (i % 3000 == 0) System.out.println(String.format("processed %s orders", i));
        }
        long elapsed = sw.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("elapsed=" + elapsed);
        System.out.println("Unfullfilled order count=" + matchingEngine.getUnfulfilledOrders().size());
//        System.out.println("Unfullfilled orders=" + matchingEngine.getUnfulfilledOrders());
    }

}
