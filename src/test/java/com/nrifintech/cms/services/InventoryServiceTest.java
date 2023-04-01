package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.repositories.InventoryRepo;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepo inventoryRepo;

    @InjectMocks
    private InventoryService inventoryService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.Test
    void testAddAlltoInventory() {
        List<Inventory> invetories = new ArrayList<>();
        for(int i = 0;i<10;i++){
            invetories.add(Inventory.builder().id(i)
        .name("Coconut")
        .quantityInHand(10.09)
        .quantityRequested(10.09)
        .build());
        }
        Mockito.when(inventoryRepo.saveAll(invetories)).thenReturn(invetories);
        assertArrayEquals(invetories.toArray(), this.inventoryService.addAlltoInventory(invetories).toArray());
    }

    @org.junit.jupiter.api.Test
    void testAddToInventory() {
        List<Inventory> invetories = new ArrayList<>();
        for(int i = 0;i<2;i++){
            invetories.add(Inventory.builder().id(i)
        .name("Coconut" + i)
        .quantityInHand(10.09)
        .quantityRequested(10.09)
        .build());
        } 
        Mockito.when(inventoryRepo.save(invetories.get(0))).thenReturn(invetories.get(0));
       // Mockito.when(inventoryRepo.save(invetories.get(1))).thenReturn(invetories.get(1));
        Mockito.when(inventoryRepo.findByName(eq(invetories.get(0).getName()))).thenReturn(Arrays.asList());
        Mockito.when(inventoryRepo.findByName(eq(invetories.get(1).getName()))).thenReturn(Arrays.asList(invetories.get(1)));
        assertEquals(invetories.get(0), inventoryService.addToInventory(invetories.get(0)));
        assertEquals(null, inventoryService.addToInventory(invetories.get(1)));
        Mockito.verify(inventoryRepo , Mockito.times(2)).findByName(anyString());
        Mockito.verify(inventoryRepo , Mockito.times(1)).save(any());
    }

    @org.junit.jupiter.api.Test
    void testGetAllInventory() {
        List<Inventory> invetories = new ArrayList<>();
        for(int i = 0;i<2;i++){
            invetories.add(Inventory.builder().id(i)
        .name("Coconut" + i)
        .quantityInHand(10.09)
        .quantityRequested(10.09)
        .build());
        } 
       Mockito.when(inventoryRepo.findAll()).thenReturn(invetories);
       assertArrayEquals(invetories.toArray(), inventoryService.getAllInventory().toArray());
    }

    @org.junit.jupiter.api.Test
    void testGetInventoryById() {
        List<Inventory> invetories = new ArrayList<>();
        for(int i = 0;i<2;i++){
            invetories.add(Inventory.builder().id(i)
        .name("Coconut" + i)
        .quantityInHand(10.09)
        .quantityRequested(10.09)
        .build());
        }
        invetories.add(null);
        Mockito.when(inventoryRepo.findById(invetories.get(0).getId())).thenReturn(Optional.ofNullable(invetories.get(0)));
        Mockito.when(inventoryRepo.findById(invetories.get(1).getId())).thenReturn(Optional.ofNullable(invetories.get(1)));
        Mockito.when(inventoryRepo.findById(2)).thenReturn(Optional.ofNullable(invetories.get(2)));
        assertEquals(inventoryService.getInventoryById(0), invetories.get(0));
        assertEquals(inventoryService.getInventoryById(1), invetories.get(1));
        assertEquals(inventoryService.getInventoryById(2), null);
    }

    @org.junit.jupiter.api.Test
    void testGetInventoryByName() {
        List<Inventory> resultSet = new ArrayList<>();
        List<Inventory> emptySet = new ArrayList<>();
        for( int i=0 ; i<5 ; i++){
            resultSet.add(
                Inventory.builder().id(i)
                    .name("Coconut")
                    .quantityInHand(10.09)
                    .quantityRequested(10.09)
                    .build()
            );
        }
        Mockito.when(inventoryRepo.findByName("Coconut")).thenReturn(resultSet);
        Mockito.when(inventoryRepo.findByName("tomato")).thenReturn(emptySet);
        assertArrayEquals(inventoryService.getInventoryByName("Coconut").toArray(), resultSet.toArray());
        assertArrayEquals(inventoryService.getInventoryByName("tomato").toArray(), emptySet.toArray());
    }

    @org.junit.jupiter.api.Test
    void testRemoveInventoryById() {
        Inventory i = new Inventory(0, "Banana", null, null, null);
        List<Integer> ids = new ArrayList<Integer>(){{
            for( int i = 0 ; i<2;i++){
                add(i);
            }
            add(null);
        }};

        Mockito.when( inventoryRepo.findById(ids.get(1)) ).thenReturn(Optional.empty());
        Mockito.when( inventoryRepo.findById(ids.get(0))).thenReturn(Optional.of(i));

        assertEquals( false , inventoryService.removeInventoryById(ids.get(0)));
        assertEquals(true, inventoryService.removeInventoryById(ids.get(1)));
        assertEquals(false, inventoryService.removeInventoryById(ids.get(2)));

    }
}
