package com.nrifintech.cms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Timestamp;
import java.util.Date;

import org.apache.tomcat.jni.Time;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.types.EmailStatus;
import com.nrifintech.cms.types.Role;
import com.nrifintech.cms.types.Status;
import com.nrifintech.cms.types.UserStatus;

@SpringBootTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setup(){
        String email="abcdzx@gmail.com";
        User userIn= new User(999, null, email, 
        "123",  "9876543210", Role.Admin, UserStatus.Active ,EmailStatus.subscribed, null
        , null, null,null);
        userRepo.save(userIn);
    }

    @AfterEach
    void destroy(){
        userRepo.deleteById(999);
    }

    @Test
    void testFindByEmail() {
        String user="abcd@gmail.com";
        User userActual = userRepo.findByEmail(user).orElse(null);
        assert(userActual.getEmail().equals(user));
    }

    @Test
    void testGetUserByOrderDate() {

    }

    @Test
    void testHasUserCartitem() {

    }
}
