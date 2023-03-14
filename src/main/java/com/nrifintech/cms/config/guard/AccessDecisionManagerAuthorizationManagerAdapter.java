package com.nrifintech.cms.config.guard;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;


@Component
public class AccessDecisionManagerAuthorizationManagerAdapter {//implements AuthorizationManager<RequestAuthorizationContext>{
    @Autowired
    private UserService uService;

    public AuthorizationDecision preCheckUserWithId(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("id"));
        String emailById = uService.getuser(id).getEmail();
        //email in principal & id are same && email is of Type User || email has authority of auths 
        return new AuthorizationDecision((email.equals(emailById) && upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User"))) || flag);
    }

    public AuthorizationDecision preCheckHasUserCartitem(Supplier authentication, RequestAuthorizationContext object,String... auths) {
        boolean flag= false;
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        //if user has authority of auths
        for(String i : auths)       
            if(upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals(i)))
                flag=true;

        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("itemId"));

        //email in principal & id are same && email is of Type User || email has authority of auths 
        return new AuthorizationDecision(uService.hasUserCartitem(email, id) && 
        upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }

    
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
    // @Override
    // public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext object) {

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
        
    //     UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
    //     System.out.println("hi**********************");
    //     String email=upt.getName();
    //     int id= Integer.parseInt(object.getVariables().get("id"));
    //     String emailById = uService.getuser(id).getEmail();
    //   //  System.out.println(user.getId()+" at "+user.getUsername());
    //   System.out.println(authentication.get());
    //   System.out.println(object.getVariables().get("id"));
    //     // User result = repo.findByEmail(name).orElse(null);
    //     return new AuthorizationDecision((email.equals(emailById)));
    //}
}
