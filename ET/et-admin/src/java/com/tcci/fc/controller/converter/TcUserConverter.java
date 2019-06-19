/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.converter;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ApplicationScoped
public class TcUserConverter implements Converter {

    @EJB
    private TcUserFacade tcUserFacade;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.length() > 0) {
            Long id = Long.parseLong(value);
            return tcUserFacade.find(id);
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((TcUser) value).getId());
        } else {
            return null;
        }
    }

}
