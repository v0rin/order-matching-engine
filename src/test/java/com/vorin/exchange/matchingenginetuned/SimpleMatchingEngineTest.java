package com.vorin.exchange.matchingenginetuned;

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
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 20000),
                                                     new Order(222, "AAPL", false, 30000),
                                                     new Order(333, "AAPL", true, 30000),
                                                     new Order(444, "AAPL", true, 5000),
                                                     new Order(555, "MSFT", true, 20000),
                                                     new Order(666, "AAPL", false, 25000));
        long[][] expected = {{}, {111}, {222}, {}, {}, {333, 444, 666}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldSellAll2() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 20000),
                                                     new Order(222, "AAPL", false, 20000));
        long[][] expected = {{}, {111, 222}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldSellAll() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 100000),
                                                     new Order(222, "AAPL", false, 20000),
                                                     new Order(333, "AAPL", false, 30000));
        long[][] expected = {{}, {222}, {333}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldMatchAll() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 10000),
                                                     new Order(222, "AAPL", true, 20000),
                                                     new Order(333, "AAPL", false, 30000));
        long[][] expected = {{}, {}, {111, 222, 333}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void shouldNotMatchAnything() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 10000),
                                                     new Order(222, "MSFT", true, 20000),
                                                     new Order(333, "GOGL", false, 30000));
        long[][] expected = {{}, {}, {}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void allBuyOrders() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 10000),
                                                     new Order(222, "MSFT", true, 20000),
                                                     new Order(333, "AAPL", true, 10000));
        long[][] expected = {{}, {}, {}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void exampleTest1() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 10000),
                                                     new Order(222, "MSFT", true, 20000),
                                                     new Order(333, "AAPL", false, 10000));
        long[][] expected = {{}, {}, {111, 333}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(actual, expected);
    }

    @Test
    public void exampleTest2() {
        // given
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", true, 10000),
                                                     new Order(222, "AAPL", true, 20000),
                                                     new Order(333, "AAPL", false, 100000));
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
