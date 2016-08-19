/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.schedule;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.enums.RewardEventEnum;
import com.tcci.tccstore.enums.RewardTypeEnum;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import com.tcci.tccstore.facade.reward.EcRewardFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class RewardJobs {
    
    private final static Logger logger = LoggerFactory.getLogger(RewardJobs.class);
    
    @EJB
    private EcRewardFacade rewardFacade;
    @EJB
    private EcMemberFacade memberFacade;

    // 每日登入贈送金幣1點
    public void loginReward() {
        // 11/2起 ~12/31
        List<EcMember> members = memberFacade.findYesterdayLogin();
        for (EcMember member : members) {
            // logger.warn("findYesterdayLogin: member id:{}", member.getId());
            // 20160120 ~ : 贈送金幣2點
            rewardFacade.awardPoints(member, RewardTypeEnum.GOLD, 2, RewardEventEnum.LOGIN, "登入赠点", null, null);
            logger.warn("add 2 gold point, member id:{}", member.getId());
        }
    }
    
}
