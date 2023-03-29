package com.nrifintech.cms.services;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.ast.Var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.entities.TokenBlacklist;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.errorhandler.UserIsEnabledException;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private List<User> users = new ArrayList<>();

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;
    @MockBean
    private TokenBlacklistRepo tokenRepo;
    @InjectMocks
    private AuthenticationService authService;

    @BeforeEach
    void setup(){
        users.add(new User(999,"avatar1.png","abc1@gamil.com","password1","9876543211",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(998,"avatar2.png","abc2@gamil.com","password2","9876543212",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(997,"avatar3.png","abc3@gamil.com","password3","9876543213",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        users.add(new User(996,"avatar4.png","abc4@gamil.com","password4","9876543214",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
    
        when(userService.getuser("abc1@gamil.com")).thenReturn( users.get(0));
        when(userService.getuser("abc2@gamil.com")).thenReturn( users.get(1));
        when(userService.getuser("abc3@gamil.com")).thenReturn( users.get(2));
        when(userService.getuser("abc4@gamil.com")).thenReturn( users.get(3));

        // when(jwtUtils.validateToken(eq("dummy-token-for1"),any())).thenReturn( true );
        // when(jwtUtils.validateToken(eq("inv-dummy-token"),any())).thenReturn( false );
        // when(jwtUtils.validateToken(eq("dummy-token-for3"),any())).thenReturn( true );
        // when(jwtUtils.validateToken(eq("dummy-token-for3"),any())).thenReturn( false );
        when(jwtUtils.validateToken(eq("inv-dummy-token"),any())).thenReturn( false );
        when(jwtUtils.validateToken(eq("dummy-token-for1"),any())).thenReturn( true );
        when(jwtUtils.validateToken(eq("dummy-token-for3"),any())).thenReturn( true );
        
        when(tokenRepo.save(any())).thenReturn( any());
        when(userService.updatePassword(users.get(2),any())).thenReturn( users.get(2));

        // Mockito.when(userRepo.save(users.get(0))).thenReturn(users.get(0));
        // Mockito.when(userRepo.save(users.get(1))).thenReturn(users.get(1));
        // Mockito.when(userRepo.save(users.get(2))).thenReturn(users.get(2));
        // Mockito.when(userRepo.save(users.get(3))).thenReturn(users.get(3));

        // Mockito.when(userRepo.findAll()).thenReturn(users);


    }

    @AfterEach
    void destroy(){
        users.clear();
    }


    @Test
    void testAuthenticate() throws Exception { ///Testing Left
        String username="user@gamil.com";
        String password="password";
        UsernamePasswordAuthenticationToken u1=new UsernamePasswordAuthenticationToken(username, password);
        UsernamePasswordAuthenticationToken u2=new UsernamePasswordAuthenticationToken("abc", password);
        
        when(authenticationManager.authenticate(u2)).thenThrow(BadCredentialsException.class);
        when(authenticationManager.authenticate(u1)).thenReturn(any());
        authService.authenticate(username, password);
        assertThrows(UsernameNotFoundException.class,()-> authService.authenticate("abc", password));
        
        verify(authenticationManager,Mockito.times(2)).authenticate(any());  
    }

    @Test
    void testChangePassword() {
        String email1=users.get(0).getEmail();
        String email2=users.get(1).getEmail();
        String email3=users.get(2).getEmail();
        String email4=users.get(3).getEmail();
        
        UserIsDisabledException thrown1 = assertThrows(UserIsDisabledException.class,()->authService.changePassword(email3,"dummy-token-for1","newPassowrd"));
        UserIsDisabledException thrown2 = assertThrows(UserIsDisabledException.class,()->authService.changePassword(email4,"inv-dummy-token","newPassowrd"));
        authService.changePassword(email1,"dummy-token-for3","newPassowrd");
        UsernameNotFoundException thrown4 = assertThrows(UsernameNotFoundException.class,()->authService.changePassword(email2,"inv-dummy-token","newPassowrd"));
       
        assertEquals("User is disabled.", thrown1.getMessage());
        assertEquals("User is disabled.", thrown2.getMessage());
        Mockito.verify(userService).getuser(email1);
        Mockito.verify(tokenRepo).save(any());
        // Mockito.verify(userService).saveUser(users.get(0));
        Mockito.verify(userService).updatePassword(any(),any());
        assertEquals("Invalid Token", thrown4.getMessage());
    }

    @Test
    void testForgetPasswordCase1Active() {
        String email1=users.get(0).getEmail();
        String email2=users.get(1).getEmail();
        String email3=users.get(2).getEmail();
        String email4=users.get(3).getEmail();
        
        authService.forgetPassword(email1);
        authService.forgetPassword(email2);
        UserIsDisabledException thrown1 = assertThrows(UserIsDisabledException.class,()->authService.forgetPassword(email3));
        UserIsDisabledException thrown2 = assertThrows(UserIsDisabledException.class,()->authService.forgetPassword(email4));
      

        Mockito.verify(userService).getuser(email1);
        Mockito.verify(userService).getuser(email2);
        assertEquals("User is disabled.", thrown1.getMessage());
        assertEquals("User is disabled.", thrown2.getMessage());

    }


    @Test
    void testSetNewPassword() {
        String email1=users.get(0).getEmail();
        String email2=users.get(1).getEmail();
        String email3=users.get(2).getEmail();
        String email4=users.get(3).getEmail();
        
        authService.setNewPassword(email3);
        authService.setNewPassword(email4);
        UserIsEnabledException thrown1 = assertThrows(UserIsEnabledException.class,()->authService.setNewPassword(email1));
        UserIsEnabledException thrown2 = assertThrows(UserIsEnabledException.class,()->authService.setNewPassword(email2));
      

        Mockito.verify(userService).getuser(email3);
        Mockito.verify(userService).getuser(email4);
        assertEquals("User is Enabled.", thrown1.getMessage());
        assertEquals("User is Enabled.", thrown2.getMessage());
    }

    @Test
    void testSetNewPasswordAndActivate() {
        String email1=users.get(0).getEmail();
        String email2=users.get(1).getEmail();
        String email3=users.get(2).getEmail();
        String email4=users.get(3).getEmail();
        
        UserIsEnabledException thrown1 = assertThrows(UserIsEnabledException.class,()->authService.setNewPasswordAndActivate(email1,"dummy-token-for1","newPassowrd"));
        UserIsEnabledException thrown2 = assertThrows(UserIsEnabledException.class,()->authService.setNewPasswordAndActivate(email2,"inv-dummy-token","newPassowrd"));
        authService.setNewPasswordAndActivate(email3,"dummy-token-for3","newPassowrd");
        UsernameNotFoundException thrown4 = assertThrows(UsernameNotFoundException.class,()->authService.setNewPasswordAndActivate(email4,"inv-dummy-token","newPassowrd"));
       
        assertEquals("User is Enabled.", thrown1.getMessage());
        assertEquals("User is Enabled.", thrown2.getMessage());
        Mockito.verify(userService).getuser(email3);
        Mockito.verify(tokenRepo).save(any());
        Mockito.verify(userService).saveUser(users.get(2));
        Mockito.verify(userService).updatePassword(any(),any());
        assertEquals("Invalid Token", thrown4.getMessage());
    }
}