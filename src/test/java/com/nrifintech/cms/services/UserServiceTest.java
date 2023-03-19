package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.UserStatus;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private List<User> users = new ArrayList<>();
    
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

        Mockito.when(userRepo.save(users.get(0))).thenReturn(users.get(0));
        Mockito.when(userRepo.save(users.get(1))).thenReturn(users.get(1));
        Mockito.when(userRepo.save(users.get(2))).thenReturn(users.get(2));
        Mockito.when(userRepo.save(users.get(3))).thenReturn(users.get(3));

        Mockito.when(userRepo.findAll()).thenReturn(users);


    }

    @AfterEach
    void destroy(){
        users.clear();
    }

    @Test
    void testAddUserCase1() {
        User created1 = userService.addUser(users.get(0));

        assertEquals(users.get(0), created1);
        // verify(userRepo).save(user1);
    }
    

    @Test
    void testAddUserCase2() {
        User created1 = userService.addUser(users.get(1));

        

        assertEquals(users.get(1), created1);
        // verify(userRepo).save(user1);
    }

    @Test
    void testCheckPassword() {

    }

    @Test
    void testGetAllConsumers() {//

    }

    @Test
    void testGetOrdersByDate() {//

    }

    @Test
    void testGetOrdersByDateAndOrderType() {//

    }

    @Test
    void testGetUserByOrderId() {//

    }

    @Test
    void testGetUsers() {
        List<User> actualUser = userService.getUsers();

        assertArrayEquals(users.toArray(),actualUser.toArray());
    }

    @Test
    void testGetuser() {

    }

    @Test
    void testGetuser2() {

    }

    @Test
    void testHasUserCartitem() {

    }

    @Test
    void testRemoveUser() {

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

    }

    @Test
    void testUpdateUser() {
        assertEquals(users.get(0),userService.updateUser(users.get(0).getId(),users.get(0)));
        assertEquals(users.get(1),userService.updateUser(users.get(1).getId(),users.get(1)));
        assertEquals(users.get(2),userService.updateUser(users.get(2).getId(),users.get(2)));
        assertEquals(users.get(3),userService.updateUser(users.get(3).getId(),users.get(3)));

    }
}
