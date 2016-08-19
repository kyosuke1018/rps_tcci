/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.facade.bpmtemplate.TcProcessTemplateFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmTemplateList")
@ViewScoped
public class BpmTemplateList {
    private List<TcProcesstemplate> processTemplates;
    
    @EJB
    private TcProcessTemplateFacade templateFacade;
    
    @PostConstruct
    private void init() {
        processTemplates = templateFacade.findAll();
    }

    // getter, setter
    public List<TcProcesstemplate> getProcessTemplates() {
        return processTemplates;
    }

    public void setProcessTemplates(List<TcProcesstemplate> processTemplates) {
        this.processTemplates = processTemplates;
    }
    
}
