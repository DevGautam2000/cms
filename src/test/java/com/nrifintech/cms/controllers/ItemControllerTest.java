package com.nrifintech.cms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.ItemRepo;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.ItemService;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.Response;
import lombok.var;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest extends MockMvcSetup {

    @Mock
    private ItemService itemService;


    @InjectMocks
    private ItemController itemController;


    private List<Item> items;
    private Item item;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Item.prefix, this, itemController);
        loadData();
    }

    private void loadData() {

        items = new ArrayList<>();
        item = Item.builder().id(10).name("Chicken").itemType(ItemType.NonVeg).price(200d).build();
        items.add(item);
        items.add(Item.builder().id(12).name("Dahi").itemType(ItemType.Veg).price(20d).build());

    }


    @Test
    public void testGetItemsSuccess() throws Exception {


        Mockito.when(itemService.getItems()).thenReturn(items);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Item.getItems)
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Item[] resItems = mapFromJson(r, Item[].class);

        assertThat(items.size(), is(resItems.length));
        assertThat(items.get(0).getId(), is(resItems[0].getId()));
    }

    @Test
    public void testGetItemSuccess() throws Exception {

        int itemId = 12;

        Mockito.when(itemService.getItem(itemId)).thenReturn(items.get(0));

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Item.getItem + "/{itemId}"), itemId
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Item res = mapFromJson(r, Item.class);

        assertThat(items.get(0).getId(), is(res.getId()));
        assertThat(items.get(0).getItemType(), is(res.getItemType()));
        assertThat(items.get(0).getName(), is(res.getName()));
    }

    @Test
    public void testGetItemFailure() throws Exception {

        int itemId = 12;

        Mockito.when(itemService.getItem(itemId)).thenThrow(new NotFoundException("Item"));

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix(Route.Item.getItem + "/{itemId}"), itemId
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat("item not found.", is(res.getMessage().toString().trim().toLowerCase()));
    }

    @Test
    public void testAddItemSuccess() throws Exception {

        Item newItem = Item.builder().id(13).name("Paneer").price(200d).itemType(ItemType.Veg).build();

        Mockito.when(itemService.addItem(any(Item.class))).thenReturn(newItem);
        Mockito.when(itemService.isNotNull(newItem)).thenReturn(true);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(
                                        prefix(Route.Item.addItem)
                                ).contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(newItem))
                )
                //.andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("item added.", is(res.getMessage().toString().trim().toLowerCase()));
    }

    @Test
    public void testAddItemFailure() throws Exception {

        Item i = item;
        Mockito.when(itemService.addItem(any(Item.class))).thenReturn(null);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix(Route.Item.addItem)
                ).contentType(MediaType.APPLICATION_JSON).content(mapToJson(i))
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.BAD_REQUEST.value(), is(res.getStatus()));
        assertThat("item exists.", is(res.getMessage().toString().trim().toLowerCase()));
    }


    @Test
    public void testAddItemsSuccess() throws Exception {


        List<Item> itemList = items;

        Mockito.when(itemService.addItems(anyList())).thenReturn(itemList);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(prefix(Route.Item.addItems))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapToJson(items))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("items added.", is(res.getMessage().toString().trim().toLowerCase()));
    }

    @Test
    public void testAddItemsFailure() throws Exception {

        List<Item> itemList = items;

        Mockito.when(itemService.addItems(anyList())).thenReturn(new ArrayList<>());

        String r = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(prefix(Route.Item.addItems))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(itemList))
        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.value(), is(res.getStatus()));
        assertThat("error adding item.", is(res.getMessage().toString().trim().toLowerCase()));
    }
}

