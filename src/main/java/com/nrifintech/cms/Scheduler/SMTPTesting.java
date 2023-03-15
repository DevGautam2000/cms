package com.nrifintech.cms.Scheduler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.dtos.OrderToken;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.events.CancelledOrderEvent;
import com.nrifintech.cms.events.DeliveredOrderEvent;
import com.nrifintech.cms.events.PlacedOrderEvent;
import com.nrifintech.cms.services.AnalyticsService;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Status;

import eu.bitwalker.useragentutils.Application;

@Component
public class SMTPTesting {
    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    AnalyticsService analyticsService;
    
    //for smtp testing
    // @Scheduled(fixedDelay = 10000)
    // public void test(){
    //     CartItem cartItem1 = new CartItem(400,0,56,780.90,ItemType.Veg,"","Veg Pizza");
    //     CartItem cartItem2 = new CartItem(401,0,50,789.90,ItemType.Veg,"","Paneer Pizza");
    //     List<CartItem> ls = new ArrayList<>();
    //     ls.add(cartItem1);
    //     ls.add(cartItem2);

    //     Order order = new Order(100, Status.Pending, MealType.Breakfast, null, null, null, ls);
    //     //publisher.publishEvent(new PlacedOrderEvent(new OrderToken("sagnik938@gmail.com", order)));
    //     //publisher.publishEvent(new CancelledOrderEvent(new OrderToken("sagnik938@gmail.com", order)));
    //     //publisher.publishEvent(new DeliveredOrderEvent(new OrderToken("sagnik938@gmail.com", order)));
    // }

    //  //for smtp testing
    // @Scheduled(fixedDelay = 10000)
    // public void test(){
    //   //System.out.println( analyticsService.getTotalExp("2023-03-19", "2023-03-15") );
    //   System.out.println( analyticsService.getExpDate("2023-03-13", "2023-03-15") );
    // }
}
