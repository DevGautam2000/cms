package com.nrifintech.cms.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.dtos.AnalyticsResponseDate;
import com.nrifintech.cms.repositories.PurchaseRepo;

@Service
public class AnalyticsService {
    
    @Autowired
    private PurchaseRepo purchaseRepo;

    public List<AnalyticsResponseDate> getExpDate(String date1 , String date2){
        List<Tuple> resTup = ( this.purchaseRepo.getExpDate(date1, date2) );
        List<AnalyticsResponseDate> result = resTup.stream().map(t->new AnalyticsResponseDate(t.get(0, String.class), t.get(1, Double.class))).collect(Collectors.toList());
        return( result );
    }

    public Double getTotalExp(String date1 , String date2){
        return( this.purchaseRepo.getTotalExp(date1, date2).orElse(0.0) );
    }
}
