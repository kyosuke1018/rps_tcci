/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.facade.admin.PermissionFacade;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.NetworkUtils;
import com.tcci.cm.util.NotificationUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.DateUtils;
import com.tcci.et.facade.rs.utils.RestDataFacade;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public abstract class AbstractWebREST {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected @EJB RestDataFacade restDataFacade;
    protected @EJB SysResourcesFacade sys;
    protected @EJB UserFacade userFacade;
    protected @EJB PermissionFacade permissionFacade;
       
    protected void processUnknowException(TcUser operator, String methodName, Exception e){
        UUID uuid = UUID.randomUUID();
        String constraintViolations = "";
        if(e instanceof EJBException){
            constraintViolations = ExceptionHandlerUtils.printConstraintViolationException((EJBException)e);
        }
        processUnknowException(operator, methodName, e, uuid.toString(), constraintViolations);
    }
    protected void processUnknowException(TcUser operator, String methodName, Exception e, String errorCode, String constraintViolations){
        logger.error("RESRful processUnknowException ["+methodName+"] ("+errorCode+") Exception:\n", e);
        String hostname = NetworkUtils.getHostIP(); // WebUtils.getHostName();
        String datetime = DateUtils.format(new Date());
        //String errmsg = "系統發生錯誤，請提供此畫面 Email 給系統管理員，並告知執行操作! (" + hostname + ") - " + datetime;

        NotificationUtils sender = new NotificationUtils();
        sender.notifyOnException(sys.getNotifyAdmins(), operator, methodName, e, errorCode, constraintViolations);
        //JsfUtils.addErrorMessage(errmsg);
    }
}
