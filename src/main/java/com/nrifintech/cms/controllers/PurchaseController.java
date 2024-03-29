package com.nrifintech.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.InventoryMail;
import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.entities.Purchase;
import com.nrifintech.cms.events.ApprovedQtyReqEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.InventoryService;
import com.nrifintech.cms.services.PurchaseService;
import com.nrifintech.cms.types.Response;
import com.stripe.model.Application;

@RestController
@CrossOrigin
@RequestMapping(Route.Purchase.prefix)
public class PurchaseController {
    
    @Autowired 
    private PurchaseService purchaseService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping(Route.Purchase.save)
    public Response save(@RequestBody Purchase purchase){
        Purchase obj = this.purchaseService.initiateNewPurchase(purchase);
        if( obj == null ){
            return( Response.setErr("Unable to save", HttpStatus.BAD_REQUEST));
        }
        this.applicationEventPublisher.publishEvent( new ApprovedQtyReqEvent( new InventoryMail(purchase.getInventoryRef().getName() , purchase.getQuantity()) ));
        return( Response.set(obj , HttpStatus.OK));
    }

    @GetMapping(Route.Purchase.rollback+"{id}")
    public Response rollback(@PathVariable int id){
        Purchase purchase = this.purchaseService.rollbackPurchase(id);
        if(purchase == null){
            return(Response.setErr("ID not found", HttpStatus.NOT_FOUND));
        }
        return( Response.set(purchase, HttpStatus.OK));
    }

    @GetMapping(Route.Purchase.get+"{id}")
    public Response getById(@PathVariable int id){
        Purchase purchase = this.purchaseService.getPurchaseById(id);
        if(purchase == null){
            return(Response.setErr("ID not found", HttpStatus.NOT_FOUND));
        }
        return( Response.set(purchase, HttpStatus.OK));
    }

    @GetMapping(Route.Purchase.get)
    public Response get(){
        List<Purchase> purchase = this.purchaseService.getAllPurchase();
        return( Response.set(purchase, HttpStatus.OK));
    }
}
