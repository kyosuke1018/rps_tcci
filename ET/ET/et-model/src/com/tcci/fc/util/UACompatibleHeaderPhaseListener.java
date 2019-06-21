/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jackson.Lee
 */
public class UACompatibleHeaderPhaseListener implements PhaseListener{
     private static final long serialVersionUID = 1L;

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        final FacesContext facesContext = event.getFacesContext();
        final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.addHeader("X-UA-Compatible", "IE=9");
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }
}
