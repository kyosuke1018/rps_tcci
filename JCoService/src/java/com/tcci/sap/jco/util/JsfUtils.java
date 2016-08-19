package com.tcci.sap.jco.util;

import com.sun.faces.component.visit.FullVisitContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsfUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsfUtils.class);
    private static final int MAX_TREE_LEVEL = 20;

    public static String getViewId(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getViewRoot().getViewId();
    }
    
    public static ResourceBundle getResourceBundle(String baseName) {
        return ResourceBundle.getBundle(baseName, getRequestLocale());
    }
    
    /**
     * 取得 Resource Bundle 文字資訊
     * @param baseName
     * @param key
     * @return 
     */
    public static String getResourceTxt(String baseName, String key){
        return getResourceBundle(baseName).getString(key);
    }
    public static String getResourceTxt(Locale locale, String baseName, String key){
        return ResourceBundle.getBundle(baseName, locale).getString(key);
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
    public static RequestContext buildSuccessCallback(String resMsg){
        RequestContext context  = buildSuccessCallback();
        // resMsg = StringEscapeUtils.escapeHtml(resMsg);
        context.addCallbackParam("msg", resMsg);
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
    public static RequestContext buildErrorCallback(String resMsg){
        RequestContext context  = buildErrorCallback();
        // resMsg = StringEscapeUtils.escapeHtml(resMsg);
        context.addCallbackParam("msg", resMsg);
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
     * 取得 ContextPath
     * @return 
     */
    public static String getContextPath(){
        final ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();

        return extContext.getRequestContextPath();
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
            logger.debug("resetDataTable dataTableID ="+dataTableID);
            // 移除 datatable 目前排序效果
            FacesContext facesContext = FacesContext.getCurrentInstance();
            DataTable dataTable = (DataTable) facesContext.getViewRoot().findComponent(dataTableID);
            if( dataTable!=null ){
                dataTable.setValueExpression("sortBy", null);
                dataTable.setFirst(0);
                
                // 移除 datatable 目前 filter 效果
                //dataTable.setFilters(null);
                dataTable.reset();
            }else{
                logger.error("no DataTable ID ="+dataTableID);
            }
            return true;
        } catch (Exception e) {
            logger.error("resetDataTable exception :\n", e);
            return false;
        }
    }

    /**
     * DataTable 換頁 (指定第一筆)
     * @param dataTableID
     * @param first
     * @return 
     */
    public static boolean changePageDataTable(String dataTableID, int first) {
        try {
            logger.debug("changePageDataTable dataTableID ="+dataTableID);
            
            final DataTable d = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(dataTableID);
            d.setFirst(first);
            return true;
        } catch (Exception e) {
            logger.error("changePageDataTable exception :\n", e);
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
    public static DefaultSubMenu newSubmenu(String id, String label, String icon, String styleClass){
        DefaultSubMenu subMenu = new DefaultSubMenu();
        subMenu.setLabel(label);
        subMenu.setId(id);
        if( icon!=null && !icon.isEmpty() ){ subMenu.setIcon(icon); }
        if( styleClass!=null && !styleClass.isEmpty() ){ subMenu.setStyleClass(styleClass); }
        
        return subMenu;
    }

    /**
     * MenuItem 無 ICON (用於 submenu 內 的MenuItem)
     * @param id
     * @param label
     * @param url
     * @return 
     */
    public static MenuItem newMenuItem(String id, String label, String url){
        return newMenuItem(id, label, url, null, null, null);
    }
    
    /**
     * MenuItem 無 ICON (用於主 menu 內 的MenuItem)
     * @param id
     * @param label
     * @param url
     * @param icon
     * @param target
     * @param style
     * @return 
     */
    public static MenuItem newMenuItem(String id, String label, String url, String icon, String target, String style){
        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue(label);
        item.setUrl(url);
        item.setId(id);
        if( icon!=null ){
            item.setIcon(icon);
        }
        if( target!=null ){
            item.setTarget(target);
        }
        if( style!=null ){
            item.setStyle(style);
        }
        item.setStyleClass("myMenuItem");// 注意：UI可利用此class名稱，透過jQuery插入切換功能前的檢查

        return item;
    }
    // END: for MegaMenu
    
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
    
    /**
     * 取得 URL Server 資訊
     * @return 
     */
    public static String getRequestServerURL(){// for jsf
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return getRequestServerURL(request);
    }
    public static String getRequestServerURL(HttpServletRequest request){// for RESTful
        logger.debug("request.getProtocol() = "+request.getProtocol());
        logger.debug("request.getServerName() = "+request.getServerName());
        logger.debug("request.getServerPort() = "+request.getServerPort());
        logger.debug("request.getRequestURL() = "+request.getRequestURL().toString());
        logger.debug("request.getRequestURI = "+request.getRequestURI());
        
        // request.getRequestURL() = http://localhost:8080/ics/service/icsNoticeREST/rest/notifyCancel
        // request.getRequestURI = /ics/service/icsNoticeREST/rest/notifyCancel
        String fullurl = request.getRequestURL().toString();
        String serverinfo = fullurl.replaceAll(request.getRequestURI(), "");
        
        return serverinfo;
    }
    
    /**
     * find first UI Component with special Id
     * @param id
     * @return 
     */
    public static UIComponent findComponent(final String id) {
        FacesContext context = FacesContext.getCurrentInstance(); 
        UIViewRoot root = context.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        root.visitTree(new FullVisitContext(context), new VisitCallback() {     
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if(component.getId().equals(id)){
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;              
            }

        });

        return found[0];
    }            
    
    /**
     * get Cookie Value 
     * @param cookieName
     * @return 
     */
    public static String getCookieValue(String cookieName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Cookie cookies[] = ((HttpServletRequest)facesContext.getExternalContext().getRequest()).getCookies();
        if(cookies == null || cookies.length == 0){
            return null;
        }
        for (Cookie cookie : cookies) {
            logger.debug("getCookieValue getPath() = "+cookie.getPath()+"; getName() = "+cookie.getName()+"; getValue() = "+cookie.getValue());
            if ( cookieName.equals(cookie.getName()) ){
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

    /**
     * save Cookie 
     * @param cookieName
     * @param cookieValue
     * @param maxAge 
     */
    public static void saveCookie(String cookieName, String cookieValue, int maxAge) {
        try {
            removeCookie(cookieName);
            
            Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
            cookie.setMaxAge(maxAge);
            cookie.setPath(getContextPath());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ((HttpServletResponse)facesContext.getExternalContext().getResponse()).addCookie(cookie);
        } catch (UnsupportedEncodingException ex) {
        }
    }
    
    /**
     * remove Cookie
     * @param cookieName 
     */
    public static void removeCookie(String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(getContextPath());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ((HttpServletResponse)facesContext.getExternalContext().getResponse()).addCookie(cookie);
    }
    
    /**
     * 取得 Request ClientIP (考慮Proxy)
     * @return 
     */
    public static String getClientIP(){
        String clientIp = null;
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            clientIp = request.getHeader("X-Forwarded-For");
            if (null == clientIp) {
                clientIp = request.getRemoteAddr();
            }
        }catch(Exception e){
            logger.error("getClientIP exception \n:", e);
        }
        return clientIp;
    }
}