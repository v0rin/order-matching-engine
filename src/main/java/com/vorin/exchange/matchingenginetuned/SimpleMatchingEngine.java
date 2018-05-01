package com.vorin.exchange.matchingenginetuned;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Adam
 */
public class SimpleMatchingEngine implements IMatchingEngine {

    private static final int NEW_MATCHED = 0x0;
    private static final int EXISTING_MATCHED = 0x1;
    private static final int BOTH_MATCHED = 0x2;
    private static final int NONE_MATCHED = 0x3;

    private IOrderBook orderBook;

    public SimpleMatchingEngine(IOrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public SimpleMatchingEngine() {
        this(new OrderBookArrayDeque());
    }

    @Override
    public List<Long> processOrder(IOrder order) {
        List<Long> matchedOrders = new ArrayList<>();
        Iterator<IOrder> orderIter = orderBook.getIteratorForMatchingOrders(order);

        IMutableOrder newOrder = new MutableOrder(order);

        while (orderIter.hasNext()) {
            IMutableOrder existingOrder = (IMutableOrder)orderIter.next();
            int match = checkOrdersMatch(newOrder, existingOrder);
            if (match == BOTH_MATCHED) {
                matchedOrders.add(existingOrder.getOrderId());
                orderIter.remove();
                matchedOrders.add(newOrder.getOrderId());
                return matchedOrders;
            }
            else if(match == NEW_MATCHED) {
                matchedOrders.add(newOrder.getOrderId());
                existingOrder.setSize(existingOrder.getSize() - newOrder.getSize());
                return matchedOrders;
            }
            else if(match == EXISTING_MATCHED) {
                matchedOrders.add(existingOrder.getOrderId());
                orderIter.remove();
                newOrder.setSize(newOrder.getSize() - existingOrder.getSize());
                break;
            }
            else if (match == NONE_MATCHED) {
                break;
            }
            else {
                throw new RuntimeException("That should not happen! match=" + match);
            }

//            switch (match) {
//                case BOTH_MATCHED:
//                    matchedOrders.add(existingOrder.getOrderId());
//                    orderIter.remove();
//                    matchedOrders.add(newOrder.getOrderId());
//                    return matchedOrders;
//
//                case NEW_MATCHED:
//                    matchedOrders.add(newOrder.getOrderId());
//                    existingOrder.setSize(existingOrder.getSize() - newOrder.getSize());
//                    return matchedOrders;
//
//                case EXISTING_MATCHED:
//                    matchedOrders.add(existingOrder.getOrderId());
//                    orderIter.remove();
//                    newOrder.setSize(newOrder.getSize() - existingOrder.getSize());
//                    break;
//
//                case NONE_MATCHED:
//                    break;
//
//                default:
//                    throw new RuntimeException("That should not happen! match=" + match);
//            }
        }
        // if not matched fully at this point add to order book
        orderBook.add(newOrder);

        return matchedOrders;
    }

    @Override
    public List<IOrder> getUnfulfilledOrders() {
        return orderBook.getUnfullfilledOrders();
    }

    private int checkOrdersMatch(IOrder newOrder, IOrder existingOrder) {
        if (newOrder.getCustomerSide() == existingOrder.getCustomerSide()) return NONE_MATCHED;
        if (!newOrder.getProduct().equals(existingOrder.getProduct())) return NONE_MATCHED;

        int sizeDiff = newOrder.getSize() - existingOrder.getSize();
        if (sizeDiff == 0) {
            return BOTH_MATCHED;
        }
        else if (sizeDiff > 0) {
            return EXISTING_MATCHED;
        }
        else {
            return NEW_MATCHED;
        }
    }

}
