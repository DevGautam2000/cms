package com.nrifintech.cms.repositories;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.nrifintech.cms.entities.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Integer>{

    @Query( value = "select name, (t.quant) from " + 
            "( select source_id,sum(quantity) as quant from cart_item where id in" + 
            "( select cart_items_id from orders_cart_items where order_id in"+
             "( select id from orders where Cast(order_delivered as date) between :date1 and :date2))"+
             "group by source_id) as t ,item where t.source_id = item.id order by quant desc limit 3" ,
             nativeQuery = true )
    public List<Tuple>getBestSeller(@Param("date1") String date1 , @Param("date2") String date2);
}
