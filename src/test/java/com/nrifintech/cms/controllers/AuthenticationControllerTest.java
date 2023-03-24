package com.nrifintech.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.config.MyUserDetailsService;
import com.nrifintech.cms.config.jwt.JwtRequest;
import com.nrifintech.cms.config.jwt.JwtResponse;
import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.dtos.UserDto;
import com.nrifintech.cms.entities.MyUserDetails;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AuthenticationService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest extends MockMvcSetup {
    private List<User> users = new ArrayList<>();
    private MockMvc mockMvc;
    @Mock
	private AuthenticationService authService;

	@Mock
	private MyUserDetailsService userDetailsServiceImple;

	@Mock
	private UserService userService;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationController authController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Authentication.prefix, this, authController);
        loadData();
    }

    private void loadData() {        
        users.add(new User(999,"avatar1.png","abcd@gamil.com","password1","9876543211",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(998,"avatar2.png","abc2@gamil.com","password2","9876543212",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(997,"avatar3.png","abc3@gamil.com","password3","9876543213",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        users.add(new User(996,"avatar4.png","abc4@gamil.com","password4","9876543214",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        
        for(User u:users){
            Mockito.when(userService.getuser(u.getEmail())).thenReturn(u);

        }

            Mockito.when(userDetailsServiceImple.loadUserByUsername(anyString())).thenReturn(new MyUserDetails(users.get(0)));
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn("password");
            Mockito.when(jwtUtils.generateToken(any())).thenReturn("token123");

            
    }

    @Test
    public void testChangePassword() throws JsonProcessingException, UnsupportedEncodingException, Exception {
            String r = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(prefix(Route.Authentication.changePassword + "?token=token123" ))                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        Mockito.verify(authService).changePassword(anyString(),anyString(),anyString());
        assertEquals("Password changed Successfully", res.getMessage().toString().trim());
    }

    @Test
    public void testForgotPassword() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        String r = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(prefix(Route.Authentication.forgotPassword ))                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        Mockito.verify(authService).forgetPassword(anyString());
        assertEquals("email sent.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testGenerateToken() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(prefix(Route.Authentication.generateToken ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        JwtResponse res = mapFromJson(r, JwtResponse.class);

        Mockito.verify(authService).authenticate(anyString(),anyString());
        assert res.getToken().equals("token123");
    }

    @Test
    public void testGetCurrentUser() throws UnsupportedEncodingException, Exception {
        /*String r = */mockMvc.perform(
            MockMvcRequestBuilders
                    .get(prefix(Route.Authentication.currentUser ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        );//.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        //Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        // Mockito.verify(userService).getuser(anyString());
        // assert res.getToken().equals("token123");
    }

    @Test
    public void testSetNewPassword() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(prefix(Route.Authentication.setNewPassword ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
    ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

    Mockito.verify(authService).setNewPassword(anyString());
    assertEquals("email sent.", res.getMessage().toString().trim().toLowerCase());
    }

    @Test
    public void testSetNewPasswordAndActivate() throws UnsupportedEncodingException, Exception {
       String r = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(prefix(Route.Authentication.activateNewPassword + "?token=token123" ))                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(new JwtRequest(users.get(0).getEmail(),users.get(0).getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        Mockito.verify(authService).setNewPasswordAndActivate(anyString(),anyString(),anyString());
        assertEquals("Password changed Successfully", res.getMessage().toString().trim());

    }
}
