/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.fc.util.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.InitialContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
public class NotificationUtils {
    private Logger logger = LoggerFactory.getLogger(NotificationUtils.class);  
    public final static String DEF_MAIL_JNDI = "mail/automail";
    public final static String DEF_MAIL_FROM = "automail@taiwancement.com";
    private static NotificationUtils instance = null;

    public static NotificationUtils getInstance() {
        if (instance == null) {
            instance = new NotificationUtils();
        }
        //instance.getEnvironmentData(); 
        return instance;
    }
    
    /**
     * 異常通知
     * @param notifyEmails
     * @param operator
     * @param methodName
     * @param e
     * @param errorCode
     * @param otherDetail 
     */
    public void notifyOnException(String notifyEmails, String operator, String methodName, Exception e, String errorCode, String otherDetail){
        try{ 
            logger.debug("notifyOnException ...");
            String emailTo = notifyEmails;
            if( emailTo==null ){
                logger.error("notifyOnException emailTo==null");
                return;
            }
            String exStr = ExceptionHandlerUtils.getStackTrace(e);
            String subject = GlobalConstant.AP_TITLE + " - 系統異常通知";

            String hostname = NetworkUtils.getHostIP(); // WebUtils.getHostName();
            String datetime = DateUtils.format(new Date());
            StringBuilder content = new StringBuilder();
            content.append("<h5>").append(subject).append("：</h5>\n");
            content.append("<hr/>\n");
            content.append("發生異常主機：").append(hostname).append("<br/>\n");
            content.append("發生異常時間：").append(datetime).append("<br/>\n");
            if( operator!=null ){
                content.append("操作人員：").append(operator).append("<br/>\n");
            }

            content.append("執行函式：").append(methodName).append("<br/>\n");
            content.append("錯誤代碼 ：").append(errorCode).append("<br/>\n");
            content.append("<hr/>\n");
            content.append("詳細錯誤訊息：(請查閱 server.log 以了解詳細錯誤原因)\n");
            content.append("<pre>\n");
            content.append(exStr);
            content.append("</pre>\n");
            if( otherDetail!=null && !otherDetail.isEmpty() ){
                content.append("<hr/>\n");
                content.append("EJB Constraint Violations 錯誤訊息：\n");
                content.append("<pre>\n");
                content.append(otherDetail);
                content.append("</pre>\n");
            }

            //NotificationUtils sender = new NotificationUtils();
            //sender.sendMail(emailTo, subject, content.toString());
            sendMail(emailTo, subject, content.toString());
        }catch(Exception ex){
            logger.error("notifyOnException Exception:\n", ex);
        }
    }

    /**
     * Exception EMail Notify
     * @param receiverList
     * @param subject
     * @param ex
     * @return 
     */
    public boolean sendExceptionMail(List<EcMember> receiverList, String subject, Exception ex){
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================").append("<br />\n")
            .append("主旨 : ").append(subject).append("<br />\n")
            .append("時間 : ").append(DateUtils.format(new Date())).append("<br />\n")
            .append("==================================================").append("<br />\n")
            .append("詳細錯誤內容：").append("<br />\n")
            .append(ExceptionHandlerUtils.getStackTrace(ex));
        
        return sendMail(receiverList, subject, sb.toString());
    }

    public boolean sendMail(EcMember receiver, String subject, String content) {
        if (receiver != null && !StringUtils.isEmpty(receiver.getEmail())) {
            return sendMail(receiver.getEmail(), subject, content);
        } else {
            return false;
        }
    }

    public boolean sendMail(List<EcMember> receiverList, String subject, String content) {
        return sendMail(receiverList, subject, content, DEF_MAIL_JNDI, DEF_MAIL_FROM);
    }
    public boolean sendMail(List<EcMember> receiverList, String subject, String content, String mailJNDI, String from) {
        if (receiverList != null && !receiverList.isEmpty()) {
            String emailStringTo = getReceiversEmail(receiverList);
            return _sendMail(emailStringTo, subject, content, null, mailJNDI, from);
        }
        return false;
    }

    public boolean sendMail(List<EcMember> receiverList, String subject, String content, Properties attachment) {
        if (receiverList != null && !receiverList.isEmpty()) {
            String emailStringTo = getReceiversEmail(receiverList);
            return _sendMail(emailStringTo, subject, content, attachment);
        }
        return false;
    }

    public boolean sendMail(String emailStringTo, String subject, String content) {
        if (!StringUtils.isEmpty(emailStringTo)) {
            return _sendMail(emailStringTo, subject, content, null);
        }
        return false;
    }

    private boolean _sendMail(String emailStringTo, String subject, String content, Properties attachment) {
        return _sendMail(emailStringTo, subject, content, attachment, DEF_MAIL_JNDI, DEF_MAIL_FROM) ;
    }
    private boolean _sendMail(String emailStringTo, String subject, String content, Properties attachment, String mailJNDI, String from) {
        boolean status = false;

        try {
            InitialContext ctx = new InitialContext();
            Session mailSession = (Session) ctx.lookup(mailJNDI);
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(emailStringTo, false));
            msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
            msg.setSentDate(new java.util.Date());
            String contentType = "text/html;charset=utf-8";
            Multipart mp = new MimeMultipart();
            
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(content, contentType);            
            mp.addBodyPart(mbp1);
            //=====================================================
            // Set Content attachment
            //=====================================================
            if (attachment != null) {
                for (java.util.Enumeration em = attachment.elements(); em.hasMoreElements();) {

                    String filename = (String) em.nextElement();
                    FileDataSource fds = new FileDataSource(filename);
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(fds.getName());
                    mp.addBodyPart(mbp2);
                }
            }
            msg.setContent(mp);
            //MimeMessage
            msg.saveChanges();
            Transport.send(msg);

            status = true;
            logger.info("Send mail success... FROM："+from+", TO："+emailStringTo+", SUBJECT："+subject);
        } catch (Exception ex) {
            logger.error("_sendMail exception :\n", ex);
        }

        return status;
    }

    /**
     * 取得格式化 email 串列 mailbox1@domain.com,mailbox2@domain.com
     * @param receivers
     * @return 
     */
    public String getReceiversEmail(List<EcMember> receivers) {
        if (receivers == null) {
            return null;
        }
        List<String> userEmails = new ArrayList<String>();
        for (EcMember user : receivers) {
            if (GenericValidator.isEmail(user.getEmail())) {
                userEmails.add(user.getEmail());
            } else {
                logger.info(user.getLoginAccount()+": "+user.getEmail()+" is not a well email format.");
            }
        }
        String emailString = StringUtils.join(userEmails.iterator(), ",");
        logger.info("getReceiversEmail emailString = "+emailString);
        return emailString;
    }

    /**
     * 回傳格式 姓名1 <mailbox1@domain.com>
     * @param user
     * @return 
     */
    public String formatMailUser(EcMember user){
        StringBuilder sb = new StringBuilder();
        if( user!=null ){
            sb.append(user.getEmail());
            if( user.getEmail()!=null && !user.getEmail().isEmpty() ){
                sb.append("<").append(user.getEmail()).append(">");
            }
        }
        return sb.toString();
    }
}
