package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.facade.SkProductMasterFacade;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

/**
 *
 * @author Neo.Fu
 */
@FacesConverter("productConverter")
public class SkProductMasterConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) { 
            return null;
        } else {
            try {
                InitialContext ctx = new InitialContext();
                SkProductMasterFacade ejbFacade = (SkProductMasterFacade)ctx.lookup("java:global/SKSalesPortal/SkProductMasterFacade");
                return ejbFacade.findByCode(submittedValue);
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
            return ((SkProductMaster) value).getCode();
        }
    }
}
