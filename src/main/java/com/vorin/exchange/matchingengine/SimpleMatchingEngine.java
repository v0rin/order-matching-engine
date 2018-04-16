package com.vorin.exchange.matchingengine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Assumptions:
 * <li>Since requirements state that orders will be delivered sequentially from the same thread I don't care
 * about thread-safety
 *
 * </br></br>
 * @author Adam
 */
public class SimpleMatchingEngine implements IMatchingEngine {

    private enum Match {
        NEW_MATCHED, EXISTING_MATCHED, BOTH_MATCHED, NONE_MATCHED
    }

    private IOrderBook orderBook;

    public SimpleMatchingEngine(IOrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public SimpleMatchingEngine() {
        this(new OrderBookLinkedList());
    }

    @Override
    public List<Long> processOrder(IOrder order) {
        List<Long> matchedOrders = new ArrayList<>();
        Iterator<IOrder> orderIter = orderBook.getIteratorForMatchingOrders(order);

        IMutableOrder newOrder = new MutableOrder(order);

        while (orderIter.hasNext()) {
            IMutableOrder existingOrder = (IMutableOrder)orderIter.next();
            Match match = checkOrdersMatch(newOrder, existingOrder);
            switch (match) {
                case BOTH_MATCHED:
                    matchedOrders.add(existingOrder.getOrderId());
                    orderIter.remove();
                    matchedOrders.add(newOrder.getOrderId());
                    return matchedOrders;

                case NEW_MATCHED:
                    matchedOrders.add(newOrder.getOrderId());
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

    @Override
    public List<IOrder> getUnfulfilledOrders() {
        return orderBook.getUnfullfilledOrders();
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
