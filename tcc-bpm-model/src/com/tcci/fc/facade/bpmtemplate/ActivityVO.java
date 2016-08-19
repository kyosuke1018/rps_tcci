/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpmtemplate;

import com.tcci.fc.entity.bpm.TcActivitytemplate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
public class ActivityVO extends ExcelVOBase {
    @NotNull(message = "activityname不能為空")
    @Size(min=1, max = 255, message = "activityname至少1個字,最多255個字")
    private String activityname;
    @NotNull(message = "activitytype不能為空")
    @Size(min=1, max = 255, message = "activitytype至少1個字,最多255個字")
    private String activitytype;
    @Size(min=0, max = 255, message = "rolename最多255個字")
    private String rolename;
    private long options;
    private Integer duration;
    private String expression;

    // c'tor
    public ActivityVO() {
    }
    
    public ActivityVO(TcActivitytemplate act) {
        activityname = act.getActivityname();
        activitytype = act.getActivitytype().value();
        rolename = act.getRolename();
        options = act.getOptions();
        duration = act.getDuration();
        expression = act.getExpression();
    }
    
    // getter, setter
    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public long getOptions() {
        return options;
    }

    public void setOptions(long options) {
        this.options = options;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

}