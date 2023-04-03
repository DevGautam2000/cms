package com.nrifintech.cms.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.types.MealType;
import com.nrifintech.cms.types.Role;

@RunWith(MockitoJUnitRunner.class)
public class UserRepoTest {
    @Mock
    private UserRepo userRepo;

    @Before
    public void setup(){
       MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testFindByEmail() {
        Optional<User> user= Optional.of(User.builder().email("abcd@gmail.com").build());


        Mockito.when(userRepo.findByEmail(anyString())).thenReturn(user);

        Optional<User> userActual = userRepo.findByEmail(user.get().getEmail());
        
        assertEquals(userActual.get().getEmail(), user.get().getEmail());
    }


    
    @Test
    public void testGetUserByOrderDate() {

        User user = User.builder().id(10).email("use@test").build();

        List<String> users = new ArrayList<>(); 
        users.add(user.getEmail());

        Mockito.when(userRepo.getUserByOrderDate(any(Date.class)))
        .thenReturn(users);

        List<String> res = userRepo.getUserByOrderDate(new Date(System.currentTimeMillis()));

        assertNotNull(res);
        assertEquals(user.getEmail(), res.get(0));
    }

    @Test
    public void testHasUserCartitem() {


        List<CartItem> cartItems =  new ArrayList<>();
        cartItems.add(CartItem.builder()
        .id(90)
        .name("Chicken")
        .build());

        User user = User.builder().id(10).email("use@test").cart(
            Cart.builder().id(13).cartItems(cartItems).build()
        ).build();

       
        Mockito.when(userRepo.hasUserCartitem(anyString(),anyInt()))
        .thenReturn(1);

        int res = userRepo.hasUserCartitem(user.getEmail(),
        user.getCart().getCartItems().get(0).getId());
        
        assertNotNull(res);
        assertEquals(1, (int)res);
    }


    @Test
    public void testHasUserCartitemFailure() {


        List<CartItem> cartItems =  new ArrayList<>();
        cartItems.add(CartItem.builder()
        .id(90)
        .name("Chicken")
        .build());

        User user = User.builder().id(10).email("use@test").cart(
            Cart.builder().id(13).cartItems(cartItems).build()
        ).build();

       
        Mockito.when(userRepo.hasUserCartitem(anyString(),anyInt()))
        .thenReturn(0);

        int res = userRepo.hasUserCartitem(user.getEmail(),12);
        assertNotNull(res);
        assertEquals(0, res);
    }

    @Test
    public void testGetAllConsumers() {
        List<String> consumers =new ArrayList<>();
        consumers.add("user@test");

        Mockito.when(userRepo.getAllConsumers()).thenReturn(Optional.of(consumers));

        Optional<List<String>> res = userRepo.getAllConsumers();

        assertNotNull(res);
        assertEquals(consumers.get(0), res.get().get(0));
    }

    @Test
    public void testGetAllUserGroup() {

        List<Tuple> tuples = new ArrayList<>();
        Tuple tuple = MockMvcSetup.tupleOf(Role.Admin, 3, Integer.class);

        tuples.add(tuple);


        Mockito.when(userRepo.getAllUserGroup()).thenReturn(tuples);

        List<Tuple> res = userRepo.getAllUserGroup();

        assertNotNull(res);
        assertEquals(Role.Admin, res.get(0).get(0));
        assertEquals(3, res.get(0).get(1));
        
    }


    @Test
    public void testGetUserByOrderDateAndType() {
        User user = User.builder().id(10).email("use@test").build();

        List<String> users = new ArrayList<>(); 
        users.add(user.getEmail());

        Mockito.when(userRepo.getUserByOrderDateAndType(any(Date.class),anyInt()))
        .thenReturn(users);

        List<String> res = userRepo.getUserByOrderDateAndType(new Date(System.currentTimeMillis()) , MealType.Breakfast.ordinal());

        assertNotNull(res);
        assertEquals(user.getEmail(), res.get(0));
    }

    @Test
    public void testGetUserByOrderId() {
        User user = User.builder().id(10).email("use@test").build();

        Mockito.when(userRepo.getUserByOrderId(anyInt()))
        .thenReturn(user.getEmail());

        String res = userRepo.getUserByOrderId(121);

        assertNotNull(res);
        assertEquals(user.getEmail(), res);
        
    }

    @Test
    public void testGetUserEmailsByRole() {
        List<String> emails = new ArrayList<>();

        emails.add("user@test");
        emails.add("user2@test");

        List<Tuple> tuples = new ArrayList<>();
        Tuple tuple = MockMvcSetup.tupleOf(emails, null, Integer.class);

        tuples.add(tuple);


        Mockito.when(userRepo.getUserEmailsByRole(anyInt())).thenReturn(tuples);

        List<Tuple> res = userRepo.getUserEmailsByRole(Role.User.ordinal());

        List<String> actualEmails = (List<String>) res.get(0).get(0);

        assertNotNull(res);
        assertEquals(emails.get(0), actualEmails.get(0));
    }
}
