package com.vorin.exchange.matchingenginetuned;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface IOrderBook {

    Iterator<IOrder> getIteratorForMatchingOrders(IOrder order);

    void add(IOrder order);

    List<IOrder> getUnfullfilledOrders();

}
