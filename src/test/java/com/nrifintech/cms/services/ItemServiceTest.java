package com.nrifintech.cms.services;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.ItemRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ItemService itemService;


    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public  void testAddItemSuccess(){

        Item item = mock(Item.class);
        item.setId(20);
        item.setName("Chicken");

        ItemService mock = mock(ItemService.class);
        Mockito.when(mock.getItems()).thenReturn(new ArrayList<>());
        Mockito.when(itemRepo.save(any(Item.class))).thenReturn(item);

        Item expectedItem = itemService.addItem(item);

        assertEquals(item,expectedItem);

    }

    @Test
    public  void testAddItemFailure(){

        Item item = mock(Item.class);
        item.setId(20);
        item.setName("Chicken");

        List<Item> exItems = new ArrayList<>();
        exItems.add(Item.builder().id(22).name("Chicken").build());

        ItemService mock = mock(ItemService.class);
        Mockito.when(mock.getItems()).thenReturn(exItems);
        Mockito.when(itemRepo.save(any(Item.class))).thenReturn(null);

        Item expectedItem = itemService.addItem(item);

        assertNull(expectedItem);

    }

    @Test
    public void testGetItemSuccess(){

        int itemId = 10;
        Item item = mock(Item.class);

        Mockito.when(itemRepo.findById(itemId)).thenReturn(Optional.ofNullable(item));

        Item expectedItem = itemService.getItem(itemId);

        assertEquals(item,expectedItem);

    }


    @Test
    public void testGetItemFailure(){


        int itemId = 10;
        Item item = mock(Item.class);

        NotFoundException notFoundException = new NotFoundException("Item");
        Mockito.when(itemRepo.findById(itemId)).thenThrow(notFoundException);

        Exception expected = null;
        try {
            Item expectedItem= itemService.getItem(itemId);
        }catch (NotFoundException ex){
            expected=ex;
        }

        assert expected != null;
        assertEquals(notFoundException.getMessage() , expected.getMessage() );

    }

    @Test
    public void testAddItems(){

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(10).name("Chicken").build());

        Mockito.when(itemRepo.saveAll(anyList())).thenReturn(items);

        List<Item> expectedList = itemService.addItems(items);

        assertEquals(items, expectedList);
    }

    @Test
    public void testGetItems(){

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(10).name("Chicken").build());

        Mockito.when(itemRepo.findAll()).thenReturn(items);

        List<Item> expectedList = itemService.getItems();

        assertEquals(items, expectedList);
    }

    @Test
    public void testGetItemsForItemIds(){

        List<String> itemIds = new ArrayList<>();
        itemIds.add("10");
        itemIds.add("13");

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(10).name("Chicken").build());
        items.add(Item.builder().id(13).name("Dahi").build());

        Mockito.when(itemService.getItems()).thenReturn(items);
        List<Item> expectedList = itemService.getItems(itemIds);

        assert expectedList != null;
        assertEquals(expectedList.size(), itemIds.size());
        assertEquals(items.get(0).getId(), expectedList.get(0).getId());
        assertEquals(items.get(1).getId(), expectedList.get(1).getId());
    }
}
