/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.test;

import com.tcci.tccstore.client.SSOClientException;
import com.tcci.tccstore.model.campaign.Campaign;

/**
 *
 * @author Jimmy.Lee
 */
public class CampaignTest extends TestBase {
    
    public static void main(String[] args) throws SSOClientException {
        CampaignTest test = new CampaignTest();
        String service = "/campaign/list";
        System.out.println(service);
        Campaign[] campaigns = test.get(service, Campaign[].class);
        System.out.println(test.toJson(campaigns));
        
        service = "/campaign/view/9";
        System.out.println(service);
        Campaign campaign = test.get(service, Campaign.class);
        System.out.println(test.toJson(campaign));
    }

}
