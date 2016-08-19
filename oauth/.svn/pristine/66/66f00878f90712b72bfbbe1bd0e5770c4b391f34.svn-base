/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.home;

import com.tcci.tccstore.entity.EcBulletin;
import com.tcci.tccstore.facade.bulletin.EcBulletinFacade;
import com.tcci.tccstore.facade.campaign.EcCampaignFacade;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.model.home.Bulletin;
import com.tcci.tccstore.model.home.Home;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Jimmy.Lee
 */
@Path("home")
public class HomeService extends ServiceBase {

    @EJB
    private EcCampaignFacade ecCapmaignFacade;
    @Inject
    private EcNotifyFacade notifyFacade;
    @Inject
    private EcBulletinFacade ecBulletinFacade;

    @GET
    @Produces("application/json")
    public Home getHome(@HeaderParam(PARAM_CLIENT_KEY) String client_key) {
        Home home = new Home();
        if (client_key != null) {
            home.setUnreadCampaigns(ecCapmaignFacade.unreadCount(client_key));
        }
        home.setBulletins(findBulletins());
        return home;
    }

    @GET
    @Path("login")
    @Produces("application/json")
    public Home getLoginHome(@HeaderParam(PARAM_CLIENT_KEY) String client_key) {
        Home home = new Home();
        if (client_key != null) {
            home.setUnreadCampaigns(ecCapmaignFacade.unreadCount(client_key));
        }
        home.setUnreadNotifys(notifyFacade.findByCriteria(getAuthMember(), Boolean.FALSE).size());
        home.setBulletins(findBulletins());
        return home;
    }

    private List<Bulletin> findBulletins() {
        List<EcBulletin> entities = ecBulletinFacade.findEffective();
        List<Bulletin> result = new ArrayList<>();
        for (EcBulletin entity : entities) {
            result.add(EntityToModel.buildBulletin(entity));
        }
        return result;
    }

}
