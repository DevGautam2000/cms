package com.nrifintech.cms.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.entities.TokenBlacklist;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.errorhandler.UserIsEnabledException;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;
import com.nrifintech.cms.types.UserStatus;

import io.jsonwebtoken.JwtException;

/**
 * It has methods to authenticate, forget password, change password, set new password and activate user
 */
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
   /**
    * It takes a username and password, and if the username and password are valid, it returns a token
    * 
    * @param username The username of the user
    * @param password The password of the user
    */
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


  /**
   * It takes an email address, finds the user with that email address, and if the user is active, it
   * sends them an email with a link to reset their password
   * 
   * @param email The email address of the user who is trying to reset their password.
   * @return A String
   */
    public String forgetPassword(String email){
        User user=userService.getuser(email);

        if(!(user.getStatus().equals(UserStatus.Active)))
            throw new UserIsDisabledException();
            
        return(resetPasswordEmail(user));
    }

   /**
    * It takes a user object, creates a MyUserDetails object from it, generates a token from the
    * MyUserDetails object, and returns a url with the token appended to it
    * 
    * @param user The user object that is being passed in from the controller.
    * @return The URL to the reset password page.
    */
    private String resetPasswordEmail(User user){
        System.out.println(user);
        MyUserDetails myUser = new MyUserDetails(user);
        String token = jwtUtils.generateResetToken(myUser);
        String url="/auth/change-password?token="+token;
        return(url);
    }

  /**
   * It takes an email, a token and a new password, checks if the user is active, if the token is
   * valid, if the token is blacklisted, and if all of the above are true, it updates the password
   * 
   * @param email the email of the user
   * @param token The token that was sent to the user's email address.
   * @param newPassword the new password
   */
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

  /**
   * It checks if the user is inactive, if so, it sends an email to the user with a link to reset the
   * password
   * 
   * @param email The email address of the user.
   * @return A String
   */
    @Transactional
    public String setNewPassword(String email){
        User user=userService.getuser(email);
        
        if(!(user.getStatus().equals(UserStatus.InActive)))
            throw new UserIsEnabledException();
        
        return(setNewPasswordEmail(user));
    }

    /**
     * It generates a token and returns a url with the token as a query parameter
     * 
     * @param user the user object that is being passed from the controller
     * @return The url is being returned.
     */
    @Transactional
    private String setNewPasswordEmail(User user){
        System.out.println(user);
        String token = jwtUtils.generateNewPasswordToken(new MyUserDetails(user));
        //TODO : ** url needs to be changed **
        String url="/auth/activate-new-password?token="+token;
        return(url);
    }
    
   /**
    * It takes an email, a token and a new password, and if the token is valid, it activates the user
    * and updates the password
    * 
    * @param email the email of the user
    * @param token The token that was sent to the user's email
    * @param newPassword The new password that the user wants to set
    */
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
