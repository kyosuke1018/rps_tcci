/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.swls;

import com.tcci.sksp.entity.swls.ComplainUpload;
import com.tcci.sksp.facade.swls.ComplainUploadFacade;
import com.tcci.sksp.facade.swls.QueryFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class ComplainQuery {
    
    private List<ComplainUpload> complains;
    private QueryFilter filter = new QueryFilter();
    private Date startDate;
    private Date endDate;
    
    @EJB
    ComplainUploadFacade complainFacade;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    @PostConstruct
    private void init() {
        Calendar cal = Calendar.getInstance();
        endDate = cal.getTime();
        cal.add(Calendar.YEAR, -1);
        startDate = cal.getTime();
    }

    // action
    public void query() {
        filter.setStartDate(startDate==null ? null : sdf.format(startDate));
        filter.setEndDate(endDate==null ? null : sdf.format(endDate));
        complains = complainFacade.find(filter);
    }
    
    // getter, setter
    public List<ComplainUpload> getComplains() {
        return complains;
    }

    public void setComplains(List<ComplainUpload> complains) {
        this.complains = complains;
    }

    public QueryFilter getFilter() {
        return filter;
    }

    public void setFilter(QueryFilter filter) {
        this.filter = filter;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
