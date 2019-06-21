/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

import com.tcci.cm.model.global.AbstractCriteriaVO;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Peter
 */
public class UsersCriteriaVO extends AbstractCriteriaVO implements IQueryCriteria, Serializable {
    private String keyword;
    private long group;
    private long orgId;
    private List<Long> orgIds;
    private boolean selectOrg;
    private boolean onlyNoDisabled;
    private String orgName;
    private boolean operator;
    
    @Override
    public void clear(){
        keyword = "";
        group = 0;
        orgId = 0;
        // orgIds = new ArrayList<Long>();
        orgIds = null;
        selectOrg = false;
        onlyNoDisabled = false;
        operator = false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean isOnlyNoDisabled() {
        return onlyNoDisabled;
    }

    public void setOnlyNoDisabled(boolean onlyNoDisabled) {
        this.onlyNoDisabled = onlyNoDisabled;
    }

    public boolean isSelectOrg() {
        return selectOrg;
    }

    public void setSelectOrg(boolean selectOrg) {
        this.selectOrg = selectOrg;
    }

    public long getGroup() {
        return group;
    }

    public void setGroup(long group) {
        this.group = group;
    }
    
    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public boolean isOperator() {
        return operator;
    }

    public void setOperator(boolean operator) {
        this.operator = operator;
    }
    
    public List<Long> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<Long> orgIds) {
        this.orgIds = orgIds;
    }
    //</editor-fold>
}
