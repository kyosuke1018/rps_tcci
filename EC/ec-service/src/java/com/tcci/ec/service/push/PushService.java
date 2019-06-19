/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.push;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.tcci.ec.entity.EcPushLog;
import com.tcci.ec.facade.push.EcPushLogFacade;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("push")
public class PushService {
    private final static Logger logger = LoggerFactory.getLogger(PushService.class);
    @EJB
    private EcPushLogFacade pushLogFacade;
//    @Resource(mappedName = "jndi/dashboard.config")
//    private Properties dashboardConfig;
    @Resource(mappedName = "jndi/ec.config")
    protected Properties jndiConfig;
    
    
    @GET
    @Path("test")
    @Produces("application/json; charset=UTF-8;")
//    @Produces(MediaType.TEXT_PLAIN)
    public String test(@Context HttpServletRequest request
            , @QueryParam("category") String category
            , @QueryParam("id") String id
            , @QueryParam("type") String type
            , @QueryParam("status") String status
            , @QueryParam("title") String title
            , @QueryParam("alert") String alert
            , @QueryParam("alias") String alias
            , @QueryParam("tag") String tag) {
        logger.debug("push test alias:"+alias);
//        if (!request.isUserInRole("ADMINISTRATORS")) {
//            throw new WebApplicationException(403);
//        }
        if (null == category) {
            category = "test";
        }
        if (null == title) {
            title = "推播測試";
        }
        if (null == alert) {
            alert = "推播內文123，推播內文123，推播內文123。";
        }
        if (alias != null) {
            return sendPushAlias(category, id, type, status, title, alert, alias);
        } else if (tag != null) {
            return sendPushTag(category, title, alert, tag);
        } else {
            return "n/a";
        }
    }
    
    public String sendPushAlias(String title, String alert, String alias) {
        return this.sendPushAlias(null, null, null, null, title, alert, alias);
    }
    
    public String sendPushAlias(String category, String id, String title, String alert, String alias) {
        return this.sendPushAlias(category, id, null, null, title, alert, alias);
    }
    
    public String sendPushAlias(String category, String id, String type, String status, 
            String title, String alert, String alias) {
        logger.warn("sendPushAlias:{}, {}, {}", new Object[]{title, alert, alias});
        final String masterSecret = jndiConfig.getProperty("jpush.master");
        final String appKey = jndiConfig.getProperty("jpush.appkey");
//        final String masterSecret1 = jndiConfig.getProperty("jpush.master");
//        final String appKey1 = jndiConfig.getProperty("jpush.appkey");
//        logger.error("sendPushAlias masterSecret1:"+ masterSecret1);
//        logger.error("sendPushAlias appKey1:"+ appKey1);
//        final String masterSecret = "da7c7386f45ca642a9843c8e";
//        final String appKey = "542a821cd3ae576d72809a9f";
        
        EcPushLog pushLog = new EcPushLog(category, "alias", title, alert, alias);
        pushLogFacade.save(pushLog);
        try {
            JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
            String[] aryAlias = alias.split(",");
            Map<String, String> extras = new HashMap<>();
            extras.put("category", category);
            extras.put("id", id);
            extras.put("type", type);
            extras.put("status", status);
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.alias(aryAlias))
                    .setNotification(Notification.android(alert, title, extras))
                    .build();
//            logger.debug("sendPushAlias toJSON:{}", payload.toJSON());
//            logger.debug("sendPushAlias PUSH_HOST_NAME:{}", (String) ClientConfig.getInstance().get(ClientConfig.PUSH_HOST_NAME));
//            logger.debug("sendPushAlias PUSH_PATH:{}",(String) ClientConfig.getInstance().get(ClientConfig.PUSH_PATH));
//            logger.debug("sendPushAlias ALIASES_PATH:{}",(String) ClientConfig.getInstance().get(ClientConfig.ALIASES_PATH));
//            PushPayload payload = PushPayload.newBuilder()
//                    .setPlatform(Platform.android())
//                    .setAudience(Audience.alias(aryAlias))
//                    .build();
            PushResult result = jpushClient.sendPush(payload);
            logger.warn("sendPushAlias result:{}", result.toString());
            pushLog.setSuccess(0 == result.statusCode);
            pushLog.setPushResult(result.toString());
            pushLogFacade.save(pushLog);
            return result.toString();
//          } catch (Exception ex) {
        } catch (APIConnectionException | APIRequestException ex) {
            logger.error("sendPushAlias masterSecret:"+ masterSecret);
            logger.error("sendPushAlias appKey:"+ appKey);
            logger.error("sendPushAlias exception:"+ ex);
            pushLog.setPushResult(ex.getMessage());
            pushLogFacade.save(pushLog);
            return "exception";
        }
    }

    private String sendPushTag(String category, String title, String alert, String tag) {
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
}
