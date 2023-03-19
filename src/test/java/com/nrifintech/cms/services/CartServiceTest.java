package com.nrifintech.cms.services;

import com.nrifintech.cms.repositories.CartRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepo cartRepo;

    @InjectMocks
    private CartService cartService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCart(){

    }
}
