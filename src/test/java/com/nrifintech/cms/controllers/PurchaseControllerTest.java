package com.nrifintech.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.Purchase;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.InventoryService;
import com.nrifintech.cms.services.PurchaseService;
import com.nrifintech.cms.types.Response;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class PurchaseControllerTest extends MockMvcSetup {
    private List<Purchase> purchases = new ArrayList<>();
    @Mock 
    private PurchaseService purchaseService;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private PurchaseController purchaseController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Purchase.prefix, this, purchaseController);
        loadData();
    }

    private void loadData() {
        purchases.add(new Purchase(1, 3, 20.3, null, null));

        for(Purchase p : purchases){
            when(purchaseService.getPurchaseById(purchases.get(0).getRefId())).thenReturn(p);
        }
        Mockito.when(purchaseService.getAllPurchase()).thenReturn(purchases);

    }

    @Test
    public void testGet() throws UnsupportedEncodingException, Exception {
        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Purchase.get)
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<Purchase> res = mapFromJson(r, ArrayList.class);

        assertEquals(purchases.size(), res.size());
        // assertEquals(purchases.get(0).getRefId(), ((Purchase) res.get(0)).getRefId());       
    }

    @Test
    public void testGetById() throws UnsupportedEncodingException, Exception {

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Purchase.get  + purchases.get(0).getRefId() )
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Purchase res = mapFromJson(r, Purchase.class);

        assertEquals(purchases.get(0), res);


        
        r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Purchase.get  + "999" )
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res2 = mapFromJson(r, Response.JsonEntity.class);

        assertEquals("ID not found", res2.getMessage());
    }

    @Test
    public void testRollback() {

    }

    @Test
    public void testSave() {

    }
}
