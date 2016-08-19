/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.util;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jimmy.Lee
 */
public class UACompatibleHeaderPhaseListener implements PhaseListener {

    private static String X_UA_COMPATIBLE = null;
    
    static {
        FacesContext fc = FacesContext.getCurrentInstance();
        X_UA_COMPATIBLE = fc.getExternalContext().getInitParameter("X_UA_COMPATIBLE");
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        if (X_UA_COMPATIBLE != null) {
            final FacesContext facesContext = event.getFacesContext();
            final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.addHeader("X-UA-Compatible", X_UA_COMPATIBLE);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}
