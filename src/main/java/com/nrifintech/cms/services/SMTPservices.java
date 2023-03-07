package com.nrifintech.cms.services;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.Order;

import freemarker.template.Configuration;

@Service
public class SMTPservices {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    Configuration fmConfiguration;

    
    public boolean forgotPasswordMail(EmailModel mail){
		MimeMessage mimeMessage =javaMailSender.createMimeMessage();
		boolean flag = false;

		Map<String,String> m = new HashMap<>();
		m.put("action_url", mail.getBody());
        m.put("name",mail.getTo());
        m.put("timestamp",mail.getTime());
        m.put("support_url","https://www.nrifintech.com/contact.html");

        try {
 
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(mail.getFrom());
            mimeMessageHelper.setTo(mail.getTo());
            mail.setBody(getContentFromTemplate(m,"forgot-password.flth"));
            mimeMessageHelper.setText(mail.getBody(), true);
 
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
			flag = true;
        } catch (MessagingException e) {
            e.printStackTrace();
			flag = false;
        }
		return(flag);
    }

    public boolean welcomeMail(EmailModel mail){
		MimeMessage mimeMessage =javaMailSender.createMimeMessage();
		boolean flag = false;

		Map<String,String> m = new HashMap<>();
		m.put("password", mail.getBody());
        m.put("name",mail.getTo());
        m.put("time",mail.getTime());
        m.put("support_url","https://www.nrifintech.com/contact.html");

        try {
 
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(mail.getFrom());
            mimeMessageHelper.setTo(mail.getTo());
            mail.setBody(getContentFromTemplate(m,"welcome-email.flth"));
            mimeMessageHelper.setText(mail.getBody(), true);
 
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
			flag = true;
        } catch (MessagingException e) {
            e.printStackTrace();
			flag = false;
        }
		return(flag);
    }
 
    public String getContentFromTemplate(Map <String, String >model, String templateName)     { 
        StringBuffer content = new StringBuffer();
 
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
