package com.vorin.exchange.matchingengine;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SimpleMatchingEngineTest {

    private IMatchingEngine matchingEngine;

    @Before
    public void setUp() {
        matchingEngine = new SimpleMatchingEngine();
    }

    @Test
    public void randomTest() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 20000),
                                                     new Order(222, "AAPL", Side.SELL, 30000),
                                                     new Order(333, "AAPL", Side.BUY, 30000),
                                                     new Order(444, "AAPL", Side.BUY, 5000),
                                                     new Order(555, "MSFT", Side.BUY, 20000),
                                                     new Order(666, "AAPL", Side.SELL, 25000));
        long[][] expected = {{}, {111}, {222}, {}, {}, {333, 444, 666}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldSellAll2() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 20000),
                                                     new Order(222, "AAPL", Side.SELL, 20000));
        long[][] expected = {{}, {111, 222}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldSellAll() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 100000),
                                                     new Order(222, "AAPL", Side.SELL, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 30000));
        long[][] expected = {{}, {222}, {333}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldMatchAll() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "AAPL", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 30000));
        long[][] expected = {{}, {}, {111, 222, 333}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldNotMatchAnything() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "MSFT", Side.BUY, 20000),
                                                     new Order(333, "GOGL", Side.SELL, 30000));
        long[][] expected = {{}, {}, {}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void allBuyOrders() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "MSFT", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.BUY, 10000));
        long[][] expected = {{}, {}, {}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void exampleTest1() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "MSFT", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 10000));
        long[][] expected = {{}, {}, {111, 333}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void exampleTest2() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "AAPL", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 100000));
        long[][] expected = {{}, {}, {111, 222}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    private void assertEquals(List<List<Long>> actual, long[][] expected) {
        assertThat(actual, is(convertArrayToList(expected)));
    }

    private List<List<Long>> convertArrayToList(long[][] array) {
        List<List<Long>> list = new ArrayList<>();
        for (long[] row : array) {
            List<Long> insideList = new ArrayList<>();
            for(long l : row) {
                insideList.add(l);
            }
            list.add(insideList);
        }
        return list;
    }

}
