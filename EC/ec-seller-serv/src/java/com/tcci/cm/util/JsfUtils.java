package com.tcci.cm.util;

import com.sun.faces.component.visit.FullVisitContext;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsfUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsfUtils.class);

    /**
     * get context-param value in web.xml
     * @param name
     * @return 
     */
    public static String getInitParameter(String name){
        if( FacesContext.getCurrentInstance()!=null ){
            if( FacesContext.getCurrentInstance().getExternalContext()!=null ){
                return FacesContext.getCurrentInstance().getExternalContext().getInitParameter(name);
            }
        }
        return null;
    }
    
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = null;
        if( FacesContext.getCurrentInstance()!=null ){
            if( FacesContext.getCurrentInstance().getExternalContext()!=null ){
                if( FacesContext.getCurrentInstance().getExternalContext().getRequest()!=null ){
                    request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                }
            }
        }
        
        return request;
    }
    
    public static void setSessionAttr(String name, Object value, boolean create){
        HttpServletRequest request = getRequest();
        if( request!=null ){
            HttpSession session = request.getSession(create);
            if( session!=null ){
                session.setAttribute(name, value);
                logger.info("setSessionAttr ..."+ name + " = " + value);
            }
        }
    }
    public static Object getSessionAttr(String name){
        HttpServletRequest request = getRequest();
        if( request!=null ){
            HttpSession session = request.getSession();
            if( session!=null ){
                return session.getAttribute(name);
            }
        }
        return null;
    }
    public static void removeSessionAttr(String name){
        HttpServletRequest request = getRequest();
        if( request!=null ){
            HttpSession session = request.getSession();
            if( session!=null ){
                session.removeAttribute(name);
            }
        }
    }

    public static String getViewId(){
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getViewRoot().getViewId();
    }
    
    public static ResourceBundle getResourceBundle() {
        return getResourceBundle(GlobalConstant.DEF_RESOURCE_BUNDLE);
    }

    public static ResourceBundle getResourceBundle(String baseName) {
        return ResourceBundle.getBundle(baseName, getRequestLocale());
    }
    
    /**
     * 取得 Resource Bundle 文字資訊
     * @param key
     * @return 
     */
    public static String getResourceTxt(String key){
        return getResourceBundle().getString(key);
    }
    public static String getResourceTxt(String baseName, String key){
        return getResourceBundle(baseName).getString(key);
    }
    public static String getResourceTxt(Locale locale, String baseName, String key){
        return ResourceBundle.getBundle(baseName, locale).getString(key);
    }
    public static String getResourceTxtWithDef(String key, String defStr){
        try{
            return getResourceBundle().getString(key);
        }catch(Exception e){
            logger.debug("getResourceTxtWithDef key = "+key, e);
            return defStr;
        }
    }
    
    /**
     * 回傳local
     *
     * @return Locale
     */
    public static Locale getRequestLocale() {
        if( FacesContext.getCurrentInstance()==null 
           || FacesContext.getCurrentInstance().getViewRoot()==null 
           || FacesContext.getCurrentInstance().getViewRoot().getLocale()==null ){
            return GlobalConstant.DEF_LOCALE.getLocale();
        }
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
     * 取得 URL Server 資訊
     * EX. RETURN http://localhost:8080
     * @return 
     */
    public static String getRequestServerURL(){// for jsf
        try{
            if( FacesContext.getCurrentInstance()==null 
                || FacesContext.getCurrentInstance().getExternalContext()==null 
                || FacesContext.getCurrentInstance().getExternalContext().getRequest()==null ){
                logger.warn("getRequestServerURL = null ");// 例如 restful 呼叫時使用
                return null;
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            return WebUtils.getRequestServerURL(request);
        }catch(Exception e){
            logger.error("getRequestServerURL exception : "+e.toString());
        }
        return null;
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
            if (StringUtils.equals(cookie.getName(), cookieName)) {
                logger.debug("getCookieValue getPath() = "+cookie.getPath()+"; getName() = "+cookie.getName()+"; getValue() = "+cookie.getValue());
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
     * @param maxAge // 秒數
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

    /**
        Chrome 用戶代理字串
        Chrome（或基於 Chromium/blink 引擎的瀏覽器）的用戶代理字串看起來像 Firefox。出於相容性的理由，它還會加上「KHTML, like Gecko」與「Safari」的字串。
        Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36
        
        Opera 用戶代理字串
        因為 Opera 瀏覽器的引擎也是基於 blink 的，所以語法也看起來也會很像。不過，還會加上「 OPR/<version>」一詞。
        Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36 OPR/38.0.2220.41
        
        Safari 用戶代理字串
        此例的 safari 用戶代理字串是攜帶版，所以會出現「Mobile」一詞。
        Mozilla/5.0 (Linux; U; Android 4.0.3; de-ch; HTC Sensation Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
        
        Internet Explorer 用戶代理字串
        Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0)
        
        網路爬蟲與機器人的用戶代理字串
        Googlebot/2.1 (+http://www.google.com/bot.html)
     * @return 
     */
    public static String getClientBrowser(){
        String browser = "unknow";
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            if( context!=null && context.getExternalContext()!=null 
                    && context.getExternalContext().getRequest()!=null ){
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                String userAgent = request.getHeader("User-Agent");
                if( userAgent!=null ) {
                    logger.debug("getClientBrowser userAgent = " + userAgent);
                    if( userAgent.toLowerCase().contains("msie") 
                        || userAgent.toLowerCase().contains("trident") ){     
                        browser = "IE";
                    }else if( userAgent.toLowerCase().contains("chrome") 
                        || userAgent.toLowerCase().contains("chromium") ){
                        browser = "Chrome";
                    }else if( userAgent.toLowerCase().contains("firefox") ){
                        browser = "Firefox";
                    }else if( userAgent.toLowerCase().contains("opera") ){
                        browser = "Opera";
                    }else if( userAgent.toLowerCase().contains("safari") ){
                        browser = "Safari";
                    } else{
                        logger.info("getClientBrowser unknow browser !");
                    }
                }
            }
        }catch(Exception e){
            logger.error("getClientBrowser exception \n:", e);
        }
        return browser;
    }
}