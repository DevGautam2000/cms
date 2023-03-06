package com.nrifintech.cms.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nrifintech.cms.config.MyUserDetailsService;
import com.nrifintech.cms.config.jwt.JwtRequest;
import com.nrifintech.cms.config.jwt.JwtResponse;
import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AuthenticationService;
import com.nrifintech.cms.services.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping(Route.Authentication.prefix)
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;
    @Autowired @Lazy
    private MyUserDetailsService userDetailsServiceImple;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
        try{
            authService.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        }catch(UsernameNotFoundException e){
            // e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            // throw new Exception("User not Found!!");
        }

       
        UserDetails userDetails=this.userDetailsServiceImple.loadUserByUsername(jwtRequest.getUsername());
         String token=this.jwtUtils.generateToken(userDetails);
        //  System.out.println("outside2 ***");
        
        return ResponseEntity.ok(new JwtResponse(token)); 
    }


    @GetMapping("current-user")
    public User getCurrentUser(Principal principal){
        return userService.getuser(principal.getName());
    }

    @GetMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody JwtRequest user){
        System.out.println(user);

        authService.forgetPassword(user.getUsername());

        //here
        return ResponseEntity.ok("Email sent"); 

    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestParam String token,
        @RequestBody JwtRequest userInfo){
        authService.changePassword(userInfo.getUsername(), token, userInfo.getPassword());                    
        return ResponseEntity.ok("Password changed Successfully"); 
    }

    
    @GetMapping("/hi")
    public String hi(){
        return "hi";
    }
    @GetMapping("/test")
    public String test(){
        return "only admin";
    }
}