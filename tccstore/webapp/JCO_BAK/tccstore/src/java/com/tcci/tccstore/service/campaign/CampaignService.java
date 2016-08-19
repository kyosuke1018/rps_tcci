/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.campaign;

import com.tcci.tccstore.entity.EcCampaign;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.facade.campaign.EcCampaignFacade;
import com.tcci.tccstore.model.campaign.Campaign;
import com.tcci.tccstore.service.ServiceBase;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("campaign")
public class CampaignService extends ServiceBase {

    @EJB
    private EcCampaignFacade ecCampaignFacade;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Campaign> list(@HeaderParam(PARAM_CLIENT_KEY) String client_key) {
        List<Campaign> result = new ArrayList<>();
        List<EcCampaign> ecCampaigns = ecCampaignFacade.getAllCampaigns();
        List<Long> visiteds = client_key==null ? null : ecCampaignFacade.findActiveVisited(client_key);
        for (EcCampaign entity : ecCampaigns) {
            boolean visited = visiteds==null ? false : visiteds.contains(entity.getId());
            Campaign vo = EntityToModel.buildCampaign(entity, visited);
            // 清單不需要顯示description
            vo.setDescription(null);
            result.add(vo);
        }
        return result;
    }

    @GET
    @Path("view/{obj_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Campaign click(@HeaderParam(PARAM_CLIENT_KEY) String client_key, @PathParam("obj_id") Long obj_id) {
        EcCampaign entity = ecCampaignFacade.click(client_key, obj_id);
        return (null == entity) ? null : EntityToModel.buildCampaign(entity, true);
    }

    @GET
    @Path("reload")
    @Produces(MediaType.TEXT_PLAIN)
    public String reload() {
        ecCampaignFacade.updateAndReload();
        return "OK";
    }

}
