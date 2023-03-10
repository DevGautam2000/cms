package com.nrifintech.cms.config.guard;

import java.util.function.Supplier;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;


@Component
public class AccessDecisionManagerAuthorizationManagerAdapter implements AuthorizationManager<RequestAuthorizationContext>{
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

    @Override
    public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext object) {
        // TODO Auto-generated method stub
        UsernamePasswordAuthenticationToken upt= (UsernamePasswordAuthenticationToken) authentication.get();
        System.out.println("hi**********************");
        String email=upt.getName();
        int id= Integer.parseInt(object.getVariables().get("id"));
        String emailById = uService.getuser(id).getEmail();
      //  System.out.println(user.getId()+" at "+user.getUsername());
      System.out.println(authentication.get());
      System.out.println(object.getVariables().get("id"));
        // User result = repo.findByEmail(name).orElse(null);
        return new AuthorizationDecision((email.equals(emailById)));
        // throw new UnsupportedOperationException("Unimplemented method 'check'");
    }
}
