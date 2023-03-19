package com.nrifintech.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.InventoryMail;
import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.events.UpdateQtyReqEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.InventoryService;
import com.nrifintech.cms.types.Response;
import com.stripe.model.Application;

@CrossOrigin
@RestController
@RequestMapping(Route.Inventory.prefix)
public class InventoryController {
    
    @Autowired
    InventoryService inventoryService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @PostMapping(Route.Inventory.saveOne)
    public Response saveOne(@RequestBody Inventory inventory){
        Inventory i = this.inventoryService.addToInventory(inventory);
        if( i == null ){
            return( Response.setErr("Unable to save", HttpStatus.BAD_REQUEST));
        }
        return( Response.set(i,HttpStatus.OK) );
    }

    @PostMapping(Route.Inventory.saveAll)
    public Response saveOne(@RequestBody List<Inventory> inventory){
        return( Response.set(this.inventoryService.addAlltoInventory(inventory),HttpStatus.OK) );
    }

    @GetMapping(Route.Inventory.getById + "{id}")
    public Response getById(@PathVariable int id){
        Inventory inventory = this.inventoryService.getInventoryById(id);
        if( inventory == null ){
            return Response.setErr("Not found", HttpStatus.NOT_FOUND);
        }
        else{
            return Response.set(inventory, HttpStatus.OK);
        }
    }

    @GetMapping(Route.Inventory.get)
    public Response getAll(){
        List<Inventory> inventory = this.inventoryService.getAllInventory();
        if( inventory.size() == 0 ){
            return Response.setErr("Not found", HttpStatus.NOT_FOUND);
        }
        else{
            return Response.set(inventory, HttpStatus.OK);
        }
    }

    @GetMapping(Route.Inventory.getByName + "{name}")
    public Response getByName(@PathVariable String name){
        List<Inventory> inventory = this.inventoryService.getInventoryByName(name);
        if( inventory.size() == 0 ){
            return Response.setErr("Not found", HttpStatus.NOT_FOUND);
        }
        else{
            return Response.set(inventory,HttpStatus.OK);
        }
    }

    @GetMapping(Route.Inventory.remove + "{id}")
    public void delete(@PathVariable int id){
       this.inventoryService.removeInventoryById(id);
    }

    @GetMapping(Route.Inventory.updateQtyInHand + "{id}" + "/" + "{qtyhand}")
    public void updateQtyInHand(@PathVariable int id , @PathVariable double qtyhand){
        this.inventoryService.updateQtyInHand(qtyhand, id);

    }

    @GetMapping(Route.Inventory.updateQtyReq + "{id}" + "/" + "{qtyreq}")
    public void updateQtyRequested(@PathVariable int id , @PathVariable double qtyreq){
        this.inventoryService.updateQtyRequested(qtyreq, id);
        //this should go to all admin users...
        this.applicationEventPublisher.publishEvent( new UpdateQtyReqEvent(new InventoryMail( this.inventoryService.getInventoryById(id).getName(), qtyreq)) );
    }

}
