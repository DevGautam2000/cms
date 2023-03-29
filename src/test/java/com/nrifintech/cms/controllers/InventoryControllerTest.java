package com.nrifintech.cms.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.Inventory;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.InventoryService;
import com.nrifintech.cms.types.Response;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class InventoryControllerTest extends MockMvcSetup{

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    List<Inventory> sample;
    List<Inventory> emptySample;
    Inventory inventory ;

    @Before
    public void setup(){
        this.mockMvc = MockMvcSetup.setUp(Route.Inventory.prefix, this, inventoryController);
        loadData();
    }

    public void loadData(){
        this.inventory = new Inventory(100,"Potato",null,null,null);
        this.emptySample = new ArrayList<>();
        this.sample = new ArrayList<Inventory>(){{
            for( int i = 0 ; i < 5 ; i++){
                add( new Inventory(i,"I_"+i,null,null,null) );
            }
        }};
    }
    

    @Test
    public void testDeleteSuccess() throws UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.removeInventoryById(100)).thenReturn(true);
        String r = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Inventory.remove + "{id}"), 100)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals(r, "Deleted successfully");
    }

    @Test
    public void testDeleteFailure() throws UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.removeInventoryById(10)).thenReturn(false);
        String r = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Inventory.remove + "{id}"), 10)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertEquals(res.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(res.getMessage(), "Deletion failed");
    }

    @Test
    public void testGetAllNonEmpty() throws UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.getAllInventory() ).thenReturn(this.sample);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Inventory.get))
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Inventory[] res = mapFromJson(r, Inventory[].class);

        assertArrayEquals(this.sample.toArray(),res);

    }

    @Test
    public void testGetAllEmpty() throws UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.getAllInventory() ).thenReturn(this.emptySample);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Inventory.get))
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Inventory[] res = mapFromJson(r, Inventory[].class);

        assertArrayEquals(this.emptySample.toArray(),res);

    }

    @Test
    public void testGetByIdSuccess() throws UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.getInventoryById(this.inventory.getId()) ).thenReturn(inventory);

        String r1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Inventory.getById + "{id}") , this.inventory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(inventory))
    ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Inventory res1 = mapFromJson(r1, Inventory.class);

    assertEquals(this.inventory , res1 );
    }

    @Test
    public void testGetByIdFailure() throws UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.getInventoryById(anyInt()) ).thenReturn(null);

        String r1 = mockMvc.perform(
            MockMvcRequestBuilders.get(prefix(Route.Inventory.getById + "{id}") , 190)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(inventory))
    ).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

    Response.JsonEntity res1 = mapFromJson(r1, Response.JsonEntity.class);

    assertEquals(HttpStatus.NOT_FOUND.value() , res1.getStatus());
    assertEquals("Not found" , res1.getMessage());
    }

    @Test
    public void testGetByNameSuccess() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.getInventoryByName("Potato") ).thenReturn(Arrays.asList(this.inventory));

        String r1 = mockMvc.perform(
        MockMvcRequestBuilders.get(prefix(Route.Inventory.getByName + "{name}") , this.inventory.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(inventory))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Inventory[] actual = mapFromJson(r1, Inventory[].class);

        assertArrayEquals(actual, Arrays.asList(this.inventory).toArray());
    }

    @Test
    public void testGetByNameFailure() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.getInventoryByName("Tomato") ).thenReturn(Arrays.asList());

        String r1 = mockMvc.perform(
        MockMvcRequestBuilders.get(prefix(Route.Inventory.getByName + "{name}") , "Tomato")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(inventory))
        ).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        Response.JsonEntity actual = mapFromJson(r1, Response.JsonEntity.class);
        assertEquals(actual.getStatus(), HttpStatus.NOT_FOUND.value());
        assertEquals("Not found" , actual.getMessage());
    }

    @Test
    public void testSaveAllSuccess() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.addAlltoInventory(sample) ).thenReturn(sample);

        String r = mockMvc.perform(
            MockMvcRequestBuilders.post(prefix(Route.Inventory.saveAll) )
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapToJson(sample))
        ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Inventory[] i = mapFromJson(r, Inventory[].class);
        
        assertArrayEquals(i, sample.toArray());
    }

    @Test
    public void testSaveAllFailure() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        Mockito.when( this.inventoryService.addAlltoInventory(emptySample) ).thenReturn(emptySample);

        String r = mockMvc.perform(
            MockMvcRequestBuilders.post(prefix(Route.Inventory.saveAll) )
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapToJson(emptySample))
        ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Inventory[] i = mapFromJson(r, Inventory[].class);
        
        assertArrayEquals(i, emptySample.toArray());
    }

    @Test
    public void testSaveOneSuccess() throws JsonProcessingException, UnsupportedEncodingException, Exception {

        Mockito.when( this.inventoryService.addToInventory(inventory) ).thenReturn(inventory);

        String r = mockMvc.perform(
            MockMvcRequestBuilders.post(prefix(Route.Inventory.saveOne))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapToJson(inventory))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Inventory i = mapFromJson(r, Inventory.class);
        
        assertEquals(inventory, i);
    }

    @Test
    public void testSaveOneFailure() throws JsonProcessingException, UnsupportedEncodingException, Exception {

        Mockito.when( this.inventoryService.addToInventory(inventory) ).thenReturn(null);

        String r = mockMvc.perform(
            MockMvcRequestBuilders.post(prefix(Route.Inventory.saveOne))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapToJson(inventory))
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
       Response.JsonEntity response = mapFromJson(r,Response.JsonEntity.class);
        
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Unable to save" , response.getMessage());
    }

    @Test
    public void testUpdateQtyInHand() {

    }

    @Test
    public void testUpdateQtyRequested() {

    }
}
