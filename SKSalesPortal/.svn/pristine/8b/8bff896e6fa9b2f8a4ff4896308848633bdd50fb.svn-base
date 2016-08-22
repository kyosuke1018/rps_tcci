package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@FacesConverter("tcUserConverter")
public class TcUserConverter implements Converter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        logger.debug("getAsObject()");
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                InitialContext ctx = new InitialContext();
                TcUserFacade tcUserFacade = (TcUserFacade) ctx.lookup("java:global/SKSalesPortal/TcUserFacade");
                return tcUserFacade.find(Long.parseLong(submittedValue));
            } catch (Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Conversion Error", "Not a valid customer"));
            }
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        logger.debug("getAsString()");
        if (value == null || value.equals("")) {
            return "";
        } else {
            return ((TcUser) value).getId().toString();
        }
    }
}
