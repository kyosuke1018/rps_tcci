package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.ar.SkProductCategory;
import com.tcci.sksp.facade.SkProductCategoryFacade;
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
 * @author Neo.Fu
 */
@FacesConverter("categoryConverter")
public class SkProductCategoryConverter implements Converter {
    private Logger logger = LoggerFactory.getLogger(SkProductCategoryConverter.class);
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        logger.debug("submittedValue={}",submittedValue);
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                InitialContext ctx = new InitialContext();
                SkProductCategoryFacade ejbFacade = (SkProductCategoryFacade) ctx.lookup("java:global/SKSalesPortal/SkProductCategoryFacade");
                return ejbFacade.find(Long.parseLong(submittedValue));
            } catch (Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Conversion Error", "Not a valid customer"));
            }
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        logger.debug("value={}", value);
        if (value == null || value.equals("")) {
            return "";
        } else {
            return ((SkProductCategory) value).getId().toString();
        }
    }
}
