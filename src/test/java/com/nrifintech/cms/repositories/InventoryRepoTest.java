package com.nrifintech.cms.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.nrifintech.cms.entities.Inventory;


@RunWith(MockitoJUnitRunner.class)
public class InventoryRepoTest {

    @Mock
    private InventoryRepo inventoryRepo;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {

        Optional<Inventory> inventory = 
        Optional.of(Inventory.builder().id(10).name("inventory").build());

        Mockito.when(
        inventoryRepo.findById(anyInt())).thenReturn(
            inventory
        );

       Optional<Inventory> expectedInventory =  inventoryRepo.findById(10);

       assert expectedInventory.isPresent();
       assertEquals(expectedInventory.get().getId(),inventory.get().getId());

    }

    @Test
    public void testFindByName() {
        Inventory inventory = 
       Inventory.builder().id(10).name("inventory").build();

        List<Inventory> inventories =  new ArrayList<>();
        inventories.add(inventory);
        
        Mockito.when(
        inventoryRepo.findByName(anyString())).thenReturn(
           inventories
        );

      List<Inventory> expectedInventories=  inventoryRepo.findByName("inventory");

       assertNotNull(expectedInventories);
       assertEquals(expectedInventories.get(0).getId(),inventories.get(0).getId());
    }

    @Test
    public void testUpdateQtyInHand() {

        inventoryRepo.updateQtyInHand(12d, 12);
        Mockito.verify(inventoryRepo , times(1)).updateQtyInHand(12d,12);
        
    }

    @Test
    public void testUpdateQtyRequested() {
       
        inventoryRepo.updateQtyRequested(12d, 12);
        Mockito.verify(inventoryRepo , times(1)).updateQtyRequested(12d,12);
        
    }
}
