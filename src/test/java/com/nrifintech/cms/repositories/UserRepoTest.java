package com.nrifintech.cms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.Status;
import com.nrifintech.cms.types.UserStatus;

@SpringBootTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    // @Test
    // void testFindByEmail() {
    //     //given
    //     String email="abcd@gmail.com";
    //     User userIn= new User(1, null, email, 
    //     "123",  "9876543210", Role.Admin, UserStatus.Active ,
    //      null, null, null, null);

    //     userRepo.save(userIn);

    //     //when
    //     User userActual = userRepo.findByEmail(email).orElse(null);

    //     //then
    //     // assert(user.);
    // }

    @Test
    void testGetUserByOrderDate() {

    }

    @Test
    void testHasUserCartitem() {

    }
}
