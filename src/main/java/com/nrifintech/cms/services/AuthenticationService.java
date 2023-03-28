package com.nrifintech.cms.services;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.entities.TokenBlacklist;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.errorhandler.UserIsEnabledException;
import com.nrifintech.cms.events.ForgotPasswordEvent;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.types.UserStatus;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.JwtException;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TokenBlacklistRepo tokenRepo;
    
    
    //TODO: check password
    //return user
    public void authenticate(String username,String password) throws Exception{
        try{

            
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        // catch(DisabledException e){
        //     throw new Exception("USER DISABLED "+e.getMessage());
        // }
        catch(BadCredentialsException e){
            // throw new Exception("Invalid Credentials "+e.getMessage());
            throw new UsernameNotFoundException("Invalid Credentials ");
        }
        // catch(Exception e){
        //     e.printStackTrace();
        // }
    }


    public String forgetPassword(String email){
        User user=userService.getuser(email);

        if(!(user.getStatus().equals(UserStatus.Active)))
            throw new UserIsDisabledException();
            
        return(resetPasswordEmail(user));
    }

    private String resetPasswordEmail(User user){
        System.out.println(user);
        MyUserDetails myUser = new MyUserDetails(user);
        String token = jwtUtils.generateResetToken(myUser);
        String url="/auth/change-password?token="+token;
        return(url);
    }

    public void changePassword(String email,String token,String newPassword) {

        User user = userService.getuser(email);

        if(!(user.getStatus().equals(UserStatus.Active)))
            throw new UserIsDisabledException();

        if(!jwtUtils.validateToken(token,new MyUserDetails(user))){
            throw new UsernameNotFoundException("Invalid Token");
        }

        if(tokenRepo.findById(token).isPresent())throw new JwtException("Invalid Token");
        tokenRepo.save(new TokenBlacklist(token));

        System.out.println("Password updated");
        userService.updatePassword(user,newPassword);

    }

    @Transactional
    public String setNewPassword(String email){
        User user=userService.getuser(email);
        
        if(!(user.getStatus().equals(UserStatus.InActive)))
            throw new UserIsEnabledException();
        
        return(setNewPasswordEmail(user));
    }

    @Transactional
    private String setNewPasswordEmail(User user){
        System.out.println(user);
        String token = jwtUtils.generateNewPasswordToken(new MyUserDetails(user));
        //TODO : ** url needs to be changed **
        String url="/auth/activate-new-password?token="+token;
        return(url);
    }
    
    public void setNewPasswordAndActivate(String email,String token,String newPassword) {

        final int statusIdEnabled=0;
        User user = userService.getuser(email);

        if(!(user.getStatus().equals(UserStatus.InActive)))
            throw new UserIsEnabledException();

        if(!jwtUtils.validateToken(token,new MyUserDetails(user))){
            throw new UsernameNotFoundException("Invalid Token");
        }

        if(tokenRepo.findById(token).isPresent())throw new JwtException("Invalid Token");
        tokenRepo.save(new TokenBlacklist(token));

		user.setStatus(UserStatus.values()[statusIdEnabled]);
		userService.saveUser(user);
        System.out.println("User Activated & Password updated");
        
        userService.updatePassword(user,newPassword);

    }

}
