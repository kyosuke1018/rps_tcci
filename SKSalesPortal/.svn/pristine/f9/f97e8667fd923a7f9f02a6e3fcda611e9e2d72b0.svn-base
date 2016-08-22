/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.rs;

import com.google.gson.Gson;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.facade.SendAchievementMail;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tcci.sksp.vo.AchievementFactVO;
import com.tcci.sksp.entity.DimSkSaleName;
import com.tcci.sksp.entity.org.SkSalesChannelMember;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesChannelMemberFacade;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.QueryParam;

/**
 *
 * @author carl.lin
 */
@Path("achieve")
@Stateless
public class AchieveREST {

    Logger logger = LoggerFactory.getLogger(AchieveREST.class);
    @EJB
    SendAchievementMail achieveFacade;
    @EJB
    SkSalesChannelsFacade ChannelsFacade;
    @EJB
    SkSalesChannelMemberFacade channelMemberFacade;
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkSalesMemberFacade memberFacade;

    //業務or區經理
    @GET
    @Path("position")
    @Produces("text/plain; charset=UTF-8;")
    public String getPosition(@Context HttpServletRequest request,
            @QueryParam("loginAccount") String loginAccount) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            name = loginAccount;
        }
        TcUser user = userFacade.findUserByLoginAccount(name);
        SkSalesChannels channel = ChannelsFacade.findByManager(user);

        String position = "S";
        if (channel != null && channel.getCode().contains("T")) {
            position = "B";
        }

        SkSalesMember members = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(members);

        Gson gson = new Gson();
        String code = channels.getCode();
        return gson.toJson(position.concat(code).toString());
    }

    // 本月業務資訊
    @GET
    @Path("TodaySapid/sales")
    @Produces("text/plain; charset=UTF-8;")
    public String getToMonth(@Context HttpServletRequest request,
            @QueryParam("sapid") String sapid) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonth = sdf.format(cal.getTime());

        Gson gson = new Gson();
        return gson.toJson(getMonthSales(sapid, yearMonth));
    }

    @GET
    @Path("Today/code")
    @Produces("text/plain; charset=UTF-8;")
    public String getToMonthCode(@Context HttpServletRequest request,
            @QueryParam("code") String code) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonth = sdf.format(cal.getTime());

        Gson gson = new Gson();
        return gson.toJson(getMonthSalesCode(code, yearMonth));
    }

    @GET
    @Path("History/code")
    @Produces("text/plain; charset=UTF-8;")
    public String getToMonthCodeH(@Context HttpServletRequest request,
            @QueryParam("code") String code,
            @QueryParam("yyyymm") String yyyymm) {

        Gson gson = new Gson();
        return gson.toJson(getMonthSalesCode(code, yyyymm));
    }

    // 本月通路資訊
    @GET
    @Path("Today/channel")
    @Produces("text/plain; charset=UTF-8;")
    public String getToChannel(@Context HttpServletRequest request,
            @QueryParam("sapid") String sapid) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonth = sdf.format(cal.getTime());

        Gson gson = new Gson();
        return gson.toJson(getToChannel(sapid, yearMonth));
    }

    // 本月全業務
    @GET
    @Path("Today/Allsales")
    @Produces("text/plain; charset=UTF-8;")
    public String getToChannel(@Context HttpServletRequest request) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonth = sdf.format(cal.getTime());

        Gson gson = new Gson();
        return gson.toJson(getToAllsales(yearMonth));
    }

    // 本月全通路
    @GET
    @Path("Today/Allchannels")
    @Produces("text/plain; charset=UTF-8;")
    public String getToChannels(@Context HttpServletRequest request,
            @QueryParam("loginAccount") String loginAccount) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonth = sdf.format(cal.getTime());

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();
        String SystemP = System.getProperty("com.taiwancement.sso.restfulUser");
        if (request.isUserInRole("Administrators")
                || (SystemP != null && SystemP.equals(name))) {
            name = loginAccount;
        }
        TcUser user = userFacade.findUserByLoginAccount(name);
        SkSalesMember members = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(members);

        Gson gson = new Gson();
        if (channels.getCode().contains("T")) {
            return gson.toJson(getToAllchannels(yearMonth, user));
        } else {
            return gson.toJson(getMonthSalesCode(channels.getCode(), yearMonth));
        }
    }

    // 全通路
    @GET
    @Path("History/Allchannels")
    @Produces("text/plain; charset=UTF-8;")
    public String getToChannelsH(@Context HttpServletRequest request,
            @QueryParam("yyyymm") String yyyymm,
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
        TcUser user = userFacade.findUserByLoginAccount(name);

        SkSalesMember members = memberFacade.findByMember(user);
        SkSalesChannels channels = channelMemberFacade.findBySalesMember(members);

        Gson gson = new Gson();
        if (channels.getCode().contains("T")) {
            return gson.toJson(getToAllchannels(yyyymm, user));
        } else {
            return gson.toJson(getMonthSalesCode(channels.getCode(), yyyymm));
        }
    }

    private List<AchievementFactVO> getToAllchannels(String year, TcUser user) {
        List<AchievementFactVO> vol = new ArrayList<AchievementFactVO>();
        if (user == null) {
            return vol;
        }
        vol = achieveFacade.findChannelReport(year);
        if (vol.isEmpty()) {
            return vol;
        }
        BigDecimal targetRate9 = vol.get(0).getTargetRate().multiply(new BigDecimal("0.9")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal targetRate7 = vol.get(0).getTargetRate().multiply(new BigDecimal("0.7")).setScale(0, RoundingMode.HALF_UP);

        BigDecimal subtotal_InvoiceAmount = BigDecimal.ZERO,
                subtotal_PremiumDiscount = BigDecimal.ZERO,
                subtotal_SalesReturn = BigDecimal.ZERO,
                subtotal_SalesDiscount = BigDecimal.ZERO,
                subtotal_SalesAmount = BigDecimal.ZERO,
                subtotal_GrossProfitRate = BigDecimal.ZERO,
                subtotal_AchievementRate = BigDecimal.ZERO,
                subtotal_Budget = BigDecimal.ZERO,
                subtotal_cost = BigDecimal.ZERO;
//     
        SkSalesMember member = memberFacade.findByMember(user);
        SkSalesChannels channel = channelMemberFacade.findBySalesMember(member);
//                ChannelsFacade.findByCode(member.getCode());
        List<SkSalesChannels> channels = ChannelsFacade.findChildChannel(channel);

        List<SkSalesChannelMember> list = channelMemberFacade.findByChannels(channel);
        for (SkSalesChannels skc : channels) {
            list.addAll(channelMemberFacade.findByChannels(skc));
        }

        for (AchievementFactVO vo : vol) {
            vo.setOwn("N");
            for (SkSalesChannelMember mchannel : list) {
                String mcode = mchannel.getSalesChannel().getCode();
                String vcode = vo.getChannel();
                if (vcode.equals(mcode)) {
                    vo.setOwn("Y");
                }
                if (channel.getCode().contains("T")) {
                    vo.setOwn("Y");
                }
            }

            BigDecimal achievementRate = vo.getAchievementRate();
            achievementRate = achievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
            vo.setAchievementRate(achievementRate);

            BigDecimal grossProfitRate = vo.getGrossProfitRate();
            grossProfitRate = grossProfitRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
            vo.setGrossProfitRate(grossProfitRate);

            String color = "";
            if (achievementRate.compareTo(targetRate9) >= 0) {
                color = "G";
            }
            if (achievementRate.compareTo(targetRate9) < 0
                    && achievementRate.compareTo(targetRate7) >= 0) {
                color = "Y";
            }
            if (achievementRate.compareTo(targetRate7) < 0) {
                color = "R";
            }
            vo.setAchieColor(color);

            subtotal_InvoiceAmount = subtotal_InvoiceAmount.add(vo.getInvoiceAmount());
            subtotal_PremiumDiscount = subtotal_PremiumDiscount.add(vo.getPremiumDiscount());
            subtotal_SalesReturn = subtotal_SalesReturn.add(vo.getSalesReturn());
            subtotal_SalesDiscount = subtotal_SalesDiscount.add(vo.getSalesDiscount());
            subtotal_SalesAmount = subtotal_SalesAmount.add(vo.getSalesAmount());
//            subtotal_GrossProfitRate = subtotal_GrossProfitRate.add(vo.getGrossProfitRate());
            subtotal_GrossProfitRate = subtotal_SalesAmount
                    .subtract(subtotal_cost)
                    .add(subtotal_SalesReturn)
                    .divide(subtotal_SalesAmount, 4, RoundingMode.HALF_UP);

            subtotal_Budget = subtotal_Budget.add(vo.getBudget());
            subtotal_cost = subtotal_cost.add(vo.getCost());
            if (!subtotal_Budget.equals(BigDecimal.ZERO)) {
                subtotal_AchievementRate = subtotal_SalesAmount.
                        divide(subtotal_Budget, 4, RoundingMode.HALF_UP);
            }

            // cumulateBudget; //累計預算
            Calendar cal = Calendar.getInstance();

            // 判斷不等於當月就不除以天數
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            String yearMonthNow = sdf.format(cal.getTime());

            BigDecimal cumulateBudget = vo.getBudget();
            if (year.equals(yearMonthNow)) {
                int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int days = new Date().getDate();
                cumulateBudget = vo.getBudget().
                        multiply(new BigDecimal(days)).
                        divide(new BigDecimal(monthDays), 0, BigDecimal.ROUND_HALF_EVEN);
            }
            vo.setCumulateBudget(cumulateBudget);

            // cumulateeAR; //累計達成率  
            BigDecimal cumulateAR = BigDecimal.ZERO;
            if (!cumulateBudget.equals(BigDecimal.ZERO)) {
                cumulateAR = new BigDecimal("100").
                        multiply(vo.getSalesAmount()).
                        divide(cumulateBudget, 0, BigDecimal.ROUND_HALF_EVEN);
            }
            vo.setCumulateAR(cumulateAR);
        }
        subtotal_AchievementRate = subtotal_AchievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
        subtotal_GrossProfitRate = subtotal_GrossProfitRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
        AchievementFactVO vot = new AchievementFactVO();

        vot.setChannelName(
                "total");
        vot.setInvoiceAmount(subtotal_InvoiceAmount);

        vot.setPremiumDiscount(subtotal_PremiumDiscount);

        vot.setSalesReturn(subtotal_SalesReturn);

        vot.setSalesDiscount(subtotal_SalesDiscount);

        vot.setSalesAmount(subtotal_SalesAmount);

        vot.setGrossProfitRate(subtotal_GrossProfitRate);

        vot.setBudget(subtotal_Budget);

        vot.setCost(subtotal_cost);

        vot.setAchievementRate(subtotal_AchievementRate);
        String color = "";

        if (subtotal_AchievementRate.compareTo(targetRate9)
                >= 0) {
            color = "G";
        }

        if (subtotal_AchievementRate.compareTo(targetRate9)
                < 0
                && subtotal_AchievementRate.compareTo(targetRate7) >= 0) {
            color = "Y";
        }

        if (subtotal_AchievementRate.compareTo(targetRate7)
                < 0) {
            color = "R";
        }

        vot.setAchieColor(color);

        // cumulateBudget; //累計預算
        Calendar cal = Calendar.getInstance();

        // 判斷不等於當月就不除以天數
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonthNow = sdf.format(cal.getTime());

        BigDecimal cumulateBudget = vot.getBudget();
        if (year.equals(yearMonthNow)) {
            int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int days = new Date().getDate();
            cumulateBudget = vot.getBudget().
                    multiply(new BigDecimal(days)).
                    divide(new BigDecimal(monthDays), 0, BigDecimal.ROUND_HALF_EVEN);
        }
        vot.setCumulateBudget(cumulateBudget);

        // cumulateeAR; //累計達成率  
        BigDecimal cumulateAR = BigDecimal.ZERO;
        if (!cumulateBudget.equals(BigDecimal.ZERO)) {
            cumulateAR = new BigDecimal("100").
                    multiply(vot.getSalesAmount()).
                    divide(cumulateBudget, 0, BigDecimal.ROUND_HALF_EVEN);
        }
        vot.setCumulateAR(cumulateAR);


        vol.add(vot);
        return vol;
    }

    private List<AchievementFactVO> getToAllsales(String year) {
        List<AchievementFactVO> vol = achieveFacade.findByYearMonth(year);
        return vol;
    }

//
//     sendMailChannel("T31", year_month);
//     sendMailChannel("T32", year_month);
//     sendMailChannel("T33", year_month);
//    
    private List<AchievementFactVO> getToChannel(String code, String year) {
        List<AchievementFactVO> vol = new ArrayList<AchievementFactVO>();

        SkSalesChannels channel = ChannelsFacade.findByCode(code);
        List<SkSalesChannels> channels = ChannelsFacade.findChildChannel(channel);

        List<SkSalesChannelMember> list = channelMemberFacade.findByChannels(channel);
        for (SkSalesChannels skc : channels) {
            list.addAll(channelMemberFacade.findByChannels(skc));
        }

        List<AchievementFactVO> volt = achieveFacade.findByYearMonth(year);
        for (SkSalesChannelMember sb : list) {
            String _sapid = sb.getSalesMember().getCode();
            for (AchievementFactVO vo : volt) {
                String _channel = vo.getSapId();
                if (_channel.equals(_sapid)) {
                    vol.add(vo);
                }
            }
        }

        return vol;
    }

    private List<AchievementFactVO> getMonthSales(String sapid, String yearMonth) {
        List<AchievementFactVO> vol = new ArrayList<AchievementFactVO>();
        List<AchievementFactVO> volt = achieveFacade.findByYearMonth(yearMonth);
        DimSkSaleName dim = achieveFacade.findSapIDsByCode(sapid);
        for (AchievementFactVO vo : volt) {
            if (dim != null) {
                if (vo.getChannel().equals(dim.getCode().getCode())) {
                    vol.add(vo);
                }
            }
        }
        return vol;
    }

    private List<AchievementFactVO> getMonthSalesCode(String code, String yearMonth) {
        List<AchievementFactVO> vol = new ArrayList<AchievementFactVO>();
        List<AchievementFactVO> volt = achieveFacade.findByYearMonth(yearMonth);
        if (volt.isEmpty()) {
            return vol;
        }

        BigDecimal targetRate9 = BigDecimal.ZERO,
                targetRate7 = BigDecimal.ZERO;

        targetRate9 = volt.get(0).getTargetRate().multiply(new BigDecimal("90")).setScale(0, RoundingMode.HALF_UP);
        targetRate7 = volt.get(0).getTargetRate().multiply(new BigDecimal("70")).setScale(0, RoundingMode.HALF_UP);

        BigDecimal subtotal_InvoiceAmount = BigDecimal.ZERO,
                subtotal_PremiumDiscount = BigDecimal.ZERO,
                subtotal_SalesReturn = BigDecimal.ZERO,
                subtotal_SalesDiscount = BigDecimal.ZERO,
                subtotal_SalesAmount = BigDecimal.ZERO,
                subtotal_GrossProfitRate = BigDecimal.ZERO,
                subtotal_AchievementRate = BigDecimal.ZERO,
                subtotal_Budget = BigDecimal.ZERO,
                subtotal_cost = BigDecimal.ZERO;

        for (AchievementFactVO vo : volt) {
            if (code != null) {
                if (vo.getChannel().equals(code)) {

                    BigDecimal achievementRate = vo.getAchievementRate();
                    achievementRate = achievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
                    vo.setAchievementRate(achievementRate);

                    BigDecimal grossProfitRate = vo.getGrossProfitRate();
                    grossProfitRate = grossProfitRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
                    vo.setGrossProfitRate(grossProfitRate);

                    String color = "";
                    if (achievementRate.compareTo(targetRate9) >= 0) {
                        color = "G";
                    }
                    if (achievementRate.compareTo(targetRate9) < 0
                            && achievementRate.compareTo(targetRate7) >= 0) {
                        color = "Y";
                    }
                    if (achievementRate.compareTo(targetRate7) < 0) {
                        color = "R";
                    }
                    vo.setAchieColor(color);

                    subtotal_InvoiceAmount = subtotal_InvoiceAmount.add(vo.getInvoiceAmount());
                    subtotal_PremiumDiscount = subtotal_PremiumDiscount.add(vo.getPremiumDiscount());
                    subtotal_SalesReturn = subtotal_SalesReturn.add(vo.getSalesReturn());
                    subtotal_SalesDiscount = subtotal_SalesDiscount.add(vo.getSalesDiscount());
                    subtotal_SalesAmount = subtotal_SalesAmount.add(vo.getSalesAmount());
//            subtotal_GrossProfitRate = subtotal_GrossProfitRate.add(vo.getGrossProfitRate());
                    subtotal_Budget = subtotal_Budget.add(vo.getBudget());
                    subtotal_cost = subtotal_cost.add(vo.getCost());

                    vol.add(vo);
                }
            }

            // cumulateBudget; //累計預算
            Calendar cal = Calendar.getInstance();

            // 判斷不等於當月就不除以天數
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            String yearMonthNow = sdf.format(cal.getTime());

            BigDecimal cumulateBudget = vo.getBudget();
            if (yearMonth.equals(yearMonthNow)) {
                int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int days = new Date().getDate();
                cumulateBudget = vo.getBudget().
                        multiply(new BigDecimal(days)).
                        divide(new BigDecimal(monthDays), 0, BigDecimal.ROUND_HALF_EVEN);
            }
            vo.setCumulateBudget(cumulateBudget);

            // cumulateeAR; //累計達成率  
            BigDecimal cumulateAR = BigDecimal.ZERO;
            if (!cumulateBudget.equals(BigDecimal.ZERO)) {
                cumulateAR = new BigDecimal("100").
                        multiply(vo.getSalesAmount()).
                        divide(cumulateBudget, 0, BigDecimal.ROUND_HALF_EVEN);
            }
            vo.setCumulateAR(cumulateAR);
        }

        subtotal_GrossProfitRate = (subtotal_SalesAmount
                .subtract(subtotal_cost)
                .add(subtotal_SalesReturn))
                .divide(subtotal_SalesAmount, 4, RoundingMode.HALF_UP);

        if (!subtotal_Budget.equals(BigDecimal.ZERO)) {
            subtotal_AchievementRate = subtotal_SalesAmount.
                    divide(subtotal_Budget, 4, RoundingMode.HALF_UP);
        }

        subtotal_AchievementRate = subtotal_AchievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
        subtotal_GrossProfitRate = subtotal_GrossProfitRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
        AchievementFactVO vot = new AchievementFactVO();
        vot.setChannelName("total");
        vot.setInvoiceAmount(subtotal_InvoiceAmount);
        vot.setPremiumDiscount(subtotal_PremiumDiscount);
        vot.setSalesReturn(subtotal_SalesReturn);
        vot.setSalesDiscount(subtotal_SalesDiscount);
        vot.setSalesAmount(subtotal_SalesAmount);
        vot.setGrossProfitRate(subtotal_GrossProfitRate);
        vot.setBudget(subtotal_Budget);
        vot.setCost(subtotal_cost);
        vot.setAchievementRate(subtotal_AchievementRate);

        String color = "";
        if (subtotal_AchievementRate.compareTo(targetRate9) >= 0) {
            color = "G";
        }
        if (subtotal_AchievementRate.compareTo(targetRate9) < 0
                && subtotal_AchievementRate.compareTo(targetRate7) >= 0) {
            color = "Y";
        }
        if (subtotal_AchievementRate.compareTo(targetRate7) < 0) {
            color = "R";
        }
        vot.setAchieColor(color);

        // cumulateBudget; //累計預算
        Calendar cal = Calendar.getInstance();

        // 判斷不等於當月就不除以天數
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String yearMonthNow = sdf.format(cal.getTime());

        BigDecimal cumulateBudget = vot.getBudget();
        if (yearMonth.equals(yearMonthNow)) {
            int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int days = new Date().getDate();
            cumulateBudget = vot.getBudget().
                    multiply(new BigDecimal(days)).
                    divide(new BigDecimal(monthDays), 0, BigDecimal.ROUND_HALF_EVEN);
        }
        vot.setCumulateBudget(cumulateBudget);

        // cumulateeAR; //累計達成率  
        BigDecimal cumulateAR = BigDecimal.ZERO;
        if (!cumulateBudget.equals(BigDecimal.ZERO)) {
            cumulateAR = new BigDecimal("100").
                    multiply(vot.getSalesAmount()).
                    divide(cumulateBudget, 0, BigDecimal.ROUND_HALF_EVEN);
        }
        vot.setCumulateAR(cumulateAR);

        vol.add(vot);

        return vol;
    }
}
