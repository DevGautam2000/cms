package com.nrifintech.cms.controller;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.nrifintech.cms.repositories.UserRepo;

@SpringBootTest
public class TestUserController {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepo userRepo;

    

}
