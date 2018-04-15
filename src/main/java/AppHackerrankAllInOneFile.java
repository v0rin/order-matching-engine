import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * <p>NOTES:
 * <li>Test done in this unusual way since JUnit is not available in this exercise format
 *
 * @author Adam
 */
public class AppHackerrankAllInOneFile {

    public static void main(String[] args) {
        IMatchingEngine matchingEngine = new SimpleMatchingEngine();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            printResult(matchingEngine.processOrder(parseOrder(scanner.nextLine())));
        }
        scanner.close();

//        runTests();
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

    private static interface IMatchingEngine {

        /**
         * <p>Processes given order.
         *
         *  @return list of order IDs that were matched, ordered oldest to newest, empty list if no orders matched
         */
        List<Long> processOrder(IOrder order);

    }

    private static interface IOrderBook {

        Iterator<IOrder> getOrderIteratorForProduct(String product);

        void add(IOrder order);

    }

    private static interface IMutableOrder extends IOrder {

        void setSize(long size);

    }

    private static interface IOrder {

        /**
         * @return Unique id of the order
         */
        long getOrderId();

        /**
         * @return Key for the financial product
         */
        String getProduct();

        /**
         * @return Buy or Sell order type
         */
        Side getCustomerSide();

        /**
         * @return Amount of the order in AUD cents
         */
        long getSize();

    }

    /**
     * <p>Assumptions:
     * <li>Since requirements state that orders will be delivered sequentially from the same thread I don't care
     * about thread-safety
     *
     * </br></br>
     * @author Adam
     */
    private static class SimpleMatchingEngine implements IMatchingEngine {

        private enum Match {
            NEW_MATCHED, EXISTING_MATCHED, BOTH_MATCHED, NONE_MATCHED
        }

        private IOrderBook orderBook;


        public SimpleMatchingEngine() {
            orderBook = new OrderBook();
        }

        @Override
        public List<Long> processOrder(IOrder order) {
            List<Long> matchedOrders = new ArrayList<>();
            Iterator<IOrder> orderIter = orderBook.getOrderIteratorForProduct(order.getProduct());

            IMutableOrder newOrder = new MutableOrder(order);

            while (orderIter.hasNext()) {
                IMutableOrder existingOrder = (IMutableOrder)orderIter.next();
                Match match = checkOrdersMatch(newOrder, existingOrder);
                switch (match) {
                case BOTH_MATCHED:
                    matchedOrders.add(existingOrder.getOrderId());
                    orderIter.remove();
                    // The requirements stated that the order being processed is not to be returned if matched.
                    // It seems unusual but I implemented it this way.
                    // If it was the below line should be uncommented
                    //                       matchedOrders.add(newOrder.getOrderId());
                    return matchedOrders;

                case NEW_MATCHED:
                    // Same case as above
                    //                       matchedOrders.add(newOrder.getOrderId());
                    existingOrder.setSize(existingOrder.getSize() - newOrder.getSize());
                    return matchedOrders;

                case EXISTING_MATCHED:
                    matchedOrders.add(existingOrder.getOrderId());
                    orderIter.remove();
                    newOrder.setSize(newOrder.getSize() - existingOrder.getSize());
                    break;

                case NONE_MATCHED:
                    break;

                default:
                    throw new RuntimeException("That should not happen! match=" + match);
                }
            }
            // if not matched fully at this point add to order book
            orderBook.add(newOrder);

            return matchedOrders;
        }

        private Match checkOrdersMatch(IOrder newOrder, IOrder existingOrder) {
            if (newOrder.getCustomerSide() == existingOrder.getCustomerSide()) return Match.NONE_MATCHED;
            if (!newOrder.getProduct().equals(existingOrder.getProduct())) return Match.NONE_MATCHED;

            long sizeDiff = newOrder.getSize() - existingOrder.getSize();
            if (sizeDiff == 0) {
                return Match.BOTH_MATCHED;
            }
            else if (sizeDiff > 0) {
                return Match.EXISTING_MATCHED;
            }
            else {
                return Match.NEW_MATCHED;
            }
        }

    }

    private static class OrderBook implements IOrderBook {

        private Map<String, List<IOrder>> orderMap;

        public OrderBook() {
            orderMap = new HashMap<>();
        }

        @Override
        public Iterator<IOrder> getOrderIteratorForProduct(String product) {
            List<IOrder> orders = orderMap.computeIfAbsent(product, str -> new LinkedList<IOrder>());
            return orders.iterator();
        }

        @Override
        public void add(IOrder order) {
            List<IOrder> list = orderMap.putIfAbsent(order.getProduct(), new LinkedList<>());
            list.add(order);
        }

    }

    private static class Order implements IOrder {

        protected long orderId;
        protected String product;
        protected Side customerSide;
        protected long size;

        public Order(long orderId, String product, Side customerSide, long size) {
            if (customerSide == null) throw new RuntimeException("CustomerSide cannot be null");
            if (product == null) throw new RuntimeException("Product cannot be null");

            this.orderId = orderId;
            this.product = product;
            this.customerSide = customerSide;
            // I'm not guarding agains <= 0 since the requirements say:
            // "for new orders it will always be a non-zero positive value"
            this.size = size;
        }

        @Override
        public long getOrderId() {
            return orderId;
        }

        @Override
        public String getProduct() {
            return product;
        }

        @Override
        public Side getCustomerSide() {
            return customerSide;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((customerSide == null) ? 0 : customerSide.hashCode());
            result = prime * result + (int) (orderId ^ (orderId >>> 32));
            result = prime * result + ((product == null) ? 0 : product.hashCode());
            result = prime * result + (int) (size ^ (size >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Order)) return false;

            Order other = (Order) obj;
            if (customerSide != other.customerSide) return false;
            if (orderId != other.orderId) return false;
            if (product == null) {
                if (other.product != null)
                    return false;
            }
            else if (!product.equals(other.product)) return false;
            if (size != other.size) return false;
            return true;
        }

        @Override
        public String toString() {
            return "Order [orderId=" + orderId + ", product=" + product + ", customerSide=" + customerSide + ", size="
                    + size + "]";
        }

    }

    private static class MutableOrder extends Order implements IMutableOrder {

        public MutableOrder(long orderId, String product, Side customerSide, long size) {
            super(orderId, product, customerSide, size);
        }

        public MutableOrder(IOrder order) {
            this(order.getOrderId(), order.getProduct(), order.getCustomerSide(), order.getSize());
        }

        @Override
        public void setSize(long size) {
            this.size = size;
        }

    }

    public enum Side {
        BUY, SELL;
    }

}

