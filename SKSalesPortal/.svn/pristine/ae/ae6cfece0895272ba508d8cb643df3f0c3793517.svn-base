package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.facade.SkCustomerFacade;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

/**
 *
 * @author Lynn.Huang
 */
@FacesConverter("skCustomerConverter")
public class SkCustomerConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) { 
            return null;
        } else {
            try {
                InitialContext ctx = new InitialContext();
                SkCustomerFacade skCustomerFacade = (SkCustomerFacade)ctx.lookup("java:global/SKSalesPortal/SkCustomerFacade");
                return skCustomerFacade.findByCode(submittedValue);
            } catch (Exception e) { 
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Conversion Error", "Not a valid customer"));  
            }
        }        
    }    
    
    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return ((SkCustomer) value).getCode();
        }
    }
}
