package com.nrifintech.cms.services;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.entities.ResetPasswordUUID;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;

import eu.bitwalker.useragentutils.UserAgent;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SMTPservices smtPservices;
    
    
    //TODO: check password
    //return user
    public void authenticate(String username,String password) throws Exception{
        try{

            
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch(DisabledException e){
            throw new Exception("USER DISABLED "+e.getMessage());
        }
        catch(BadCredentialsException e){
            // throw new Exception("Invalid Credentials "+e.getMessage());
            throw new UsernameNotFoundException("Invalid Credentials "+e.getMessage());
        }
        // catch(Exception e){
        //     e.printStackTrace();
        // }
    }


    public void forgetPassword(String email){
        User user=userService.getuser(email);
        
        resetPasswordEmail(user);
    }

    private void resetPasswordEmail(User user){
        System.out.println(user);
        String token = jwtUtils.generateResetToken(new MyUserDetails(user));
        //TODO : ** url needs to be changed **
        String url="/auth/change-password?token="+token;
        System.out.println(url);
        //code here
        url = Route.root+url;
        EmailModel em = new EmailModel(user.getEmail(), "CMS-Pass Reset", null);
        this.smtPservices.forgotPasswordMail(em,url);
    }

    public void changePassword(String email,String token,String newPassword){

        User user = userService.getuser(email);
        if(!jwtUtils.validateToken(token,new MyUserDetails(user))){
            throw new UsernameNotFoundException("token: "+token+" is not valid");
        }

        System.out.println("Password updated");
        userService.updatePassword(user,newPassword);


    }
}