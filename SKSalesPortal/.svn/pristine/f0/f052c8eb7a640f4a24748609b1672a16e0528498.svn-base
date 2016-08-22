/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.facade.ReSendAchievement;
import com.tcci.sksp.facade.SendAchievementMail;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author carl.lin
 */
@ManagedBean
@ViewScoped
public class reSendAchievementMail {

    private final static Logger logger = LoggerFactory.getLogger(reSendAchievementMail.class);
    @ManagedProperty(value = "#{queryCriteriaController}")
    private QueryCriteriaController queryCriteriaController;
    @EJB
    private ReSendAchievement reSendAchievement;
    @EJB
    private SendAchievementMail sendAchievementMail;
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
     @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;
    
    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }

    @PostConstruct
    private void init() {
        queryCriteriaController.initYearList();
        queryCriteriaController.initMonthList();
    }

    // action
    public void reSendMail() {
        String sapid = queryCriteriaController.getFilter().getSales().getCode();
        String year = queryCriteriaController.getFilter().getYear();
        String month = queryCriteriaController.getFilter().getMonth();
        
        logger.info("{} fired reSendMail by "+sapid , userSession.getUser().getCname());
        if (month.length() == 1) {
            month = "0".concat(month);
        }

        // 年月
        year = year.concat(month);
        try {
            // send single sales mail  and his boss excel only         
//            reSendAchievement.sendMail(sapid, year, "Y");
            sendAchievementMail.sendMail(sapid, year, "Y");
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void reSendMailAll() {
        try {
            // send all mail  excel only
//             reSendAchievement.sendAllMails();
            logger.warn("{} fired reSendMailAll ", userSession.getUser().getCname());
            sendAchievementMail.sendAllMails();
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }
    // getter, setter

    public QueryCriteriaController getQueryCriteriaController() {
        return queryCriteriaController;
    }

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
}
