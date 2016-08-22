/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.rs;

import com.google.gson.Gson;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.entity.FactWeb1;
import com.tcci.sksp.entity.FactWeb1PK;
import com.tcci.sksp.entity.FactWeb2;
import com.tcci.sksp.entity.FactWeb2PK;
import com.tcci.sksp.entity.FactWeb3;
import com.tcci.sksp.entity.FactWeb3PK;
import com.tcci.sksp.entity.FactWeb4;
import com.tcci.sksp.entity.FactWeb4PK;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesChannelMemberFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.security.Principal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import com.tcci.sksp.vo.AchievementFact2VO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author carl.lin
 */
@Path("achieve2")
@Stateless
public class AchieveREST2 {

    @EJB
    TcUserFacade userFacade;
    @EJB
    SkSalesMemberFacade memberFacade;
    @EJB
    SkSalesChannelMemberFacade channelMemberFacade;
    @PersistenceContext(unitName = "dmModel")
    private EntityManager em;

    // 全區資料
    @GET
    @Path("GetAchieve/Allareas")
    @Produces("text/plain; charset=UTF-8;")
    public String getAllareas(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        Boolean find = false;
        if (request.isUserInRole("Administrators")) {
            find = true;
        }
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }
        TcUser user = userFacade.findUserByLoginAccount(name);

        List<TcGroup> tgl = userFacade.findGroupOfUser(user);
        for (TcGroup tg : tgl) {
            if (tg.getCode().contains("ADMINISTRATORS")) {
                find = true;
            }
        }

        List<SkSalesMember> members = memberFacade.findByMembers(user);

        SkSalesMember member = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(member);

        Gson gson = new Gson();
        String result = "";
        String code = channels.getCode();
        // T1,T2 get all areas data

        if (!find) {
            for (SkSalesMember sb : members) {
                String scode = sb.getCode();
                if (scode.contains("T1") || scode.contains("T2")) {
                    find = true;
                }
            }
        }

        // T31,T32,T33 get large  area sales 
        if (find == true) {
            result = gson.toJson(getFact_WEB_1(yyyymm));
        } else if (code.contains("T31")
                || code.contains("T32")
                || code.contains("T33")) {
            result = gson.toJson(getFact_WEB_2_ALL(yyyymm, channels.getCode()));
        } // sales get small area sales
        else {
            result = gson.toJson(getFact_WEB_3(yyyymm, channels.getCode()));
        }
        return result;
    }
    
    //業務Total getFact_WEB_4_Total
    @GET
    @Path("GetAchieve/AllareasTotalS")
    @Produces("text/plain; charset=UTF-8;")
    public String getAllareasTotalS(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        Boolean find = false;
        if (request.isUserInRole("Administrators")) {
            find = true;
        }
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }
        TcUser user = userFacade.findUserByLoginAccount(name);

        List<TcGroup> tgl = userFacade.findGroupOfUser(user);
        for (TcGroup tg : tgl) {
            if (tg.getCode().contains("ADMINISTRATORS")) {
                find = true;
            }
        }

        List<SkSalesMember> members = memberFacade.findByMembers(user);

        SkSalesMember member = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(member);

        Gson gson = new Gson();
        String result = "";
        String code = channels.getCode();

        result = gson.toJson(getFact_WEB_4_Total(yyyymm, channels.getCode()));
        return result;
    }

    // 區經理Total
    @GET
    @Path("GetAchieve/AllareasTotalT")
    @Produces("text/plain; charset=UTF-8;")
    public String getAllareasTotalT(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        Boolean find = false;
        if (request.isUserInRole("Administrators")) {
            find = true;
        }
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }
        TcUser user = userFacade.findUserByLoginAccount(name);

        List<TcGroup> tgl = userFacade.findGroupOfUser(user);
        for (TcGroup tg : tgl) {
            if (tg.getCode().contains("ADMINISTRATORS")) {
                find = true;
            }
        }

        List<SkSalesMember> members = memberFacade.findByMembers(user);

        SkSalesMember member = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(member);

        Gson gson = new Gson();
        String result = "";
        String code = channels.getCode();

        if (code.contains("T31")
                || code.contains("T32")
                || code.contains("T33")) {
            result = gson.toJson(getFact_WEB_3_Total(yyyymm, channels.getCode()));
        } // sales get small area sales

        return result;
    }

    // 全區資料Total
    @GET
    @Path("GetAchieve/AllareasTotal")
    @Produces("text/plain; charset=UTF-8;")
    public String getAllareasTotal(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        Boolean find = false;
        if (request.isUserInRole("Administrators")) {
            find = true;
        }
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }
        TcUser user = userFacade.findUserByLoginAccount(name);

        List<TcGroup> tgl = userFacade.findGroupOfUser(user);
        for (TcGroup tg : tgl) {
            if (tg.getCode().contains("ADMINISTRATORS")) {
                find = true;
            }
        }

        List<SkSalesMember> members = memberFacade.findByMembers(user);

        SkSalesMember member = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(member);

        Gson gson = new Gson();
        String result = "";
        // T1,T2 get all areas data

        if (!find) {
            for (SkSalesMember sb : members) {
                String scode = sb.getCode();
                if (scode.contains("T1") || scode.contains("T2")) {
                    find = true;
                }
            }
        }

        if (find == true) {
            result = gson.toJson(getFact_WEB_1_Total(yyyymm, ""));
        }
        return result;
    }

    // 區經理資料
    @GET
    @Path("GetAchieve/Area")
    @Produces("text/plain; charset=UTF-8;")
    public String getArea(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("code") String code,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }

        Gson gson = new Gson();
        return gson.toJson(getFact_WEB_2(yyyymm, code));

    }

    //業代資料
    @GET
    @Path("GetAchieve/Sales")
    @Produces("text/plain; charset=UTF-8;")
    public String getSales(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("code") String code,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }
        Gson gson = new Gson();
        // sales get small area sales
        return gson.toJson(getFact_WEB_3(yyyymm, code));
    }

    // 產品別資料
    @GET
    @Path("GetAchieve/Products")
    @Produces("text/plain; charset=UTF-8;")
    public String getProducts(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
            @QueryParam("code") String code,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            if (null != loginAccount) {
                name = loginAccount;
            }
        }
        Gson gson = new Gson();
        // sales get small area sales
        return gson.toJson(getFact_WEB_4(yyyymm, code));
    }

    //get Fact_WEB_1_TOTAL
    private List<AchievementFact2VO> getFact_WEB_1_Total(String yearMonth, String Code) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();
        Query q = em.createNamedQuery("FactWeb2.findByYyyymmAccessid");
        FactWeb2PK pk2 = new FactWeb2PK();
        pk2.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk2.getYyyymm());
        q.setParameter("accessid", "10");

        List<FactWeb2> listfb2 = q.getResultList();
        for (FactWeb2 fb2 : listfb2) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb2.getFactWeb2PK().getYyyymm());
            vo.setDOMAIN(fb2.getDomain());
            vo.setDOMAINID(fb2.getFactWeb2PK().getDomainid());
            vo.setACCESS_1(fb2.getAccess1());
            vo.setACCESSID(fb2.getFactWeb2PK().getAccessid());
            vo.setSALES_AMOUNT(fb2.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb2.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb2.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb2.getGrossProfitRate()));
            vo.setLIGHT(fb2.getLight());
            vo.setRESPONSE_NAME(fb2.getResponseName());
            vo.setRESPONSE_PHONE(fb2.getResponsePhone());
            vo.setSHOULD_PAY_AMOUNT(fb2.getShouldPayAmount());
            vo.setPAYMENT_AMOUNT(fb2.getPaymentAmount());
            vo.setPAYMENT_RATE(Rate100(fb2.getPaymentRate()));

            vo.setINVOICE_AMOUNT(fb2.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb2.getPremiumDiscount());
            vo.setSALES_RETURN(fb2.getSalesReturn());
            vo.setSALES_DISCOUNT(fb2.getSalesDiscount());
            vo.setBUDGET_TARGET(fb2.getBudgetTarget());
            vo.setBUDGET(fb2.getBudget());
            result.add(vo);
        }

        return result;
    }

    // get Fact_WEB_2_ALL
    private List<AchievementFact2VO> getFact_WEB_2_ALL(String yearMonth, String Code) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();
        Query q = em.createNamedQuery("FactWeb2.findByYyyymm");
        FactWeb2PK pk2 = new FactWeb2PK();
        pk2.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk2.getYyyymm());
        List<FactWeb2> listfb2 = q.getResultList();
        for (FactWeb2 fb2 : listfb2) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb2.getFactWeb2PK().getYyyymm());
            vo.setDOMAIN(fb2.getDomain());
            vo.setDOMAINID(fb2.getFactWeb2PK().getDomainid());
            vo.setACCESS_1(fb2.getAccess1());
            vo.setACCESSID(fb2.getFactWeb2PK().getAccessid());
            vo.setSALES_AMOUNT(fb2.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb2.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb2.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb2.getGrossProfitRate()));
            vo.setLIGHT(fb2.getLight());
            vo.setRESPONSE_NAME(fb2.getResponseName());
            vo.setRESPONSE_PHONE(fb2.getResponsePhone());
            vo.setSHOULD_PAY_AMOUNT(fb2.getShouldPayAmount());
            vo.setPAYMENT_AMOUNT(fb2.getPaymentAmount());
            vo.setPAYMENT_RATE(Rate100(fb2.getPaymentRate()));

            vo.setINVOICE_AMOUNT(fb2.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb2.getPremiumDiscount());
            vo.setSALES_RETURN(fb2.getSalesReturn());
            vo.setSALES_DISCOUNT(fb2.getSalesDiscount());
            vo.setBUDGET_TARGET(fb2.getBudgetTarget());
            vo.setBUDGET(fb2.getBudget());
            result.add(vo);
        }

        return result;
    }

    // get Fact_WEB_3_Total
    // select * from FACT_WEB_4 where yyyymm='201511' and sapid='01_TO'
    private List<AchievementFact2VO> getFact_WEB_4_Total(String yearMonth, String salesman) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();

        Query q = em.createNamedQuery("FactWeb4.findByYyyymmSalesman");
        FactWeb4PK pk4 = new FactWeb4PK();
        pk4.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk4.getYyyymm());
        q.setParameter("salesman", "01_TO");
        List<FactWeb4> listfb4 = q.getResultList();
        for (FactWeb4 fb : listfb4) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb.getFactWeb4PK().getYyyymm());
            vo.setDOMAIN(fb.getFactWeb4PK().getDomainid());
            vo.setACCESS_1(fb.getAccess1());
            vo.setSALESMAN(fb.getSalesman());
            vo.setDOMAINID(fb.getFactWeb4PK().getDomainid());
            vo.setACCESSID(fb.getFactWeb4PK().getAccessid());
            vo.setSAPID(fb.getFactWeb4PK().getSapid());
            vo.setPDTYPE(fb.getPdtype());
            vo.setSALES_AMOUNT(fb.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb.getGrossProfitRate()));
            vo.setLIGHT(fb.getLight());
            vo.setRESPONSE_NAME(fb.getResponseName());
            vo.setRESPONSE_PHONE(fb.getResponsePhone());

            vo.setINVOICE_AMOUNT(fb.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb.getPremiumDiscount());
            vo.setSALES_RETURN(fb.getSalesReturn());
            vo.setSALES_DISCOUNT(fb.getSalesDiscount());
            vo.setBUDGET_TARGET(fb.getBudgetTarget());
            vo.setBUDGET(fb.getBudget());
            result.add(vo);
        }

        return result;
    }
    
    //get Fact_WEB_4
    private List<AchievementFact2VO> getFact_WEB_4(String yearMonth, String salesman) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();

        Query q = em.createNamedQuery("FactWeb4.findByYyyymmSalesman");
        FactWeb4PK pk4 = new FactWeb4PK();
        pk4.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk4.getYyyymm());
        q.setParameter("salesman", salesman);
        List<FactWeb4> listfb4 = q.getResultList();
        for (FactWeb4 fb : listfb4) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb.getFactWeb4PK().getYyyymm());
            vo.setDOMAIN(fb.getFactWeb4PK().getDomainid());
            vo.setACCESS_1(fb.getAccess1());
            vo.setSALESMAN(fb.getSalesman());
            vo.setDOMAINID(fb.getFactWeb4PK().getDomainid());
            vo.setACCESSID(fb.getFactWeb4PK().getAccessid());
            vo.setSAPID(fb.getFactWeb4PK().getSapid());
            vo.setPDTYPE(fb.getPdtype());
            vo.setSALES_AMOUNT(fb.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb.getGrossProfitRate()));
            vo.setLIGHT(fb.getLight());
            vo.setRESPONSE_NAME(fb.getResponseName());
            vo.setRESPONSE_PHONE(fb.getResponsePhone());

            vo.setINVOICE_AMOUNT(fb.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb.getPremiumDiscount());
            vo.setSALES_RETURN(fb.getSalesReturn());
            vo.setSALES_DISCOUNT(fb.getSalesDiscount());
            vo.setBUDGET_TARGET(fb.getBudgetTarget());
            vo.setBUDGET(fb.getBudget());
            result.add(vo);
        }

        return result;
    }

    // get Fact_WEB_3_Total
    private List<AchievementFact2VO> getFact_WEB_3_Total(String yearMonth, String code) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();

        Query q = em.createNamedQuery("FactWeb3.findByYyyymmDomain");
        FactWeb3PK pk3 = new FactWeb3PK();
        pk3.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk3.getYyyymm());
        q.setParameter("domain", "01_TO");
        List<FactWeb3> listfb3 = q.getResultList();
        for (FactWeb3 fb : listfb3) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb.getFactWeb3PK().getYyyymm());
            vo.setDOMAIN(fb.getDomain());
            vo.setACCESS_1(fb.getAccess1());
            vo.setDOMAINID(fb.getFactWeb3PK().getDomainid());
            vo.setACCESSID(fb.getFactWeb3PK().getAccessid());
            vo.setSAPID(fb.getFactWeb3PK().getSapid());
            vo.setSALESMAN(fb.getSalesman());
            vo.setSALES_AMOUNT(fb.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb.getGrossProfitRate()));
            vo.setLIGHT(fb.getLight());
            vo.setRESPONSE_NAME(fb.getResponseName());
            vo.setRESPONSE_PHONE(fb.getResponsePhone());
            vo.setSHOULD_PAY_AMOUNT(fb.getShouldPayAmount());
            vo.setPAYMENT_AMOUNT(fb.getPaymentAmount());
            vo.setPAYMENT_RATE(Rate100(fb.getPaymentRate()));
            vo.setINVOICE_AMOUNT(fb.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb.getPremiumDiscount());
            vo.setSALES_RETURN(fb.getSalesReturn());
            vo.setSALES_DISCOUNT(fb.getSalesDiscount());
            vo.setBUDGET_TARGET(fb.getBudgetTarget());
            vo.setBUDGET(fb.getBudget());
            result.add(vo);
        }

        return result;
    }

    //get Fact_WEB_3
    private List<AchievementFact2VO> getFact_WEB_3(String yearMonth, String code) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();

        Query q = em.createNamedQuery("FactWeb3.findByYyyymmDomain");
        FactWeb3PK pk3 = new FactWeb3PK();
        pk3.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk3.getYyyymm());
        q.setParameter("domain", code);
        List<FactWeb3> listfb3 = q.getResultList();
        for (FactWeb3 fb : listfb3) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb.getFactWeb3PK().getYyyymm());
            vo.setDOMAIN(fb.getDomain());
            vo.setACCESS_1(fb.getAccess1());
            vo.setDOMAINID(fb.getFactWeb3PK().getDomainid());
            vo.setACCESSID(fb.getFactWeb3PK().getAccessid());
            vo.setSAPID(fb.getFactWeb3PK().getSapid());
            vo.setSALESMAN(fb.getSalesman());
            vo.setSALES_AMOUNT(fb.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb.getGrossProfitRate()));
            vo.setLIGHT(fb.getLight());
            vo.setRESPONSE_NAME(fb.getResponseName());
            vo.setRESPONSE_PHONE(fb.getResponsePhone());
            vo.setSHOULD_PAY_AMOUNT(fb.getShouldPayAmount());
            vo.setPAYMENT_AMOUNT(fb.getPaymentAmount());
            vo.setPAYMENT_RATE(Rate100(fb.getPaymentRate()));
            vo.setINVOICE_AMOUNT(fb.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb.getPremiumDiscount());
            vo.setSALES_RETURN(fb.getSalesReturn());
            vo.setSALES_DISCOUNT(fb.getSalesDiscount());
            vo.setBUDGET_TARGET(fb.getBudgetTarget());
            vo.setBUDGET(fb.getBudget());
            result.add(vo);
        }

        return result;
    }

    // rates multipy 100 and round 2 scales from display
    private BigDecimal Rate100(BigDecimal bb) {
        return bb.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }

    // get Fact_WEB_2
    private List<AchievementFact2VO> getFact_WEB_2(String yearMonth, String Code) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();
        Query q = em.createNamedQuery("FactWeb2.findByYyyymmAccess1");
        FactWeb2PK pk2 = new FactWeb2PK();
        pk2.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk2.getYyyymm());
        q.setParameter("access1", Code);
        List<FactWeb2> listfb2 = q.getResultList();
        for (FactWeb2 fb2 : listfb2) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb2.getFactWeb2PK().getYyyymm());
            vo.setDOMAIN(fb2.getDomain());
            vo.setDOMAINID(fb2.getFactWeb2PK().getDomainid());
            vo.setACCESS_1(fb2.getAccess1());
            vo.setACCESSID(fb2.getFactWeb2PK().getAccessid());
            vo.setSALES_AMOUNT(fb2.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb2.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb2.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb2.getGrossProfitRate()));
            vo.setLIGHT(fb2.getLight());
            vo.setRESPONSE_NAME(fb2.getResponseName());
            vo.setRESPONSE_PHONE(fb2.getResponsePhone());
            vo.setSHOULD_PAY_AMOUNT(fb2.getShouldPayAmount());
            vo.setPAYMENT_AMOUNT(fb2.getPaymentAmount());
            vo.setPAYMENT_RATE(Rate100(fb2.getPaymentRate()));

            vo.setINVOICE_AMOUNT(fb2.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb2.getPremiumDiscount());
            vo.setSALES_RETURN(fb2.getSalesReturn());
            vo.setSALES_DISCOUNT(fb2.getSalesDiscount());
            vo.setBUDGET_TARGET(fb2.getBudgetTarget());
            vo.setBUDGET(fb2.getBudget());
            result.add(vo);
        }

        return result;
    }

    // get Fact_WEB_1
    private List<AchievementFact2VO> getFact_WEB_1(String yearMonth) {
        List<AchievementFact2VO> result = new ArrayList<AchievementFact2VO>();
        Query q = em.createNamedQuery("FactWeb1.findByYyyymm");
        FactWeb1PK pk = new FactWeb1PK();
        pk.setYyyymm(yearMonth);
        q.setParameter("yyyymm", pk.getYyyymm());
        List<FactWeb1> listfb1 = q.getResultList();
        for (FactWeb1 fb1 : listfb1) {
            AchievementFact2VO vo = new AchievementFact2VO();
            vo.setYYYYMM(fb1.getFactWeb1PK().getYyyymm());
            vo.setACCESS_1(fb1.getAccess1());
            vo.setACCESSID(fb1.getFactWeb1PK().getAccessid());
            vo.setSALES_AMOUNT(fb1.getSalesAmount());
            vo.setCUMULATIVE_ACHIVEMENT_RATE(Rate100(fb1.getCumulativeAchivementRate()));
            vo.setMONTH_ACHIVEMENT_RATE(Rate100(fb1.getMonthAchivementRate()));
            vo.setGROSS_PROFIT_RATE(Rate100(fb1.getGrossProfitRate()));
            vo.setLIGHT(fb1.getLight());
            vo.setRESPONSE_NAME(fb1.getResponseName());
            vo.setRESPONSE_PHONE(fb1.getResponsePhone());
            vo.setSHOULD_PAY_AMOUNT(fb1.getShouldPayAmount());
            vo.setPAYMENT_AMOUNT(fb1.getPaymentAmount());
            vo.setPAYMENT_RATE(Rate100(fb1.getPaymentRate()));

            vo.setINVOICE_AMOUNT(fb1.getInvoiceAmount());
            vo.setPREMIUM_DISCOUNT(fb1.getPremiumDiscount());
            vo.setSALES_RETURN(fb1.getSalesReturn());
            vo.setSALES_DISCOUNT(fb1.getSalesDiscount());
            vo.setBUDGET_TARGET(fb1.getBudgetTarget());
            vo.setBUDGET(fb1.getBudget());

            result.add(vo);
        }

        return result;
    }
}
