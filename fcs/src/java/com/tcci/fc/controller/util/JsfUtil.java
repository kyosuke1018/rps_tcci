package com.tcci.fc.controller.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsfUtil {
    
    static final Logger logger = LoggerFactory.getLogger(JsfUtil.class);
    private static final int MAX_TREE_LEVEL = 10;
    
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

    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static String getCookieValue(String cookieName) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        Cookie cookies[] = request.getCookies();
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
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();
            Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            cookie.setPath(path);
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException ex) {
        }
    }
    
    public static boolean isDummySelectItem(UIComponent component, String value) {
        for (UIComponent children : component.getChildren()) {
            if (children instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) children;
                if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * 重設 DataTable 狀態
     * @return 
     */
    public static boolean resetDataTable(String dataTableID){
        try {
            // 移除 datatable 目前排序效果
            FacesContext facesContext = FacesContext.getCurrentInstance();
            DataTable dataTable = (DataTable) facesContext.getViewRoot().findComponent(dataTableID);
            dataTable.setValueExpression("sortBy", null);
            dataTable.setFirst(0);
            
            // 移除 datatable 目前 filter 效果
            //dataTable.setFilters(null);
            dataTable.reset();

            return true;
        }catch(Exception e) {
            logger.error("resetDataTable exception :\n"+e);
//            e.printStackTrace();
            return false;
        }
   }    
    
    /**
     * 樹狀圖展開合併
     * @param n
     * @param option
     * @param opLevelLimit 向下處理階層 (0:不動作; 1:一層; ...)
     */
    public static void collapsingORExpandingTree(TreeNode n, boolean option, int opLevelLimit) {
        if( n==null || n.getChildren()==null ){
            return;
        }
        if( opLevelLimit<=0 ){
            return;
        }
        if( !n.getChildren().isEmpty() ) {
            for(TreeNode s: n.getChildren()) {
                collapsingORExpandingTree(s, option, opLevelLimit--);
            }
            n.setExpanded(option);
        }
        n.setSelected(false);
    }
    public static void collapsingORExpandingTree(TreeNode n, boolean option) {
        collapsingORExpandingTree(n, option, MAX_TREE_LEVEL);
    }
}
