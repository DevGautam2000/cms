package com.nrifintech.cms.Scheduler;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.entities.Menu;
import com.nrifintech.cms.entities.TokenBlacklist;
import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.events.BreakfastStartEvent;
import com.nrifintech.cms.events.LunchStartEvent;
import com.nrifintech.cms.events.PromotionalEvent;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.services.MenuService;
import com.nrifintech.cms.services.OrderService;
import com.nrifintech.cms.services.UserService;
import com.nrifintech.cms.types.Approval;
import com.nrifintech.cms.types.MealType;


@Component
public class Tasks {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    UserService userService;

    @Autowired
    TokenBlacklistRepo tokenBlacklistRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuRepo menuRepo;

    @Scheduled( fixedDelay = 10000)
    public void promotionalEvent(){
        List<String> recipients = userService.getAllConsumers();
        if( recipients.size()!=0 ){
            applicationEventPublisher.publishEvent(new PromotionalEvent(recipients));
        }
    }

    @Scheduled(fixedDelay = 10000)
    //@Scheduled(cron = "0 30 3 ? * MON,TUE,WED,THU,FRI *")
    public void BreakfastStart(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        List<String> recipients = userService.getOrdersByDateAndOrderType( Date.valueOf(dtf.format(now).toString()) , MealType.Breakfast.ordinal());
        if(recipients.size()>0){
            applicationEventPublisher.publishEvent(new BreakfastStartEvent(recipients));
        } 
    }
    @Scheduled(fixedDelay = 10000)
    //@Scheduled(cron = "0 30 6 ? * MON,TUE,WED,THU,FRI *")
    public void LunchStart(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        List<String> recipients = userService.getOrdersByDateAndOrderType( Date.valueOf(dtf.format(now).toString()) , MealType.Lunch.ordinal());
        if( recipients.size()>0 ){
            applicationEventPublisher.publishEvent(new LunchStartEvent(recipients));
        } 
    }

    @Scheduled(fixedDelay = 10000)
    // @Scheduled(cron = "0 30 18 ? * MON,TUE,WED,THU,FRI,SAT *" )
    public void autoArchive(){
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();
        orderService.autoArchive( (Date.valueOf(dtf.format(now).toString())).toString() );
    }

    @Scheduled(fixedDelay = 10000)
    // @Scheduled(cron = "0 30 18 ? * MON,TUE,WED,THU,FRI,SAT *" )
    public void autoFlush(){
        List<TokenBlacklist> operationList = this.tokenBlacklistRepo.findAll();

        List<TokenBlacklist> expiredList = operationList.stream().filter(t->{
            try{
                return(this.jwtUtils.isTokenExpired(t.getToken()));
        } catch(Exception e){ 
                return(true);} }
             ).collect(Collectors.toList());

        this.tokenBlacklistRepo.deleteAll(expiredList);
    }

    @Scheduled(fixedDelay = 10000)
    //@Scheduled(cron = "0 30 18 ? * MON,TUE,WED,THU,FRI,SAT *" )
    @Transactional
    public void clearCart(){
        List<User> users = userService.getUsers();
        users.stream().filter( u->u.getCart()!=null ).forEach( u->{
            u.getCart().getCartItems().clear();
            userService.saveUser(u);
        });
    }

    @Scheduled(fixedDelay = 10000)
    //@Scheduled(cron = "0 30 11 ? * SUN,MON,TUE,WED,THU *")
    public void menuAutoApprove() throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime()); 
        c.add(Calendar.DATE, 1); 
        String output = sdf.format(c.getTime());
        Date date = new Date(sdf.parse(output).getTime());
        List<Menu> menus = menuRepo.findMenuByDate(date); 

        menus.stream().filter(m->m.getItems().size()!=0).filter(m->m.getApproval()==Approval.Pending).forEach(m->{ 
            m.setApproval(Approval.Approved);
            menuRepo.save(m);
            }
        );
    }
    
}
