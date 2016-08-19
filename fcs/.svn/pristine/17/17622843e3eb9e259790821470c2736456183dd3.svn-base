/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.converter;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.facade.FcCompanyFacade;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

/**
 *
 * @author gilbert
 */
@FacesConverter("fcCompanyConverter")
public class FcCompanyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                InitialContext ctx = new InitialContext();
                String apCode = "fcs";
                FcCompanyFacade facade = (FcCompanyFacade)ctx.lookup("java:global/"+apCode+"/FcCompanyFacade");            
                return facade.find(Long.valueOf(value));
            } catch (Exception e) { 
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "FcCompanyConverter : Conversion Error", "Not a valid Classification"));  
            }

        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof FcCompany?((FcCompany)value).getId().toString():"";
    }
    
}
