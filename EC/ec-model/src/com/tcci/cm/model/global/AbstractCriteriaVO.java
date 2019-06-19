/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.global;


import com.tcci.cm.model.interfaces.ILoggable;
import com.tcci.cm.model.interfaces.IQueryCriteria;
import com.tcci.cm.util.QueryCondUtils;
import java.io.Serializable;

/**
 *
 * @author Peter
 */
public class AbstractCriteriaVO implements IQueryCriteria, ILoggable, Serializable {
    protected boolean countOnly;
    protected Integer setMaxResultsSize;
    protected Integer firstResult;
    protected Integer maxResults;
    protected String orderBy;

    public void setOrderBy(String sortField, String sortOrder) {
        StringBuilder sb = new StringBuilder();
        if( sortField!=null && !sortField.trim().isEmpty() ){
            sb.append(sortField);
            if( sortOrder!=null 
             && !sortOrder.trim().isEmpty() 
             && (sortOrder.trim().equalsIgnoreCase("ASC") || sortOrder.trim().equalsIgnoreCase("DESC"))
            ){
                sb.append(" ").append(sortOrder);
            }
            
            this.orderBy = sb.toString();
        }else{
            this.orderBy = null;
        }
    }

    public boolean isCountOnly() {
        return countOnly;
    }

    public void setCountOnly(boolean countOnly) {
        this.countOnly = countOnly;
    }

    public Integer getSetMaxResultsSize() {
        return setMaxResultsSize;
    }

    public void setSetMaxResultsSize(Integer setMaxResultsSize) {
        this.setMaxResultsSize = setMaxResultsSize;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public void reset(){
        clear();
    }
    
    @Override
    public void clear(){
    }
    
    @Override
    public String show(){
        return this.toString();
    }
    
    @Override
    public String toLogString(){
        // return ExtBeanUtils.dump(this);
        return QueryCondUtils.dumpCondVO(this);
    }
}
