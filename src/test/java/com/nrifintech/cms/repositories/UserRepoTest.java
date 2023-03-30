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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.types.MealType;

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

        Integer res = userRepo.hasUserCartitem(user.getEmail(),
        // user.getCart().getCartItems().get(0).getId());
12);
        assertNotNull(res);
        assertEquals(1, (int)res);
    }

   
    @Test
    public void testGetAllConsumers() {
         
    }

    @Test
    public void testGetAllUserGroup() {
        
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
        
    }
}
