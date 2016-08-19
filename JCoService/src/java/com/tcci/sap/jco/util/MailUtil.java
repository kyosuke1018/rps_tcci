/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.util;

import com.tcci.sap.jco.conf.GlobalConstant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    
    public static final String MAIL_ENCODING = "UTF-8";
    public static final String DEF_MAIL_CONTENT_TYPE = "text/html;charset=utf-8";
    public static final String DEF_MAIL_TEMPLATE = "<html><body><b>_TITLE_</b><hr />_DETAIL_</body></html>";
    public static final String DEF_SENDER_EMAIL = "automail@taiwancement.com";
    public static final String DEF_SENDER_NAME = "系統通知";
    
    /**
     * 寄發 EMAIL (Content 為 String Template)
     * @param subject
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param bodyTemplate
     * @param params
     * @param attachments 
     */
    public static synchronized void sendMail(String subject, String from, String to, String cc, String bcc, 
            String bodyTemplate, Map<String, String> params, List<String> attachments)
    {
        logger.info("sendMail: to="+to);
        try {
            InitialContext ctx = new InitialContext();
            Session mailSession = (Session) ctx.lookup(GlobalConstant.JNDI_MAIL_SESSION);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            MimeMessage msg = new MimeMessage(mailSession);
            
            for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it.hasNext();) {
                Entry<String, String> entry = it.next();
                // Note that backslashes (\) and dollar signs ($) in the replacement string 
                // may cause the results to be different than if it were being treated as a literal replacement string; see Matcher.replaceAll. 
                // Use Matcher.quoteReplacement(java.lang.String) to suppress the special meaning of these characters, if desired.
                bodyTemplate = bodyTemplate.replaceAll("_"+entry.getKey()+"_", Matcher.quoteReplacement(entry.getValue()));
            }
            mbp.setContent(bodyTemplate, DEF_MAIL_CONTENT_TYPE);
            mp.addBodyPart(mbp);

            // Set Content attachment
            if (attachments != null) {
                for (String filename : attachments) {
                    FileDataSource fds = new FileDataSource(filename);
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(fds.getName());
                    mp.addBodyPart(mbp2);
                }
            }
            msg.setContent(mp);

            // mail header
            msg.setSubject(subject, MAIL_ENCODING);
            if (from != null && !from.isEmpty()) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom(new InternetAddress(DEF_SENDER_EMAIL, DEF_SENDER_NAME, MAIL_ENCODING));
            }
            // mail recipients
            msg.setRecipients(Message.RecipientType.TO, getInternetAddressArray(to));
            if (cc != null) {
                InternetAddress[] mails = getInternetAddressArray(cc);
                if (mails != null && mails.length > 0) {
                    msg.setRecipients(Message.RecipientType.CC, mails);
                }
            }
            if (bcc != null) {
                InternetAddress[] mails = getInternetAddressArray(bcc);
                if (mails != null && mails.length > 0) {
                    msg.setRecipients(Message.RecipientType.BCC, mails);
                }
            }

            Transport.send(msg);
        } catch (Exception ex) {
            logger.error("sendMail Exception: \n", ex);
        }
    }
    public static synchronized void sendMail(String subject, String to, Map<String, String> params){
        sendMail(subject, null, to, null, null, DEF_MAIL_TEMPLATE, params, null);
    }

    /**
     * email格式 
     * 姓名1 <mailbox1@domain.com>,姓名2 <mailbox2@domain.com>,...
     * mailbox1@domain.com,mailbox2@domain.com
     * @param recipients
     * @return 
     */
    private static InternetAddress[] getInternetAddressArray(String recipients) {
        ArrayList<InternetAddress> recipientsArray = new ArrayList<InternetAddress>();
        // 過濾相同 email
        HashSet<String> set = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(recipients, ",");
        while (st.hasMoreTokens()) {
            String email = st.nextToken();
            if (!set.add(email)) {
                continue;
            }
            try {
                String[] nameMail = email.split(" ");
                if (nameMail.length == 2) {// 姓名1 <mailbox1@domain.com>
                    String name = nameMail[0];
                    String address = nameMail[1].replace("<", "").replace(">", "");
                    recipientsArray.add(new InternetAddress(address, name, MAIL_ENCODING));
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
