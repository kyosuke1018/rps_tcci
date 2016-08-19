package com.tcci.solr.server.util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsfUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsfUtils.class);

    public static String getViewId(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getViewRoot().getViewId();
    }
    
    public static ResourceBundle getResourceBundle() {
        return getResourceBundle("messages");
    }

    public static ResourceBundle getResourceBundle(String baseName) {
        return ResourceBundle.getBundle(baseName, getRequestLocale());
    }

    /**
     * Success Callback Response 
     * @return 
     */
    public static RequestContext buildSuccessCallback(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("success", true);
        return context;
    }
    
    /**
     * Fail Callback Response 
     * @return 
     */
    public static RequestContext buildErrorCallback(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("success", false);
        return context;
    }
    
    /**
     * 回傳local
     *
     * @return Locale
     */
    public static Locale getRequestLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
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

    public static void addErrorMessage(String msg, String[] args) {
        String formattedMsg = formatMessage(msg, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, formattedMsg, formattedMsg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addSuccessMessage(String msg, String[] args) {
        String formattedMsg = formatMessage(msg, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, formattedMsg, formattedMsg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
        FacesContext.getCurrentInstance().addMessage(msg, facesMsg);
    }

    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addWarningMessage(String msg, String[] args) {
        String formattedMsg = formatMessage(msg, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, formattedMsg, formattedMsg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtils.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    /**
     * 清除指定Component內容(如message)
     *
     * @param idComponent
     */
    public static void clearMessageForComponent(final String idComponent) {
        if (idComponent != null) {
            Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages(idComponent);
            while (it.hasNext()) {
                ((FacesMessage) it.next()).setDetail("");
            }
        }
    }

    private static String formatMessage(String msg, String[] args) {
        MessageFormat format = new MessageFormat(msg);
        return format.format(args);
    }

    public static String getRealPath() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getRealPath("/");
    }

    public static void toErrorPage(String msg) throws IOException {
        toErrorPage(msg, null);
    }

    public static void toErrorPage(String bundleKey, String[] args) throws IOException {
        final ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

        extContext.redirect(extContext.getRequestContextPath() + "/faces/error_page.xhtml?bundleKey=" + bundleKey);
    }

    /**
     * 
     * @param url (/faces/xxx)
     * @throws IOException 
     */
    public static void redirect(String url) throws IOException{
        final ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        
        extContext.redirect(extContext.getRequestContextPath() + url);
    }
    
    /**
     * 取得Server端HostName
     *
     * @return
     */
    public static String getHostName() {
        java.net.InetAddress server;
        String serverName = "";
        try {
            server = java.net.InetAddress.getLocalHost();
            serverName = server.getCanonicalHostName();
        } catch (UnknownHostException ex) {
            logger.error("getHostName", ex);
        }
        return serverName;
    }

    /**
     * 重設 DataTable 狀態
     *
     * @param dataTableID
     * @return
     */
    public static boolean resetDataTable(String dataTableID) {
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
        } catch (Exception e) {
            logger.error("resetDataTable exception :\n");
            return false;
        }
    }
    
    /**
     * Go To UI Position by ID
     * @param UIID
     */
    public static void gotoUIById(String UIID) {
        RequestContext.getCurrentInstance().scrollTo(UIID);
    }
    
    // BEGIN: for MegaMenu
    public static Submenu newSubmenu(String id, String label, String icon, String styleClass){
        Submenu subMenu = new Submenu();
        subMenu.setLabel(label);
        subMenu.setId(id);
        if( icon!=null && !icon.isEmpty() ){ subMenu.setIcon(icon); }
        if( styleClass!=null && !styleClass.isEmpty() ){ subMenu.setStyleClass(styleClass); }
        
        return subMenu;
    }

    public static MenuItem newMenuItem(String id, String label, String url){
        MenuItem item = new MenuItem();
        item.setValue(label);
        item.setUrl(url);
        item.setId(id);
        item.setStyleClass("myMenuItem");// 注意：UI可利用此class名稱，透過jQuery插入切換功能前的檢查

        return item;
    }
    // END: for MegaMenu
    
    /**
     * 樹狀圖展開合併
     * @param n
     * @param option 
     */
    public static void collapsingORExpandingTree(TreeNode n, boolean option) {
        if( n==null || n.getChildren()==null ){
            return;
        }
        if( n.getChildren().isEmpty() ) {
            n.setSelected(false);
        } else {
            for(TreeNode s: n.getChildren()) {
                collapsingORExpandingTree(s, option);
            }
            n.setExpanded(option);
            n.setSelected(false);
        }
    }    
}