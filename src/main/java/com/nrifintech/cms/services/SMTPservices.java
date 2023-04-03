package com.nrifintech.cms.services;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.nrifintech.cms.dtos.EmailModel;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * This class is used to send emails using SMTP
 */
@Service
public class SMTPservices {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    Configuration fmConfiguration;

    
    /**
     * It takes an EmailModel object, creates a MimeMessage object, and sends the email
     * 
     * @param mail This is the object of the EmailModel class.
     * @return A boolean value.
     */
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
 
   /**
    * It takes a map of key-value pairs and a template name, and returns a string
    * 
    * @param model A map of key-value pairs that will be used to populate the template.
    * @param templateName The name of the template file.
    * @return The content of the template.
    */
    public String getContentFromTemplate(Map <String, String >model, String templateName) throws IOException, TemplateException     { 
        StringBuffer content = new StringBuffer();
 
        content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
        
        return content.toString();
    }

}
