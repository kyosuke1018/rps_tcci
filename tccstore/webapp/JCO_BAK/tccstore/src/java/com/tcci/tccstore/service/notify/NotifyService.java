/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.notify;

import com.tcci.tccstore.entity.EcArticle;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcNotify;
import com.tcci.tccstore.facade.article.EcArticleFacade;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.model.notify.Notify;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Neo.Fu
 */
@Path("notify")
public class NotifyService extends ServiceBase {

    @Inject
    private EcNotifyFacade notifyFacade;
    @Inject
    private EcArticleFacade articelFacade;
    
    @GET
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notify> query(@QueryParam("read") Boolean readFlag) {
        List<Notify> resultList = new ArrayList();
        EcMember authMember = getAuthMember();
        for (EcNotify ecNotify : notifyFacade.findByCriteria(authMember, readFlag)) {
            if (ecNotify.getReadCount() == null || ecNotify.getReadCount() == 0) {
                ecNotify.setReadCount(1);
                notifyFacade.edit(ecNotify);
            }
            Notify notify = EntityToModel.buildNotify(ecNotify);
            if (EcArticle.class.getCanonicalName().equals(ecNotify.getNotifyClassname())) {
                EcArticle ecArticle = articelFacade.find(ecNotify.getNotifyClassid());
                notify.setLink(null==ecArticle ? null : ecArticle.getLink());
            }
            resultList.add(notify);
        }
        return resultList;
    }

//    @GET
//    @Path("read/{notify_id}")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String setRead(@Context HttpServletRequest request, @PathParam("notify_id") Long id) {
//        ResourceBundle rb = ResourceBundle.getBundle("/msgNotify", request.getLocale());
//        String result = "";
//        EcNotify notify = notifyFacade.find(id);
//        if (notify != null) {
//            notify.setReadCount(notify.getReadCount() + 1);
//            notifyFacade.editThenReturn(notify);
//            result = rb.getString("notify.msg.setNotifyReadSuccess");
//        } else {
//            result = rb.getString("notify.err.notifyNotExists");
//        }
//        return result;
//    }
//
//    @POST
//    @Path("read_all")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.TEXT_PLAIN)
//    public String setReadAll(@Context HttpServletRequest request, List<Long> idList) {
//        String result = "";
//        ResourceBundle rb = ResourceBundle.getBundle("/msgNotify", request.getLocale());
//        List<String> resultList = new ArrayList();
//        if (!idList.isEmpty()) {
//            for (Long id : idList) {
//                String eachResult = setRead(request, id);
//                if (!eachResult.equals(rb.getString("notify.msg.setNotifyReadSuccess"))) {
//                    resultList.add(eachResult);
//                }
//            }
//        } else {
//            resultList.add(rb.getString("notify.err.idListIsRequired"));
//        }
//        if (resultList.isEmpty()) {
//            resultList.add(rb.getString("notify.msg.setNotifyReadSuccess"));
//        }
//        for (String eachResult : resultList) {
//            if (result.length() > 0) {
//                result += "\n";
//            }
//            result += eachResult;
//        }
//        return result;
//    }
    @GET
    @Path("remove/{notify_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@Context HttpServletRequest request, @PathParam("notify_id") Long id) {
        ResourceBundle rb = ResourceBundle.getBundle("/msgNotify", request.getLocale());
        String result = "";
        EcNotify notify = notifyFacade.find(id);
        if (notify != null) {
            notifyFacade.removeNotify(notify);
            result = rb.getString("notify.msg.notifyRemoveSuccess");
        } else {
            result = rb.getString("notify.err.notifyNotExists");
        }
        return result;
    }

}
