package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import static org.mockito.ArgumentMatchers.anyInt;
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
    Inventory inventorySampleNotExists;
    Purchase purchaseSample;
    Purchase purchaseSampleSaveFailure1;
    Purchase purchaseSampleSaveFailure2;
    List<Purchase> purchases;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.inventorySample = new Inventory(10, "potato", 10.09, 20.09, null);
        this.inventorySampleNotExists = new Inventory(11, "tomato", 10.09, 20.09, null);
        this.purchaseSample = new Purchase(100,2.0,12,null,null);
        this.purchaseSampleSaveFailure1 = new Purchase(101,2.0,12,null,null);
        this.purchaseSampleSaveFailure2 = new Purchase(102,2.0,12,null,null);
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
        this.purchaseSample.setInventoryRef(inventorySample);
        this.purchaseSampleSaveFailure2.setInventoryRef(inventorySampleNotExists);
        Mockito.when( this.purchaseRepo.save(purchaseSample) ).thenReturn(purchaseSample);
        Mockito.when( this.inventoryRepo.findById(this.inventorySample.getId()) ).thenReturn(Optional.ofNullable(this.inventorySample));
        Mockito.when( this.inventoryRepo.findById(102) ).thenReturn(Optional.ofNullable(null));
        assertEquals(this.purchaseSample, this.purchaseService.initiateNewPurchase(purchaseSample));
        assertNull(this.purchaseService.initiateNewPurchase(purchaseSampleSaveFailure1)); 
        assertNull(this.purchaseService.initiateNewPurchase(purchaseSampleSaveFailure2)); 
    }

    @Test
    public void testRollbackPurchase() {
        this.purchaseSample.setInventoryRef(inventorySample);
        this.purchaseSampleSaveFailure2.setInventoryRef(inventorySampleNotExists);
        Mockito.when(this.purchaseRepo.findById(this.purchaseSample.getRefId())).thenReturn(Optional.ofNullable(this.purchaseSample));
        Mockito.when(this.purchaseRepo.findById(this.purchaseSampleSaveFailure1.getRefId())).thenReturn(Optional.ofNullable(null));
        Mockito.when(this.purchaseRepo.findById(this.purchaseSampleSaveFailure2.getRefId())).thenReturn(Optional.ofNullable(this.purchaseSampleSaveFailure2));

        Mockito.when(this.inventoryRepo.findById(this.purchaseSample.getInventoryRef().getId())).thenReturn(Optional.ofNullable(this.inventorySample));
        Mockito.when(this.inventoryRepo.findById(this.purchaseSampleSaveFailure2.getInventoryRef().getId())).thenReturn(Optional.ofNullable(null));

        assertEquals( this.purchaseService.rollbackPurchase(purchaseSample.getRefId()), purchaseSample);
        assertNull( this.purchaseService.rollbackPurchase(this.purchaseSampleSaveFailure1.getRefId()) );
        assertNull( this.purchaseService.rollbackPurchase(this.purchaseSampleSaveFailure2.getRefId()) );
        Mockito.verify( purchaseRepo , Mockito.times(3)).findById(anyInt());
        Mockito.verify( inventoryRepo , Mockito.times(2)).findById(anyInt());
        Mockito.verify( purchaseRepo , Mockito.times(1)).delete(any());
    }
}
