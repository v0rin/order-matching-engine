package com.vorin.exchange.matchingenginetuned;
/**
 * @author Adam
 */
public class Order implements IOrder {

    protected long orderId;
    protected String product;
    protected boolean customerSide;
    protected int size;

    public Order(long orderId, String product, boolean customerSide, int size) {
        this.orderId = orderId;
        this.product = product;
        this.customerSide = customerSide;
        // I'm not guarding agains <= 0 since the requirements say:
        // "for new orders it will always be a non-zero positive value"
        this.size = size;
    }

    @Override
    public long getOrderId() {
        return orderId;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public boolean getCustomerSide() {
        return customerSide;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (customerSide ? 1231 : 1237);
        result = prime * result + (int) (orderId ^ (orderId >>> 32));
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;

        Order other = (Order) obj;
        if (customerSide != other.customerSide) return false;
        if (orderId != other.orderId) return false;
        if (product == null) {
            if (other.product != null)
                return false;
        }
        else if (!product.equals(other.product)) return false;
        if (size != other.size) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", product=" + product + ", customerSide=" + customerSide + ", size="
                + size + "]";
    }

}
