package com.nrifintech.cms.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.Order;
public interface OrderRepo extends JpaRepository<Order,Integer>{
    @Modifying
    @Transactional
    @Query(value = "update orders set status = 2 where status = 0 ",nativeQuery = true)
    public void autoArchive();

    @Query(value = "select Cast(order_delivered as date) as date , sum(amount) as value from orders,transaction  where orders.transaction_id = transaction.id group by date having date between :date1 and :date2",
           nativeQuery = true)
    public List<Tuple> getDaybyDaySales(@Param("date1") String date1 , @Param("date2") String date2 );

    @Query(value = "select  sum(amount) as value from orders,transaction  where orders.transaction_id = transaction.id and Cast(order_delivered as date) between :date1 and :date2",
    nativeQuery = true)
    public Optional<Double> getTotalSales(@Param("date1") String date1 , @Param("date2") String date2 );

    @Query(value = "select status , count(id) from orders where Cast(order_placed as date) between :date1 and :date2  group by status" , nativeQuery=true)
    public List<Tuple> getOrderStats(@Param("date1") String date1 , @Param("date2") String date2 );
}
