package com.nrifintech.cms.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    /**
     * It updates the status of all orders with status 0 (pending) to 2 (archived)
     * if the order was
     * placed on the date passed as a parameter
     * 
     * @param date The date on which the order was placed.
     */
    @Modifying
    @Transactional
    @Query(value = "update orders set status = 2 where status = 0 and Cast(order_placed as date) = :date", nativeQuery = true)
    public void autoArchive(@Param("date") String date);

    /**
     * It returns a list of tuples, each tuple contains a date and a value, the date
     * is the date of the
     * order, the value is the sum of the amount of the orders that were delivered
     * on that date
     * 
     * @param date1 The start date of the range
     * @param date2 The end date of the range
     * @return A list of tuples.
     */
    @Query(value = "select Cast(Cast(order_delivered as date) as char) as date , sum(amount) as value from orders,transaction  where orders.transaction_id = transaction.id group by date having date between :date1 and :date2", nativeQuery = true)
    public List<Tuple> getDaybyDaySales(@Param("date1") String date1, @Param("date2") String date2);

    /**
     * It returns the total sales of the store between two dates
     * 
     * @param date1 The start date of the period
     * @param date2 The end date of the period
     * @return A double value
     */

    @Query(value = "select  sum(amount) as value from orders,transaction  where orders.transaction_id = transaction.id and Cast(order_delivered as date) between :date1 and :date2", nativeQuery = true)
    public Optional<Double> getTotalSales(@Param("date1") String date1, @Param("date2") String date2);

    /**
     * It returns a list of tuples, each tuple containing the status of the order
     * and the count of orders
     * with that status
     * 
     * @param date1 The start date of the range
     * @param date2 The end date of the range.
     * @return A list of tuples.
     */
    @Query(value = "select status , count(id) from orders where cast(order_placed as date) between :date1 and :date2  group by status", nativeQuery = true)
    public List<Tuple> getOrderStats(@Param("date1") String date1, @Param("date2") String date2);

    /**
     * It returns a list of tuples, each tuple containing the order type and the
     * number of orders of that
     * type, for the orders that were delivered between the two dates passed as
     * parameters
     * 
     * @param date1 The start date of the range
     * @param date2 The end date of the range
     * @return A list of tuples.
     */
    @Query(value = "select order_type , count(id) from orders where cast(order_delivered as date) between :date1 and :date2 group by order_type", nativeQuery = true)
    public List<Tuple> getBreakfastVsLunchStats(@Param("date1") String date1, @Param("date2") String date2);

    /**
     * It returns a list of orders where the order_placed column is equal to the
     * date passed in as a
     * parameter
     * 
     * @param date The date to search for.
     * @return A list of orders
     */
    @Query(value = "select * from orders where Cast(order_placed as date) = :date and status != 3", nativeQuery = true)
    public List<Order> findByOrderPlaced(@Param("date") String date);
}
