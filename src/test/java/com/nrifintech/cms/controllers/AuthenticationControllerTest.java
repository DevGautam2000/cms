package com.nrifintech.cms.controllers;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.config.MyUserDetailsService;
import com.nrifintech.cms.config.jwt.JwtRequest;
import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.dtos.UserDto;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.AuthenticationService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest extends MockMvcSetup{
    private List<User> users = new ArrayList<>();
    private MockMvc mockMvc;
    @Mock
	private AuthenticationService authService;

	@Mock
	// @Lazy
	private MyUserDetailsService userDetailsServiceImple;

	@Mock
	private UserService userService;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationController authController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Authentication.prefix, this, authController);
        loadData();
    }

    private void loadData() {
        users.add(new User(999,"avatar1.png","abc1@gamil.com","password1","9876543211",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(998,"avatar2.png","abc2@gamil.com","password2","9876543212",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(997,"avatar3.png","abc3@gamil.com","password3","9876543213",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        users.add(new User(996,"avatar4.png","abc4@gamil.com","password4","9876543214",Role.User,UserStatus.InActive,EmailStatus.unsubscribed,null,null,null,null));
        
        for(User u:users){
            Mockito.when(userService.getuser(u.getEmail())).thenReturn(u);

        }

            // Mockito.when(userService.setNewPasswordAndActivate(users.get(0).getEmail(),"token1",Mockito.any())).thenReturn(u);

    }
    
    @Test
    void testChangePassword() {

    }

    @Test
    void testForgotPassword() {

    }

    @Test
    void testGenerateToken() {

    }

    @Test
    void testGetCurrentUser() throws Exception {

        String r = mockMvc.perform(
                MockMvcRequestBuilders.get(
                        prefix( Route.Authentication.currentUser)
                ).header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkQGdtYWlsLmNvbSIsImV4cCI6MTY4MDEyMzkwNiwiaWF0IjoxNjc5MjU5OTA2fQ.fRwyOi3uZU81zaMR9duDiMXzvSCH1Zowk115GJKmE28") 
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        UserDto resItems = mapFromJson(r, UserDto.class);

        assertEquals(null, resItems);
        // assertThat(items.size(), is(resItems.length));
        // assertThat(items.get(0).getId(), is(resItems[0].getId()));
    }

    @Test
    void testSetNewPassword() {

    }

    @Test
    void testSetNewPasswordAndActivate() {
        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(
                        prefix( Route.Authentication.activateNewPassword)
                        //.content(objectWriter.writeValueAsString(new JwtRequest("abc1@gamil.com","password1")))
                ).contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        
        
        Mockito.verify(userService).setNewPasswordAndActivate(users.get(0).getEmail(),"token","passwordEncoder.encode(userInfo.getPassword()");

    }
}
