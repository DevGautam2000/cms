package com.nrifintech.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest extends MockMvcSetup{
    private List<User> users = new ArrayList<>();
    private MockMvc mockMvc;
    @Mock
	private UserService userService;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	@Mock
	private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException, NoSuchAlgorithmException {
        this.mockMvc = MockMvcSetup.setUp(Route.User.prefix, this, userController);
        loadData();
    }

    private void loadData() throws IOException, NoSuchAlgorithmException {        
        users.add(new User(999,"avatar1.png","abcd@gamil.com","password1","9876543211",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(998,"avatar2.png","abc2@gamil.com","password2","9876543212",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(997,"avatar3.png","abc3@gamil.com","password3","9876543213",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        users.add(new User(996,"avatar4.png","abc4@gamil.com","password4","9876543214",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        
        for(User u:users){
            Mockito.when(userService.getuser(u.getId())).thenReturn(u);
        }

            Mockito.when(userService.saveUser(any())).thenReturn(users.get(0));
            Mockito.when(userService.addUser(any())).thenReturn(users.get(0));
            Mockito.when(passwordEncoder.encode(anyString())).thenReturn("password");
            Mockito.when(userService.getUsers()).thenReturn(users);

            
    }
    @Test
    public void testAddUser() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        when(userService.isNotNull(users.get(0))).thenReturn(false);
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(prefix(Route.User.addUser ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson( users.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        Mockito.verify(applicationEventPublisher).publishEvent(any());
        assertEquals("User added.", res.getMessage().toString().trim());


        when(userService.isNotNull(users.get(0))).thenReturn(true);
        r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(prefix(Route.User.addUser ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson( users.get(0)))
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn().getResponse().getContentAsString();

        res = mapFromJson(r, Response.JsonEntity.class);

        // Mockito.verify(applicationEventPublisher).publishEvent(any());
        assertEquals("User already exists.", res.getMessage().toString().trim());
    }

    @Test
    public void testGetEmailStatus() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        when(userService.isNotNull(users.get(0))).thenReturn(true);
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .get(prefix(Route.User.getEmailStatus + "/" + users.get(0).getId() ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        when(userService.isNotNull(users.get(0))).thenReturn(false);
        r = mockMvc.perform(
            MockMvcRequestBuilders
                    .get(prefix(Route.User.getEmailStatus + "/" + users.get(0).getId() ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().is( HttpStatus.INTERNAL_SERVER_ERROR.value())).andReturn().getResponse().getContentAsString();
        // EmailStatus res = mapFromJson(r, User.class).getEmailStatus();

        // Mockito.verify(applicationEventPublisher).publishEvent(any());
        // assert(res.getMessage().toString().trim().contains("User status updated to: "));
        
    }

    @Test
    public void testGetOrders() {

    }

    @Test
    public void testGetUsers() throws UnsupportedEncodingException, Exception {
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .get(prefix(Route.User.getUsers ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        // Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);
        List res = mapFromJson(r, List.class);

        // Mockito.verify(applicationEventPublisher).publishEvent(any());
        // assertArrayEquals(users.toArray(), res.toArray());
        when(userService.getUsers()).thenReturn(Arrays.asList());
        r = mockMvc.perform(
            MockMvcRequestBuilders
                    .get(prefix(Route.User.getUsers))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res1 = mapFromJson(r, Response.JsonEntity.class);

        // Mockito.verify(applicationEventPublisher).publishEvent(any());
        assertEquals("No users found.", res1.getMessage().toString().trim());
    }

    @Test
    public void testRemoveUser() {

    }

    @Test
    public void testSubsciptionToggler() throws UnsupportedEncodingException, Exception {
        when(userService.isNotNull(users.get(0))).thenReturn(true);
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .get(prefix(Route.User.subscriptionToggler + "/" + 
                    users.get(0).getId() + "/1" ))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        
        r = mockMvc.perform(
            MockMvcRequestBuilders
                .get(prefix(Route.User.subscriptionToggler + "/" + 
                users.get(0).getId() + "/3" ))   
                .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().is( HttpStatus.BAD_REQUEST.value())).andReturn().getResponse().getContentAsString();
        
    }

    @Test
    public void testUpdateUserStatus() throws JsonProcessingException, UnsupportedEncodingException, Exception {
        when(userService.isNotNull(users.get(0))).thenReturn(true);
        String r = mockMvc.perform(
            MockMvcRequestBuilders
                    .post(prefix(Route.User.updateStatus + "/" + users.get(0).getId() +
                    "/1"))                        
                    .contentType(MediaType.APPLICATION_JSON)
                    // .content(mapToJson( users.get(0)))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        Mockito.verify(applicationEventPublisher).publishEvent(any());
        assert(res.getMessage().toString().trim().contains("User status updated to: "));

    }
}
