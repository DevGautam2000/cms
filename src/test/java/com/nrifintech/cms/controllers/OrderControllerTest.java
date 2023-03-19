package com.nrifintech.cms.controllers;


import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.*;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
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

    @InjectMocks
    private OrderController orderController;

    private List<Order> orders;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Order.prefix, this, orderController);
        loadData();
    }

    private void loadData() {
        orders = new ArrayList<>();
        orders.add(Order.builder().id(10).orderType(MealType.Lunch).build());
        orders.add(Order.builder().id(12).orderType(MealType.Lunch).build());
        orders.add(Order.builder().id(13).orderType(MealType.Lunch).build());
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
        assertThat(orders.get(0).getId(),is(res[0].getId()));
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

        Mockito.when(orderService.getOrders( anyList() )).thenReturn(orders);
        Mockito.when(orderService.isNotNull(orders)).thenReturn(true);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Order.getOrders + "/{orderIds}"),10,12,13)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Order[] res = mapFromJson(r, Order[].class);
        assertThat(orders.size(), is(res.length));
        assertThat(orders.get(0).getId(),is(res[0].getId()));


    }

    @Test
    public void testGetOrdersForOrderIdsFailure() throws Exception {

        Mockito.when(orderService.getOrders( anyList() )).thenReturn(null);

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(prefix(Route.Order.getOrders + "/{orderIds}"),10,12,13)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();


        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.value(), is(res.getStatus()));
        assertThat("error getting orders.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    
}
