/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.schedule.TcScheduleFacade;
import com.tcci.fc.util.VelocityMail;
import com.tcci.sksp.vo.AchievementFactVO;
import com.tcci.sksp.entity.DimSkSaleName;
import com.tcci.sksp.entity.FactSkAchievementMonth;
import com.tcci.sksp.entity.org.SkSalesChannelMember;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author carl.lin
 */
@Stateless
public class SendAchievementMail {

    private final static Logger logger = LoggerFactory.getLogger(SendAchievementMail.class);
    @PersistenceContext(unitName = "dmModel")
    private EntityManager em;
    @EJB
    private TcScheduleFacade scheduleFacade;
    @EJB
    private SkSalesMemberFacade memberFacade;
    @EJB
    SkSalesChannelsFacade ChannelsFacade;
    @EJB
    SkSalesChannelMemberFacade channelMemberFacade;
    @EJB
    TcUserFacade tcUserFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    // 每個星期1,2,3,4,5 20:00 send mail
//    @Schedule(dayOfWeek = "Mon, Tue, Wed, Thu, Fri", hour = "19", minute = "00", persistent = false)
//    public void scheduleSendAchievementMail() {
//        try {
//            // 僅一台server可以執行排程
//            if (scheduleFacade.canExecute("SendAchievementMail", 30)) {
//                sendAllMails();
//            } else {
//                logger.warn("SendAchievementMail not execute");
//            }
//        } catch (Exception ex) {
//            logger.error("SendAchievementMail exception", ex);
//        }
//    }

    public String getTitle() {
//      xxx年xx 月 xx 日業績達成率報表        
        String retString = "日業績達成率報表";

        Date myDate = new Date();
        String thisYear = Integer.toString(myDate.getYear() + 1900);
        String thisMonth = Integer.toString(myDate.getMonth() + 1);
        String thisDate = Integer.toString(myDate.getDate());

        retString = thisYear.concat("年").concat(thisMonth)
                .concat("月").concat(thisDate).concat(retString);

        return retString;
    }

    // send mail by batch job
    public void sendAllMails() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        month++;
        String tempMonth = Integer.toString(month);
        if (tempMonth.length() == 1) {
            tempMonth = "0".concat(tempMonth);
        }
        String year_month = Integer.toString(year).concat(tempMonth);

        sendMailT(year_month);

        //send mail  all sales assistant
        sendMailAssistant(year_month);

        // channel boss send mail
        sendMailChannel("T31", year_month);
        sendMailChannel("T32", year_month);
        sendMailChannel("T33", year_month);

        // sales send mail
        List<SkSalesMember> sml = memberFacade.findAll();
        for (SkSalesMember sm : sml) {
            if (!sm.getCode().contains("T")) {
                sendMail(sm.getCode(), year_month, "N");
            }
        }
    }

    public void sendMailChannel(String code, String year) {
        List<AchievementFactVO> vol = new ArrayList<AchievementFactVO>();
        SkSalesChannels channel = ChannelsFacade.findByCode(code);
        List<SkSalesChannels> channels = ChannelsFacade.findChildChannel(channel);

        List<SkSalesChannelMember> list = channelMemberFacade.findByChannels(channel);
        for (SkSalesChannels skc : channels) {
            list.addAll(channelMemberFacade.findByChannels(skc));
        }

        List<AchievementFactVO> volt = findByYearMonth(year);
        for (SkSalesChannelMember sb : list) {
            String _sapid = sb.getSalesMember().getCode();
            for (AchievementFactVO vo : volt) {
                String _channel = vo.getSapId();
                if (_channel.equals(_sapid)) {
                    vol.add(vo);
                }
            }
        }
        if (!vol.isEmpty()) {
            try {
                // create temple excel
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Channel").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Channel");

                File detailTempFile1 = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_TotalChannels").concat(".xls"));
                // total channel's data
                List<AchievementFactVO> vol1 = findChannelReport(year);
                createAchievExcel(vol1, detailTempFile1, "TotalChannels");

                sendMailActionC(code, detailTempFile, detailTempFile1, vol, vol1);

                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();
                detailTempFile1.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 年度業績月結總表 yearMonth:yyyyMM
    public List<AchievementFactVO> findChannelReport(String yearMonth) {
        List<AchievementFactVO> result = new ArrayList<AchievementFactVO>();
//        String sql = "SELECT YEAR_MONTH,CHANNEL,AMOUNT,PREMIUM_DISCOUNT,
//                      SALES_RETURN,SALES_DISCOUNT,BUDGET,COST,NAME"
//                + " FROM V_SALES_ACHIEVEMENT_CHANNEL v "
//                + " left join DIM_SK_CHANNEL_NAME  d "
//                + " on v.CHANNEL = d.CODE"
//                + " WHERE YEAR_MONTH = #yearMonth";

        String sql = " SELECT  YEAR_MONTH, CHANNEL, AMOUNT, PREMIUM_DISCOUNT, "
                + "   SALES_RETURN, SALES_DISCOUNT, BUDGET, "
                + "   COST, dc.name NAME, CREATEDATE, TARGET_RATE "
                + " FROM  FACT_SK_ACHIEVEMENT_CHANNEL m "
                + "   left join dim_sk_channel_name dc "
                + "   on dc.code=m.channel "
                + " WHERE YEAR_MONTH=#yearMonth  AND CREATEDATE= "
                + "     (SELECT MAX(F.CREATEDATE) FROM FACT_SK_ACHIEVEMENT_CHANNEL F "
                + "       WHERE F.YEAR_MONTH=#yearMonth) ";

        Query q = em.createNativeQuery(sql);
        q.setParameter("yearMonth", yearMonth);
        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementFactVO vo = new AchievementFactVO();
            vo.setYearMonth((String) v[idx++]);
            vo.setChannel((String) v[idx++]);
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            BigDecimal budget = (BigDecimal) v[idx++];
            vo.setBudget(budget);
            BigDecimal cost = (BigDecimal) v[idx++];
            vo.setCost(cost);

            vo.setChannelName(vo.getChannel());

            String saleName = "temp";
            if (StringUtils.isNotEmpty((String) v[idx++])) {
                saleName = (String) v[--idx];
            }
            vo.setSaleName(saleName);

            BigDecimal salesAmount = vo.getInvoiceAmount().
                    subtract(vo.getPremiumDiscount()).
                    subtract(vo.getSalesDiscount()).
                    subtract(vo.getSalesReturn());
            vo.setSalesAmount(salesAmount);
            if (salesAmount.compareTo(BigDecimal.ZERO) == 0) {
                vo.setGrossProfitRate(BigDecimal.ZERO);
            } else {
                vo.setGrossProfitRate(salesAmount.subtract(cost).add(vo.getSalesReturn()).divide(salesAmount, 4, RoundingMode.HALF_UP));
            }
            if (budget.compareTo(BigDecimal.ZERO) == 0) {
                vo.setAchievementRate(BigDecimal.ZERO);
            } else {
                vo.setAchievementRate(salesAmount.divide(budget, 4, RoundingMode.HALF_UP));
            }

//            Calendar cal = Calendar.getInstance();
//            int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//            int days = new Date().getDate();
//            BigDecimal targetRate = new BigDecimal("100").
//                    multiply(new BigDecimal(days)).
//                    divide(new BigDecimal(monthDays), 0, BigDecimal.ROUND_HALF_EVEN);

            BigDecimal targetRate = new BigDecimal("100").
                    multiply((BigDecimal) v[10]);
            vo.setTargetRate(targetRate);
            vo.setAchieColor("rptValue");
            result.add(vo);
        }
        return result;
    }

    public void sendMailAssistant(String year) {
        List<AchievementFactVO> vol = findByYearMonth(year);
        if (!vol.isEmpty()) {
            try {
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Total").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Total");

                // send all sales Assistant email
                sendMailActionA(detailTempFile, vol);

                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMailActionC(String code, File tempFile, File tempFile1,
            List<AchievementFactVO> vol, List<AchievementFactVO> vol1) throws Exception {
        try {
            SkSalesChannels channel = ChannelsFacade.findByCode(code);

            String to = channel.getManager().getEmail();
            String subject = code.concat("區-區經理:").
                    concat(channel.getManager().getCname()).concat(getTitle());
            HashMap<String, Object> mailBean = new HashMap<String, Object>();

            mailBean.put(VelocityMail.SUBJECT, subject);
            mailBean.put(VelocityMail.TO, to);
            mailBean.put("content", subject);
            mailBean.put("titleLabel", getTitle());
            mailBean.put("targetRate", vol.get(0).getTargetRate().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate9", vol.get(0).getTargetRate().multiply(new BigDecimal("90")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate7", vol.get(0).getTargetRate().multiply(new BigDecimal("70")).setScale(0, RoundingMode.HALF_UP));

            HashMap<String, String> attachmentFiles = new HashMap<String, String>();
            String key = tempFile.getAbsolutePath();
            String value = tempFile.getName();
            attachmentFiles.put(key, value);
            String key1 = tempFile1.getAbsolutePath();
            String value1 = tempFile1.getName();
            attachmentFiles.put(key1, value1);
            mailBean.put(VelocityMail.ATTACHMENT, attachmentFiles);

            List<AchievementFactVO> mVol = sendMailList(vol, "");
            mailBean.put("reportItems", mVol);
            List<AchievementFactVO> mVol1 = sendMailList(vol1, "TotalChannels");
            mailBean.put("reportItems1", mVol1);

            VelocityMail.sendMail(mailBean, "achievementChannel.vm");

        } catch (Exception me) {
            me.printStackTrace();
        }

    }
//    

    private void sendMailActionA(File tempFile, List<AchievementFactVO> vol)
            throws Exception {
        try {
            String to = "";
            HashMap<String, Object> mailBean = new HashMap<String, Object>();
            // send all Assistant email
            List<TcUser> assistants = tcUserFacade.findUsersByGroupCode("Assistant");
            String subject = getTitle();
            // email to "jennifer.lin@sking.com.tw"
            // assistants.add(tcUserFacade.getUserByEmail("jennifer.lin@sking.com.tw"));
            for (TcUser tc : assistants) {
                if (!tc.getDisabled()) {
                    if (to.length() > 0) {
                        to = to.concat(",");
                    }
                    to = to.concat(tc.getEmail());
                }
            }
            mailBean.put(VelocityMail.SUBJECT, subject);
            mailBean.put(VelocityMail.TO, to);
            mailBean.put("content", subject);
            mailBean.put("titleLabel", getTitle());
            mailBean.put("targetRate", vol.get(0).getTargetRate().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate9", vol.get(0).getTargetRate().multiply(new BigDecimal("90")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate7", vol.get(0).getTargetRate().multiply(new BigDecimal("70")).setScale(0, RoundingMode.HALF_UP));

            HashMap<String, String> attachmentFiles = new HashMap<String, String>();
            String key = tempFile.getAbsolutePath();
            String value = tempFile.getName();
            attachmentFiles.put(key, value);
            mailBean.put(VelocityMail.ATTACHMENT, attachmentFiles);
            List<AchievementFactVO> mVol = sendMailList(vol, "");
            mailBean.put("reportItems", mVol);

            VelocityMail.sendMail(mailBean, "achievementSales.vm");

        } catch (Exception me) {
            me.printStackTrace();
        }
    }

    public void sendMailT(String year) {
        List<AchievementFactVO> vol = findByYearMonth(year);
        if (!vol.isEmpty()) {
            try {
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Total").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Total");

                // send e'mail to T1 
                sendMailActionT(detailTempFile, vol);

                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMailActionT(File tempFile,
            List<AchievementFactVO> vol) throws Exception {
        try {
            String to = "";
            List<SkSalesMember> T1List = memberFacade.findByTgroup("T1");
            // add T2 to sent mail
            List<SkSalesMember> T2List = memberFacade.findByTgroup("T2");
            if (!T2List.isEmpty()){
                T1List.addAll(T2List);
            }
            if (!T1List.isEmpty()) {
                for (SkSalesMember skm : T1List) {
                    SkSalesMember sales = memberFacade.findByCode(skm.getCode());

                    if (sales.getMember() != null && !sales.getMember().getDisabled()) {
                        if (to.length() > 0) {
                            to = to.concat(",");
                        }
                        to = to.concat(sales.getMember().getEmail());
                    }
                }
            }
            String subject = getTitle();
            HashMap<String, Object> mailBean = new HashMap<String, Object>();

            mailBean.put(VelocityMail.SUBJECT, subject);
            mailBean.put(VelocityMail.TO, to);
            mailBean.put("content", subject);
            mailBean.put("titleLabel", getTitle());
            mailBean.put("targetRate", vol.get(0).getTargetRate().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate9", vol.get(0).getTargetRate().multiply(new BigDecimal("90")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate7", vol.get(0).getTargetRate().multiply(new BigDecimal("70")).setScale(0, RoundingMode.HALF_UP));

            HashMap<String, String> attachmentFiles = new HashMap<String, String>();
            String key = tempFile.getAbsolutePath();
            String value = tempFile.getName();
            attachmentFiles.put(key, value);
            mailBean.put(VelocityMail.ATTACHMENT, attachmentFiles);
            List<AchievementFactVO> mVol = sendMailList(vol, "");
            mailBean.put("reportItems", mVol);

            VelocityMail.sendMail(mailBean, "achievementSales.vm");

        } catch (Exception me) {
            me.printStackTrace();
        }

    }

    // send mail by interactive
    public void sendMail(String sapid, String yearMonth, String sendBoss) {
        List<AchievementFactVO> vol = new ArrayList<AchievementFactVO>();
        List<AchievementFactVO> volt = findByYearMonth(yearMonth);
        DimSkSaleName dim = findSapIDsByCode(sapid);
        for (AchievementFactVO vo : volt) {
            if (dim != null) {
                if (vo.getChannel().equals(dim.getCode().getCode())) {
                    vol.add(vo);
                }
            }
        }
        if (!vol.isEmpty()) {
            try {
                // create temple excel
//                File detailTempFile = File.createTempFile(getTitle().concat("_Sales"), ".xls");
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Sales").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Sales");

                //   send e'mail to sales and cc to his boss
                SkSalesMember sales = memberFacade.findByCode(sapid);
                if (sales.getMember() != null) {
                    sendMailAction(sapid, detailTempFile, vol, sendBoss);
                }
                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<AchievementFactVO> sendMailList(List<AchievementFactVO> vol,
            String totalChannel) {
        List<AchievementFactVO> retList = new ArrayList<AchievementFactVO>();
        BigDecimal channel_InvoiceAmount = BigDecimal.ZERO,
                channel_PremiumDiscount = BigDecimal.ZERO,
                channel_SalesReturn = BigDecimal.ZERO,
                channel_SalesDiscount = BigDecimal.ZERO,
                channel_SalesAmount = BigDecimal.ZERO,
                channel_GrossProfitRate = BigDecimal.ZERO,
                channel_AchievementRate = BigDecimal.ZERO,
                channel_Budget = BigDecimal.ZERO,
                channel_target = BigDecimal.ZERO,
                channel_cost = BigDecimal.ZERO;
        String channel_Color = "";


        BigDecimal total_InvoiceAmount = BigDecimal.ZERO,
                total_PremiumDiscount = BigDecimal.ZERO,
                total_SalesReturn = BigDecimal.ZERO,
                total_SalesDiscount = BigDecimal.ZERO,
                total_SalesAmount = BigDecimal.ZERO,
                total_AchievementRate = BigDecimal.ZERO,
                total_Budget = BigDecimal.ZERO,
                total_cost = BigDecimal.ZERO;

        String channelName = "";
        for (AchievementFactVO vo : vol) {
            String _channelName = vo.getChannelName();
            AchievementFactVO tmpVo = new AchievementFactVO();
            if (StringUtils.isEmpty(channelName)) {
                channelName = vo.getChannelName();
            }

            if (!StringUtils.equals(channelName, _channelName)) {
                AchievementFactVO chlVo = new AchievementFactVO();
                chlVo.setChannelName(channelName);
                chlVo.setSaleName("subtotal");
                chlVo.setInvoiceAmount(channel_InvoiceAmount);
                chlVo.setPremiumDiscount(channel_PremiumDiscount);
                chlVo.setSalesReturn(channel_SalesReturn);
                chlVo.setSalesDiscount(channel_SalesDiscount);
                chlVo.setSalesAmount(channel_SalesAmount);

                if (channel_Budget.compareTo(BigDecimal.ZERO) > 0) {
                    channel_AchievementRate = channel_SalesAmount
                            .divide(channel_Budget, 4, RoundingMode.HALF_UP);
                    chlVo.setAchievementRate(channel_AchievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
                    if (totalChannel.contains("TotalChannels")) {
                        channel_AchievementRate = channel_AchievementRate.multiply(new BigDecimal("100"));
                    }
                }

                chlVo.setCost(channel_cost);

                channel_GrossProfitRate = chlVo.getSalesAmount()
                        .subtract(chlVo.getCost())
                        .add(chlVo.getSalesReturn())
                        .divide(chlVo.getSalesAmount(), 4, RoundingMode.HALF_UP);

                chlVo.setGrossProfitRate(channel_GrossProfitRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
                chlVo.setBudget(channel_Budget);

                // setup channel's color
                BigDecimal target = vo.getTargetRate();
                BigDecimal tr90 = target.multiply(new BigDecimal("0.9"));
                BigDecimal tr70 = target.multiply(new BigDecimal("0.7"));

                if (channel_AchievementRate.compareTo(tr90) > 0) {
                    chlVo.setAchieColor("Lime");
                }
                if (channel_AchievementRate.compareTo(tr70) > 0
                        && channel_AchievementRate.compareTo(tr90) < 0) {
                    chlVo.setAchieColor("Yellow");
                }
                if (channel_AchievementRate.compareTo(tr70) < 0) {
                    chlVo.setAchieColor("Red");
                }
                chlVo.setAchieSubColor("rptValue");
                retList.add(chlVo);

                total_InvoiceAmount = total_InvoiceAmount.add(channel_InvoiceAmount);
                total_PremiumDiscount = total_PremiumDiscount.add(channel_PremiumDiscount);
                total_SalesReturn = total_SalesReturn.add(channel_SalesReturn);
                total_SalesDiscount = total_SalesDiscount.add(channel_SalesDiscount);
                total_SalesAmount = total_SalesAmount.add(channel_SalesAmount);
                total_Budget = total_Budget.add(channel_Budget);
                total_cost = total_cost.add(channel_cost);

                channel_InvoiceAmount = BigDecimal.ZERO;
                channel_PremiumDiscount = BigDecimal.ZERO;
                channel_SalesReturn = BigDecimal.ZERO;
                channel_SalesDiscount = BigDecimal.ZERO;
                channel_SalesAmount = BigDecimal.ZERO;
                channel_GrossProfitRate = BigDecimal.ZERO;
                channel_AchievementRate = BigDecimal.ZERO;
                channel_Budget = BigDecimal.ZERO;
                channel_cost = BigDecimal.ZERO;
                channelName = _channelName;
            }

            tmpVo.setChannelName(vo.getChannelName());

            tmpVo.setSaleName(vo.getSaleName());
            if (vo.getSapId() != null) {
                tmpVo.setSaleName(vo.getSapId().concat(vo.getSaleName()));
            }
            tmpVo.setInvoiceAmount(vo.getInvoiceAmount());
            tmpVo.setPremiumDiscount(vo.getPremiumDiscount());
            tmpVo.setSalesReturn(vo.getSalesReturn());
            tmpVo.setSalesDiscount(vo.getSalesDiscount());
            tmpVo.setSalesAmount(vo.getSalesAmount());
            tmpVo.setGrossProfitRate(vo.getGrossProfitRate().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
            tmpVo.setAchievementRate(vo.getAchievementRate().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
            tmpVo.setBudget(vo.getBudget());

            BigDecimal target = vo.getTargetRate();
            BigDecimal tr90 = target.multiply(new BigDecimal("0.9"));
            BigDecimal tr70 = target.multiply(new BigDecimal("0.7"));
            BigDecimal achievementRate = vo.getAchievementRate();
             if (totalChannel.contains("TotalChannels")) {
                achievementRate = achievementRate.multiply(new BigDecimal("100"));
            }

            tmpVo.setAchieColor("rptValue");
            if (achievementRate.compareTo(tr90) > 0) {
                tmpVo.setAchieColor("Lime");
            }
            if (achievementRate.compareTo(tr70) > 0
                    && achievementRate.compareTo(tr90) < 0) {
                tmpVo.setAchieColor("Yellow");
            }
            if (achievementRate.compareTo(tr70) < 0) {
                tmpVo.setAchieColor("Red");
            }

            retList.add(tmpVo);

            channel_InvoiceAmount = channel_InvoiceAmount.add(vo.getInvoiceAmount());
            channel_PremiumDiscount = channel_PremiumDiscount.add(vo.getPremiumDiscount());
            channel_SalesReturn = channel_SalesReturn.add(vo.getSalesReturn());
            channel_SalesDiscount = channel_SalesDiscount.add(vo.getSalesDiscount());
            channel_SalesAmount = channel_SalesAmount.add(vo.getSalesAmount());
            channel_Budget = channel_Budget.add(vo.getBudget());
            channel_cost = channel_cost.add(vo.getCost());
            channel_target = vo.getTargetRate();

        } // end of for
        AchievementFactVO channelVo = new AchievementFactVO();
        channelVo.setChannelName("");
        channelVo.setSaleName("subtotal");
        channelVo.setInvoiceAmount(channel_InvoiceAmount);
        channelVo.setPremiumDiscount(channel_PremiumDiscount);
        channelVo.setSalesReturn(channel_SalesReturn);
        channelVo.setSalesDiscount(channel_SalesDiscount);
        channelVo.setSalesAmount(channel_SalesAmount);

        if (channel_Budget.compareTo(BigDecimal.ZERO) > 0) {
            channel_AchievementRate = channel_SalesAmount
                    .divide(channel_Budget, 4, RoundingMode.HALF_UP);
            if (totalChannel.contains("TotalChannels")) {
                channel_AchievementRate = channel_AchievementRate.multiply(new BigDecimal("100"));
            }
        }

        channelVo.setAchievementRate(channel_AchievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
        channelVo.setCost(channel_cost);

        BigDecimal temp_GrossProfiRate = channelVo.getSalesAmount()
                .subtract(channelVo.getCost())
                .add(channelVo.getSalesReturn())
                .divide(channelVo.getSalesAmount(), 4, RoundingMode.HALF_UP);

        channelVo.setGrossProfitRate(temp_GrossProfiRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
        channelVo.setBudget(channel_Budget);
// setup channel's color
        BigDecimal target = channel_target;
        BigDecimal tr90 = target.multiply(new BigDecimal("0.9"));
        BigDecimal tr70 = target.multiply(new BigDecimal("0.7"));

        channelVo.setAchieColor("rptLabel");
        if (channel_AchievementRate.compareTo(tr90) > 0) {
            channelVo.setAchieColor("Lime");
        }
        if (channel_AchievementRate.compareTo(tr70) > 0
                && channel_AchievementRate.compareTo(tr90) < 0) {
            channelVo.setAchieColor("Yellow");
        }
        if (channel_AchievementRate.compareTo(tr70) < 0) {
            channelVo.setAchieColor("Red");
        }
        channelVo.setAchieSubColor("rptLabel");
        retList.add(channelVo);

        AchievementFactVO totalVo = new AchievementFactVO();

        total_InvoiceAmount = total_InvoiceAmount.add(channel_InvoiceAmount);
        total_PremiumDiscount = total_PremiumDiscount.add(channel_PremiumDiscount);
        total_SalesReturn = total_SalesReturn.add(channel_SalesReturn);
        total_SalesDiscount = total_SalesDiscount.add(channel_SalesDiscount);
        total_SalesAmount = total_SalesAmount.add(channel_SalesAmount);
        total_Budget = total_Budget.add(channel_Budget);
        total_cost = total_cost.add(channel_cost);

        totalVo.setChannelName("");
        totalVo.setSaleName("total");
        totalVo.setInvoiceAmount(total_InvoiceAmount);
        totalVo.setPremiumDiscount(total_PremiumDiscount);
        totalVo.setSalesReturn(total_SalesReturn);
        totalVo.setSalesDiscount(total_SalesDiscount);
        totalVo.setSalesAmount(total_SalesAmount);
        totalVo.setCost(total_cost);
        totalVo.setBudget(total_Budget);

        temp_GrossProfiRate = totalVo.getSalesAmount()
                .subtract(totalVo.getCost())
                .add(totalVo.getSalesReturn())
                .divide(totalVo.getSalesAmount(), 4, RoundingMode.HALF_UP);

        totalVo.setGrossProfitRate(temp_GrossProfiRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));

        total_AchievementRate = total_SalesAmount
                .divide(total_Budget, 4, RoundingMode.HALF_UP);

        totalVo.setAchievementRate(total_AchievementRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
        totalVo.setAchieColor("rptLabel");
        totalVo.setAchieSubColor("rptLabel");
        retList.add(totalVo);

        return retList;
    }

    private void sendMailAction(String sapid, File tempFile,
            List<AchievementFactVO> vol, String sendBoss) throws Exception {
        try {
            SkSalesMember sales = memberFacade.findByCode(sapid);
            String subject = " 第 " + sapid + " 區 " + sales.getMember().getCname() + getTitle();
            HashMap<String, Object> mailBean = new HashMap<String, Object>();
            String to = sales.getMember().getEmail();

            mailBean.put(VelocityMail.SUBJECT, subject);
            mailBean.put(VelocityMail.TO, to);
            mailBean.put("content", subject);
            mailBean.put("titleLabel", getTitle());
            mailBean.put("targetRate", vol.get(0).getTargetRate().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate9", vol.get(0).getTargetRate().multiply(new BigDecimal("90")).setScale(0, RoundingMode.HALF_UP));
            mailBean.put("targetRate7", vol.get(0).getTargetRate().multiply(new BigDecimal("70")).setScale(0, RoundingMode.HALF_UP));
            if (sendBoss.equals("Y")) {
                SkSalesChannels channel = channelMemberFacade.findBySalesMember(sales);
                if (channel.getManager() != null) {
                    String cc = "";
                    cc = channel.getManager().getEmail();
                    mailBean.put(VelocityMail.CC, cc);
                }
            }

            HashMap<String, String> attachmentFiles = new HashMap<String, String>();
            String key = tempFile.getAbsolutePath();
            String value = tempFile.getName();
            attachmentFiles.put(key, value);
            mailBean.put(VelocityMail.ATTACHMENT, attachmentFiles);
            List<AchievementFactVO> mVol = sendMailList(vol, "");
            mailBean.put("reportItems", mVol);
            VelocityMail.sendMail(mailBean, "achievementSales.vm");

        } catch (Exception me) {
            me.printStackTrace();
        }

    }

    public DimSkSaleName findSapIDsByCode(String sapid) {
        DimSkSaleName saleName = null;
        try {
            saleName = (DimSkSaleName) em.createNamedQuery("DimSkSaleName.findBySapId")
                    .setParameter("sapId", sapid)
                    .getSingleResult();
        } catch (NoResultException nre) {
        }
        return saleName;
    }

    public List<AchievementFactVO> findByYearMonth(String yearMonth) {
        List<AchievementFactVO> result = new ArrayList<AchievementFactVO>();
        String sql = "SELECT YEAR_MONTH,dc.CODE as CHANNEL, dc.NAME as CHANNELNAME, "
                + "   m.SAP_ID,NVL(ds.CNAME, 'temp') as SALES_NAME,"
                + "   INVOICE_AMOUNT,PREMIUM_DISCOUNT, "
                + "   SALES_RETURN,SALES_DISCOUNT,SALES_AMOUNT,GROSS_PROFIT_RATE, "
                + "   ACHIEVEMENT_RATE,BUDGET,TARGET_RATE,CREATEDATE,COST "
                + " FROM FACT_SK_ACHIEVEMENT_MONTH m"
                + "   left join dim_sk_sale_name ds"
                + "     on ds.sap_id=m.sap_id "
                + "   left join dim_sk_channel_name dc "
                + "     on dc.code=ds.code "
                + " WHERE YEAR_MONTH=#yearMonth AND CREATEDATE= "
                + "   (SELECT MAX(F.CREATEDATE) FROM FACT_SK_ACHIEVEMENT_MONTH F "
                + "    WHERE F.YEAR_MONTH=#yearMonth )"
                + " order by m.SAP_ID ";
        Query q = em.createNativeQuery(sql);
        q.setParameter("yearMonth", yearMonth);

        List list = q.getResultList();
        for (Object o : list) {
            Object[] v = (Object[]) o;
            int idx = 0;
            AchievementFactVO vo = new AchievementFactVO();
            vo.setYearMonth((String) v[idx++]);
            vo.setChannel((String) v[idx++]);
            vo.setChannelName((String) v[idx++]);
            vo.setSapId((String) v[idx++]);
            vo.setSaleName((String) v[idx++]);
            vo.setInvoiceAmount((BigDecimal) v[idx++]);
            vo.setPremiumDiscount((BigDecimal) v[idx++]);
            vo.setSalesReturn((BigDecimal) v[idx++]);
            vo.setSalesDiscount((BigDecimal) v[idx++]);
            vo.setSalesAmount((BigDecimal) v[idx++]);
            vo.setGrossProfitRate((BigDecimal) v[idx++]);
            vo.setAchievementRate((BigDecimal) v[idx++]);
            vo.setBudget((BigDecimal) v[idx++]);
            vo.setTargetRate((BigDecimal) v[idx++]);
            vo.setCreateDate((String) v[idx++]);
            vo.setCost((BigDecimal) v[idx++]);
            BigDecimal target = vo.getTargetRate();
            BigDecimal ar = vo.getAchievementRate();
            BigDecimal tr90 = target.multiply(new BigDecimal("0.9"));
            BigDecimal tr70 = target.multiply(new BigDecimal("0.7"));

            if (ar.compareTo(tr90) > 0) {
                vo.setAchieColor("Lime");
            }
            if (ar.compareTo(tr70) > 0 && ar.compareTo(tr90) < 0) {
                vo.setAchieColor("Yellow");
            }
            if (ar.compareTo(tr70) < 0) {
                vo.setAchieColor("Red");
            }
            result.add(vo);
        }
        return result;

    }

    public String findByMaxCreateDate(String sapid, String year) {
        String creatdate = "";

        TypedQuery<FactSkAchievementMonth> query = em.createQuery(
                " SELECT f FROM FactSkAchievementMonth f "
                + " WHERE f.factSkAchievementMonthPK.createdate "
                + "     = (select max(ff.factSkAchievementMonthPK.createdate) from "
                + "     FactSkAchievementMonth ff where ff.factSkAchievementMonthPK.sapId = :sapId "
                + "     and ff.factSkAchievementMonthPK.yearMonth =: yearMonth ",
                FactSkAchievementMonth.class);
        query.setParameter("sapId", sapid);
        query.setParameter("yearMonth", year);
        FactSkAchievementMonth ff = query.getSingleResult();
        creatdate = ff.getFactSkAchievementMonthPK().getCreatedate();

        return creatdate;
    }

    private void createAchievExcel(List<AchievementFactVO> lvo,
            File tempfile, String titleType) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        int rowCount = 0;
        HSSFRow titleRow0 = sheet.createRow(rowCount++);

        createAchievExcel(titleRow0, wb, "GREY");
        String title = getTitle().concat("-").concat(titleType);
        titleRow0.getCell(0).setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(
                0, 0, 0, 9));
        sheet.setColumnWidth(8, 5000);
        HSSFRow titleRow = sheet.createRow(rowCount++);
        createAchievExcel(titleRow, wb, "GREY");
        int idx = 0;
        titleRow.getCell(idx++).setCellValue("通路");
        titleRow.getCell(idx++).setCellValue("銷售群組");
        titleRow.getCell(idx++).setCellValue("發票金額");
        titleRow.getCell(idx++).setCellValue("事前溢折");
        titleRow.getCell(idx++).setCellValue("退貨金額");
        titleRow.getCell(idx++).setCellValue("事後折讓");
        titleRow.getCell(idx++).setCellValue("銷貨淨額");
        titleRow.getCell(idx++).setCellValue("毛利率");

        BigDecimal tmp100 = new BigDecimal("100");

        BigDecimal targetRate = lvo.get(0).getTargetRate();
        if (!titleType.contains("TotalChannels")) {
            targetRate = targetRate.multiply(tmp100);
        }
        String rateString = "目標達成率".concat(targetRate.toBigInteger().toString())
                .concat("%").concat("\r\n").concat("達成率");
        titleRow.getCell(idx++).setCellValue(rateString);
        titleRow.getCell(idx++).setCellValue("預算");

        BigDecimal subtotal_InvoiceAmount = BigDecimal.ZERO,
                subtotal_PremiumDiscount = BigDecimal.ZERO,
                subtotal_SalesReturn = BigDecimal.ZERO,
                subtotal_SalesDiscount = BigDecimal.ZERO,
                subtotal_SalesAmount = BigDecimal.ZERO,
                subtotal_GrossProfitRate = BigDecimal.ZERO,
                subtotal_AchievementRate = BigDecimal.ZERO,
                subtotal_Budget = BigDecimal.ZERO,
                subtotal_cost = BigDecimal.ZERO;

        BigDecimal total_InvoiceAmount = BigDecimal.ZERO,
                total_PremiumDiscount = BigDecimal.ZERO,
                total_SalesReturn = BigDecimal.ZERO,
                total_SalesDiscount = BigDecimal.ZERO,
                total_SalesAmount = BigDecimal.ZERO,
                total_GrossProfitRate = BigDecimal.ZERO,
                total_AchievementRate = BigDecimal.ZERO,
                total_Budget = BigDecimal.ZERO,
                total_cost = BigDecimal.ZERO;

        String channelName = "";
        DecimalFormat df2 = new DecimalFormat("###");
        HSSFCellStyle stylef2 = wb.createCellStyle();
        HSSFCellStyle stylef3 = wb.createCellStyle();
        HSSFCellStyle styleLime = wb.createCellStyle();
        HSSFCellStyle styleYellow = wb.createCellStyle();
        HSSFCellStyle styleRed = wb.createCellStyle();

        for (AchievementFactVO vo : lvo) {
            String _channelName = "";
            HSSFRow row = sheet.createRow(rowCount++);
            idx = 0;
            createAchievExcel(row, wb, "GREY");

            _channelName = vo.getChannelName();

            if (StringUtils.isEmpty(channelName)) {
                channelName = _channelName;
            }

            if (!StringUtils.equals(channelName, _channelName)) {
                idx = 0;
                createAchievExcel(row, wb, "GREY");
                row.getCell(idx++).setCellValue(channelName);
                row.getCell(idx++).setCellValue("subtotal");
                stylef2 = setStyle(stylef2, "GREY");
                row.getCell(idx).setCellValue(subtotal_InvoiceAmount.doubleValue());
                row.getCell(idx++).setCellStyle(stylef2);
                row.getCell(idx).setCellValue(subtotal_PremiumDiscount.doubleValue());
                row.getCell(idx++).setCellStyle(stylef2);
                row.getCell(idx).setCellValue(subtotal_SalesReturn.doubleValue());
                row.getCell(idx++).setCellStyle(stylef2);
                row.getCell(idx).setCellValue(subtotal_SalesDiscount.doubleValue());
                row.getCell(idx++).setCellStyle(stylef2);
                row.getCell(idx).setCellValue(subtotal_SalesAmount.doubleValue());
                row.getCell(idx++).setCellStyle(stylef2);


                subtotal_GrossProfitRate = subtotal_SalesAmount
                        .subtract(subtotal_cost)
                        .add(subtotal_SalesReturn)
                        .divide(subtotal_SalesAmount, 4, RoundingMode.HALF_UP);

                row.getCell(idx++).setCellValue(df2.format(subtotal_GrossProfitRate.multiply(tmp100)) + '%');
                row.getCell(idx++).setCellValue(df2.format(subtotal_AchievementRate.multiply(tmp100)) + '%');
                row.getCell(idx).setCellValue(subtotal_Budget.doubleValue());
                row.getCell(idx).setCellStyle(stylef2);

                total_InvoiceAmount = total_InvoiceAmount.add(subtotal_InvoiceAmount);
                total_PremiumDiscount = total_PremiumDiscount.add(subtotal_PremiumDiscount);
                total_SalesReturn = total_SalesReturn.add(subtotal_SalesReturn);
                total_SalesDiscount = total_SalesDiscount.add(subtotal_SalesDiscount);
                total_SalesAmount = total_SalesAmount.add(subtotal_SalesAmount);
                total_GrossProfitRate = total_GrossProfitRate.add(subtotal_GrossProfitRate);
                total_Budget = total_Budget.add(subtotal_Budget);
                total_cost = total_cost.add(subtotal_cost);
                if (!total_Budget.equals(BigDecimal.ZERO)) {
                    total_AchievementRate = total_SalesAmount
                            .divide(total_Budget, 4, RoundingMode.HALF_UP);
                }

                subtotal_InvoiceAmount = BigDecimal.ZERO;
                subtotal_PremiumDiscount = BigDecimal.ZERO;
                subtotal_SalesReturn = BigDecimal.ZERO;
                subtotal_SalesDiscount = BigDecimal.ZERO;
                subtotal_SalesAmount = BigDecimal.ZERO;
                subtotal_GrossProfitRate = BigDecimal.ZERO;
                subtotal_AchievementRate = BigDecimal.ZERO;
                subtotal_Budget = BigDecimal.ZERO;
                subtotal_cost = BigDecimal.ZERO;

                channelName = _channelName;
                row = sheet.createRow(rowCount++);
            }
            stylef3 = setStyle(stylef3, "");
            createAchievExcel(row, wb, "");
            idx = 0;
            row.getCell(idx++).setCellValue(_channelName);

            String saleName = "";
            if (vo.getSaleName() != null) {
                saleName = vo.getSaleName();
            }

            if (!titleType.contains("TotalChannels")) {
                saleName = vo.getSapId().concat(vo.getSaleName());
            }
            row.getCell(idx++).setCellValue(saleName);
            row.getCell(idx).setCellValue(vo.getInvoiceAmount().doubleValue());
            row.getCell(idx++).setCellStyle(stylef3);
            row.getCell(idx).setCellValue(vo.getPremiumDiscount().doubleValue());
            row.getCell(idx++).setCellStyle(stylef3);
            row.getCell(idx).setCellValue(vo.getSalesReturn().doubleValue());
            row.getCell(idx++).setCellStyle(stylef3);
            row.getCell(idx).setCellValue(vo.getSalesDiscount().doubleValue());
            row.getCell(idx++).setCellStyle(stylef3);
            row.getCell(idx).setCellValue(vo.getSalesAmount().doubleValue());
            row.getCell(idx++).setCellStyle(stylef3);
            row.getCell(idx++).setCellValue(df2.format(vo.getGrossProfitRate().multiply(tmp100)) + '%');
            row.getCell(idx).setCellValue(df2.format(vo.getAchievementRate().multiply(tmp100)) + '%');

            BigDecimal ar = vo.getAchievementRate().multiply(tmp100);
            BigDecimal tr90 = targetRate.multiply(new BigDecimal("0.9"));
            BigDecimal tr70 = targetRate.multiply(new BigDecimal("0.7"));
            if (ar.compareTo(tr90) > 0) {
                styleLime = setStyle(styleLime, "LIME");
                row.getCell(idx).setCellStyle(styleLime);
                row.getCell(1).setCellStyle(styleLime);
            }
            if (ar.compareTo(tr70) > 0 && ar.compareTo(tr90) < 0) {
                styleYellow = setStyle(styleYellow, "YELLOW");
                row.getCell(idx).setCellStyle(styleYellow);
                row.getCell(1).setCellStyle(styleYellow);
            }
            if (ar.compareTo(tr70) < 0) {
                styleRed = setStyle(styleRed, "RED");
                row.getCell(idx).setCellStyle(styleRed);
                row.getCell(1).setCellStyle(styleRed);
            }

            BigDecimal tmp = BigDecimal.ZERO;
            if (!vo.getBudget().equals(BigDecimal.ZERO)) {
                tmp = vo.getBudget();
            }
            row.getCell(++idx).setCellValue(tmp.doubleValue());
            row.getCell(idx).setCellStyle(stylef3);

            subtotal_InvoiceAmount = subtotal_InvoiceAmount.add(vo.getInvoiceAmount());
            subtotal_PremiumDiscount = subtotal_PremiumDiscount.add(vo.getPremiumDiscount());
            subtotal_SalesReturn = subtotal_SalesReturn.add(vo.getSalesReturn());
            subtotal_SalesDiscount = subtotal_SalesDiscount.add(vo.getSalesDiscount());
            subtotal_SalesAmount = subtotal_SalesAmount.add(vo.getSalesAmount());
            subtotal_GrossProfitRate = subtotal_GrossProfitRate.add(vo.getGrossProfitRate());
            subtotal_Budget = subtotal_Budget.add(vo.getBudget());
            subtotal_cost = subtotal_cost.add(vo.getCost());
            if (!subtotal_Budget.equals(BigDecimal.ZERO)) {
                subtotal_AchievementRate = subtotal_SalesAmount.
                        divide(subtotal_Budget, 4, RoundingMode.HALF_UP);
            }
        }
        // print subtotal
        HSSFRow row = sheet.createRow(rowCount++);
        idx = 0;
        createAchievExcel(row, wb, "GREY");
        stylef2 = setStyle(stylef2, "GREY");
        row.getCell(idx++).setCellValue(channelName);
        row.getCell(idx++).setCellValue("subtotal");
        row.getCell(idx).setCellValue(subtotal_InvoiceAmount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(subtotal_PremiumDiscount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(subtotal_SalesReturn.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(subtotal_SalesDiscount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(subtotal_SalesAmount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);

        subtotal_GrossProfitRate = subtotal_SalesAmount
                .subtract(subtotal_cost)
                .add(subtotal_SalesReturn)
                .divide(subtotal_SalesAmount, 4, RoundingMode.HALF_UP);

        row.getCell(idx++).setCellValue(df2.format(subtotal_GrossProfitRate.multiply(tmp100)) + '%');
        row.getCell(idx).setCellValue(df2.format(subtotal_AchievementRate.multiply(tmp100)) + '%');

        BigDecimal arSub = subtotal_AchievementRate.multiply(tmp100);
        BigDecimal tr90 = targetRate.multiply(new BigDecimal("0.9"));
        BigDecimal tr70 = targetRate.multiply(new BigDecimal("0.7"));
        if (arSub.compareTo(tr90) > 0) {
            styleLime = setStyle(styleLime, "LIME");
            row.getCell(idx++).setCellStyle(styleLime);
        }
        if (arSub.compareTo(tr70) > 0 && arSub.compareTo(tr90) < 0) {
            styleYellow = setStyle(styleYellow, "YELLOW");
            row.getCell(idx++).setCellStyle(styleYellow);
        }
        if (arSub.compareTo(tr70) < 0) {
            styleRed = setStyle(styleRed, "RED");
            row.getCell(idx++).setCellStyle(styleRed);
        }
//        
        row.getCell(idx).setCellValue(subtotal_Budget.doubleValue());
        row.getCell(idx).setCellStyle(stylef2);

        total_InvoiceAmount = total_InvoiceAmount.add(subtotal_InvoiceAmount);
        total_PremiumDiscount = total_PremiumDiscount.add(subtotal_PremiumDiscount);
        total_SalesReturn = total_SalesReturn.add(subtotal_SalesReturn);
        total_SalesDiscount = total_SalesDiscount.add(subtotal_SalesDiscount);
        total_SalesAmount = total_SalesAmount.add(subtotal_SalesAmount);
        total_GrossProfitRate = total_GrossProfitRate.add(subtotal_GrossProfitRate);
        total_cost = total_cost.add(subtotal_cost);
        total_Budget = total_Budget.add(subtotal_Budget);
        if (!total_Budget.equals(BigDecimal.ZERO)) {
            total_AchievementRate = total_SalesAmount.divide(total_Budget, 4, RoundingMode.HALF_UP);
        }

        // print total
        row = sheet.createRow(rowCount++);
//        idx = 0;
        createAchievExcel(row, wb, "GREY");
        stylef2 = setStyle(stylef2, "GREY");
        row.getCell(0).setCellValue("");
        row.getCell(1).setCellValue("total");
        row.getCell(2).setCellValue(total_InvoiceAmount.doubleValue());
        row.getCell(2).setCellStyle(stylef2);
        row.getCell(3).setCellValue(total_PremiumDiscount.doubleValue());
        row.getCell(3).setCellStyle(stylef2);
        row.getCell(4).setCellValue(total_SalesReturn.doubleValue());
        row.getCell(4).setCellStyle(stylef2);
        row.getCell(5).setCellValue(total_SalesDiscount.doubleValue());
        row.getCell(5).setCellStyle(stylef2);
        row.getCell(6).setCellValue(total_SalesAmount.doubleValue());
        row.getCell(6).setCellStyle(stylef2);

        total_GrossProfitRate = total_SalesAmount
                .subtract(total_cost)
                .add(total_SalesReturn)
                .divide(total_SalesAmount, 4, RoundingMode.HALF_UP);

        row.getCell(7).setCellValue(df2.format(total_GrossProfitRate.multiply(tmp100)) + '%');
        row.getCell(8).setCellValue(df2.format(total_AchievementRate.multiply(tmp100)) + '%');
        BigDecimal arTotal = total_AchievementRate.multiply(tmp100);
        if (arTotal.compareTo(tr90) > 0) {
            styleLime = setStyle(styleLime, "LIME");
            row.getCell(8).setCellStyle(styleLime);
        }
        if (arTotal.compareTo(tr70) > 0 && arSub.compareTo(tr90) < 0) {
            styleYellow = setStyle(styleYellow, "YELLOW");
            row.getCell(8).setCellStyle(styleYellow);
        }
        if (arTotal.compareTo(tr70) < 0) {
            styleRed = setStyle(styleRed, "RED");
            row.getCell(8).setCellStyle(styleRed);
        }

        row.getCell(9).setCellValue(total_Budget.doubleValue());
        row.getCell(9).setCellStyle(stylef2);

        // print memo
        HSSFRow rowDemo = sheet.createRow(rowCount++);
        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell1A = rowDemo.createCell(0);
        cell1A.setCellValue("實際達成率達目標達成率:");
        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell2A = rowDemo.createCell(0);
        cell2A.setCellValue("90%以上");
        HSSFCell cell2B = rowDemo.createCell(1);
        styleLime = setStyleDemo(styleLime, "LIME");
        cell2B.setCellStyle(styleLime);
        HSSFCell cell2C = rowDemo.createCell(2);
        String c2String = "達成率".concat(tr90.toBigInteger().toString()).concat("%以上");
        cell2C.setCellValue(c2String);
        //
        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell3A = rowDemo.createCell(0);
        cell3A.setCellValue("70%~90%");
        HSSFCell cell3B = rowDemo.createCell(1);
        styleYellow = setStyleDemo(styleYellow, "YELLOW");
        cell3B.setCellStyle(styleYellow);
        HSSFCell cell3C = rowDemo.createCell(2);
        String c3String = "達成率".concat(tr90.toBigInteger().toString()).concat("%~")
                .concat(tr70.toBigInteger().toString()).concat("%");
        cell3C.setCellValue(c3String);
        //
        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell4A = rowDemo.createCell(0);
        cell4A.setCellValue("70%以下");
        HSSFCell cell4B = rowDemo.createCell(1);
        styleRed = setStyleDemo(styleRed, "RED");
        cell4B.setCellStyle(styleRed);
        HSSFCell cell4C = rowDemo.createCell(2);
        String c4String = "達成率".concat(tr70.toBigInteger().toString()).concat("%以下");
        cell4C.setCellValue(c4String);

        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell5A = rowDemo.createCell(0);
        cell5A.setCellValue("說明:如目標達成率50%，因12/15為50%，若12/16則目標達成率為52%(16/31=52%)");

        FileOutputStream fileOut = new FileOutputStream(tempfile);
        wb.write(fileOut);
        fileOut.close();

    }

    private HSSFCellStyle setStyleDemo(HSSFCellStyle style, String color) {
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        if (color.equals("GREY")) {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        }
        if (color.equals("LIME")) {
            style.setFillForegroundColor(HSSFColor.LIME.index);
        }
        if (color.equals("YELLOW")) {
            style.setFillForegroundColor(HSSFColor.YELLOW.index);
        }
        if (color.equals("RED")) {
            style.setFillForegroundColor(HSSFColor.RED.index);
        }
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }

    private HSSFCellStyle setStyle(HSSFCellStyle style, String color) {
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        if (color.equals("GREY")) {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        }
        if (color.equals("LIME")) {
            style.setFillForegroundColor(HSSFColor.LIME.index);
        }
        if (color.equals("YELLOW")) {
            style.setFillForegroundColor(HSSFColor.YELLOW.index);
        }
        if (color.equals("RED")) {
            style.setFillForegroundColor(HSSFColor.RED.index);
        }

        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        return style;
    }

    private void createAchievExcel(HSSFRow row, HSSFWorkbook wb, String color) {
        HSSFCellStyle style = wb.createCellStyle();

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        if (color.equals("GREY")) {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        }
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        for (int i = 0; i < 10; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);

            cell.setCellStyle(style);
        }

    }
}
