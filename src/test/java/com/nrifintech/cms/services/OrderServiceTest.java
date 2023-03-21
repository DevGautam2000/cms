package com.nrifintech.cms.services;

import com.nrifintech.cms.entities.FeedBack;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.OrderRepo;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private FeedBackService feedBackService;

    @InjectMocks
    private OrderService orderService;


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAdOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder().id(10).orderType(MealType.Lunch).build());

        Mockito.when(orderRepo.saveAll(anyList())).thenReturn(orders);

        List<Order> expectedList = orderService.addOrders(orders);

        assertEquals(orders, expectedList);
    }

    @Test
    public void testSaveOrder() {
        Order order = mock(Order.class);
        order.setId(20);

        Mockito.when(orderRepo.save(any(Order.class))).thenReturn(order);

        Order expectedOrder = orderService.saveOrder(order);

        assertEquals(order, expectedOrder);
    }

    @Test
    public void testAddNewOrder() {

        Order order = Order.builder().orderType(MealType.Breakfast).status(Status.Pending).build();

        Order expectedOrder = orderService.addNewOrder(MealType.Breakfast);


        assert expectedOrder != null;
        assertEquals(order.getStatus(), expectedOrder.getStatus());
    }


    @Test
    public void testGetOrders() {

        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder().id(10).build());

        Mockito.when(orderRepo.findAll()).thenReturn(orders);

        List<Order> expectedList = orderService.getOrders();

        assertEquals(orders, expectedList);
    }

    @Test
    public void testGetOrdersForOrderIds() {

        List<String> orderIds = new ArrayList<>();
        orderIds.add("10");
        orderIds.add("13");

        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder().id(10).build());
        orders.add(Order.builder().id(12).build());

        orderIds.forEach(id -> {
            Mockito.when(orderRepo.findById(anyInt())).thenReturn(Optional.ofNullable(orders.get(orderIds.indexOf(id))));
        });
        List<Order> expectedList = orderService.getOrders(orderIds);

        assert expectedList != null;
        assertEquals(orderIds.size(), expectedList.size());
    }

    @Test
    public void testAddFeedBackToOrder() {

        Order order = Order.builder().id(10).feedBack(
                null).build();

        OrderService mock = mock(OrderService.class);

        Mockito.when(mock.getOrder(anyInt())).thenReturn(order);
        Mockito.when(orderRepo.findById(anyInt())).thenReturn(Optional.of(order));

        Mockito.lenient()
                .doAnswer((Answer<Void>) invocation -> null)
                .when(feedBackService).addFeedBack(any(FeedBack.class));

        Mockito.lenient()
                .doAnswer((Answer<Void>) invocation -> null)
                .when(orderRepo).save(any(Order.class));


        Object expectedOrder = orderService.addFeedBackToOrder(order.getId(),new FeedBack(12, 3, "Good food."));

        assertEquals( order ,  expectedOrder);
    }


    @Test
    public void testGetOrderSuccess(){

        int orderId = 10;
        Order order = mock(Order.class);

        Mockito.when(orderRepo.findById(orderId)).thenReturn(Optional.ofNullable(order));

        Order expectedOrder = orderService.getOrder(orderId);

        assertEquals(order,expectedOrder);

    }


    @Test
    public void testGetOrderFailure(){


        int orderId = 10;
        Order order = mock(Order.class);

        NotFoundException notFoundException = new NotFoundException("Item");
        Mockito.when(orderRepo.findById(orderId)).thenThrow(notFoundException);

        Exception expected = null;
        try {
            Order expectedOrder = orderService.getOrder(orderId);
        }catch (NotFoundException ex){
            expected=ex;
        }

        assert expected != null;
        assertEquals(notFoundException.getMessage() , expected.getMessage() );

    }

    @Test
    public void testAutoArchive(){

        Mockito.lenient()
                .doAnswer((Answer<Void>) invocation -> null)
                .when(orderRepo).autoArchive("2023-03-02");


    }
}
