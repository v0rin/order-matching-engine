import java.util.Iterator;

/**
 *
 * @author Adam
 */
public interface IOrderBook {

    Iterator<IOrder> getIteratorForMatchingOrders(IOrder order);

    void add(IOrder order);

}
