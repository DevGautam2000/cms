package com.nrifintech.cms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nrifintech.cms.MockMvcSetup;
import com.nrifintech.cms.dtos.WalletEmailResponse;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.events.WalletRechargeEvent;
import com.nrifintech.cms.payments.dto.StripeToken;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.services.WalletService;
import com.nrifintech.cms.types.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;

import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest extends MockMvcSetup {

    @Mock
    private WalletService walletService;

    @Mock
    private UserService userService;

    @InjectMocks
    private WalletController walletController;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcSetup.setUp(Route.Wallet.prefix, this, walletController);
        loadData();
    }

    private User user;
    private Wallet wallet;

    private void loadData() {

        wallet = Wallet.builder().id(12).balance(2000d).build();

        user = User.builder()
                .id(10).email("user@test").password("123").wallet(wallet).build();

    }

    @Test
    public void testGetWalletSuccess() throws Exception {

        int walletId = 12;
        Principal principal = user::getUsername;

        Mockito.when(userService.getuser(principal.getName())).thenReturn(user);
        Mockito.when(walletService.getWallet(walletId)).thenReturn(wallet);


        String r = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(prefix(Route.Wallet.getWallet + "/{walletId}"), walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


        Wallet res = mapFromJson(r, Wallet.class);
        assertThat(res.getId(), is(user.getWallet().getId()));
        assertThat(res.getBalance(), is(user.getWallet().getBalance()));
    }

    @Test
    public void testGetWalletFailure() throws Exception {


        int walletId = 12;

        Wallet newWallet = new Wallet(14, 3000d, null);
        User newUser = User.builder().email("new@test").password("213").wallet(newWallet).build();

        Principal principal = newUser::getUsername;

        Mockito.when(userService.getuser(principal.getName())).thenReturn(newUser);
        Mockito.when(walletService.getWallet(newUser.getWallet().getId())).thenReturn(newWallet);
        Mockito.when(walletService.getWallet(walletId)).thenReturn(wallet);

        String r = mockMvc.perform(
                MockMvcRequestBuilders
                        .get(prefix(Route.Wallet.getWallet + "/{walletId}"), walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
        ).andExpect(status().isNotAcceptable()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.NOT_ACCEPTABLE.value(), is(res.getStatus()));
        assertThat("wallet does not exist for user.", is(res.getMessage().toString().trim().toLowerCase()));
    }

    @Test
    public void testAddMoneySuccess() throws Exception {

        Integer amount = 1000;
        Principal principal = user::getUsername;
        StripeToken stripeToken = new StripeToken();
        String chargeId = "chargeId";

        Mockito.when(userService.getuser(principal.getName())).thenReturn(user);
        Mockito.when(walletService.getWallet(user.getWallet().getId())).thenReturn(wallet);
        Mockito.when(walletService.addMoneyToWallet(user.getEmail(), wallet, amount, stripeToken.getToken()))
                .thenReturn(chargeId);

        Mockito.lenient().doAnswer((Answer<Void>) invocation -> null)
                .when(mock(ApplicationEventPublisher.class))
                .publishEvent(
                        new WalletRechargeEvent(
                                new WalletEmailResponse(principal.getName(),
                                        wallet.getBalance(), amount, chargeId)
                        ));

        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.Wallet.addMoney + "/{amount}"), amount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
                        .content(mapToJson(stripeToken))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.OK.value(), is(res.getStatus()));
        assertThat("money added to wallet.", is(res.getMessage().toString().trim().toLowerCase()));

    }

    @Test
    public void testAddMoneyFailure() throws Exception {

        Integer amount = 1000;
        Principal principal = user::getUsername;
        StripeToken stripeToken = new StripeToken();
        String chargeId = null;

        Mockito.when(userService.getuser(principal.getName())).thenReturn(user);
        Mockito.when(walletService.getWallet(user.getWallet().getId())).thenReturn(wallet);
        Mockito.when(walletService.addMoneyToWallet(user.getEmail(), wallet, amount, stripeToken.getToken()))
                .thenReturn(chargeId);


        String r = mockMvc.perform(
                MockMvcRequestBuilders.post(prefix(Route.Wallet.addMoney + "/{amount}"), amount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal)
                        .content(mapToJson(stripeToken))
        ).andExpect(status().isInternalServerError()).andReturn().getResponse().getContentAsString();

        Response.JsonEntity res = mapFromJson(r, Response.JsonEntity.class);

        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.value(), is(res.getStatus()));
        assertThat("error adding money.", is(res.getMessage().toString().trim().toLowerCase()));

    }
}
