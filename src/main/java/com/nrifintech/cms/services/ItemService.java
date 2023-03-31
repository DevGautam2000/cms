package com.nrifintech.cms.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.CartItemRepo;
import com.nrifintech.cms.repositories.ItemRepo;
import com.nrifintech.cms.utils.Validator;

@Service
public class ItemService implements Validator {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ImageService imageService;

    // add a food
    public Item addItem(Item i) throws IOException {
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
    public Item getItem(Integer id) {
        return itemRepo.findById(id).orElseThrow(() -> new NotFoundException("Item"));
    }

    // add foods
    public List<Item> addItems(List<Item> items) {
        itemRepo.saveAll(items);
        return items;
    }

    // get foods
    public List<Item> getItems() {
        return itemRepo.findAll();
    }


    public List<Item> getItems(List<String> itemIds) {
        List<Item> allItems = this.getItems();
        List<Item> items = allItems.stream().filter(item -> itemIds.contains(item.getId().toString()))
                .collect(Collectors.toList());

        return items;
    }


}
