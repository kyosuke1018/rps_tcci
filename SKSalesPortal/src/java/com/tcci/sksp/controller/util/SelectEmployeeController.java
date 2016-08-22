/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.facade.SkOrgFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputHidden;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean(name = "selectEmployeeController")
@ViewScoped
public class SelectEmployeeController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    protected final static Logger logger = LoggerFactory.getLogger(SelectEmployeeController.class);
    private static final String DONE = "done";
    private TcUserFilter filter = new TcUserFilter();
    private List<TcUser> users = new ArrayList<TcUser>();
    private TcUser[] selectedUsers;
    private TcUserDataModel tcUserDataModel;
    private String selectedUser;
    private String inputUser;
    private String inputUserCname;
    private String empId;
    private String cname;
    private HtmlInputHidden initModifierHidden = new HtmlInputHidden();
    private HtmlInputHidden initUserBySessionUserHidden = new HtmlInputHidden();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private TcUserFacade userFacade;
    @EJB
    private SkOrgFacade orgFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public List<TcUser> getUsers() {
        return users;
    }

    public void setUsers(List<TcUser> users) {
        this.users = users;
    }

    public TcUserFilter getFilter() {
        return filter;
    }

    public void setFilter(TcUserFilter filter) {
        this.filter = filter;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public TcUserDataModel getTcUserDataModel() {
        return tcUserDataModel;
    }

    public void setTcUserDataModel(TcUserDataModel tcUserDataModel) {
        this.tcUserDataModel = tcUserDataModel;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public HtmlInputHidden getInitModifierHidden() {
        logger.debug("getInitModifierHidden()");
        String done = DONE;
        if (!done.equals((String) initModifierHidden.getValue())) {
            String modifier = FacesUtil.getRequestParameter("modifier");
            TcUser user = userFacade.findUserByEmpId(modifier);
            this.selectedUser = FacesUtil.getRequestParameter("modifier");
            if (user != null) {
                this.cname = user.getCname();
            }
            initModifierHidden.setValue(done);
        }
        return initModifierHidden;
    }

    public void setInitModifierHidden(HtmlInputHidden initModifierHidden) {
        this.initModifierHidden = initModifierHidden;
    }

    public HtmlInputHidden getInitUserBySessionUserHidden() {
        logger.debug("getInitUserBySessionUserHidden()");
        String done = DONE;
        if (!done.equals((String) initUserBySessionUserHidden.getValue())) {
            TcUser user = sessionController.getUser();
            logger.debug("user={}", user);
            if (user != null) {
                logger.debug("user.getEmpId()={}", user.getEmpId());
                this.selectedUser = user.getEmpId();
//TODO need check why advancePayment.xhtml & uploadRemit.xhtml didn't show usercname.
//                logger.debug("user.getCname()={}",user.getCname());
//                this.cname = user.getCname();
            }
            if (inputUser != null) {
                this.selectedUser = inputUser;
                this.cname=inputUserCname;
            }
            initUserBySessionUserHidden.setValue(done);
        }
        return initUserBySessionUserHidden;
    }

    public void setInitUserBySessionUserHidden(HtmlInputHidden initUserBySessionUserHidden) {
        this.initUserBySessionUserHidden = initUserBySessionUserHidden;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public String getInputUserCname() {
        return inputUserCname;
    }

    public void setInputUserCname(String inputUserCname) {
        this.inputUserCname = inputUserCname;
    }

    //</editor-fold>
    public void init() {
        logger.debug("init()");
        filter.reset();
        selectedUsers = null;
        selectedUser = null;
        tcUserDataModel = new TcUserDataModel(new ArrayList<TcUser>());
    }

    public void query() {
        logger.debug("query()");
        users = orgFacade.findByCriteria(filter);
        tcUserDataModel = new TcUserDataModel(users);
        logger.debug("users={}", users.size());
    }

    public List<String> completeMethod(String input) {
        logger.debug("completeMethod(), input={}", input);
        empId = input;
        logger.debug("empId={}", empId);
        List<String> result = new ArrayList<String>();
        HashMap<TcUser, TcUser> uniqueUsers = new HashMap<TcUser, TcUser>();
        TcUserFilter filter = new TcUserFilter();
        filter.setCname(input);
        List<TcUser> usersByName = orgFacade.findByCriteria(filter);
        logger.debug("usersByName.size()={}", usersByName.size());
        for (TcUser userByName : usersByName) {
            uniqueUsers.put(userByName, userByName);
        }
        filter.reset();
        filter.setEmpId(input);
        List<TcUser> usersByEmpId = orgFacade.findByCriteria(filter);
        logger.debug("usersByEmpId.size()={}", usersByEmpId.size());
        for (TcUser userByEmpId : usersByEmpId) {
            uniqueUsers.put(userByEmpId, userByEmpId);
        }
        for (TcUser uniqueUser : uniqueUsers.values()) {
            result.add(uniqueUser.getEmpId());
        }
        return result;
    }

    public void handleSelect(SelectEvent event) {
        logger.debug("handleSelect(), selection={}", event.getObject().toString());
        String empId = (String) event.getObject();
        setCname(userFacade.findUserByEmpId(empId).getCname());
        setSelectedUser(empId);
    }

    public void onRowSelect(SelectEvent event) {
        logger.debug("onRowSelect(), selection={}", event.getObject().toString());
        TcUser user = (TcUser) event.getObject();
        setCname(user.getCname());
        setSelectedUser(user.getEmpId());
    }

    public String getUserCname(String empId) {
        return userFacade.findUserByEmpId(empId).getCname();
    }
}
