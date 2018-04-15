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


    public SimpleMatchingEngine() {
        orderBook = new OrderBook();
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
                    // The requirements stated that the order being processed is not to be returned if matched.
                    // It seems unusual but I implemented it this way.
                    // If it was the below line should be uncommented
//                    matchedOrders.add(newOrder.getOrderId());
                    return matchedOrders;

                case NEW_MATCHED:
                    // Same case as above
//                    matchedOrders.add(newOrder.getOrderId());
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
