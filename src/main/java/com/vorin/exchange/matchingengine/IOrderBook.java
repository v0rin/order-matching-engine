package com.vorin.exchange.matchingengine;
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
