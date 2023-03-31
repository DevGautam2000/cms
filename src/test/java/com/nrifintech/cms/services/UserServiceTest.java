package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private List<User> users = new ArrayList();

    @Mock(lenient = true)
    private UserRepo userRepo;

    @Mock(lenient = true)
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup(){
        users.add(new User(999,"avatar1.png","abc1@gamil.com","password1","9876543211",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(998,"avatar2.png","abc2@gamil.com","password2","9876543212",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(997,"avatar3.png","abc3@gamil.com","password3","9876543213",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
        users.add(new User(996,"avatar4.png","abc4@gamil.com","password4","9876543214",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null));
    
        when(userRepo.findByEmail("abc1@gamil.com")).thenReturn(Optional.of(users.get(0)));
        when(userRepo.findByEmail("abc2@gamil.com")).thenReturn(Optional.of(users.get(1)));
        when(userRepo.findByEmail("abc3@gamil.com")).thenReturn(Optional.of(users.get(2)));
        when(userRepo.findByEmail("abc4@gamil.com")).thenReturn(Optional.of(users.get(3)));

        
        when(userRepo.findById(999)).thenReturn(Optional.of(users.get(0)));
        when(userRepo.findById(998)).thenReturn(Optional.of(users.get(1)));
        when(userRepo.findById(997)).thenReturn(Optional.of(users.get(2)));
        when(userRepo.findById(996)).thenReturn(Optional.of(users.get(3)));

        Mockito.when(userRepo.save(users.get(0))).thenReturn(users.get(0));
        Mockito.when(userRepo.save(users.get(1))).thenReturn(users.get(1));
        Mockito.when(userRepo.save(users.get(2))).thenReturn(users.get(2));
        Mockito.when(userRepo.save(users.get(3))).thenReturn(users.get(3));

        Mockito.when(userRepo.findAll()).thenReturn(users);

        for(User u : users){
            when(userRepo.getUserByOrderId(u.getId())).thenReturn(u.getEmail());
    
        }
    
    }
    @AfterEach
    void destroy(){
        users.clear();
    }

    @DisplayName("add user test")
    @Test
    void testAddUserCase1() throws IOException, NoSuchAlgorithmException {
        User user1 = new User(1,"avatar.png","abc@gamil.com","password","9876543210",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null);
        when(userRepo.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(userRepo.save(user1)).thenReturn(user1);

        User created = userService.addUser(user1);

        System.out.println(created);
        assertEquals(user1, created);
    }
    @Test
    void testAddUserCase2() throws IOException, NoSuchAlgorithmException {
        User user1 = new User(1,"avatar.png","abc@gamil.com","password","9876543210",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null);
        when(userRepo.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(userRepo.save(user1)).thenReturn(user1);

        User created = userService.addUser(user1);

        System.out.println(created);
        assertEquals(user1, created);
    }
    @Test
    void testAddUserCase3() throws IOException, NoSuchAlgorithmException {
        User user1 = new User(1,"avatar.png","abc@gamil.com","password","9876543210",Role.User,UserStatus.Active,EmailStatus.subscribed,null,null,null,null);
        when(userRepo.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(userRepo.save(user1)).thenReturn(user1);

        User created = userService.addUser(user1);

        System.out.println(created);
        assertEquals(user1, created);
    }

    @Test
    void testCheckPassword() {

    }

    @Test
    void testGetAllConsumers() {
        List<String> emails=Arrays.asList("test1","test2");
        when(userRepo.getAllConsumers()).thenReturn(Optional.ofNullable(emails));
        assertEquals(emails.size(), userService.getAllConsumers().size());

        
        when(userRepo.getAllConsumers()).thenReturn(Optional.ofNullable(null));
        assertEquals(0, userService.getAllConsumers().size());
    }

    //TODO: wrong func. name 
    @Test
    void testGetOrdersByDate() {
        List<String> emails=Arrays.asList("test1","test2");
        when(userRepo.getUserByOrderDate(any())).thenReturn(emails);
        assertArrayEquals(emails.toArray(), userService.getOrdersByDate(new Date(2023,3,28)).toArray());
    
    }

    @Test
    void testGetOrdersByDateAndOrderType() {
        List<String> emails=Arrays.asList("test1","test2");
        when(userRepo.getUserByOrderDateAndType(any(),anyInt())).thenReturn(emails);
        assertArrayEquals(emails.toArray(), userService.getOrdersByDateAndOrderType(new Date(2023,3,28), 1).toArray());
    }

    @Test
    void testGetUserByOrderId() {
        assertEquals(users.get(0).getEmail(), userService.getUserByOrderId(users.get(0).getId()) );

    }

    @Test
    void testGetUsers() {
        List<User> actualUser = userService.getUsers();

        assertArrayEquals(users.toArray(),actualUser.toArray());
    }

    @Test
    void testGetuser() {
        assertEquals(users.get(0), userService.getuser(users.get(0).getId()));
        assertEquals(users.get(1), userService.getuser(users.get(1).getId()));
        assertEquals(users.get(2), userService.getuser(users.get(2).getId()));
        assertEquals(users.get(3), userService.getuser(users.get(3).getId()));
    }

    @Test
    void testGetuser2() {
        assertEquals(users.get(0), userService.getuser(users.get(0).getEmail()));
        assertEquals(users.get(1), userService.getuser(users.get(1).getEmail()));
        assertEquals(users.get(2), userService.getuser(users.get(2).getEmail()));
        assertEquals(users.get(3), userService.getuser(users.get(3).getEmail()));
    }

    @Test
    void testHasUserCartitem() {
        when(userRepo.hasUserCartitem(anyString(),anyInt())).thenReturn(1);
        assertEquals(true, userService.hasUserCartitem("testuser", 12));

        when(userRepo.hasUserCartitem(anyString(),anyInt())).thenReturn(0);
        assertEquals(false, userService.hasUserCartitem("testuser", 12));
    
    }


    @Test
    void testSaveUser() {
        assertEquals(users.get(0),userService.saveUser(users.get(0)));
        assertEquals(users.get(1),userService.saveUser(users.get(1)));
        assertEquals(users.get(2),userService.saveUser(users.get(2)));
        assertEquals(users.get(3),userService.saveUser(users.get(3)));
    }

    @Test
    void testUpdatePassword() {
        assertEquals("newPassword" , userService.updatePassword(users.get(0),"newPassword").getPassword());
        assertEquals("newPassword" , userService.updatePassword(users.get(1),"newPassword").getPassword());
        assertEquals("newPassword" , userService.updatePassword(users.get(2),"newPassword").getPassword());
        assertEquals("newPassword" , userService.updatePassword(users.get(3),"newPassword").getPassword());
    }

    @Test
    void testUpdateUser() {
        assertEquals(users.get(0),userService.updateUser(users.get(0).getId(),users.get(0)));
        assertEquals(users.get(1),userService.updateUser(users.get(1).getId(),users.get(1)));
        assertEquals(users.get(2),userService.updateUser(users.get(2).getId(),users.get(2)));
        assertEquals(users.get(3),userService.updateUser(users.get(3).getId(),users.get(3)));
    }
}
