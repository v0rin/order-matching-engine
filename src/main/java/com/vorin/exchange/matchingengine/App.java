package com.vorin.exchange.matchingengine;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

/**
 * @author Adam
 */
public class App {

    public static void main(String[] args) {
        loadTest();
    }

    private static void loadTest() {
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();

        final int orderCount = 1_000_000;
        OrderGenerator orderGenerator = new OrderGenerator();

        Stopwatch sw = Stopwatch.createStarted();
        for (int i = 0; i < orderCount; i++) {
            matchingEngine.processOrder(orderGenerator.getNextRandom());
        }
        long elapsed = sw.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("elapsed=" + elapsed);
        System.out.println("Unfullfilled order count=" + matchingEngine.getUnfulfilledOrders().size());
//        System.out.println("Unfullfilled orders=" + matchingEngine.getUnfulfilledOrders());
    }

}
