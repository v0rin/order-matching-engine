package com.vorin.exchange.matchingenginetuned;
/**
 *
 * @author Adam
 */
public interface IOrder {

    /**
     * @return Unique id of the order
     */
    long getOrderId();

    /**
     * @return Key for the financial product
     */
    String getProduct();

    /**
     * @return Buy or Sell order type
     */
    boolean getCustomerSide();

    /**
     * @return Amount of the order in AUD cents
     */
    int getSize();

}
