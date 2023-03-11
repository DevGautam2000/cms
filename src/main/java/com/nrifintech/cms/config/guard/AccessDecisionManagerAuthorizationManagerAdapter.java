package com.nrifintech.cms.config.guard;

import java.util.List;
import java.util.function.Supplier;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.entities.Cart;
import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
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
        System.out.println("*******************");

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
        System.out.println("*******************");
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
        System.out.println("*******************");
        List<Order> rec = uService.getuser(email).getRecords();
        if(rec.isEmpty())return new AuthorizationDecision(false);
        
        boolean hasOrder=false;
        for(Order o:rec){
            if(o.getId()==id)hasOrder=true;
        }
        //email in principal & id are same && email is of  Type User || email has authority of auths 
        return new AuthorizationDecision(hasOrder && upt.getAuthorities().stream().anyMatch((e)->e.getAuthority().equals("User")) || flag);
    }
    // @Override
    // public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext object) {
        
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
