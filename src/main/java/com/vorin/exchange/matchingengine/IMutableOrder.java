package com.vorin.exchange.matchingengine;

/**
 *
 * @author Adam
 */
public interface IMutableOrder extends IOrder {

    void setSize(long size);

}
