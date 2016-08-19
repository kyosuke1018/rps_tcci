/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jimmy
 */
public class VelocityMail {
    private final static Logger logger = LoggerFactory.getLogger(VelocityMail.class);
    
    // input parameter key
    public final static String SUBJECT = "subject";    // String, mail subject
    public final static String FROM = "from";          // String, mail from (option, email address)
    public final static String TO = "to";              // String, mail to (email address, separate by comma)
    public final static String CC = "cc";              // mail cc (option, email address, separate by comma)
    public final static String BCC = "bcc";            // mail bcc (option, email address, separate by comma)

    static {
        Velocity.setProperty(Velocity.RESOURCE_LOADER, "webapp");
        Velocity.setProperty("webapp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.setProperty("webapp.resource.loader.path", "");
        Velocity.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        Velocity.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        Velocity.init();
    }

    public static void sendMail(HashMap parameters, Object bean, String template) {
        if (parameters == null) {
            return;
        }        
        String subject = (String) parameters.get(SUBJECT);
        String from = (String) parameters.get(FROM); // option
        String to = (String) parameters.get(TO);
        String cc = (String) parameters.get(CC);     // option
        String bcc = (String) parameters.get(BCC);   // option
        logger.info("sendMail() parameters = {}", parameters);
        try {
            InitialContext ctx = new InitialContext();
            Session mailSession = (Session) ctx.lookup("mail/automail");
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            MimeMessage msg = new MimeMessage(mailSession);
            try {
                // mail body
                mbp.setContent(getContent(bean, template), "text/html;charset=utf-8");
                mp.addBodyPart(mbp);
                msg.setContent(mp);
                // mail header
                msg.setSubject(subject, "UTF-8");
                if (from != null) {
                    msg.setFrom(new InternetAddress(from));
                } else {
                    msg.setFrom(new InternetAddress("automail@taiwancement.com", "系統通知信", "UTF-8"));
                }
                msg.setRecipients(RecipientType.TO, getInternetAddressArray(to));                
                if (cc != null) {
                    InternetAddress[] mails = getInternetAddressArray(cc);
                    if (mails != null && mails.length>0)
                        msg.setRecipients(RecipientType.CC, mails);
                }
                if (bcc != null) {
                    InternetAddress[] mails = getInternetAddressArray(bcc);
                    if (mails != null && mails.length>0)
                        msg.setRecipients(RecipientType.BCC, mails);
                }
                Transport.send(msg);
            } catch (Exception ex) {
                logger.error("sendMail() exception, parameters = {}, ex = {}",
                        parameters,
                        ex);
            }
        } catch (NamingException ex) {
            logger.error("sendMail() NamingException", ex);
        }
    }

    private static String getContent(Object bean, String templateFile) {
        VelocityContext context = new VelocityContext();
        context.put("bean", bean);
        StringWriter writer = new StringWriter();
        try {
            Template template = Velocity.getTemplate(templateFile);
            template.merge(context, writer);
        } catch (ResourceNotFoundException rnfe) {
            logger.error("getContent() ResourceNotFoundException, templateFile = {}, ex = {}",
                    templateFile,
                    rnfe);
        }
        return writer.toString();
    }

    // email格式 姓名1 <mailbox1@domain.com>,姓名2 <mailbox2@domain.com>,...
    //          mailbox1@domain.com,mailbox2@domain.com
    private static InternetAddress[] getInternetAddressArray(String recipients) {
        ArrayList<InternetAddress> recipientsArray = new ArrayList<InternetAddress>();
        // 過濾相同 email
        HashSet<String> set = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(recipients, ",");
        while (st.hasMoreTokens()) {
            String email = StringUtils.trim(st.nextToken());
            if (!set.add(email)) {
                continue;
            }
            try {
                String[] nameMail = StringUtils.split(email, ' ');
                if (nameMail.length==2) {
                    String name = nameMail[0];
                    String address = StringUtils.remove(StringUtils.remove(nameMail[1],'<'),'>');
                    recipientsArray.add(new InternetAddress(address, name, "UTF-8"));
                } else {
                    recipientsArray.add(new InternetAddress(email));
                }
            } catch (Exception ex) {
                logger.error("getInternetAddressArray() ex, email = {}", email);
            }
        }
        return recipientsArray.toArray(new InternetAddress[recipientsArray.size()]);
    }

}
