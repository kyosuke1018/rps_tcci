/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.util;

import com.tcci.cm.model.global.GlobalConstant;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.Pan
 */
public class ResourceBundleUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResourceBundleUtils.class);
    
    /**
     * for JSF only
     * 取得 ResourceBundle 值
     * (fc 的 ResourceBundleUtil.getDisplayName 找不到設定時，會傳回 ??? not found 字串。作法不好，尤其有預設值時，故 Override。)
     * 
     * @param baseName
     * @param key
     * @return 
     */
    public static String getDisplayName(String baseName, String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale ;
        if (context != null) {
            locale = context.getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        } else {
            locale = Locale.getDefault();
        }

        return getDisplayName(locale, baseName, key);
    }

    public static String getDisplayName(Locale locale, String baseName, String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
        String result = null;
        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            logger.debug("ResourceBundleUtils MissingResourceException ... baseName="+baseName+"; key="+key);
        }
        return result;
    }
    
    public static String getMessage(Locale locale, String key) {
        return getDisplayName(locale, GlobalConstant.DEF_RESOURCE_BUNDLE, key);
    }
}
