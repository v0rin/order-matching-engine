package com.vorin.exchange.matchingengine;
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

    private Map<Key, Deque<IOrder>> orderMap;


    public OrderBookArrayDeque() {
        orderMap = new HashMap<>();
    }

    @Override
    public Iterator<IOrder> getIteratorForMatchingOrders(IOrder order) {
        Side customerSide = order.getCustomerSide() == Side.BUY ? Side.SELL : Side.BUY;
        Deque<IOrder> orders = orderMap.computeIfAbsent(new Key(order.getProduct(), customerSide),
                                                        str -> new ArrayDeque<IOrder>());
        return orders.iterator();
    }

    @Override
    public void add(IOrder order) {
        Deque<IOrder> list = orderMap.computeIfAbsent(new Key(order.getProduct(), order.getCustomerSide()),
                                                      str -> new ArrayDeque<>());
        list.add(order);
    }

    @Override
    public List<IOrder> getUnfullfilledOrders() {
        return orderMap.values().stream().flatMap(ordersForSecurity -> ordersForSecurity.stream()).collect(toList());
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
