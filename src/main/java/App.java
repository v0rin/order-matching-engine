import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * <p>NOTES:
 * <li>Test done in this unusual way since JUnit is not available in this exercise format
 *
 * @author Adam
 */
public class App {

    public static void main(String[] args) {
//        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            printResult(matchingEngine.processOrder(parseOrder(scanner.nextLine())));
//        }
//        scanner.close();

        runTests();
    }

    private static void printResult(List<Long> result) {
        StringJoiner sj = new StringJoiner(",");
        result.forEach(l -> sj.add(Long.toString(l)));
        System.out.println(result.size() + " matches: " + sj.toString());
    }

    private static IOrder parseOrder(String orderAsStr) {
        String[] params = orderAsStr.split(" ");
        return new Order(Long.parseLong(params[0]),
                         params[1],
                         Side.valueOf(params[2]),
                         Long.parseLong(params[3]));
    }

    private static void runTests() {
        randomTest();
        shouldSellAll2();
        shouldMatchAll();
        shouldSellAll();
        allBuyOrders();
        nullConsumerSideShouldResultInException();
        nullProductShouldResultInException();
        exampleTest1();
        exampleTest2();
    }

    private static void randomTest() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 20000),
                                                     new Order(222, "AAPL", Side.SELL, 30000),
                                                     new Order(333, "AAPL", Side.BUY, 30000),
                                                     new Order(444, "AAPL", Side.BUY, 5000),
                                                     new Order(555, "MSFT", Side.BUY, 20000),
                                                     new Order(666, "AAPL", Side.SELL, 25000));
        long[][] expected = {{}, {111}, {222}, {}, {}, {333, 444}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    private static void shouldSellAll2() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 20000),
                                                     new Order(222, "AAPL", Side.SELL, 20000));
        long[][] expected = {{}, {111}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    private static void shouldSellAll() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 100000),
                                                     new Order(222, "AAPL", Side.SELL, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 30000));
        long[][] expected = {{}, {}, {}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    private static void shouldMatchAll() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "AAPL", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 30000));
        long[][] expected = {{}, {}, {111, 222}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    private static void allBuyOrders() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "MSFT", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.BUY, 10000));
        long[][] expected = {{}, {}, {}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    private static void nullConsumerSideShouldResultInException() {
        try {
            new Order(111, "AAPL", null, 10000);
        }
        catch (Exception e){
            return;
        }
        throw new RuntimeException("Should throw an exception, but it didn't");
    }

    private static void nullProductShouldResultInException() {
        try {
            new Order(111, null, Side.BUY, 10000);
        }
        catch (Exception e){
            return;
        }
        throw new RuntimeException("Should throw an exception, but it didn't");
    }

    private static void exampleTest1() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "MSFT", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 10000));
        long[][] expected = {{}, {}, {111}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    private static void exampleTest2() {
        // given
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();
        List<IOrder> ordersToProcess = Arrays.asList(new Order(111, "AAPL", Side.BUY, 10000),
                                                     new Order(222, "AAPL", Side.BUY, 20000),
                                                     new Order(333, "AAPL", Side.SELL, 100000));
        long[][] expected = {{}, {}, {111, 222}};

        // when
        List<List<Long>> actual = ordersToProcess.stream().map(matchingEngine::processOrder).collect(toList());

        // then
        assertEquals(expected, actual);
    }

    /**
     * Convenience test method
     */
    private static void assertEquals(List<List<Long>> expected, List<List<Long>> actual) {
        if (!expected.equals(actual)) throw new AssertionError("actual is " + actual + " but expected was " + expected);
    }

    /**
     * Convenience test method
     */
    private static void assertEquals(long[][] expected, List<List<Long>> actual) {
        assertEquals(convertArrayToList(expected), actual);
    }

    /**
     * Convenience test method
     */
    private static List<List<Long>> convertArrayToList(long[][] array) {
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
