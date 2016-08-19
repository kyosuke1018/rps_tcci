package com.tcci.fc.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

public class ResourceBundleUtil {

    //Resource Bundle
    //Example: 
    //Package Name: com.tcci.enum, Class name: TypeEnum
    //baseName=com.tcci.enum.TypeEnum
    public static String getDisplayName(String baseName, String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = null;
        if (context != null) {
            locale = context.getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();
        }
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
        String result = null;
        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }

    //System Message of Resource Bundle
    public static String getString(String rbVar, String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, rbVar);
        String result = null;
        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }

    public static String getString(String key) {
        return getString("msg", key);
    }
}
