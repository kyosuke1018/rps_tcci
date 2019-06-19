/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmViewHelper")
@ViewScoped
public class BpmViewHelper {
    
    private transient ResourceBundle rb = ResourceBundle.getBundle("msgBpm",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    public String i18nState(ExecutionStateEnum state) {
        try {
            return (null==state) ? null : rb.getString("state." + state.getName());
        } catch (Exception ex) {
            return state.getName();
        }
    }
    
    public String i18nBallot(String ballot) {
        try {
            return rb.getString("ballot." + ballot);
        } catch (Exception ex) {
            return ballot;
        }
    }
    
}
