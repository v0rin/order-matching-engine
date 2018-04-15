/**
 * @author Adam
 */
public class Order implements IOrder {

    protected long orderId;
    protected String product;
    protected Side customerSide;
    protected long size;

    public Order(long orderId, String product, Side customerSide, long size) {
        if (customerSide == null) throw new RuntimeException("CustomerSide cannot be null");
        if (product == null) throw new RuntimeException("Product cannot be null");

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
    public Side getCustomerSide() {
        return customerSide;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerSide == null) ? 0 : customerSide.hashCode());
        result = prime * result + (int) (orderId ^ (orderId >>> 32));
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));
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
