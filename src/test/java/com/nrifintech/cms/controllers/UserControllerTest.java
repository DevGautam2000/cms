package com.nrifintech.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.tomcat.util.http.parser.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Response;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
	private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock(lenient = true)
    private UserService userService;

    @Mock(lenient = true)
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock(lenient = true)
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        User user = new User(999,"avatar.png","abc@gamil.com","password","9876543210",Role.User,UserStatus.Active,EmailStatus.subscribed,null,
        null,null,null);

        // when(passwordEncoder.encode(any())).thenReturn("password");
        when(userService.addUser(user)).thenReturn(user);

    }

    @Test
    public void testAddUser() {
        MockitoAnnotations.initMocks(this);

        User user = new User(999,"avatar.png","abc@gamil.com","password","9876543210",Role.User,UserStatus.Active,EmailStatus.subscribed,null,
        null,null,null);

        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userService.addUser(user)).thenReturn(user);
        // when(applicationEventPublisher.publishEvent(any())).thenReturn(any());

        Response response = userController.addUser(user);

        verify(passwordEncoder, times(1)).encode(any());
        verify(userService, times(1)).addUser(user);
        // verify(applicationEventPublisher, times(0)).publishEvent(any());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Response.JsonEntity(message=User added., status=200)", response.getBody().toString());
    }

    @Test
    void testGetEmailStatus() throws Exception {



        this.mockMvc.perform(get(Route.User.prefix + Route.User.getEmailStatus))
        .andDo(print()).andExpect(status().isOk())
		.andExpect(content().string(containsString("Hello, Mock")));

        // mockMvc.perform(MockMvcRequestBuilders
        // .get(Route.User.getEmailStatus)
        // .accept(MediaType.APPLICATION_JSON)
        // .andDo(print())
        // .andExpect(status().isOk())
        // .andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
        // .andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty());
    }

    @Test
    void testGetOrders() {

    }

    @Test
    void testGetUsers() {

    }

    @Test
    void testRemoveUser() {

    }

    @Test
    void testSubsciptionToggler() {

    }

    @Test
    void testUpdateUserStatus() {

    }
}
