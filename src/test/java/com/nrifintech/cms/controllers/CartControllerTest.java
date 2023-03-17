package com.nrifintech.cms.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.repositories.CartRepo;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.types.ItemType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper =  new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    CartItem cartItem = new CartItem(2,3,4,300.0, ItemType.NonVeg,"","Chicken");
    CartItem cartItem2 = new CartItem(3,3,4,300.0, ItemType.Veg,"","Dahi");
    CartItem cartItem3 = new CartItem(4,3,4,200.0, ItemType.NonVeg,"","Chicken");


    Cart c1 = new Cart(3,Arrays.asList(cartItem3));
    Cart c2 = new Cart(4,Arrays.asList(cartItem2));

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    public void testGetCart() throws Exception {

        List<Cart> carts = Arrays.asList(c1,c2);

        Mockito.when(cartService.getCart(4)).thenReturn(c1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/cart/getcart/{cartId}","4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());



    }
}
