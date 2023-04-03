package com.nrifintech.cms.controllers;


import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartItemService;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import lombok.var;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest extends MockMvcSetup {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private CartItemService cartItemService;
    @Mock
    private UserService userService;
    @InjectMocks
    private CartController cartController;

    private CartItem cartItem1, cartItem2, cartItem3;

    private Cart cart1, cart2;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Cart.prefix, this, cartController);
        loadData();
    }

    private void loadData() {
        cartItem1 = new CartItem(2, 3, MealType.Breakfast, 4, 300.0, ItemType.NonVeg, "", "Chicken");
        cartItem2 = new CartItem(3, 3, MealType.Lunch, 4, 300.0, ItemType.Veg, "", "Dahi");
        cartItem3 = new CartItem(4, 3, MealType.Breakfast, 4, 200.0, ItemType.NonVeg, "", "Chicken");

        List<CartItem> cartItemsForCart1 = new ArrayList<>();
        cartItemsForCart1.add(cartItem1);

        List<CartItem> cartItemsForCart2 = new ArrayList<>();
        cartItemsForCart1.add(cartItem2);

        cart1 = new Cart(1, cartItemsForCart1);
        cart2 = new Cart(2, cartItemsForCart2);
    }

    @Test
    public void testAddToCartSuccess() throws Exception {

        //mock data
        User user = User.builder()
                .id(10)
                .cart(cart1)
                .email("user@test")
                .password("1234")
                .role(Role.User)
                .build();


        List<CartItemUpdateRequest> cartItemUpdateRequests = Arrays.asList(
                new CartItemUpdateRequest("2", "3", MealType.Breakfast)
                , new CartItemUpdateRequest("3", "5", MealType.Lunch)
        );


        Mockito.when(userService.getuser(10)).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);
        Mockito.when(cartService.isNotNull(user.getCart())).thenReturn(true);
        Mockito.when(cartService.addToCart(cartItemUpdateRequests, cart1)).thenReturn(cart1);


        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.addToCart) + "/{userId}", "10")
                        .content(objectWriter.writeValueAsString(cartItemUpdateRequests))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("added to cart.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testAddToCartFailure() throws Exception {

        //mock data
        User user = null;

        List<CartItemUpdateRequest> cartItemUpdateRequests = Arrays.asList(
                new CartItemUpdateRequest("2", "3", MealType.Breakfast)
                , new CartItemUpdateRequest("3", "5", MealType.Lunch)
        );


        Mockito.when(userService.getuser(10)).thenThrow(new NotFoundException("User"));

        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.addToCart) + "/{userId}", 10)
                        .content(objectWriter.writeValueAsString(cartItemUpdateRequests))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat("user not found.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testGetCartSuccess() throws Exception {

        //mock data
        int requestedCartId = 2;
        Mockito.when(cartService.getCart(requestedCartId)).thenReturn(cart2);


        String r = mockMvc.perform(MockMvcRequestBuilders
                        .get(prefix(Route.Cart.getCart) + "/{cartId}", requestedCartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


        Cart res = mapFromJson(r, Cart.class);
        assertThat(cart2.getId(), is(res.getId()));

    }

    @Test
    public void testGetCartFailure() throws Exception {

        //mock data
        int requestedCartId = 5;
        Mockito.when(cartService.getCart(requestedCartId)).thenThrow(new NotFoundException("Cart"));


        var r = mockMvc.perform(MockMvcRequestBuilders
                        .get(prefix(Route.Cart.getCart) + "/{cartId}", requestedCartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity jsonEntity = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.NOT_FOUND.value(), is(jsonEntity.getStatus()));

    }

    @Test
    public void testGetCartFailureNegative() throws Exception {

        //mock data
        int requestedCartId = -1;
        Mockito.when(cartService.getCart(requestedCartId)).thenThrow(new AccessDeniedException("Access denied."));


        var r = mockMvc.perform(MockMvcRequestBuilders
                        .get(prefix(Route.Cart.getCart) + "/{cartId}", requestedCartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn().getResponse();

        Response.JsonEntity jsonEntity = mapFromJson(r.getContentAsString(), Response.JsonEntity.class);
        assertThat(HttpStatus.FORBIDDEN.value(), is(jsonEntity.getStatus()));

    }

    @Test
    public void testClearCartSuccess() throws Exception {

        Cart fullCart = Cart.builder()
                .cartItems(Arrays.asList(cartItem1, cartItem2))
                .id(10).build();

        Cart emptyCart = Cart.builder()
                .cartItems(new ArrayList<>())
                .id(10).build();

        Mockito.when(cartService.getCart(10)).thenReturn(fullCart);
        Mockito.when(cartService.isNotNull(fullCart)).thenReturn(true);
        Mockito.when(cartService.clearCart(fullCart)).thenReturn(emptyCart);

        var r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.clear) + "/{cartId}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();


        Response.JsonEntity jsonEntity = mapFromJson(r.getContentAsString(), Response.JsonEntity.class);
        assertThat(HttpStatus.OK.value(), is(jsonEntity.getStatus()));
        assertThat("Cart cleared.", is(jsonEntity.getMessage().toString().trim()));

    }

    @Test
    public void testClearCartFailure() throws Exception {

        Mockito.when(cartService.getCart(10)).thenThrow(new NotFoundException("Cart"));

        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.clear) + "/{cartId}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity jsonEntity = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.NOT_FOUND.value(), is(jsonEntity.getStatus()));
        assertThat("cart not found.", is(jsonEntity.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testUpdateQuantityIncreaseSuccess() throws Exception {

        Integer itemId = 10;
        Integer factor = 2;

        Mockito.when(cartItemService.getItem(itemId)).thenReturn(cartItem1);
        Mockito.when(cartItemService.isNotNull(cartItem1)).thenReturn(true);

        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.updateQuantity + "/inc/{itemId}/{factor}"), itemId, factor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("cartitem quantity updated.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testUpdateQuantityIncreaseFailure() throws Exception {

        Integer itemId = 10;
        Integer factor = 2;

        Mockito.when(cartItemService.getItem(itemId)).thenThrow(new NotFoundException("CartItem"));

        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.updateQuantity + "/inc/{itemId}/{factor}"), itemId, factor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat("cartitem not found.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testUpdateQuantityDecreaseSuccess() throws Exception {

        Integer itemId = 10;
        Integer factor = 2;

        Mockito.when(cartItemService.getItem(itemId)).thenReturn(cartItem1);
        Mockito.when(cartItemService.isNotNull(cartItem1)).thenReturn(true);

        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.updateQuantity + "/dec/{itemId}/{factor}"), itemId, factor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("cartitem quantity updated.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testUpdateQuantityDecreaseFailure() throws Exception {

        Integer itemId = 10;
        Integer factor = 2;

        Mockito.when(cartItemService.getItem(itemId)).thenThrow(new NotFoundException("CartItem"));

        String r = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix(Route.Cart.updateQuantity + "/dec/{itemId}/{factor}"), itemId, factor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat("cartitem not found.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testRemoveFromCartSuccess() throws Exception {

        Integer cartId = 1;
        Integer itemId = 2;

        Mockito.when(cartService.getCart(cartId)).thenReturn(cart1);
        Mockito.when(cartService.isNotNull(cart1)).thenReturn(true);
        Mockito.when(cartItemService.getItem(itemId)).thenReturn(cartItem1);
        Mockito.when(cartItemService.isNotNull(cartItem1)).thenReturn(true);

        String r = mockMvc.perform(MockMvcRequestBuilders
                .post(prefix(Route.Cart.remove + "/{cartId}/{itemId}"), cartId, itemId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("cartitem removed.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testRemoveFromCartFailure() throws Exception {

        Integer cartId = 1;
        Integer itemId = 2;

        Mockito.when(cartService.getCart(cartId)).thenThrow(new NotFoundException("Cart"));

        String r = mockMvc.perform(MockMvcRequestBuilders
                .post(prefix(Route.Cart.remove + "/{cartId}/{itemId}"), cartId, itemId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        assertThat(HttpStatus.NOT_FOUND.value(), is(res.getStatus()));
        assertThat("cart not found.", is(res.getMessage().toString().trim().toLowerCase()));

    }
}
