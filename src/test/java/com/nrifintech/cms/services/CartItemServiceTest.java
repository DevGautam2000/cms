package com.nrifintech.cms.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.CartItemRepo;

@RunWith(MockitoJUnitRunner.class)
public class CartItemServiceTest {

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private CartItemService cartItemService;

    private List<CartItemUpdateRequest> cartItemUpdateRequests;

    private List<Item> items;

    private List<CartItem> cartItems;

    private Item item1;

    private Item item2;

    private CartItem cartItem1;

    private CartItem cartItem2;

    private MealType mealType1;

    private MealType mealType2;

    private ItemType itemType1;

    private ItemType itemType2;

    @Before
    public void setup() {
        // Creating test data
        cartItemUpdateRequests = new ArrayList<>();

        mealType1 = MealType.Breakfast;
        mealType2 = MealType.Lunch;

        cartItemUpdateRequests.add(new CartItemUpdateRequest("1", "3", mealType1));
        cartItemUpdateRequests.add(new CartItemUpdateRequest("2", "4", mealType2));

        itemType1 = ItemType.Veg;
        itemType2 = ItemType.NonVeg;

        item1 = new Item(1,10,40.0,itemType1, "Item1", "Item1_Description" , "Item1");
        item2 = new Item(2,20,80.0,itemType2, "Item2", "Item2_Description" , "Item2");

        items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        cartItem1 = new CartItem(item1, 3);
        cartItem2 = new CartItem(item2, 4);

        cartItems = new ArrayList<>();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        // Mock the behavior of the dependencies
        when(itemService.getItems(anyList())).thenReturn(items);
        when(cartItemRepo.saveAll(anyList())).thenReturn(cartItems);
        when(cartItemRepo.findById(1)).thenReturn(java.util.Optional.ofNullable(cartItem1));
        
        doNothing().when(cartItemRepo).deleteById(1);
    }

    @Test
    public void testAddItems() {
        List<CartItem> result = cartItemService.addItems(cartItemUpdateRequests);
        assertEquals(cartItems.size(), result.size());
        assertEquals(cartItems.get(0).getSourceId(), result.get(0).getSourceId());
    }

    @Test
    public void testGetItem() {
        CartItem result = cartItemService.getItem(1);
        assertEquals(cartItem1, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetItemNotFound() {
        cartItemService.getItem(3);
    }

    @Test
    public void testDeleteItem() {
        Boolean result = cartItemService.deleteItem(1);
        assertEquals(true, result);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteItemNotFound() {
        Boolean result = cartItemService.deleteItem(3);
        assertEquals(false, result);
    }

}