package com.vorin.exchange.matchingenginetuned;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adam
 */
public class OrderBookArrayDeque implements IOrderBook {

    private Map<Integer, Deque<IOrder>> orderMap;

    public OrderBookArrayDeque() {
        orderMap = new HashMap<>();
    }

    @Override
    public Iterator<IOrder> getIteratorForMatchingOrders(IOrder order) {
        boolean customerSide = !order.getCustomerSide();
        Deque<IOrder> orders = orderMap.get(getIntegerKey(order.getProduct(), customerSide));
        if (orders == null) {
            orders = new ArrayDeque<>();
            orderMap.put(getIntegerKey(order.getProduct(), customerSide), orders);
        }
        return orders.iterator();
    }

    @Override
    public void add(IOrder order) {
        Deque<IOrder> orders = orderMap.get(getIntegerKey(order.getProduct(), order.getCustomerSide()));
        if (orders == null) {
            orders = new ArrayDeque<>();
            orderMap.put(getIntegerKey(order.getProduct(), order.getCustomerSide()), orders);
        }

        orders.add(order);
    }

    @Override
    public List<IOrder> getUnfullfilledOrders() {
        return orderMap.values().stream().flatMap(ordersForSecurity -> ordersForSecurity.stream()).collect(toList());
    }

    private static int getIntegerKey(String product, boolean customerSide) {
        final int prime = 31;
        int result = 1;
        result = prime * result + (customerSide ? 1231 : 1237);
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        return result;
    }

}
