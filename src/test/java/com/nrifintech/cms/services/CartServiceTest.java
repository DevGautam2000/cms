package com.nrifintech.cms.services;

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Item;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.repositories.CartRepo;
import com.nrifintech.cms.types.MealType;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartService cartService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCart(){

        Cart cart = mock(Cart.class);

        Mockito.when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart expectedCart  = cartService.saveCart(cart);

        assertEquals(cart,expectedCart);
    }

    @Test
    public void testAddNewCart(){

        Cart cart = mock(Cart.class);

        Mockito.when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart expectedCart  = cartService.addNewCart();

        assertEquals(cart,expectedCart);
    }

    @Test
    public void testGetCartSuccess(){

        int cartId = 10;
        Cart cart = mock(Cart.class);
        cart.setId(cartId);

        Mockito.when(cartRepo.findById(cartId)).thenReturn(Optional.ofNullable(cart));

        Cart expectedCart = cartService.getCart(cartId);

        assertEquals(cart,expectedCart);

    }


    @Test
    public void testGetItemFailure(){


        int cartId = 10;
        Cart cart = mock(Cart.class);
        cart.setId(cartId);

        NotFoundException notFoundException = new NotFoundException("Cart");
        Mockito.when(cartRepo.findById(cartId)).thenThrow(notFoundException);

        Exception expected = null;
        try {
            Cart expectedCart = cartService.getCart(cartId);
        }catch (NotFoundException ex){
            expected=ex;
        }

        assert expected != null;
        assertEquals(notFoundException.getMessage() , expected.getMessage() );

    }

    @Test
    public void testClearCart(){

        int cartItemId = 10;
        Cart cart = mock(Cart.class);
        cart.setId(20);
        cart.setCartItems(new ArrayList<>());
        cart.getCartItems().add(CartItem.builder().id(10).build());

        Mockito.lenient()
                .doAnswer((Answer<Void>) invocation -> null)
                .when(cartItemService).deleteItem(cartItemId);
        Mockito.when(cartService.saveCart(cart)).thenReturn(Cart.builder().cartItems(new ArrayList<>()).build());

        Cart expectedCart = cartService.clearCart(cart);

        cart.getCartItems().clear();
        assertEquals(cart.getCartItems().size() , expectedCart.getCartItems().size());
    }

    @Test
    public void testAddToCart(){

        Cart cart = mock(Cart.class);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(CartItem.builder().id(10).name("paneer").build());

        Mockito.when(cartItemService.getCartItems(anyList())).thenReturn(cartItems);

        Mockito.when(cartItemService.saveItems(anyList())).thenReturn(cartItems);

        List<CartItemUpdateRequest> requests = Arrays.asList(
                new CartItemUpdateRequest("10","23", MealType.Breakfast),
                new CartItemUpdateRequest("11","24", MealType.Lunch)
        );

        Cart expectedCart = cartService.addToCart(requests,cart);

        assertEquals(cart,expectedCart);

    }
}
