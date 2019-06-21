/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.cm.controller.global.TcSessionController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.bpm.TcReassignConf;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.TcReassignConfFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmReassignConf")
@ViewScoped
public class BpmReassignConf {

    private List<TcReassignConf> confs;
    private List<TcReassignConf> newownerConfs;
    private boolean bpmAdmin; 
    private TcUser owner;
    private TcUser newowner;
    private Date starttime;
    private Date endtime;
    private String comments;
    private String processname;
    private boolean enable;

    @ManagedProperty(value = "#{tcSessionController}")
    private TcSessionController userSession;

    @EJB
    private TcReassignConfFacade reassignConfFacade;
    @EJB
    private TcUserFacade userFacade;

    private TcReassignConf editItem;
    private List<TcUser> users;
    private transient ResourceBundle msgBpm = ResourceBundle.getBundle("msgBpm",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    @PostConstruct
    private void init() {
        bpmAdmin = "/bpmadmin/reassignConf.xhtml".equals(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        if (bpmAdmin) {
            confs = reassignConfFacade.findAll();
        } else {
            confs = reassignConfFacade.findByOwner(userSession.getLoginTcUser());
            newownerConfs = reassignConfFacade.findByNewowner(userSession.getLoginTcUser());
        }
        List<TcUser> allUsers = userFacade.findAll();
        users = new ArrayList<>();
        for (TcUser user : allUsers) { // ignore administrator, web.restful, ...
            if (null != user.getCname() && null != user.getEmail()) {
                users.add(user);
            }
        }
        Collections.sort(users, new Comparator<TcUser>() {
            @Override
            public int compare(TcUser o1, TcUser o2) {
                return o1.getCname().compareTo(o2.getCname());
            }
        });
    }
    
    public void edit(TcReassignConf row) {
        editItem = row;
        if (null == row) {
            owner = userSession.getLoginTcUser();
            newowner = null;
            starttime = null;
            endtime = null;
            comments = null;
            processname = null;
            enable = true;
        } else {
            owner = editItem.getOwner();
            newowner = editItem.getNewowner();
            starttime = editItem.getStarttime();
            endtime = editItem.getEndtime();
            comments = editItem.getComments();
            processname = editItem.getProcessname();
            enable = editItem.isEnable();
        }
    }
    
    public void remove(TcReassignConf row) {
        reassignConfFacade.remove(row);
        JsfUtils.addSuccessMessage(msgBpm.getString("msg.removesuccess")); // 資料已刪除!
        if (bpmAdmin) {
            confs = reassignConfFacade.findAll();
        } else {
            confs = reassignConfFacade.findByOwner(userSession.getLoginTcUser());
        }
    }
    
    public void save() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.addCallbackParam("success", false);
        if (owner == null) {
            JsfUtils.addErrorMessage(msgBpm.getString("err.approvermustexist")); // 簽核人不得為空白!
            return;
        }
        if (owner.equals(newowner)) {
            JsfUtils.addErrorMessage(msgBpm.getString("err.approveragentequal")); // 簽核人與代理人不得相同!
            return;
        }
        if (starttime != null && endtime != null && starttime.compareTo(endtime)>=0) {
            JsfUtils.addErrorMessage(msgBpm.getString("err.time")); // 結束時間必需晚於開始時間!
            return;
        }
        try {
            if (null == editItem) {
                editItem = new TcReassignConf();
            }
            editItem.setOwner(owner);
            editItem.setNewowner(newowner);
            editItem.setStarttime(starttime);
            editItem.setEndtime(endtime);
            editItem.setComments(comments);
            editItem.setProcessname(processname);
            editItem.setEnable(enable);
            editItem.setModifytime(new Date());
            reassignConfFacade.save(editItem);
            JsfUtils.addSuccessMessage(msgBpm.getString("msg.savesuccess")); // 資料已儲存!
            if (bpmAdmin) {
                confs = reassignConfFacade.findAll();
            } else {
                confs = reassignConfFacade.findByOwner(userSession.getLoginTcUser());
            }
            rc.addCallbackParam("success", true);
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    public List<TcUser> completeUser(String query) {
        List<TcUser> result = new ArrayList<>();
        String tmp = StringUtils.trimToNull(query);
        int count = 0;
        for (TcUser u : users) {
            if (null == tmp
                    || StringUtils.containsIgnoreCase(u.getLoginAccount(), tmp)
                    || StringUtils.containsIgnoreCase(u.getCname(), tmp)) {
                result.add(u);
                count++;
                if (count >= 15) {
                    break;
                }
            }
        }
        return result;
    }

    // getter, setter
    public List<TcReassignConf> getConfs() {
        return confs;
    }

    public void setConfs(List<TcReassignConf> confs) {
        this.confs = confs;
    }

    public List<TcReassignConf> getNewownerConfs() {
        return newownerConfs;
    }

    public void setNewownerConfs(List<TcReassignConf> newownerConfs) {
        this.newownerConfs = newownerConfs;
    }

    public boolean isBpmAdmin() {
        return bpmAdmin;
    }

    public void setBpmAdmin(boolean bpmAdmin) {
        this.bpmAdmin = bpmAdmin;
    }

    public TcUser getOwner() {
        return owner;
    }

    public void setOwner(TcUser owner) {
        this.owner = owner;
    }

    public TcUser getNewowner() {
        return newowner;
    }

    public void setNewowner(TcUser newowner) {
        this.newowner = newowner;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public TcSessionController getUserSession() {
        return userSession;
    }

    public void setUserSession(TcSessionController userSession) {
        this.userSession = userSession;
    }

}
