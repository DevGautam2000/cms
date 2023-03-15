package com.nrifintech.cms.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.hibernate.BaseSessionEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.AnalyticsResponseDate;
import com.nrifintech.cms.dtos.BestSellerResponse;
import com.nrifintech.cms.dtos.OrderStatusReport;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.repositories.PurchaseRepo;
import com.nrifintech.cms.types.Status;

@Service
public class AnalyticsService {
    
    @Autowired
    private PurchaseRepo purchaseRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    //get expenes day by day
    public List<AnalyticsResponseDate> getExpDate(String date1 , String date2){
        List<Tuple> resTup = ( this.purchaseRepo.getExpDate(date1, date2) );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class), t.get(1, Double.class))).collect(Collectors.toList());
        return( result );
    }

    //get total expenses
    public Double getTotalExp(String date1 , String date2){
        return( this.purchaseRepo.getTotalExp(date1, date2).orElse(0.0) );
    }

    //get day by day sales
    public List<AnalyticsResponseDate> getSalesDate(String date1 , String date2){
        List<Tuple> resTup = ( this.orderRepo.getDaybyDaySales(date1, date2) );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class), t.get(1, Double.class))).collect(Collectors.toList());
        return( result );
    }

    //get total sales
    public Double getTotalSales(String date1 , String date2){
        return( this.orderRepo.getTotalSales(date1, date2).orElse(0.0) );
    }

     //get bestSeller
     public List<BestSellerResponse> getBestSeller(String date1 , String date2){
        List<Tuple> resTup = ( this.cartItemRepo.getBestSeller(date1, date2) );
        List<BestSellerResponse> result = resTup.stream().map(t->new BestSellerResponse(t.get(0, String.class), t.get(1, BigDecimal.class))).collect(Collectors.toList());
        return( result );
    }

    //get order status stats
    public List<OrderStatusReport> getStatusReport(String date1 , String date2){
        List<Tuple> resTup = this.orderRepo.getOrderStats(date1, date2);
        List<OrderStatusReport> result = resTup.stream().map(t->new OrderStatusReport( Status.values()[(t.get(0,Integer.class))] , t.get(1,BigInteger.class) )).collect(Collectors.toList());
        return(result);
    }

}
