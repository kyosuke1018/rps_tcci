
package com.tcci.fcservice.controller.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

public class JsfUtil {
    
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }
    
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }
    
    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }
    
    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    public static String getCookieValue(String cookieName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Cookie cookies[] = ((HttpServletRequest)facesContext.getExternalContext()
            .getRequest()).getCookies();
        if(cookies == null || cookies.length == 0)
            return null;
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), cookieName)) {
                try {
                    String cookieValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    return cookieValue;
                } catch (UnsupportedEncodingException ex) {
                    return null;
                }
            }
        }
        return null;
    }
    
    public static void saveCookie(String cookieName, String cookieValue, int maxAge) {
        try {
            Cookie cookieLastPath = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            cookieLastPath.setMaxAge(maxAge);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ((HttpServletResponse)facesContext.getExternalContext().getResponse()).addCookie(cookieLastPath);
        } catch (UnsupportedEncodingException ex) {
        }
    }
}