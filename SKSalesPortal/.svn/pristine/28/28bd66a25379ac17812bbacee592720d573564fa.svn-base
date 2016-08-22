/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.schedule.TcScheduleFacade;
import com.tcci.sksp.entity.org.SkSalesChannelMember;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import static com.tcci.sksp.facade.ZrtBsegAragFacade.getBytesFromFile;
import com.tcci.sksp.vo.AchievementVO;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class ReSendAchievement {

    private final static Logger logger = LoggerFactory.getLogger(ReSendAchievement.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    private SkSalesMemberFacade memberFacade;
    @EJB
    TcUserFacade tcUserFacade;
    @EJB
    SkSalesChannelMemberFacade channelMemberFacade;
    @EJB
    SkSalesChannelsFacade ChannelsFacade;
    @EJB
    private TcScheduleFacade scheduleFacade;
    @EJB
    private SkSalesAchievementFacade salesAchievementFacade;
    @Resource(name = "mail/automail")
    private Session mailSession;

    protected EntityManager getEntityManager() {
        return em;
    }

 
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

        List<SkSalesMember> T1List = memberFacade.findByTgroup("T1");
        if (T1List != null) {
            for (SkSalesMember skm : T1List) {
                sendMailT(skm.getCode(), year_month);
            }
        }
        
        //send mail  all sales assistant
        sendMailAssistant(year_month);

        // channel boss send mail
        sendMailChannel("T31", year_month);
        sendMailChannel("T32", year_month);
        sendMailChannel("T33", year_month);

        //channel's manager send mail
        List<SkSalesChannels> channels = ChannelsFacade.findAll();
        for (SkSalesChannels sc : channels) {
            if (!sc.getCode().contains("T")) {
                sendMailChannel(sc.getCode(), year_month);
            }
        }

        // sales send mail
        List<SkSalesMember> sml = memberFacade.findAll();
        for (SkSalesMember sm : sml) {
            if (!sm.getCode().contains("T")) {
                sendMail(sm.getCode(), year_month, "N");
            }
        }
    }

    public void sendMailChannel(String code, String year) {
        List<AchievementVO> vol = new ArrayList<AchievementVO>();
        SkSalesChannels channel = ChannelsFacade.findByCode(code);
        List<SkSalesChannels> channels = ChannelsFacade.findChildChannel(channel);

        List<SkSalesChannelMember> list = channelMemberFacade.findByChannels(channel);
        for (SkSalesChannels skc : channels) {
            list.addAll(channelMemberFacade.findByChannels(skc));
        }

        for (SkSalesChannelMember sb : list) {
            String _sapid = sb.getSalesMember().getCode();
            List<AchievementVO> volt = salesAchievementFacade.findMonthReport(_sapid, year);
            vol.addAll(volt);
        }

        if (!vol.isEmpty()) {
            try {
                // create temple excel
//                File detailTempFile = File.createTempFile(getTitle().concat("_Channel"), ".xls");
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Channel").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Channel");

                File detailTempFile1 = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_TotalChannels").concat(".xls"));
                // total channel's data
                List<AchievementVO> vol1 = salesAchievementFacade.findChannelReport(year);
                createAchievExcel(vol1, detailTempFile1, "TotalChannels");

                sendMailActionC(code, detailTempFile, detailTempFile1);

                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();
                detailTempFile1.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMailActionC(String code, File tempFile, File tempFile1) throws Exception {
        try {
            Message msg = new MimeMessage(mailSession);
            SkSalesChannels channel = ChannelsFacade.findByCode(code);

            InternetAddress[] address = InternetAddress.parse(channel.getManager().getEmail(), false);
            msg.setRecipients(Message.RecipientType.TO, address);

            // 主旨 (需編碼)
            String mailSubject = channel.getName() + channel.getManager().getCname() + getTitle();
            msg.setSubject(MimeUtility.encodeText(mailSubject, "utf-8", "B"));
            // 寄件時間
            msg.setSentDate(new Date());
            // 內文
            String mailContent = "DEAR ALL..<br/><br/>"
                    + channel.getName() + channel.getManager().getCname() + getTitle()
                    + "<br/> PS.此為系統自動郵件發送,請勿回信 <br/> B.R. <br/>";
            msg.setContent(mailContent, "text/html;charset=utf-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailContent, "text/html;charset=utf-8");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(MimeUtility.encodeText(tempFile.getName(), "UTF-8", "B"));
            attachment.setContent(getBytesFromFile(tempFile), "application/xls");
            mp.addBodyPart(attachment);

            MimeBodyPart attachment1 = new MimeBodyPart();
            attachment1.setFileName(MimeUtility.encodeText(tempFile1.getName(), "UTF-8", "B"));
            attachment1.setContent(getBytesFromFile(tempFile1), "application/xls");
            mp.addBodyPart(attachment1);



            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception me) {
            me.printStackTrace();
        }

    }
    
    public void sendMailAssistant(String year) {
        List<AchievementVO> vol = salesAchievementFacade.findMonthReport(year);
        if (!vol.isEmpty()) {
            try {
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Total").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Total");
               
                // send all sales Assistant email
                sendMailActionA(detailTempFile);

                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMailT(String t1, String year) {
        List<AchievementVO> vol = salesAchievementFacade.findMonthReport(year);
        if (!vol.isEmpty()) {
            try {
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Total").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Total");

                // send e'mail to T1 
                sendMailActionT(t1, detailTempFile);
                
                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMailActionA(File tempFile) throws Exception {
        try {
            Message msg = new MimeMessage(mailSession);
            StringBuilder to = new StringBuilder();

            // send all Assistant email
            List<TcUser> assistants = tcUserFacade.findUsersByGroupCode("Assistant");

            // email to "jennifer.lin@sking.com.tw"
            assistants.add(tcUserFacade.getUserByEmail("jennifer.lin@sking.com.tw"));
            for (TcUser tc : assistants) {
                if (!tc.getDisabled()) {
                    if (to.length() > 0) {
                        to.append(',');
                    }
                    to.append(tc.getEmail());
                }

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.toString()));
            }

            // 主旨 (需編碼)
            String mailSubject = getTitle();
            msg.setSubject(MimeUtility.encodeText(mailSubject, "utf-8", "B"));
            // 寄件時間
            msg.setSentDate(new Date());
            // 內文
            String mailContent = "DEAR ALL..<br/><br/>"
                    + getTitle()
                    + "<br/> PS.此為系統自動郵件發送,請勿回信 <br/> B.R. <br/>";
            msg.setContent(mailContent, "text/html;charset=utf-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailContent, "text/html;charset=utf-8");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(MimeUtility.encodeText(tempFile.getName(), "UTF-8", "B"));
            attachment.setContent(getBytesFromFile(tempFile), "application/xls");
            mp.addBodyPart(attachment);

            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception me) {
            me.printStackTrace();
        }
    }

    private void sendMailActionT(String t, File tempFile) throws Exception {
        try {
            Message msg = new MimeMessage(mailSession);
            SkSalesMember sales = memberFacade.findByCode(t);
            InternetAddress[] address = InternetAddress.parse(sales.getMember().getEmail(), false);
            msg.setRecipients(Message.RecipientType.TO, address);

            String cname = sales.getMember().getCname();
            // 主旨 (需編碼)
            String mailSubject = getTitle();
            msg.setSubject(MimeUtility.encodeText(mailSubject, "utf-8", "B"));
            // 寄件時間
            msg.setSentDate(new Date());
            // 內文
            String mailContent = "DEAR ALL..<br/><br/>"
                    + cname + getTitle()
                    + "<br/> PS.此為系統自動郵件發送,請勿回信 <br/> B.R. <br/>";
            msg.setContent(mailContent, "text/html;charset=utf-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailContent, "text/html;charset=utf-8");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(MimeUtility.encodeText(tempFile.getName(), "UTF-8", "B"));
            attachment.setContent(getBytesFromFile(tempFile), "application/xls");
            mp.addBodyPart(attachment);

            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception me) {
            me.printStackTrace();
        }
    }

    // sendMail by single sapid ex.(010) +  年月
    public void sendMail(String sapid, String year, String sendBoss) {
        List<AchievementVO> vol = new ArrayList<AchievementVO>();
        SkSalesMember saleM = memberFacade.findByCode(sapid);
        SkSalesChannels channel = channelMemberFacade.findBySalesMember(saleM);
        List<SkSalesChannelMember> list = channelMemberFacade.findByChannels(channel);

        for (SkSalesChannelMember sb : list) {
            String _sapid = sb.getSalesMember().getCode();
            List<AchievementVO> volt = salesAchievementFacade.findMonthReport(_sapid, year);
            vol.addAll(volt);
        }

        if (!vol.isEmpty()) {
            try {
                // create temple excel
//                File detailTempFile = File.createTempFile(getTitle().concat("_Sales"), ".xls");
                File detailTempFile = new File(System.getProperty("java.io.tmpdir"), getTitle().concat("_Sales").concat(".xls"));
                createAchievExcel(vol, detailTempFile, "Sales");

                //   send e'mail to sales and cc to his boss
                sendMailAction(sapid, detailTempFile, sendBoss);

                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMailAction(String sapid, File tempFile, String sendBoss) throws Exception {
        try {
            Message msg = new MimeMessage(mailSession);
            SkSalesMember sales = memberFacade.findByCode(sapid);
            InternetAddress[] address = InternetAddress.parse(sales.getMember().getEmail(), false);
            msg.setRecipients(Message.RecipientType.TO, address);
            if (sendBoss.equals("Y")) {
                SkSalesChannels channel = channelMemberFacade.findBySalesMember(sales);
                InternetAddress[] bossAddress = InternetAddress.parse(channel.getManager().getEmail(), false);
                msg.setRecipients(Message.RecipientType.CC, bossAddress);
            }
            String cname = sales.getMember().getCname();
            // 主旨 (需編碼)
            String mailSubject = " 第 " + sapid + " 區 " + cname + getTitle();
            msg.setSubject(MimeUtility.encodeText(mailSubject, "utf-8", "B"));
            // 寄件時間
            msg.setSentDate(new Date());
            // 內文
            String mailContent = "DEAR ALL..<br/><br/>"
                    + cname + getTitle()
                    + "<br/> PS.此為系統自動郵件發送,請勿回信 <br/> B.R. <br/>";
            msg.setContent(mailContent, "text/html;charset=utf-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailContent, "text/html;charset=utf-8");
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setFileName(MimeUtility.encodeText(tempFile.getName(), "UTF-8", "B"));
            attachment.setContent(getBytesFromFile(tempFile), "application/xls");
            mp.addBodyPart(attachment);

            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception me) {
            me.printStackTrace();
        }

    }

    private void createAchievExcel(List<AchievementVO> lvo,
            File tempfile, String titleType) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        int rowCount = 0;
        HSSFRow titleRow0 = sheet.createRow(rowCount++);
        Calendar cal = Calendar.getInstance();
        int monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int days = new Date().getDate();
        int targetRate = 100 * days / monthDays;

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

        String rateString = "目標達成率".concat(Integer.toString(targetRate))
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
        BigDecimal tmp100 = new BigDecimal("100");
        DecimalFormat df2 = new DecimalFormat("###");
        HSSFCellStyle stylef2 = wb.createCellStyle();
        HSSFCellStyle stylef3 = wb.createCellStyle();
        HSSFCellStyle styleLime = wb.createCellStyle();
        HSSFCellStyle styleYellow = wb.createCellStyle();
        HSSFCellStyle styleRed = wb.createCellStyle();

        for (AchievementVO vo : lvo) {
            String _channelName = "";
            HSSFRow row = sheet.createRow(rowCount++);
            idx = 0;
            createAchievExcel(row, wb, "GREY");
            if (!titleType.contains("TotalChannels")) {
                SkSalesMember sales = memberFacade.findByCode(vo.getChannel());
                if (sales != null) {
                    SkSalesChannels channel = channelMemberFacade.findBySalesMember(sales);
                    _channelName = channel.getName();
                }
            }

            if (titleType.contains("TotalChannels")) {
                _channelName = vo.getChannel();
            }

            if (channelName == "") {
                channelName = _channelName;
            }

            if (channelName != _channelName) {
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
                total_AchievementRate = total_SalesAmount
                        .divide(total_Budget, 4, RoundingMode.HALF_UP);

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

            createAchievExcel(row, wb, "");
            idx = 0;
            row.getCell(idx++).setCellValue(_channelName);
            // check sales not exist
            String temp = "";
            if (!titleType.contains("TotalChannels")) {
                String saleName = "temp";
                SkSalesMember sales = memberFacade.findByCode(vo.getChannel());
                if (sales.getMember() != null) {
                    saleName = sales.getMember().getName();
                }
                stylef3 = setStyle(stylef3, "");
                temp = sales.getCode().concat(saleName);
            }

            if (titleType.contains("TotalChannels")) {
                SkSalesChannels channel = ChannelsFacade.findByCode(_channelName);
                if (channel != null) {
                    temp = channel.getName();
                }
            }
            row.getCell(idx++).setCellValue(temp);
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
            BigDecimal tr90 = new BigDecimal(targetRate).multiply(new BigDecimal("0.9"));
            BigDecimal tr70 = new BigDecimal(targetRate).multiply(new BigDecimal("0.7"));
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

            if (!vo.getBudget().equals(BigDecimal.ZERO)) {
                Double tmp = vo.getBudget().doubleValue();
                row.getCell(++idx).setCellValue(tmp);
                row.getCell(idx).setCellStyle(stylef3);
            }

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
        BigDecimal tr90 = new BigDecimal(targetRate).multiply(new BigDecimal("0.9"));
        BigDecimal tr70 = new BigDecimal(targetRate).multiply(new BigDecimal("0.7"));
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
        total_AchievementRate = total_SalesAmount.divide(total_Budget, 4, RoundingMode.HALF_UP);

        // print total
        row = sheet.createRow(rowCount++);
        idx = 0;
        createAchievExcel(row, wb, "GREY");
        stylef2 = setStyle(stylef2, "GREY");
        row.getCell(idx++).setCellValue("");
        row.getCell(idx++).setCellValue("total");
        row.getCell(idx).setCellValue(total_InvoiceAmount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(total_PremiumDiscount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(total_SalesReturn.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(total_SalesDiscount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);
        row.getCell(idx).setCellValue(total_SalesAmount.doubleValue());
        row.getCell(idx++).setCellStyle(stylef2);

        total_GrossProfitRate = total_SalesAmount
                .subtract(total_cost)
                .add(total_SalesReturn)
                .divide(total_SalesAmount, 4, RoundingMode.HALF_UP);

        row.getCell(idx++).setCellValue(df2.format(total_GrossProfitRate.multiply(tmp100)) + '%');
        row.getCell(idx).setCellValue(df2.format(total_AchievementRate.multiply(tmp100)) + '%');
        BigDecimal arTotal = total_AchievementRate.multiply(tmp100);
        if (arTotal.compareTo(tr90) > 0) {
            styleLime = setStyle(styleLime, "LIME");
            row.getCell(idx++).setCellStyle(styleLime);
        }
        if (arTotal.compareTo(tr70) > 0 && arSub.compareTo(tr90) < 0) {
            styleYellow = setStyle(styleYellow, "YELLOW");
            row.getCell(idx++).setCellStyle(styleYellow);
        }
        if (arTotal.compareTo(tr70) < 0) {
            styleRed = setStyle(styleRed, "RED");
            row.getCell(idx++).setCellStyle(styleRed);
        }

        row.getCell(idx).setCellValue(total_Budget.doubleValue());
        row.getCell(idx).setCellStyle(stylef2);

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
        String c2String = "達成率".concat(tr90.toString()).concat("%以上");
        cell2C.setCellValue(c2String);
        //
        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell3A = rowDemo.createCell(0);
        cell3A.setCellValue("70%~90%");
        HSSFCell cell3B = rowDemo.createCell(1);
        styleYellow = setStyleDemo(styleYellow, "YELLOW");
        cell3B.setCellStyle(styleYellow);
        HSSFCell cell3C = rowDemo.createCell(2);
        String c3String = "達成率".concat(tr90.toString()).concat("%~")
                .concat(tr70.toString()).concat("%");
        cell3C.setCellValue(c3String);
        //
        rowDemo = sheet.createRow(rowCount++);
        HSSFCell cell4A = rowDemo.createCell(0);
        cell4A.setCellValue("70%以下");
        HSSFCell cell4B = rowDemo.createCell(1);
        styleRed = setStyleDemo(styleRed, "RED");
        cell4B.setCellStyle(styleRed);
        HSSFCell cell4C = rowDemo.createCell(2);
        String c4String = "達成率".concat(tr70.toString()).concat("%以下");
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
