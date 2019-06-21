/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.cm.controller.global.TcSessionController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.bpm.ProcessFilter;
import com.tcci.fc.facade.org.TcUserFacade;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name="bpmProcessList")
@ViewScoped
public class BpmProcessList {
    
    private ProcessFilter filter = new ProcessFilter();
    private String creator;
    private List<TcProcess> processes;
    private String terminateReason = "管理員取消流程";
    
    @ManagedProperty(value = "#{tcSessionController}")
    private TcSessionController userSession;
    
    @Inject
    private IBPMEngine bpmEngine;
    @EJB
    private TcUserFacade userFacade;
    
    private transient ResourceBundle rb = ResourceBundle.getBundle("msgBpm",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    @PostConstruct
    private void init() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        filter.setDateStart(cal.getTime());
        query();
    }

    // action
    public void query() {
        String account = StringUtils.trimToNull(creator);
        if (null==account) {
            filter.setCreator(null); // 所有人
        } else {
            TcUser user = userFacade.findUserByLoginAccount(account.toLowerCase());
            if (null == user) {
                processes = null;
                JsfUtils.addErrorMessage("無此使用者!");
                return;
            }
            filter.setCreator(user);
        }
        processes = bpmEngine.findProcess(filter);
    }
    
    public void terminateProcess(TcProcess row) {
        try {
            bpmEngine.terminateProcess(row, terminateReason, userSession.getLoginTcUser());
            JsfUtils.addSuccessMessage("流程(" + row.getId() + ")已停止!");
            query();
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex, "terminateProcess exception!");
        }
    }
    
    // getter, setter
    public ProcessFilter getFilter() {
        return filter;
    }

    public void setFilter(ProcessFilter filter) {
        this.filter = filter;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<TcProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<TcProcess> processes) {
        this.processes = processes;
    }

    public String getTerminateReason() {
        return terminateReason;
    }

    public void setTerminateReason(String terminateReason) {
        this.terminateReason = terminateReason;
    }

    public TcSessionController getUserSession() {
        return userSession;
    }

    public void setUserSession(TcSessionController userSession) {
        this.userSession = userSession;
    }

}
