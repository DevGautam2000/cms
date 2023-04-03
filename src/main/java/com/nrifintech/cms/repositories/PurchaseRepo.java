package com.nrifintech.cms.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nrifintech.cms.entities.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase, Integer> {

    /**
     * It returns the total amount of money spent on purchases between two dates
     * 
     * @param date1 The start date of the period
     * @param date2 The end date of the period you want to get the total expenses
     *              for.
     * @return Optional<Double>
     */

    @Query(value = "select sum(amount) from purchase where Cast(time as date) between :date1 and :date2", nativeQuery = true)
    public Optional<Double> getTotalExp(@Param("date1") String date1, @Param("date2") String date2);

    /**
     * It returns a list of tuples, each tuple contains the date and the total
     * amount of purchases made on
     * that date
     * 
     * @param date1 The start date of the range
     * @param date2 the end date
     * @return A list of tuples.
     */

    @Query(value = "select Cast(Cast(time as date) as char) as dates, sum(amount) as value from purchase group by dates having dates between :date1 and :date2", nativeQuery = true)
    public List<Tuple> getExpDate(@Param("date1") String date1, @Param("date2") String date2);
}
