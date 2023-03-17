package com.nrifintech.cms.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.dtos.AnalyticsResponseDate;
import com.nrifintech.cms.entities.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase,Integer> {


    @Query( value = "select sum(amount) from purchase where Cast(time as date) between :date1 and :date2" , nativeQuery = true)
    public Optional<Double> getTotalExp(@Param("date1") String date1 , @Param("date2") String date2 );

    @Query( value = "select Cast(Cast(time as date) as char) as dates, sum(amount) as value from purchase group by dates having dates between :date1 and :date2" , nativeQuery = true)
    public List<Tuple> getExpDate(@Param("date1") String date1 , @Param("date2") String date2 );
}
