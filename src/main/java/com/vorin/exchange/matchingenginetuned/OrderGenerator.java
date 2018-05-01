package com.vorin.exchange.matchingenginetuned;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Adam
 */
public class OrderGenerator {

    private static final String[] SECURITIES = {"AAPL", "MSFT", "GOOG", "FB", "AMZN", "ORCL", "GS",
                                                "QCOM", "NVDA", "SNAP", "AVGO", "BRK", "TSLA", "INTC"};
    private static final int MAX_ORDER_SIZE = 100;

    private long orderId;

    public IOrder getNextRandom() {
        return new Order(orderId++,
                         SECURITIES[ThreadLocalRandom.current().nextInt(SECURITIES.length)],
                         ThreadLocalRandom.current().nextBoolean(),
                         ThreadLocalRandom.current().nextInt(1, MAX_ORDER_SIZE));
    }

}
