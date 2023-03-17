package com.nrifintech.cms.listeners;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.dtos.InventoryMail;
import com.nrifintech.cms.dtos.OrderToken;
import com.nrifintech.cms.dtos.WalletEmailResponse;
import com.nrifintech.cms.entities.CartItem;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.ApprovedQtyReqEvent;
import com.nrifintech.cms.events.CancelledOrderEvent;
import com.nrifintech.cms.events.DeliveredOrderEvent;
import com.nrifintech.cms.events.ForgotPasswordEvent;
import com.nrifintech.cms.events.PlacedOrderEvent;
import com.nrifintech.cms.events.UpdateQtyReqEvent;
import com.nrifintech.cms.events.UpdateUserStatusEvent;
import com.nrifintech.cms.events.WalletDebitEvent;
import com.nrifintech.cms.events.WalletRechargeEvent;
import com.nrifintech.cms.events.WalletRefundEvent;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.services.SMTPservices;
import com.nrifintech.cms.types.UserStatus;

@Component
public class Listeners {

    @Autowired
    private SMTPservices smtpServices;

    @Autowired
    private UserRepo userRepo;

    @EventListener
    @Async
    public void onForgotPassword(ForgotPasswordEvent event){
        HashMap<String,String> body = (HashMap<String, String>) event.getSource();
        body.put("timestamp", LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        body.put("link","<pass-forgot-link>");
        List<String> recipients = new ArrayList<>();
        recipients.add(body.get("username"));
        EmailModel email = new EmailModel(recipients , "Forgot-password-request" , body , "forgot-password.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onAddedNewUser(AddedNewUserEvent event){
        User user = (User) event.getSource();

        List<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        HashMap<String,String> body = new HashMap<>();
        body.put("unsublink" , "...link to user page...");
        body.put("link", "...pass-reset-link...");
        body.put("username",user.getEmail());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients,"Welcome to Canteen Management System NRI Fintech India Pvt.Ltd." , body,"welcome-email.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onUpdateUserStatus(UpdateUserStatusEvent event){
        User user = (User) event.getSource();
        String template = "inactive-user.flth";
        if(user.getStatus() == UserStatus.Active){
            template = "active-user.flth";
        }
        List<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        HashMap<String,String> body = new HashMap<>();
        body.put("status", user.getStatus().toString());
        body.put("username",user.getEmail());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,template);
        this.smtpServices.sendMail(email); 
    }

    @EventListener
    @Async
    public void onPlacedOrder(PlacedOrderEvent placedOrderEvent) throws JsonProcessingException{
        OrderToken placedOrderToken = (OrderToken) placedOrderEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        body.put("username", placedOrderToken.getUsername());
        body.put("orderid",placedOrderToken.getOrder().getId()+"");
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> listCartItems = placedOrderToken.getOrder().getCartItems().stream().map(c->c.getName()).collect(Collectors.toList());
        String itemDetails = mapper.writeValueAsString(listCartItems);
        body.put("items",itemDetails);
        List<String> recipients = new ArrayList<>();
        recipients.add(placedOrderToken.getUsername());
        System.out.println(placedOrderToken.getUsername());
        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"placed-order.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onCancelledOrder(CancelledOrderEvent cancelledOrderEvent) throws JsonProcessingException{
        OrderToken cancelledOrderToken = (OrderToken) cancelledOrderEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        body.put("username", cancelledOrderToken.getUsername());
        body.put("orderid",cancelledOrderToken.getOrder().getId()+"");
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> listCartItems = cancelledOrderToken.getOrder().getCartItems().stream().map(c->c.getName()).collect(Collectors.toList());
        String itemDetails = mapper.writeValueAsString(listCartItems);
        body.put("items",itemDetails);
        List<String> recipients = new ArrayList<>();
        recipients.add(cancelledOrderToken.getUsername());
        System.out.println(cancelledOrderToken.getUsername());
        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"cancelled-order.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onDeliveredOrder(CancelledOrderEvent deliveredOrderEvent) throws JsonProcessingException{
        OrderToken deliveredOrderToken = (OrderToken) deliveredOrderEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        body.put("username", deliveredOrderToken.getUsername());
        body.put("orderid",deliveredOrderToken.getOrder().getId()+"");
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> listCartItems =  deliveredOrderToken.getOrder().getCartItems().stream().map(c->c.getName()).collect(Collectors.toList());
        String itemDetails = mapper.writeValueAsString(listCartItems);
        body.put("items",itemDetails);
        List<String> recipients = new ArrayList<>();
        recipients.add(deliveredOrderToken.getUsername());
        System.out.println(deliveredOrderToken.getUsername());
        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"delivered-order.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onRecharge(WalletRechargeEvent walletRechargeEvent){
        WalletEmailResponse w = (WalletEmailResponse) walletRechargeEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("username",w.getUsername());
        body.put("curr", String.valueOf(w.getCurrentBalance()) );
        body.put("money", String.valueOf(w.getMoneyAdded()) );
        body.put("transactionId", w.getTransactionId());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> recipients = new ArrayList<>();
        recipients.add(w.getUsername());
        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"wallet-recharge.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onDebit(WalletDebitEvent walletDebitEvent){
        WalletEmailResponse w = (WalletEmailResponse) walletDebitEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("username",w.getUsername());
        body.put("curr", String.valueOf(w.getCurrentBalance()) );
        body.put("money", String.valueOf(w.getMoneyAdded()) );
        body.put("transactionId", w.getTransactionId());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> recipients = new ArrayList<>();
        recipients.add(w.getUsername());
        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"wallet-refund.flth");
        this.smtpServices.sendMail(email);
    }
   
    @EventListener
    @Async
    public void onRefund(WalletRefundEvent walletRefundEvent){
        WalletEmailResponse w = (WalletEmailResponse) walletRefundEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("username",w.getUsername());
        body.put("curr", String.valueOf(w.getCurrentBalance()) );
        body.put("money", String.valueOf(w.getMoneyAdded()) );
        body.put("transactionId", w.getTransactionId());
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> recipients = new ArrayList<>();
        recipients.add(w.getUsername());
        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"wallet-refund.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onApproval(ApprovedQtyReqEvent approvedQtyReqEvent){

        List<Tuple> resTup = userRepo.getUserEmailsByRole(1);
        List<String> recipients = resTup.stream().map(u->u.get(0,String.class)).collect(Collectors.toList());
        HashMap<String , String> body = new HashMap<>();
        InventoryMail iMail = (InventoryMail) approvedQtyReqEvent.getSource();

        body.put("id" , String.valueOf(iMail.getId()) );
        body.put("name" , iMail.getName() );
        body.put("qtyInHand", String.valueOf(iMail.getQuantityInHand()) );
        body.put("qtyReq", String.valueOf(iMail.getQuantityRequested()) );
        body.put("qtypur" , String.valueOf(iMail.getQuantityPurchased()) );
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"approval-inventory.flth");
        this.smtpServices.sendMail(email);

    }

    @EventListener
    @Async
    public void onRequest(UpdateQtyReqEvent updateQtyReqEvent){

        List<Tuple> resTup = userRepo.getUserEmailsByRole(0);
        List<String> recipients = resTup.stream().map(u->u.get(0,String.class)).collect(Collectors.toList());
        HashMap<String , String> body = new HashMap<>();
        InventoryMail iMail = (InventoryMail) updateQtyReqEvent.getSource();

        body.put("id" , String.valueOf(iMail.getId()) );
        body.put("name" , iMail.getName() );
        body.put("qtyInHand", String.valueOf(iMail.getQuantityInHand()) );
        body.put("qtyReq", String.valueOf(iMail.getQuantityRequested()) );
        body.put("timestamp",LocalTime.now(ZoneId.of("GMT+05:30")).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients,"Canteen Management System NRI Fintech India Pvt.Ltd." , body,"request-inventory.flth");
        this.smtpServices.sendMail(email);

    }
}

