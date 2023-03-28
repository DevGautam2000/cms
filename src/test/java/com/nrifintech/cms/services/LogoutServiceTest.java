package com.nrifintech.cms.services;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.nrifintech.cms.repositories.TokenBlacklistRepo;

public class LogoutServiceTest {
    @Mock
    private TokenBlacklistRepo tokenRepository;
    @InjectMocks
    private LogoutService logoutService;
    @Test
    public void testLogout() {
        
    }
}
