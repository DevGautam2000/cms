package com.nrifintech.cms.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.dtos.CartItemUpdateRequest;
import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.CartItemService;
import com.nrifintech.cms.services.CartService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.utils.ErrorHandlerImplemented;

/**
 * > This class  handles requests to the `/cart` endpoint
 */
@RestController
@RequestMapping(Route.Cart.prefix)
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    private String itemNotFoundErrorMsg = "CartItem not found. ";

    /**
     * It adds the items to the cart
     * 
     * @param userId The id of the user whose cart is to be updated.
     * @param reqs List of CartItemUpdateRequest objects
     * @return A Response object is being returned.
     */
    @PostMapping(Route.Cart.addToCart + "/{userId}")
    public Response addToCart(@PathVariable Integer userId, @RequestBody List<CartItemUpdateRequest> reqs) {

        User user = userService.getuser(userId);

        if (userService.isNotNull(user)) {

            Cart cart = user.getCart();

            if (cartService.isNull(cart))
                cart = new Cart();

            if (cartService.isNotNull(cart)) {

                cart = cartService.addToCart(reqs, cart);

                if (cartService.isNotNull(cart)) {

					//save the cart 
                    cartService.saveCart(cart);

                    user.setCart(cart);
                    userService.saveUser(user);

                    return Response.setMsg("Added to cart.", HttpStatus.OK);
                } else
                    return Response.setErr("Duplicate Entry.", HttpStatus.BAD_REQUEST);
            }
        }

        return Response.setErr("User not found.", HttpStatus.BAD_REQUEST);

    }

   /**
    * It increases the quantity of a cart item by a factor
    * 
    * @param itemId The id of the cart item to be updated.
    * @param factor The amount by which the quantity is to be increased.
    * @return A Response object.
    */
    @PostMapping(Route.Cart.updateQuantity + "/inc/{itemId}/{factor}")
    public Response updateQuantityIncrease(@PathVariable Integer itemId, @PathVariable Integer factor) {
        CartItem cartItem = cartItemService.getItem(itemId);

        if (cartItemService.isNotNull(cartItem)) {

            cartItem.increaseBy(factor);
            cartItemService.saveItem(cartItem);
            return Response.setMsg("CartItem quantity updated. ", HttpStatus.OK);
        }

        return Response.setErr(itemNotFoundErrorMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * It decreases the quantity of a cart item by a factor
     * 
     * @param itemId The id of the cart item to be updated.
     * @param factor The amount by which the quantity is to be decreased.
     * @return A Response object.
     */
    @PostMapping(Route.Cart.updateQuantity + "/dec/{itemId}/{factor}")
    public Response updateQuantityDecrease(@PathVariable Integer itemId, @PathVariable Integer factor) {
        CartItem cartItem = cartItemService.getItem(itemId);

        if (cartItemService.isNotNull(cartItem)) {

            cartItem.decreaseBy(factor);

            cartItemService.saveItem(cartItem);
            return Response.setMsg("CartItem quantity updated. ", HttpStatus.OK);
        }

        return Response.setErr(itemNotFoundErrorMsg, HttpStatus.BAD_REQUEST);
    }

   /**
    * > Removes a cart item from a cart
    * 
    * @param cartId The id of the cart you want to remove the item from.
    * @param itemId The id of the item to be removed from the cart.
    * @return A Response object.
    */
    @PostMapping(Route.Cart.remove + "/{cartId}/{itemId}")
    public Response removeFromCart(@PathVariable Integer cartId, @PathVariable Integer itemId) {
        Cart cart = cartService.getCart(cartId);

        if (cartService.isNotNull(cart)) {
            CartItem cartItem = cartItemService.getItem(itemId);

            if (cartItemService.isNotNull(cartItem)) {
                cart.getCartItems().remove(cart.getCartItems().indexOf(cartItem));
                cartService.saveCart(cart);

                cartItemService.deleteItem(cartItem.getId());
                return Response.setMsg("CartItem removed. ", HttpStatus.OK);
            }

        }
        return Response.setErr(itemNotFoundErrorMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * It clears the cart by removing all the items in the cart
     * 
     * @param cartId The id of the cart to be cleared.
     * @return A Response object.
     */
    @PostMapping(Route.Cart.clear + "/{cartId}")
    public Response removeFromCart(@PathVariable Integer cartId) {
        Cart cart = cartService.getCart(cartId);

        if (cartService.isNotNull(cart)) {

            cart = cartService.clearCart(cart);

            if (cart.getCartItems().isEmpty()) {
                return Response.setMsg("Cart cleared. ", HttpStatus.OK);
            }

            return Response.setErr("Error clearing cart. ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return Response.setErr(itemNotFoundErrorMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * It returns a cart with the given cartId.
     * 
     * @param cartId The id of the cart you want to get.
     * @return Response object
     */
    @ErrorHandlerImplemented(handler = NotFoundException.class)
    @GetMapping(Route.Cart.getCart + "/{cartId}")
    public Response getCart(@PathVariable  Integer cartId) {

        Cart cart = cartService.getCart(cartId);

        return Response.set(cart, HttpStatus.OK);

    }

    /**
     * It gets the cart of the user who is logged in
     * 
     * @param principal This is the user object that is passed to the controller.
     * @return A cart object
     */
    @GetMapping(Route.Cart.getCart)
    public Response getCart(Principal principal) {

        User user = userService.getuser(principal.getName());

        if (userService.isNotNull(user.getCart())) {

            Cart cart = user.getCart();
            return Response.set(cart, HttpStatus.OK);

        }

        return Response.setErr(itemNotFoundErrorMsg, HttpStatus.NOT_ACCEPTABLE);
    }

}
