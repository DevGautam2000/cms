package com.nrifintech.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.InventoryDto;
import com.nrifintech.cms.dtos.InventoryMail;
import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.events.UpdateQtyReqEvent;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.InventoryService;
import com.nrifintech.cms.types.Response;



/**
 * > This class is a controller that handles requests to the `/inventory` route
 */
@RequestMapping(Route.Inventory.prefix)
@RestController
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * > This function takes a JSON object, converts it to a Java object, saves it to the database, and
     * returns the saved object
     * 
     * @param inventoryDto This is the object that will be sent to the server.
     * @return A Response object.
     */
    @PostMapping(Route.Inventory.saveOne)
    public Response saveOne(@RequestBody InventoryDto inventoryDto){
        Inventory inventory = new Inventory(inventoryDto);
        Inventory i = this.inventoryService.addToInventory(inventory);
        if( i == null ){
            return( Response.setErr("Unable to save", HttpStatus.BAD_REQUEST));
        }
        return( Response.set(i,HttpStatus.OK) );
    }

    /**
     * It saves all the inventory items in the database.
     * 
     * @param inventory This is the list of inventory objects that we want to save.
     * @return A Response object with the inventory list and a status of OK.
     */
    @PostMapping(Route.Inventory.saveAll)
    public Response saveAll(@RequestBody List<Inventory> inventory){
        return( Response.set(this.inventoryService.addAlltoInventory(inventory),HttpStatus.OK) );
    }

    /**
     * > This function gets an inventory by id
     * 
     * @param id The id of the inventory you want to get.
     * @return A Response object.
     */
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

    /**
     * > This function returns a list of all inventory items
     * 
     * @return A list of inventory objects
     */
    @GetMapping(Route.Inventory.get)
    public Response getAll(){
        List<Inventory> inventory = this.inventoryService.getAllInventory();
        return Response.set(inventory, HttpStatus.OK);
    }

    /**
     * > This function will return a list of inventory items that match the name passed in the URL
     * 
     * @param name The name of the inventory item
     * @return A list of inventory objects
     */
    @GetMapping(Route.Inventory.getByName + "{name}")
    public Response getByName(@PathVariable String name){
        List<Inventory> inventory = this.inventoryService.getInventoryByName(name);
        if( inventory.isEmpty() ){
            return Response.setErr("Not found", HttpStatus.NOT_FOUND);
        }
        else{
            return Response.set(inventory,HttpStatus.OK);
        }
    }

   /**
    * > This function deletes an inventory item by its id
    * 
    * @param id The id of the inventory to be deleted
    * @return A Response object is being returned.
    */
    @GetMapping(Route.Inventory.remove + "{id}")
    public Response delete(@PathVariable int id){
        boolean flag = this.inventoryService.removeInventoryById(id);
        if(flag){
            return Response.set("Deleted successfully", HttpStatus.OK);
        }
        return Response.setErr("Deletion failed", HttpStatus.BAD_REQUEST);
    }

   
    /**
     * It updates the quantity in hand of a particular inventory item
     * 
     * @param id the id of the inventory item
     * @param qtyhand the quantity that is being sold
     * @return Response is a class that I have created.
     */
    @GetMapping(Route.Inventory.updateQtyInHand + "{id}" + "/" + "{qtyhand}")
    public Response updateQtyInHand(@PathVariable int id , @PathVariable double qtyhand){
        Inventory i = this.inventoryService.getInventoryById(id);
        if(i==null){
            return Response.setErr("Inventory item not found",HttpStatus.NOT_FOUND);
        }
        Double presentInHand = i.getQuantityInHand();
        if(presentInHand+qtyhand<0.0){
            return Response.setErr("Sorry cannot update", HttpStatus.BAD_REQUEST);
        }
        this.inventoryService.updateQtyInHand(qtyhand, id);
        return Response.set("Stocks updated", HttpStatus.OK);
    }

 
  /**
   * It updates the quantity requested of an inventory item and sends an email to all admin users
   * 
   * @param id the id of the inventory item
   * @param qtyreq the quantity requested
   * @return A Response object.
   */
    @GetMapping(Route.Inventory.updateQtyReq + "{id}" + "/" + "{qtyreq}")
    public Response updateQtyRequested(@PathVariable int id , @PathVariable double qtyreq){
        this.inventoryService.updateQtyRequested(qtyreq, id);
        //this should go to all admin users...
        Inventory i = this.inventoryService.getInventoryById(id);
        this.applicationEventPublisher.publishEvent( new UpdateQtyReqEvent(new InventoryMail( i )) );
        if(i==null){
            return Response.setErr("Inventory item not found",HttpStatus.NOT_FOUND);
        }
        return Response.set("Updated Stocks", HttpStatus.OK);
    }

}
