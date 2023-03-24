package com.nrifintech.cms.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.entities.Purchase;
import com.nrifintech.cms.repositories.InventoryRepo;
import com.nrifintech.cms.repositories.PurchaseRepo;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseRepo purchaseRepo;

    @Mock
    private InventoryRepo inventoryRepo;

    Inventory inventorySample;
    Purchase purchaseSample;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.inventorySample = new Inventory(10, "potato", 10.09, 20.09, null);
        this.purchaseSample = new Purchase(100,2.0,12,null,null);
    }

    @Test
    public void testGetAllPurchase() {

    }

    @Test
    public void testGetPurchaseById() {

    }

    @Test
    public void testInitiateNewPurchase() {
    }

    @Test
    public void testRollbackPurchase() {

    }
}
