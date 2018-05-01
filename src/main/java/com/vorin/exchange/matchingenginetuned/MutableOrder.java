package com.vorin.exchange.matchingenginetuned;
/**
 * @author Adam
 */
public class MutableOrder extends Order implements IMutableOrder {

    public MutableOrder(long orderId, String product, boolean customerSide, int size) {
        super(orderId, product, customerSide, size);
    }

    public MutableOrder(IOrder order) {
        this(order.getOrderId(), order.getProduct(), order.getCustomerSide(), order.getSize());
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

}
