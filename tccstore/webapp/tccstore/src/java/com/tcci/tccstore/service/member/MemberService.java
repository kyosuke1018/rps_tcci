/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.member;

import com.tcci.tccstore.entity.EcCompany;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcReward;
import com.tcci.tccstore.entity.EcRewardLog;
import com.tcci.tccstore.entity.datawarehouse.ZstdCreditdataVO;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.enums.RewardTypeEnum;
import com.tcci.tccstore.facade.company.EcCompanyFacade;
import com.tcci.tccstore.facade.datawarehouse.ZstdFacade;
import com.tcci.tccstore.facade.reward.EcRewardFacade;
import com.tcci.tccstore.model.customer.Customer;
import com.tcci.tccstore.model.member.CreditInfo;
import com.tcci.tccstore.model.member.Member;
import com.tcci.tccstore.model.member.MemberCredit;
import com.tcci.tccstore.model.member.MemberReward;
import com.tcci.tccstore.model.member.RewardLog;
import com.tcci.tccstore.service.ServiceBase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Path("member")
public class MemberService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(MemberService.class);
    
//    @EJB
//    private Zt001CnFacade zt001CnFacade;
    @EJB
    private EcCompanyFacade ecCompanyFacade;
    @EJB
    private EcRewardFacade ecRewardFacade;
    @EJB
    private ZstdFacade zstdFacade;
    
    @Resource(mappedName = "jndi/global.config")
    transient private Properties globalConfig;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Member reload() {
        EcMember loginMember = getAuthMember();
        Member member = EntityToModel.buildMember(loginMember);
        List<EcCustomer> ecCustomers = ecCustomerFacade.findByMember(loginMember);
        List<Customer> customers = member.getCustomers();
        for (EcCustomer ecCustomer : ecCustomers) {
            if (ecCustomer.isActive()) {
                customers.add(EntityToModel.buildCustomer(ecCustomer));
            }
        }
        ecMemberFacade.addLoginLog(loginMember);
        return member;
    }

    @GET
    @Path("credit/{customer_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MemberCredit memberCredit(@PathParam("customer_id") Long customer_id) {
        MemberCredit memberCredit = new MemberCredit();
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        List<CreditInfo> creditInfos = findCreditInfo(ecCustomer);
        memberCredit.setCreditList(creditInfos);
        List<EcReward> list = ecRewardFacade.find(this.getAuthMember());
        for (EcReward e : list) {
            int type = e.getEcRewardPK().getType();
            if (type == RewardTypeEnum.BONUS.getValue()) {
                memberCredit.setBonus(e.getPoints());
            } else if (type == RewardTypeEnum.GOLD.getValue()) {
                memberCredit.setGold(e.getPoints());
            }
        }
        return memberCredit;
    }

    @GET
    @Path("customercredit/{customer_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreditInfo> customerCredit(@PathParam("customer_id") Long customer_id) {
        EcCustomer ecCustomer = getAuthCustomer(customer_id);
        List<CreditInfo> creditInfos = findCreditInfo(ecCustomer);
        return creditInfos;
    }

    @GET
    @Path("reward")
    @Produces(MediaType.APPLICATION_JSON)
    public MemberReward memberReward() {
        MemberReward memberReward = new MemberReward();
        List<EcReward> list = ecRewardFacade.find(this.getAuthMember());
        for (EcReward e : list) {
            int type = e.getEcRewardPK().getType();
            if (type == RewardTypeEnum.BONUS.getValue()) {
                memberReward.setBonus(e.getPoints());
            } else if (type == RewardTypeEnum.GOLD.getValue()) {
                memberReward.setGold(e.getPoints());
            }
        }
        return memberReward;
    }

    @GET
    @Path("bonuslist")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RewardLog> bonuslist(@QueryParam("year_month") String yearMonth) {
        if (null == yearMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            yearMonth = sdf.format(new Date());
        }
        return convertToRewardLogs(ecRewardFacade.findLog(getAuthMember(), RewardTypeEnum.BONUS, yearMonth));
    }

    @GET
    @Path("goldlist")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RewardLog> goldlist(@QueryParam("year_month") String yearMonth) {
        if (null == yearMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            yearMonth = sdf.format(new Date());
        }
        return convertToRewardLogs(ecRewardFacade.findLog(getAuthMember(), RewardTypeEnum.GOLD, yearMonth));
    }

    private List<RewardLog> convertToRewardLogs(List<EcRewardLog> ecLogs) {
        List<RewardLog> results = new ArrayList<>();
        for (EcRewardLog entity : ecLogs) {
            results.add(EntityToModel.buildRewardLog(entity));
        }
        return results;
    }

    /*
    private List<CreditInfo> findCreditInfo(EcCustomer ecCustomer) {
        List<CreditInfo> creditInfos = new ArrayList<>();
        try {
            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            SapProxyResponseDto result = sdProxy.findCustomerCredits(ecCustomer.getCode());
            SapTableDto sapTableDto = (SapTableDto) result.getResult();
            if (sapTableDto != null && sapTableDto.getDataMapList().size() > 0) {
                Map<String, EcCompany> mapCompany = ecCompanyFacade.findNoHideCreditToMap();
                List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                for (Map<String, Object> dataMap : dataMapList) {
                    String compCode = (String) dataMap.get("KKBER");
                    if (null == compCode || compCode.length() != 4) {
                        logger.warn("unknown compCode:{}", compCode);
                        continue;
                    }
                    EcCompany ecCompany = mapCompany.get(compCode.substring(0, 2) + "00");
                    if (null == ecCompany) {
                        continue;
                    }
                    BigDecimal amount = (BigDecimal) dataMap.get("OBLIG");
                    String currency = (String) dataMap.get("WAERB");
                    creditInfos.add(new CreditInfo(ecCompany.getCode(), ecCompany.getName(), amount, currency));
                }
            }
            sdProxy.dispose();
        } catch (Exception ex) {
            logger.error("customerCredit exception!", ex);
            throw new ServiceException(ex.getMessage());
        }
        return creditInfos;
    }
    */

    private List<CreditInfo> findCreditInfo(EcCustomer ecCustomer) {
        List<CreditInfo> creditInfos = new ArrayList<>();
        List<ZstdCreditdataVO> datas = zstdFacade.findCreditdata(ecCustomer.getCode());
        Map<String, EcCompany> mapCompany = ecCompanyFacade.findNoHideCreditToMap();
        for (ZstdCreditdataVO vo : datas) {
            EcCompany ecCompany = mapCompany.get(vo.getBukrs());
            if (null == ecCompany) {
                continue;
            }
            creditInfos.add(new CreditInfo(ecCompany.getCode(), ecCompany.getName(), vo.getOblig(), "RMB", vo.getDatum(), vo.getUzeit()));
        }
        return creditInfos; 
    }
    
    /*
    private List<CreditInfo> findCreditInfo(EcCustomer ecCustomer) {
        String jcoServiceUrl = globalConfig.getProperty("SAP_REST_ROOT");
        if (null == jcoServiceUrl) {
            throw new ServiceException("系統設定有誤!");
        }
        List<Map<String, Object>> result = RFCExec.expCreditList(jcoServiceUrl, ecCustomer.getCode());
        if (null == result) {
            throw new ServiceException("無法取得資料!");
        }
        Map<String, EcCompany> mapCompany = ecCompanyFacade.findNoHideCreditToMap();
        List<CreditInfo> creditInfos = new ArrayList<>();
        for (Map<String, Object> dataMap : result) {
            String compCode = (String) dataMap.get("KKBER");
            if (null == compCode || compCode.length() != 4) {
                logger.warn("unknown compCode:{}", compCode);
                continue;
            }
            EcCompany ecCompany = mapCompany.get(compCode.substring(0, 2) + "00");
            if (null == ecCompany) {
                continue;
            }
            BigDecimal amount = new  BigDecimal(dataMap.get("OBLIG").toString()); // JCO return Double or BigDeciaml ?
            String currency = (String) dataMap.get("WAERB");
            creditInfos.add(new CreditInfo(ecCompany.getCode(), ecCompany.getName(), amount, currency));
        }
        return creditInfos;
    }
    */

}
