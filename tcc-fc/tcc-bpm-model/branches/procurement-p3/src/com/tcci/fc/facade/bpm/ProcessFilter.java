/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;

/**
 *
 * @author Jimmy.Lee
 */
public class ProcessFilter {
    private Long processid;
    private ExecutionStateEnum executionstate;
    private String primaryobjectclassname;
    private Long primaryobjectid;
    private TcUser creator;
    private Date dateStart;
    private Date dateEnd;

    // getter, setter
    public Long getProcessid() {
        return processid;
    }

    public void setProcessid(Long processid) {
        this.processid = processid;
    }

    public ExecutionStateEnum getExecutionstate() {
        return executionstate;
    }

    public void setExecutionstate(ExecutionStateEnum executionstate) {
        this.executionstate = executionstate;
    }

    public String getPrimaryobjectclassname() {
        return primaryobjectclassname;
    }

    public void setPrimaryobjectclassname(String primaryobjectclassname) {
        this.primaryobjectclassname = primaryobjectclassname;
    }

    public Long getPrimaryobjectid() {
        return primaryobjectid;
    }

    public void setPrimaryobjectid(Long primaryobjectid) {
        this.primaryobjectid = primaryobjectid;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

}
