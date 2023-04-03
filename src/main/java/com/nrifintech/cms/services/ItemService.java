package com.nrifintech.cms.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorcontroller.ImageFailureException;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.repositories.ItemRepo;
import com.nrifintech.cms.utils.Validator;

/**
 * It's a service class that has a method to add an item to the database.
 */
@Service
public class ItemService implements Validator {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ImageService imageService;

    // add a food
   /**
    * It takes an item, checks if it exists in the database, if it doesn't, it uploads the image to the
    * cloud, and then saves the item to the database
    * 
    * @param i the item object
    * @return The item is being returned.
 * @throws ImageFailureException
    */
    public Item addItem(Item i) throws IOException, NoSuchAlgorithmException, ImageFailureException {
        List<Item> items = this.getItems();


        for (Item item : items) {
            if (item.getName().trim().equalsIgnoreCase(i.getName().trim()))
                return null;
        }
        String url = imageService.uploadImage( i.getName() , i.getItemType().toString() , i.getImagePath() , 1 );
        i.setImagePath(url);
        return itemRepo.save(i);
    }


    // get a food
   /**
    * If the item is found, return it. If not, throw an exception
    * 
    * @param id The id of the item to be retrieved
    * @return The item with the given id.
    */
    public Item getItem(Integer id) {
        return itemRepo.findById(id).orElseThrow(() -> new NotFoundException("Item"));
    }

    // add foods
   /**
    * It takes a list of items, saves them to the database, and returns the list of items
    * 
    * @param items The list of items to be added to the database.
    * @return A list of items.
    */
    public List<Item> addItems(List<Item> items) {
        itemRepo.saveAll(items);
        return items;
    }

    // get foods
   /**
    * It returns a list of all items in the database
    * 
    * @return A list of all items in the database.
    */
    public List<Item> getItems() {
        return itemRepo.findAll();
    }


  /**
   * It takes a list of item ids and returns a list of items that match the ids
   * 
   * @param itemIds List of item ids
   * @return A list of items that match the itemIds.
   */
    public List<Item> getItems(List<String> itemIds) {
        List<Item> allItems = this.getItems();
        List<Item> items = allItems.stream().filter(item -> itemIds.contains(item.getId().toString()))
                .collect(Collectors.toList());

        return items;
    }


}
