package com.nrifintech.cms.payments.service;

import com.nrifintech.cms.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


public class StripeServiceTest{

    @Mock
    private StripeService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCharge() {

        User user = User.builder().email("test@example.com").build();
        String token = "stripe_token";
        int amount = 1000;
        String chargeId = "chargeId";

        Mockito.when(service.createCharge(any(String.class), any(String.class),any(Integer.class))).thenReturn(chargeId);

        String assertChargeId = service.createCharge(user.getEmail(), token,amount);
        assertEquals(assertChargeId, chargeId);
    }
}
