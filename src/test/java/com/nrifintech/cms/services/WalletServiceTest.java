package com.nrifintech.cms.services;

import com.nrifintech.cms.entities.Transaction;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.entities.Wallet;
import com.nrifintech.cms.errorhandler.NotFoundException;
import com.nrifintech.cms.payments.service.StripeService;
import com.nrifintech.cms.repositories.WalletRepo;
import com.stripe.exception.StripeException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private StripeService stripeService;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckMinimumAmount() {
        // Mock the Wallet object
        WalletService mock = mock(WalletService.class);
        Wallet wallet = Wallet.builder().id(12).balance(2000d).build();

        Mockito.lenient().when(mock.checkMinimumAmount(any(Wallet.class))).thenReturn(true);

        boolean result = walletService.checkMinimumAmount(wallet);

        Assertions.assertTrue(result);
    }

    @Test
    public void testSave() {
        // Mock the Wallet object
        Wallet wallet = mock(Wallet.class);

        Mockito.when(walletRepo.save(wallet)).thenReturn(wallet);

        Wallet result = walletService.save(wallet);

        assertEquals(wallet, result);
    }

    @Test
    public void testUpdateWallet() {

        int amount = 1000;

        Wallet wallet = mock(Wallet.class);
        Transaction transaction = mock(Transaction.class);
        List<Object> objects = new ArrayList<>();
        objects.add(wallet);
        objects.add(transaction);

        Mockito.when(transactionService.add(any(Transaction.class))).thenReturn(transaction);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);

        List<Object> expectedObjects = walletService.updateWallet(wallet, amount);

        assertEquals(objects, expectedObjects);
    }

    @Test
    public void testAddMoneyToWallet() throws StripeException {

        int amount = 1000;

        Wallet wallet = mock(Wallet.class);
        Transaction transaction = mock(Transaction.class);
        String chargeId = "chargeId";
        String token = "token_stripe";

        User user = mock(User.class);

        Mockito.when(transactionService.add(any(Transaction.class))).thenReturn(transaction);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);
        Mockito.when(stripeService.createCharge(user.getEmail(), token, amount)).thenReturn(chargeId);

        String expectedChargeId = walletService.addMoneyToWallet(user.getEmail(),
                wallet,
                amount,
                token
        );

        assertEquals(chargeId, expectedChargeId);
    }

    @Test
    public void testAddMoneyToWalletRemarks() throws StripeException {

        int amount = 1000;

        Wallet wallet = mock(Wallet.class);
        Transaction transaction = mock(Transaction.class);
        String chargeId = "chargeId";
        String token = "token_stripe";
        String remarks = "remarks";

        User user = mock(User.class);

        Mockito.when(transactionService.add(any(Transaction.class))).thenReturn(transaction);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);
        Mockito.when(stripeService.createCharge(user.getEmail(), token, amount)).thenReturn(chargeId);

        String expectedChargeId = walletService.addMoneyToWallet(user.getEmail(),
                wallet,
                amount,
                token,
                remarks
        );

        assertEquals(chargeId, expectedChargeId);
    }

    @Test
    public void getWalletSuccess(){

        int walletId = 10;
        Wallet wallet = mock(Wallet.class);

        Mockito.when(walletRepo.findById(walletId)).thenReturn(Optional.ofNullable(wallet));

        Wallet expectedWallet = walletService.getWallet(walletId);

        assertEquals(wallet,expectedWallet);

    }


    @Test
    public void getWalletFailure(){

        int walletId = 10;
        Wallet wallet = mock(Wallet.class);

        NotFoundException notFoundException = new NotFoundException("Wallet");
        Mockito.when(walletRepo.findById(walletId)).thenThrow(notFoundException);

        Exception expected = null;
        try {
            Wallet expectedWallet = walletService.getWallet(walletId);
        }catch (NotFoundException ex){
            expected=ex;
        }

        assert expected != null;
        assertEquals(notFoundException.getMessage() , expected.getMessage() );

    }

    @Test
    public void testRefundToWallet(){

        Wallet wallet = mock(Wallet.class);
        wallet.setId(20);
        wallet.setBalance(2000d);
        wallet.setTransactions(new ArrayList<>());

        Transaction transaction = mock(Transaction.class);
        wallet.getTransactions().add(transaction);

        Mockito.when(transactionService.add(any(Transaction.class))).thenReturn(transaction);
        Mockito.when(transactionService.isNotNull(any(Transaction.class))).thenReturn(true);

        Wallet expectedWallet  = walletService.refundToWallet(wallet,200,12);

        assertEquals(wallet , expectedWallet );
        assertEquals(wallet.getId() , expectedWallet.getId() );
    }
}


