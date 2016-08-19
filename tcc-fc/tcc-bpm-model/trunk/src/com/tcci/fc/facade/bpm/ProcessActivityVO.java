/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jimmy.Lee
 */
public class ProcessActivityVO {
    private TcActivity activity;
    private List<TcUser> owner;
    private TcWorkitem workitem;
    
    public ProcessActivityVO(TcActivity activity) {
        this.activity = activity;
    }
    
    // helper
    public ExecutionStateEnum getExecutionstate() {
        ExecutionStateEnum state = (workitem != null) ? workitem.getExecutionstate() : activity.getExecutionstate();
        return (ExecutionStateEnum.NOT_START==state) ? null :
               (ActivityTypeEnum.TASK==activity.getActivitytype() && (null==owner || owner.isEmpty())) ? null :
               state;
    }
    
    public Date getStarttimestamp() {
        ExecutionStateEnum state = (workitem != null) ? workitem.getExecutionstate() : activity.getExecutionstate();
        return (ExecutionStateEnum.NOT_START==state) ? null :
               (ActivityTypeEnum.TASK==activity.getActivitytype() && (null==owner || owner.isEmpty())) ? null :
               (workitem != null) ? workitem.getStarttimestamp() : activity.getStarttimestamp();
    }

    public Date getEndtimestamp() {
        return (workitem != null) ? workitem.getEndtimestamp(): activity.getEndtimestamp();
    }

    // getter, setter
    public TcActivity getActivity() {
        return activity;
    }

    public void setActivity(TcActivity activity) {
        this.activity = activity;
    }

    public List<TcUser> getOwner() {
        return owner;
    }

    public void setOwner(List<TcUser> owner) {
        this.owner = owner;
    }

    public TcWorkitem getWorkitem() {
        return workitem;
    }

    public void setWorkitem(TcWorkitem workitem) {
        this.workitem = workitem;
    }
}
