package com.nrifintech.cms.controllers;


import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.dtos.OrderResponseRequest;
import com.nrifintech.cms.entities.*;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.*;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest extends MockMvcSetup {


    @Mock
    private OrderService orderService;

    @Mock
    private MenuService menuService;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private WalletService walletService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private OrderController orderController;

    private List<Order> orders;
    private Order order;
    private FeedBack feedBack;

    private Principal principal;
    private User user;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Order.prefix, this, orderController);
        loadData();
    }

    private void loadData() {
        orders = new ArrayList<>();
        order = Order.builder().id(10).orderType(MealType.Lunch).build();
        orders.add(order);
        orders.add(Order.builder().id(12).orderType(MealType.Lunch).build());
        orders.add(Order.builder().id(13).orderType(MealType.Lunch).build());

        feedBack = new FeedBack();
        feedBack.setRating(5);
        feedBack.setComments("Great service!");
        order.setFeedBack(feedBack);

        user = User.builder().id(20).records(orders).build();
        principal = user::getUsername;
    }

    @Test
    public void testGetOrdersSuccess() throws Exception {


        Mockito.when(orderService.getOrders()).thenReturn(orders);
        Mockito.when(orderService.isNotNull(orders)).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Order.getOrders))
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Order[] res = mapFromJson(r, Order[].class);
        assertThat(orders.size(), is(res.length));
        assertThat(orders.get(0).getId(), is(res[0].getId()));
    }

    @Test
    public void testGetOrdersFailure() throws Exception {


        Mockito.when(orderService.getOrders()).thenReturn(null);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Order.getOrders))
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.value(), is(res.getStatus()));
        assertThat("error getting orders.", is(res.getMessage().toString().trim().toLowerCase()));
    }

    @Test
    public void testGetOrdersForOrderIdsSuccess() throws Exception {

        Mockito.when(orderService.getOrders(anyList())).thenReturn(orders);
        Mockito.when(orderService.isNotNull(orders)).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Order.getOrders + "/{orderIds}"), 10, 12, 13)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Order[] res = mapFromJson(r, Order[].class);
        assertThat(orders.size(), is(res.length));
        assertThat(orders.get(0).getId(), is(res[0].getId()));


    }

    @Test
    public void testGetOrdersForOrderIdsFailure() throws Exception {

        Mockito.when(orderService.getOrders(anyList())).thenReturn(null);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Order.getOrders + "/{orderIds}"), 10, 12, 13)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.value(), is(res.getStatus()));
        assertThat("error getting orders.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testAddFeedbackSuccess() throws Exception {
        // Arrange
        Integer orderId = 10;
        Mockito.when(orderService.addFeedBackToOrder(eq(orderId), any(FeedBack.class))).thenReturn(order);
        Mockito.when(orderService.isNotNull(any(Order.class))).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.FeedBack.addFeedback + "/{orderId}"), orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(feedBack))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals("feedback added.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testAddFeedbackFailureIfFeedBackExistsForOrder() throws Exception {
        // Arrange
        Integer orderId = 10;

        Mockito.when(orderService.addFeedBackToOrder(eq(orderId), any(FeedBack.class))).thenReturn(feedBack);
        Mockito.when(orderService.isNotNull(any(FeedBack.class))).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.FeedBack.addFeedback + "/{orderId}"), orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(feedBack))
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatus());
        assertEquals("feedback already exists.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testAddFeedbackFailureIfOrderDoesNotExist() throws Exception {
        // Arrange
        Integer orderId = 10;

        Mockito.when(orderService.addFeedBackToOrder(eq(orderId), any(FeedBack.class))).thenReturn(null);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.FeedBack.addFeedback + "/{orderId}"), orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(feedBack))
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatus());
        assertEquals("order not found.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testUpdateStatusSuccess() throws Exception {

        Integer orderId = 12;
        Integer statusId = Status.Delivered.ordinal();

        order.setStatus(Status.Pending);

        Mockito.when(orderService.getOrder(eq(orderId))).thenReturn(order);
        Mockito.when(orderService.isNotNull(order)).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.Order.updateStatus + "/{orderId}/{statusId}"), orderId, statusId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals("order delivered.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testUpdateStatusSuccessForCancelled() throws Exception {

        Integer orderId = 12;
        Integer statusId = Status.Cancelled.ordinal();

        order.setStatus(Status.Pending);

        Mockito.when(orderService.getOrder(eq(orderId))).thenReturn(order);
        Mockito.when(orderService.isNotNull(order)).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.Order.updateStatus + "/{orderId}/{statusId}"), orderId, statusId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.OK.value(), res.getStatus());
        assertEquals("order cancelled.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testUpdateStatusFailureIfStatusRequestedIsPending() throws Exception {

        Integer orderId = 12;
        Integer statusId = Status.Pending.ordinal();

        order.setStatus(Status.Pending);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.Order.updateStatus + "/{orderId}/{statusId}"), orderId, statusId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatus());
        assertEquals("operation not allowed.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testUpdateStatusFailureIfOrderAlreadyDelivered() throws Exception {

        Integer orderId = 12;
        Integer statusId = Status.Delivered.ordinal();

        order.setStatus(Status.Delivered);

        Mockito.when(orderService.getOrder(eq(orderId))).thenReturn(order);
        Mockito.when(orderService.isNotNull(order)).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.Order.updateStatus + "/{orderId}/{statusId}"), orderId, statusId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), res.getStatus());
        assertEquals("operation not allowed.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testPlaceOrderSuccess() throws Exception {

        int userId = 20;

        Wallet wallet = Wallet.builder().id(23).balance(2000d).build();


        Mockito.when(menuService.isServingToday()).thenReturn(true);

        Mockito.when(userService.getuser(userId)).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        user.setWallet(wallet);

        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(
                CartItem.
                        builder()
                        .id(1)
                        .name("Item 1")
                        .quantity(10)
                        .price(20d)
                        .mealType(MealType.Breakfast)
                        .itemType(ItemType.Veg).build());
        cartItems.add(
                CartItem.
                        builder()
                        .id(12)
                        .name("Item 1")
                        .quantity(13)
                        .mealType(MealType.Lunch)
                        .price(20d)
                        .itemType(ItemType.Veg).build());

        cart.setCartItems(cartItems);
        user.setCart(cart);

        Mockito.when(walletService.isNotNull(wallet)).thenReturn(true);
        Mockito.when(walletService.checkMinimumAmount(wallet)).thenReturn(true);

        Order lunchOrder = order;
        Order breakFastOrder = Order.builder().id(12).orderType(MealType.Breakfast).build();

        Mockito.when(orderService.addNewOrder(MealType.Lunch)).thenReturn(lunchOrder);
        Mockito.when(orderService.addNewOrder(MealType.Breakfast)).thenReturn(breakFastOrder);

        Mockito.when(orderService.isNotNull(any(Order.class))).thenReturn(true);

        Mockito.when(cartService.isNull(cart)).thenReturn(false);
        Mockito.when(orderService.isNull(cartItems)).thenReturn(false);

        Mockito.when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        Mockito.when(userService.saveUser(any(User.class))).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        Transaction transaction = Transaction.builder().id(30).amount(200).build();
        List<Object> objects = new ArrayList<>();
        objects.add(wallet);
        objects.add(transaction);

        Mockito.when(walletService.updateWallet(any(Wallet.class), any(Integer.class))).thenReturn(objects);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(prefix(Route.Order.placeOrder + "/{id}"), userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("added 2 new orders.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testPlaceOrderSuccessForLunch() throws Exception {

        int userId = 20;

        Wallet wallet = Wallet.builder().id(23).balance(2000d).build();


        Mockito.when(menuService.isServingToday()).thenReturn(true);

        Mockito.when(userService.getuser(userId)).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        user.setWallet(wallet);

        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(
                CartItem.
                        builder()
                        .id(12)
                        .name("Item 1")
                        .quantity(13)
                        .mealType(MealType.Lunch)
                        .price(20d)
                        .itemType(ItemType.Veg).build());

        cart.setCartItems(cartItems);
        user.setCart(cart);

        Mockito.when(walletService.isNotNull(wallet)).thenReturn(true);
        Mockito.when(walletService.checkMinimumAmount(wallet)).thenReturn(true);

        Order lunchOrder = order;
        Order breakFastOrder = Order.builder().id(12).orderType(MealType.Breakfast).build();

        Mockito.when(orderService.addNewOrder(MealType.Lunch)).thenReturn(lunchOrder);
        Mockito.when(orderService.addNewOrder(MealType.Breakfast)).thenReturn(breakFastOrder);

        Mockito.when(orderService.isNotNull(any(Order.class))).thenReturn(true);

        Mockito.when(cartService.isNull(cart)).thenReturn(false);
        Mockito.when(orderService.isNull(cartItems)).thenReturn(false);

        Mockito.when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        Mockito.when(userService.saveUser(any(User.class))).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        Transaction transaction = Transaction.builder().id(30).amount(200).build();
        List<Object> objects = new ArrayList<>();
        objects.add(wallet);
        objects.add(transaction);

        Mockito.when(walletService.updateWallet(any(Wallet.class), any(Integer.class))).thenReturn(objects);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(prefix(Route.Order.placeOrder + "/{id}"), userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("added new order.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testPlaceOrderSuccessForBreakFast() throws Exception {

        int userId = 20;

        Wallet wallet = Wallet.builder().id(23).balance(2000d).build();


        Mockito.when(menuService.isServingToday()).thenReturn(true);

        Mockito.when(userService.getuser(userId)).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        user.setWallet(wallet);

        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(
                CartItem.
                        builder()
                        .id(12)
                        .name("Item 1")
                        .quantity(13)
                        .mealType(MealType.Breakfast)
                        .price(20d)
                        .itemType(ItemType.Veg).build());

        cart.setCartItems(cartItems);
        user.setCart(cart);

        Mockito.when(walletService.isNotNull(wallet)).thenReturn(true);
        Mockito.when(walletService.checkMinimumAmount(wallet)).thenReturn(true);

        Order lunchOrder = order;
        Order breakFastOrder = Order.builder().id(12).orderType(MealType.Breakfast).build();

        Mockito.when(orderService.addNewOrder(MealType.Lunch)).thenReturn(lunchOrder);
        Mockito.when(orderService.addNewOrder(MealType.Breakfast)).thenReturn(breakFastOrder);

        Mockito.when(orderService.isNotNull(any(Order.class))).thenReturn(true);

        Mockito.when(cartService.isNull(cart)).thenReturn(false);
        Mockito.when(orderService.isNull(cartItems)).thenReturn(false);

        Mockito.when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        Mockito.when(userService.saveUser(any(User.class))).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        Transaction transaction = Transaction.builder().id(30).amount(200).build();
        List<Object> objects = new ArrayList<>();
        objects.add(wallet);
        objects.add(transaction);

        Mockito.when(walletService.updateWallet(any(Wallet.class), any(Integer.class))).thenReturn(objects);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(prefix(Route.Order.placeOrder + "/{id}"), userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("added new order.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testPlaceOrderFailure() throws Exception {

        int userId = 20;

        Mockito.when(menuService.isServingToday()).thenReturn(true);

        Mockito.when(userService.getuser(userId)).thenReturn(null);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(prefix(Route.Order.placeOrder + "/{id}"), userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.BAD_REQUEST.value(), is(res.getStatus()));
        assertThat("user does not exist.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testPlaceOrderFailureIfWalletNotFound() throws Exception {

        int userId = 20;

        Mockito.when(menuService.isServingToday()).thenReturn(true);

        Mockito.when(userService.getuser(userId)).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        Mockito.when(walletService.isNotNull(user.getWallet())).thenReturn(false);

        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(prefix(Route.Order.placeOrder + "/{id}"), userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.BAD_REQUEST.value(), is(res.getStatus()));
        assertThat("wallet not found.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testPlaceOrderFailureIfWalletBalanceLow() throws Exception {

        int userId = 20;

        Wallet wallet = Wallet.builder().id(23).balance(2000d).build();


        Mockito.when(menuService.isServingToday()).thenReturn(true);

        Mockito.when(userService.getuser(userId)).thenReturn(user);
        Mockito.when(userService.isNotNull(user)).thenReturn(true);

        user.setWallet(wallet);

        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(
                CartItem.
                        builder()
                        .id(12)
                        .name("Item 1")
                        .quantity(13)
                        .mealType(MealType.Breakfast)
                        .price(20d)
                        .itemType(ItemType.Veg).build());

        cart.setCartItems(cartItems);
        user.setCart(cart);

        Mockito.when(walletService.isNotNull(wallet)).thenReturn(true);
        Mockito.when(walletService.checkMinimumAmount(wallet)).thenReturn(false);


        String r = mockMvc.perform(
                        MockMvcRequestBuilders.post(prefix(Route.Order.placeOrder + "/{id}"), userId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotAcceptable())
                .andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.NOT_ACCEPTABLE.value(), is(res.getStatus()));
        assertThat("low wallet balance.", is(res.getMessage().toString().trim().toLowerCase()));

    }



}


