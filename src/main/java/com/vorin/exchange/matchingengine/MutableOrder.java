package com.vorin.exchange.matchingengine;
/**
 * @author Adam
 */
public class MutableOrder extends Order implements IMutableOrder {

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
