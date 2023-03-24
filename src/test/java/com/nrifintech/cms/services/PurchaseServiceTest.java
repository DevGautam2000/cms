package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.entities.Purchase;
import com.nrifintech.cms.repositories.InventoryRepo;
import com.nrifintech.cms.repositories.PurchaseRepo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseRepo purchaseRepo;

    @Mock
    private InventoryRepo inventoryRepo;

    @InjectMocks
    private PurchaseService purchaseService;

    Inventory inventorySample;
    Purchase purchaseSample;
    List<Purchase> purchases;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.inventorySample = new Inventory(10, "potato", 10.09, 20.09, null);
        this.purchaseSample = new Purchase(100,2.0,12,null,null);
        this.purchases = new ArrayList<Purchase>(){{
            for( int i = 0 ;i < 1000 ; i++){
                add( new Purchase(i,20.0+i,1100.0+i,null,null) );
            }
        }};
    }

    @Test
    public void testGetAllPurchaseNonEmpty() {
        Mockito.when( this.purchaseRepo.findAll() ).thenReturn(purchases);
        assertArrayEquals(purchases.toArray(), this.purchaseService.getAllPurchase().toArray() );
    }
    @Test
    public void testGetAllPurchaseEmpty() {
        Mockito.when( this.purchaseRepo.findAll() ).thenReturn( new ArrayList<>() );
        assertEquals(0 , this.purchaseService.getAllPurchase().size() );
    }

    @Test
    public void testGetPurchaseById() {
        Mockito.when( this.purchaseRepo.findById(100) ).thenReturn(Optional.of(this.purchaseSample));
        assertEquals(this.purchaseSample, this.purchaseService.getPurchaseById(100));
        assertEquals(null, this.purchaseService.getPurchaseById(10));
    }

    @Test
    public void testInitiateNewPurchase() {
    }

    @Test
    public void testRollbackPurchase() {

    }
}
