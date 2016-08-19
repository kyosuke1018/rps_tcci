/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.schedule;

import com.tcci.tccstore.facade.campaign.EcCampaignFacade;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class DailyJobs {
    private final static Logger logger = LoggerFactory.getLogger(DailyJobs.class);
    
    @EJB
    private EcCampaignFacade ecCampaignFacade;
    
    // 停用逾期的活動
    @Schedule(minute = "0", hour = "0", persistent = false)
    public void campaignReload() {
        try {
            logger.warn("campaignReload");
            ecCampaignFacade.updateAndReload();
        } catch (Exception ex) {
        }
    }

}
