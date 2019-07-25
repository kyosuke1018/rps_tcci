/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpmtemplate.ActivityVO;
import com.tcci.fc.facade.bpmtemplate.RouteVO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class CreateProcessParam {

    private TcUser creator;
    private String processname;
    private List<ActivityVO> activityVOes = new ArrayList<ActivityVO>();
    private List<RouteVO> routeVOes = new ArrayList<RouteVO>();
    private Map<String, Object> roleUsers = new HashMap<String, Object>();
    private Persistable primaryObj;
    private String subject;
    private boolean startProcess = true;

    // c'tor
    public CreateProcessParam() {
    }

    public CreateProcessParam(TcUser creator, String processname, Persistable primaryObj) {
        this.creator = creator;
        this.processname = processname;
        this.primaryObj = primaryObj;
    }

    public CreateProcessParam addActivityStart() {
        ActivityVO actVO = new ActivityVO();
        actVO.setActivityname("START");
        actVO.setActivitytype(ActivityTypeEnum.START.name());
        activityVOes.add(actVO);
        return this;
    }

    public CreateProcessParam addActivityEnd() {
        ActivityVO actVO = new ActivityVO();
        actVO.setActivityname("END");
        actVO.setActivitytype(ActivityTypeEnum.END.name());
        activityVOes.add(actVO);
        return this;
    }

    public CreateProcessParam addActivityTask(String actName, String roleName, long options) {
        ActivityVO actVO = new ActivityVO();
        actVO.setActivityname(actName);
        actVO.setActivitytype(ActivityTypeEnum.TASK.name());
        actVO.setRolename(roleName);
        actVO.setOptions(options);
        activityVOes.add(actVO);
        return this;
    }

    public CreateProcessParam addActivityTask(String actName, String roleName, long options, TcUser tcUser) {
        ActivityVO actVO = new ActivityVO();
        actVO.setActivityname(actName);
        actVO.setActivitytype(ActivityTypeEnum.TASK.name());
        actVO.setRolename(roleName);
        actVO.setOptions(options);
        activityVOes.add(actVO);
        addRoleTcUser(roleName, tcUser);
        return this;
    }

    public CreateProcessParam addActivityTask(String actName, String roleName, long options, Collection<TcUser> tcUsers) {
        ActivityVO actVO = new ActivityVO();
        actVO.setActivityname(actName);
        actVO.setActivitytype(ActivityTypeEnum.TASK.name());
        actVO.setRolename(roleName);
        actVO.setOptions(options);
        activityVOes.add(actVO);
        addRoleTcUsers(roleName, tcUsers);
        return this;
    }

    public CreateProcessParam addActivityTask(String actName, String roleName, long options, TcGroup tcGroup) {
        ActivityVO actVO = new ActivityVO();
        actVO.setActivityname(actName);
        actVO.setActivitytype(ActivityTypeEnum.TASK.name());
        actVO.setRolename(roleName);
        actVO.setOptions(options);
        activityVOes.add(actVO);
        addRoleTcGroup(roleName, tcGroup);
        return this;
    }

    public CreateProcessParam addActivity(ActivityVO actVO) {
        activityVOes.add(actVO);
        return this;
    }
    
    public CreateProcessParam addRoute(String fromAct, String toAct, String routeName) {
        RouteVO routeVO = new RouteVO();
        routeVO.setFromactivity(fromAct);
        routeVO.setToactivity(toAct);
        routeVO.setRoutename(routeName);
        routeVOes.add(routeVO);
        return this;
    }

    public CreateProcessParam addRoute(RouteVO routeVO) {
        routeVOes.add(routeVO);
        return this;
    }

    public CreateProcessParam addRoleTcUser(String role, TcUser tcUser) {
        if (tcUser != null) {
            roleUsers.put(role, tcUser);
        }
        return this;
    }

    public CreateProcessParam addRoleTcUsers(String role, Collection<TcUser> tcUsers) {
        if (tcUsers != null) {
            roleUsers.put(role, tcUsers);
        }
        return this;
    }

    public CreateProcessParam addRoleTcGroup(String role, TcGroup tcGroup) {
        if (tcGroup != null) {
            roleUsers.put(role, tcGroup);
        }
        return this;
    }

    public CreateProcessParam buildSimpleRoutes() {
        routeVOes = new ArrayList<RouteVO>();
        String fromAct = null;
        for (ActivityVO actVO : activityVOes) {
            if (null == fromAct) {
                fromAct = actVO.getActivityname();
            } else {
                String toAct = actVO.getActivityname();
                String routeName = "START".equals(fromAct) ? "go" : "approve";
                addRoute(fromAct, toAct, routeName);
                fromAct = toAct;
            }
        }
        return this;
    }

    // getter, setter
    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public List<ActivityVO> getActivityVOes() {
        return activityVOes;
    }

    public void setActivityVOes(List<ActivityVO> activityVOes) {
        this.activityVOes = activityVOes;
    }

    public List<RouteVO> getRouteVOes() {
        return routeVOes;
    }

    public void setRouteVOes(List<RouteVO> routeVOes) {
        this.routeVOes = routeVOes;
    }

    public Map<String, Object> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(Map<String, Object> roleUsers) {
        this.roleUsers = roleUsers;
    }

    public Persistable getPrimaryObj() {
        return primaryObj;
    }

    public void setPrimaryObj(Persistable primaryObj) {
        this.primaryObj = primaryObj;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isStartProcess() {
        return startProcess;
    }

    public void setStartProcess(boolean startProcess) {
        this.startProcess = startProcess;
    }

}
