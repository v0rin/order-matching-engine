import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adam
 */
public class OrderBook implements IOrderBook {

    private Map<Key, List<IOrder>> orderMap;


    public OrderBook() {
        orderMap = new HashMap<>();
    }

    @Override
    public Iterator<IOrder> getIteratorForMatchingOrders(IOrder order) {
        Side customerSide = order.getCustomerSide() == Side.BUY ? Side.SELL : Side.BUY;
        List<IOrder> orders = orderMap.computeIfAbsent(new Key(order.getProduct(), customerSide),
                                                       str -> new LinkedList<IOrder>());
        return orders.iterator();
    }

    @Override
    public void add(IOrder order) {
        List<IOrder> list = orderMap.computeIfAbsent(new Key(order.getProduct(), order.getCustomerSide()),
                                                     str -> new LinkedList<>());
        list.add(order);
    }

    private static class Key {
        String product;
        Side customerSide;

        Key(String product, Side customerSide) {
            this.product = product;
            this.customerSide = customerSide;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((customerSide == null) ? 0 : customerSide.hashCode());
            result = prime * result + ((product == null) ? 0 : product.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Key))
                return false;
            Key other = (Key) obj;
            if (customerSide != other.customerSide)
                return false;
            if (product == null) {
                if (other.product != null)
                    return false;
            } else if (!product.equals(other.product))
                return false;
            return true;
        }
    }

}
