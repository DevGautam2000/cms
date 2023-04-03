package com.nrifintech.cms.config.guard;

import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.services.UserService;

@Component
/**
 * > This class is an adapter that allows the Spring Security AccessDecisionManager to be used as an
 * AuthorizationManager
 */
public class AccessDecisionManagerAuthorizationManagerAdapter {
    @Autowired
    private UserService uService;

   /**
    * If the user has the authority of the given auths or the user's email is the same as the email of
    * the user with the given id and the user is of type User, then return true
    * 
    * @param authentication The authentication object that was passed to the method.
    * @param object The object that is being authorized.
    * @return AuthorizationDecision
    */
    public AuthorizationDecision preCheckUserWithId(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch( e->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("id"));
        String emailById = uService.getuser(id).getEmail();
        //email in principal & id are same && email is of Type User || email has authority of auths 
        return new AuthorizationDecision((email.equals(emailById) && upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User"))) || flag);
    }

    /**
     * If the user has the authority of the given auths or the user has the cartitem with the given id,
     * then return true
     * 
     * @param authentication The authentication object that was passed into the method.
     * @param object The object being authorized.
     * @return AuthorizationDecision
     */
    public AuthorizationDecision preCheckHasUserCartitem(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch( e->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("itemId"));

        //email in principal & id are same && email is of Type User || email has authority of auths 
        return new AuthorizationDecision(uService.hasUserCartitem(email, id) && 
        upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }

    
   /**
    * > If the user has the authority of the given auths or the user is of type User and the cartId in
    * the request is the same as the cartId of the user, then return true
    * 
    * @param authentication The authentication object that was passed into the method.
    * @param object The object being authorized.
    * @return AuthorizationDecision
    */
    public AuthorizationDecision preCheckUserCartId(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("cartId"));

        Cart cart = uService.getuser(email).getCart();
        if(cart==null)return new AuthorizationDecision(false);
        
        //email in principal & id are same && email is of  Type User || email has authority of auths 
        return new AuthorizationDecision(cart.getId()==id && upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }
    
    
    /**
     * If the user has the authority of the auths, or the user is of type User and the orderId is in
     * the user's records, then the user is authorized
     * 
     * @param authentication The authentication object that was used to authenticate the user.
     * @param object The object being authorized.
     * @return AuthorizationDecision
     */
    public AuthorizationDecision preCheckUserOrderId(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("orderId"));
        
        List<Order> rec = uService.getuser(email).getRecords();
        if(rec.isEmpty())return new AuthorizationDecision(false);
        
        boolean hasOrder=false;
        for(Order o:rec){
            if(o.getId()==id)hasOrder=true;
        }
        //email in principal & id are same && email is of  Type User || email has authority of auths 
        return new AuthorizationDecision(hasOrder && upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }

    /**
     * > This function checks if the user has the authority of the auths passed as arguments and if the
     * cartId and cartItemId passed as arguments are associated with the user
     * 
     * @param authentication The authentication object that was passed into the method.
     * @param object The object being secured.
     * @return AuthorizationDecision is being returned.
     */
    public AuthorizationDecision preCheckUserCartIdAndCartItemId(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int cartId= Integer.parseInt(object.getVariables().get("cartId"));
        int cartItemId= Integer.parseInt(object.getVariables().get("itemId"));
        System.out.println(cartId+"+"+cartItemId);
        //is cartId is associated with email
        Cart cart = uService.getuser(email).getCart();
        if(cart==null  || cart.getId()!=cartId)return new AuthorizationDecision(false); 
        System.out.println("success-1"+cart);
        //is cartItemId is associated with cart
        CartItem cartItem = cart.getCartItems().stream().filter((e)->e.getId()==cartItemId).findAny().orElse(null);// .collect(Collectors.toList()). ;
        if(cartItem==null || cartItem.getId()!=cartItemId)return new AuthorizationDecision(false);
        System.out.println("success");
        //email in principal & id are same && email is of  Type User || email has authority of auths 
        return new AuthorizationDecision(/*cart.getId()==cartId &&*/ upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }

   /**
    * > If the user has the authority of `auths` or the user is of type `User` and the walletId in the
    * request is associated with the user, then the user is authorized
    * 
    * @param authentication The authentication object that was created by the previous authorization
    * provider.
    * @param object The object being authorized.
    * @return AuthorizationDecision is being returned.
    */
    public AuthorizationDecision preCheckUserWalletId(Supplier<Authentication> authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int walletId= Integer.parseInt(object.getVariables().get("walletId"));
        //is walletId is associated with email
        Wallet wallet = uService.getuser(email).getWallet();
        if(wallet==null  || wallet.getId()!=walletId)return new AuthorizationDecision(false); 
     
        //email in principal & id are same && email is of  Type User || email has authority of auths 
        return new AuthorizationDecision(/*cart.getId()==cartId &&*/ upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }
}
