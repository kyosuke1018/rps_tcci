/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.tcci.cm.enums.LocaleEnum;
import com.tcci.ec.entity.EcCustomer;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPushLog;
import com.tcci.ec.facade.customer.EcCustomerFacade;
import com.tcci.ec.facade.member.EcMemberFacade;
import com.tcci.ec.facade.push.EcPushLogFacade;
import com.tcci.ec.facade.util.EntityTransforVO;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kyle.cheng
 */
public class ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(ServiceBase.class);
    
    @Inject
    private javax.security.enterprise.SecurityContext securityContext;
    @EJB
    protected EntityTransforVO entityTransforVO;
//    @Inject
//    protected Pbkdf2PasswordHash passwordHash;
//    protected AESPasswordHash passwordHash;
    @Context
    protected SecurityContext sc;
    @Context
    protected HttpServletRequest request;
    
    @EJB
    protected EcMemberFacade ecMemberFacade;
    @EJB
    protected EcCustomerFacade ecCustomerFacade;
    @EJB
    private EcPushLogFacade pushLogFacade;
    
    @Resource(mappedName = "jndi/ec.config")
    protected Properties jndiConfig;
    
    private EcMember _loginMember; // is this thread safe ?
    private EcCustomer _loginCustomer;
    
    protected String CLIENT_LANGUAGE_CUS = "Custom-Language";
    protected String CLIENT_LANGUAGE = "Accept-Language";
    
    @PostConstruct
    private void init() {
        logger.debug("securityContext={}", securityContext);
        Principal principal = securityContext == null ? null : securityContext.getCallerPrincipal();
        logger.debug("principal={}", principal);
        String loginAccount = principal == null ? null : principal.getName().toLowerCase();
        logger.debug("loginAccount={}", loginAccount);
        EcMember member = loginAccount == null ? null : ecMemberFacade.findByLoginAccount(loginAccount);
        logger.debug("member={}", member);
        if (member == null && loginAccount != null) {
            logger.error("member not active/found, loginAccount={}", loginAccount);
            
            member = ecMemberFacade.createNewMember(loginAccount, "訪客");
        }
        
        if (member != null && member.isActive()) {
            _loginMember = member;
            EcCustomer customer = ecCustomerFacade.findByMember(member);
            if (customer != null) {
                _loginCustomer = customer;
            }else{
                _loginCustomer = null;
                logger.debug("not customer member, loginAccount={}", loginAccount);
            }
            
        }
    }
    
    protected EcMember getAuthMember() {
        if (null == _loginMember) {
            String error = "尚未建立会员资料或会员帐号已停用!";
            logger.error(error);
            throw new ServiceException(error);
        }
        return _loginMember;
    }
    
    protected EcCustomer getAuthCustomer() {
        if (null == _loginCustomer) {
            String error = "尚未建立会员资料或会员帐号已停用!";
            logger.error(error);
            throw new ServiceException(error);
        }
        return _loginCustomer;
    }
    
    
    //推播
//    private String sendPushAlias(String title, String alert, String alias) {
//        return this.sendPushAlias(null, null, null, title, alert, alias);
//    }
//    
//    private String sendPushAlias(String category, String title, String alert, String alias) {
//        return this.sendPushAlias(category, null, null, title, alert, alias);
//    }
//    
//    private String sendPushAlias(String category, String status, String id, String title, String alert, String alias) {
//        logger.warn("sendPushAlias:{}, {}, {}", new Object[]{title, alert, alias});
//        final String masterSecret = jndiConfig.getProperty("jpush.master");
//        final String appKey = jndiConfig.getProperty("jpush.appkey");
//        EcPushLog pushLog = new EcPushLog(category, "alias", title, alert, alias);
//        pushLogFacade.save(pushLog);
//        try {
//            JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
//            String[] aryAlias = alias.split(",");
//            Map<String, String> extras = new HashMap<>();
//            extras.put("category", category);
//            if(id!=null){
//                extras.put("status", status);
//                extras.put("id", id);
//            }
//            PushPayload payload = PushPayload.newBuilder()
//                    .setPlatform(Platform.android())
//                    .setAudience(Audience.alias(aryAlias))
//                    .setNotification(Notification.android(alert, title, extras))
//                    .build();
//            PushResult result = jpushClient.sendPush(payload);
//            logger.warn("sendPushAlias result:{}", result.toString());
//            pushLog.setSuccess(0 == result.statusCode);
//            pushLog.setPushResult(result.toString());
//            pushLogFacade.save(pushLog);
//            return result.toString();
//        } catch (APIConnectionException | APIRequestException ex) {
//            logger.error("sendPushAlias masterSecret:"+ masterSecret);
//            logger.error("sendPushAlias appKey:"+ appKey);
//            logger.error("sendPushAlias exception:"+ ex);
//            pushLog.setPushResult(ex.getMessage());
//            pushLogFacade.save(pushLog);
//            return "exception";
//        }
//    }

    protected String sendPushTag(String category, String title, String alert, String tag) {
        logger.warn("sendPushTag:{}, {}, {}", new Object[]{title, alert, tag});
        final String masterSecret = jndiConfig.getProperty("jpush.master");
        final String appKey = jndiConfig.getProperty("jpush.appkey");
        EcPushLog pushLog = new EcPushLog(category, "tag", title, alert, tag);
        pushLogFacade.save(pushLog);
        try {
            JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
            String[] aryTags = tag.split(",");
            Map<String, String> extras = new HashMap<>();
            extras.put("category", category);
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.tag_and(aryTags))
                    .setNotification(Notification.android(alert, title, extras))
                    .build();
            
            PushResult result = jpushClient.sendPush(payload);
            logger.warn("sendPushTag result:{}", result.toString());
            pushLog.setSuccess(0 == result.statusCode);
            pushLog.setPushResult(result.toString());
            pushLogFacade.save(pushLog);
            return result.toString();
        } catch (APIConnectionException | APIRequestException ex) {
            logger.error("sendPushTag exception", ex);
            return "exception";
        }
    }
    
    public Locale getLocale(HttpServletRequest request){
        LocaleEnum localeEnum = LocaleEnum.SIMPLIFIED_CHINESE;
        String language = request.getHeader(CLIENT_LANGUAGE_CUS);
        try{
            if( language==null ){
                language = request.getHeader(CLIENT_LANGUAGE);
                if( language != null && language.indexOf(",")>0 ){
                    language = language.substring(0, language.indexOf(",")).trim();
                }
            }
            logger.info("getLocale language = ", language);
            localeEnum = LocaleEnum.getFromCode(language, LocaleEnum.SIMPLIFIED_CHINESE);
        }catch(Exception e){
            logger.error("getLocale Exception:\n", e);
        }
        return localeEnum!=null?localeEnum.getLocale():LocaleEnum.SIMPLIFIED_CHINESE.getLocale();
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Repsone">
//    public Response genFailRepsone(HttpServletRequest request){
//        return Response.ok(genBaseFailRepsone(request, ResStatusEnum.FAIL), MediaType.APPLICATION_JSON).build();
//    }
//    public Response genFailRepsone(HttpServletRequest request, ResStatusEnum failEnum, List<String> errors){
//        return Response.ok(this.genFailRepsoneVO(request, failEnum, errors), MediaType.APPLICATION_JSON).build();
//    }
    
//    public Response genSuccessRepsone(HttpServletRequest request){
//        return Response.ok(this.genSuccessRepsoneVO(request), MediaType.APPLICATION_JSON).encoding("UTF-8").build();
//    }
//    public Response genSuccessRepsone(HttpServletRequest request, BaseResponseVO res){
//        res.setRes(this.genSuccessRepsoneVO(request));
//        return Response.ok(res, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
//    }
    //</editor-fold>
}
