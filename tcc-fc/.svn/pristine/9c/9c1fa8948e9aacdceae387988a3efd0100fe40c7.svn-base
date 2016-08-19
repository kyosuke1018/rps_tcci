/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gilbert.Lin
 */
public class TCCResourceBundle {

    private static ResourceBundle bundle;
    public static String getValue(String bundleName,String key) {
        if (bundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, bundleName);
        }
        String result = null;
        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }
    public static String getValue(String key) {
       return getValue("msg", key);
    }
}
