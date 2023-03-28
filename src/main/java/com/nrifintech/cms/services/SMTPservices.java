package com.nrifintech.cms.services;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.event.StoreListener;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.nrifintech.cms.dtos.EmailModel;
import com.nrifintech.cms.entities.Order;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class SMTPservices {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    Configuration fmConfiguration;

    
    public boolean sendMail(EmailModel mail) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException{
		MimeMessage mimeMessage =javaMailSender.createMimeMessage();
		boolean flag = false;

		Map<String,String> m = mail.getBody();

        try {
 
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(mail.getFrom());
            String[] strarray = new String[mail.getTo().size()];
            (mail.getTo()).toArray(strarray);
            mimeMessageHelper.setBcc(strarray);
            mail.setEmbeddedHTML(getContentFromTemplate(m,mail.getTemplateUsed()));
            mimeMessageHelper.setText(mail.getEmbeddedHTML(), true);
 
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
			flag = true;
        } catch (MessagingException e) {
			flag = false;
            throw e;
        }
		return(flag);
    }
 
    public String getContentFromTemplate(Map <String, String >model, String templateName) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException     { 
        StringBuffer content = new StringBuffer();
 
        content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
        
        return content.toString();
    }

}
