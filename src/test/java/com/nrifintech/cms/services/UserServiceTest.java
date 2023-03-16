package com.nrifintech.cms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.aspectj.lang.annotation.Before;
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
    @Mock(lenient = true)
    private UserRepo userRepo;

    @Mock(lenient = true)
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    @DisplayName("add user test")
    @Test
    void testAddUser() {
        User user1 = new User(1,"avatar.png","abc@gamil.com","password","9876543210",Role.User,UserStatus.Active,EmailStatus.subscribed,null,
        null,null,null);
        when(userRepo.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(userRepo.save(user1)).thenReturn(user1);

        User created = userService.addUser(user1);

        System.out.println(created);
        assertEquals(user1, created);
        // verify(userRepo).save(user1);
    }

    @Test
    void testCheckPassword() {

    }

    @Test
    void testGetAllConsumers() {

    }

    @Test
    void testGetOrdersByDate() {

    }

    @Test
    void testGetOrdersByDateAndOrderType() {

    }

    @Test
    void testGetUserByOrderId() {

    }

    @Test
    void testGetUsers() {

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

    }

    @Test
    void testUpdatePassword() {

    }

    @Test
    void testUpdateUser() {

    }
}
