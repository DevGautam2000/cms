package com.nrifintech.cms.listeners;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.dtos.InventoryMail;
import com.nrifintech.cms.dtos.OrderToken;
import com.nrifintech.cms.dtos.WalletEmailResponse;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.events.AddedNewUserEvent;
import com.nrifintech.cms.events.ApprovedQtyReqEvent;
import com.nrifintech.cms.events.CancelledOrderEvent;
import com.nrifintech.cms.events.DeliveredOrderEvent;
import com.nrifintech.cms.events.ForgotPasswordEvent;
import com.nrifintech.cms.events.MenuStatusChangeEvent;
import com.nrifintech.cms.events.PlacedOrderEvent;
import com.nrifintech.cms.events.UpdateQtyReqEvent;
import com.nrifintech.cms.events.UpdateUserStatusEvent;
import com.nrifintech.cms.events.WalletDebitEvent;
import com.nrifintech.cms.events.WalletRechargeEvent;
import com.nrifintech.cms.events.WalletRefundEvent;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.services.AuthenticationService;
import com.nrifintech.cms.services.SMTPservices;
import com.nrifintech.cms.types.UserStatus;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Component
public class Listeners {

    @Autowired
    private SMTPservices smtpServices;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationService authenticationService;

    private String timezone = "GMT+05:30";
    private String subject = "Canteen Management System NRI Fintech India Pvt.Ltd.";

    @EventListener
    @Async
    public void onForgotPassword(ForgotPasswordEvent event) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        HashMap<String,String> body = (HashMap<String, String>) event.getSource();
        body.put("timestamp", LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        body.put("actionurl","http://localhost:3000/resetPassword?link=" + body.get("forgotlink"));
        List<String> recipients = new ArrayList<>();
        recipients.add(body.get("username"));
        EmailModel email = new EmailModel(recipients , "Forgot-password-request" , body , "forgot-password.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onAddedNewUser(AddedNewUserEvent event) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        User user = (User) event.getSource();

        List<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());
        HashMap<String,String> body = new HashMap<>();
        body.put("unsublink" , "...link to user page...");
        body.put("actionurl" , "http://localhost:3000/resetPassword?link=" +authenticationService.setNewPassword(user.getEmail()) + "&username=" + user.getEmail()  );
        body.put("username",user.getEmail());
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        EmailModel email = new EmailModel(recipients,"Welcome to " + subject , body,"welcome-email.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onUpdateUserStatus(UpdateUserStatusEvent event) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
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
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients , subject , body,template);
        this.smtpServices.sendMail(email); 
    }

    @EventListener
    @Async
    public void onPlacedOrder(PlacedOrderEvent placedOrderEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        OrderToken placedOrderToken = (OrderToken) placedOrderEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        body.put("username", placedOrderToken.getUsername());
        body.put("orderid",placedOrderToken.getOrder().getId()+"");
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> listCartItems = placedOrderToken.getOrder().getCartItems().stream().map(c->c.getName()).collect(Collectors.toList());
        String itemDetails = mapper.writeValueAsString(listCartItems);
        body.put("items",itemDetails);
        List<String> recipients = new ArrayList<>();
        recipients.add(placedOrderToken.getUsername());
        EmailModel email = new EmailModel(recipients , subject , body,"placed-order.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onCancelledOrder(CancelledOrderEvent cancelledOrderEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        OrderToken cancelledOrderToken = (OrderToken) cancelledOrderEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        body.put("username", cancelledOrderToken.getUsername());
        body.put("orderid",cancelledOrderToken.getOrder().getId()+"");
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> listCartItems = cancelledOrderToken.getOrder().getCartItems().stream().map(c->c.getName()).collect(Collectors.toList());
        String itemDetails = mapper.writeValueAsString(listCartItems);
        body.put("items",itemDetails);
        List<String> recipients = new ArrayList<>();
        recipients.add(cancelledOrderToken.getUsername());
        EmailModel email = new EmailModel(recipients , subject , body,"cancelled-order.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onDeliveredOrder(DeliveredOrderEvent deliveredOrderEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        OrderToken deliveredOrderToken = (OrderToken) deliveredOrderEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        body.put("username", deliveredOrderToken.getUsername());
        body.put("orderid",deliveredOrderToken.getOrder().getId()+"");
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> listCartItems =  deliveredOrderToken.getOrder().getCartItems().stream().map(c->c.getName()).collect(Collectors.toList());
        String itemDetails = mapper.writeValueAsString(listCartItems);
        body.put("items",itemDetails);
        List<String> recipients = new ArrayList<>();
        recipients.add(deliveredOrderToken.getUsername());
        EmailModel email = new EmailModel(recipients , subject , body,"delivered-order.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onRecharge(WalletRechargeEvent walletRechargeEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        WalletEmailResponse w = (WalletEmailResponse) walletRechargeEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("username",w.getUsername());
        body.put("curr", String.valueOf(w.getCurrentBalance()) );
        body.put("money", String.valueOf(w.getMoneyAdded()) );
        body.put("transactionId", w.getTransactionId());
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> recipients = new ArrayList<>();
        recipients.add(w.getUsername());
        EmailModel email = new EmailModel(recipients , subject, body,"wallet-recharge.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onDebit(WalletDebitEvent walletDebitEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        WalletEmailResponse w = (WalletEmailResponse) walletDebitEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("username",w.getUsername());
        body.put("curr", String.valueOf(w.getCurrentBalance()) );
        body.put("money", String.valueOf(w.getMoneyAdded()) );
        body.put("transactionId", w.getTransactionId());
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> recipients = new ArrayList<>();
        recipients.add(w.getUsername());
        EmailModel email = new EmailModel(recipients , subject , body,"wallet-debit.flth");
        this.smtpServices.sendMail(email);
    }
   
    @EventListener
    @Async
    public void onRefund(WalletRefundEvent walletRefundEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{
        WalletEmailResponse w = (WalletEmailResponse) walletRefundEvent.getSource();
        HashMap<String,String> body = new HashMap<>();
        body.put("username",w.getUsername());
        body.put("curr", String.valueOf(w.getCurrentBalance()) );
        body.put("money", String.valueOf(w.getMoneyAdded()) );
        body.put("transactionId", w.getTransactionId());
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());
        List<String> recipients = new ArrayList<>();
        recipients.add(w.getUsername());
        EmailModel email = new EmailModel(recipients , subject , body,"wallet-refund.flth");
        this.smtpServices.sendMail(email);
    }

    @EventListener
    @Async
    public void onApproval(ApprovedQtyReqEvent approvedQtyReqEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{

        List<Tuple> resTup = userRepo.getUserEmailsByRole(1);
        List<String> recipients = resTup.stream().map(u->u.get(0,String.class)).collect(Collectors.toList());
        HashMap<String , String> body = new HashMap<>();
        InventoryMail iMail = (InventoryMail) approvedQtyReqEvent.getSource();

        body.put("id" , String.valueOf(iMail.getId()) );
        body.put("name" , iMail.getName() );
        body.put("qtyInHand", String.valueOf(iMail.getQuantityInHand()) );
        body.put("qtyReq", String.valueOf(iMail.getQuantityRequested()) );
        body.put("qtypur" , String.valueOf(iMail.getQuantityPurchased()) );
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients , subject , body,"approval-inventory.flth");
        this.smtpServices.sendMail(email);

    }

    @EventListener
    @Async
    public void onRequest(UpdateQtyReqEvent updateQtyReqEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{

        List<Tuple> resTup = userRepo.getUserEmailsByRole(0);
        List<String> recipients = resTup.stream().map(u->u.get(0,String.class)).collect(Collectors.toList());
        HashMap<String , String> body = new HashMap<>();
        InventoryMail iMail = (InventoryMail) updateQtyReqEvent.getSource();

        body.put("id" , String.valueOf(iMail.getId()) );
        body.put("name" , iMail.getName() );
        body.put("qtyInHand", String.valueOf(iMail.getQuantityInHand()) );
        body.put("qtyReq", String.valueOf(iMail.getQuantityRequested()) );
        body.put("timestamp",LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString());

        EmailModel email = new EmailModel(recipients , subject , body,"request-inventory.flth");
        this.smtpServices.sendMail(email);

    }

    @EventListener
    @Async
    public void onMenuStatusChange(MenuStatusChangeEvent menuStatusChangeEvent) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException{

        Menu m = (Menu) menuStatusChangeEvent.getSource();

        List<Tuple> resAdmins = userRepo.getUserEmailsByRole(0);
        List<Tuple> resCanteen = userRepo.getUserEmailsByRole(1);
        List<String> admins = resAdmins.stream().map( u -> u.get(0,String.class) ).collect( Collectors.toList() );
        List<String> canteen = resCanteen.stream().map( u -> u.get(0,String.class) ).collect( Collectors.toList() );

        HashMap<String,String> body = new HashMap<>();
        body.put("id", String.valueOf(m.getId()) );
        body.put("forday" , m.getDate().toString() );
        body.put("status" , m.getApproval().toString() );
        body.put("mealtype" , m.getMenuType().toString() );
        body.put("timestamp" , LocalTime.now(ZoneId.of(timezone)).truncatedTo(ChronoUnit.MINUTES).toString() );

        EmailModel email1 = new EmailModel(admins , subject , body,"menu-status-change.flth");
        EmailModel email2 = new EmailModel(canteen , subject , body,"menu-status-change.flth");
        this.smtpServices.sendMail(email1);
        this.smtpServices.sendMail(email2);

    }
}
