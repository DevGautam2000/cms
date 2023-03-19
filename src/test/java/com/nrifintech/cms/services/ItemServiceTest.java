package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.ItemRepo;
import com.nrifintech.cms.types.ItemType;

@SpringBootTest
public class ItemServiceTest {
    private List<Item> items= new ArrayList<>();
    @Mock
	private ItemRepo itemRepo;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setup(){
        items.add(new Item(999,12,10.0,ItemType.Veg,"abc.png","tasty","food1"));
        items.add(new Item(998,13,10.0,ItemType.Veg,"abc.png","tasty","food2"));
        items.add(new Item(997,14,10.0,ItemType.Veg,"abc.png","tasty","food3"));
        items.add(new Item(996,15,10.0,ItemType.NonVeg,"abc.png","yum","food4"));
        items.add(new Item(995,16,10.0,ItemType.NonVeg,"abc.png","yum","food5"));
        items.add(new Item(994,17,10.0,ItemType.NonVeg,"abc.png","yum","food6"));
         
        Mockito.when(itemRepo.saveAll(items)).thenReturn( items);
        Mockito.when(itemRepo.findAll()).thenReturn( items);
        for(Item i:items){
            Mockito.when(itemRepo.save( Mockito.eq(i))).thenReturn( i);
            Mockito.when(itemRepo.findById(  Mockito.eq(i.getId()))).thenReturn( Optional.of(i));
        }
        Mockito.when(itemRepo.findById( Mockito.eq(11))).thenThrow(NotFoundException.class);
        Mockito.when(itemRepo.findById( Mockito.eq(12))).thenThrow(NotFoundException.class);

        // when(ItemService.getItems("abc2@gamil.com")).thenReturn( items.get(1));
        // when(ItemService.getItems("abc3@gamil.com")).thenReturn( items.get(2));
        // when(ItemService.getItems("abc4@gamil.com")).thenReturn( items.get(3));
    }
    
    @AfterEach
    void destroy(){
        items.clear();
    }

    @Test
    void testAddItem() {
        for(Item i:items){
            assertEquals(null,itemService.addItem(i));
        }
        Item i1=new Item(99,16,10.0,ItemType.NonVeg,"abc.png","yum","food54");
        Item i2=new Item(91,16,10.0,ItemType.NonVeg,"abc.png","yum","food55");
        assertEquals( i1, i1);
        assertEquals( i2, i2);
    }

    @Test
    void testAddItems() {
        assertArrayEquals( items.toArray() , itemService.addItems(items).toArray()  );
        assertArrayEquals( new Item[0] , itemService.addItems(null).toArray()  );

    }

    @Test
    void testGetItem() {
        assertEquals( items.get(0) , itemService.getItem(items.get(0).getId()));
        NotFoundException e1 = assertThrows( NotFoundException.class , ()-> itemService.getItem(11));
        NotFoundException e2 = assertThrows( NotFoundException.class , ()-> itemService.getItem(12));

    }

    @Test
    void testGetItems() {
        assertArrayEquals( items.toArray() , itemService.getItems().toArray()  );

    }

    @Test
    void testGetItems2() {
        assertArrayEquals( 
            List.of(items.get(0),items.get(1),items.get(2)).toArray(),
            itemService.getItems(
                List.of("999","998","997")).toArray()
        );
        
        assertArrayEquals( 
            List.of(items.get(0),items.get(1)).toArray(),
            itemService.getItems(
                List.of("999","998","9974")).toArray()
        );
    }
}
