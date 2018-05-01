package com.vorin.exchange.matchingenginetuned;
import java.util.List;

/**
 *
 * @author Adam
 */
public interface IMatchingEngine {

    /**
     * <p>Processes given order.
     *
     *  @return list of order IDs that were matched, ordered oldest to newest, empty list if no orders matched
     */
    List<Long> processOrder(IOrder order);

    List<IOrder> getUnfulfilledOrders();

}
