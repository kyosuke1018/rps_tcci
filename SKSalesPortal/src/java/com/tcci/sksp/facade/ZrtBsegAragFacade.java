/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.schedule.TcScheduleFacade;
import com.tcci.sksp.entity.ZrtBsegArag;
import com.tcci.sksp.entity.ZrtBsegArtl;
import com.tcci.sksp.entity.org.SkSalesChannelMember;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.io.*;
import java.util.ArrayList;
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
import javax.mail.internet.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author carl.lin
 */
@Stateless
public class ZrtBsegAragFacade extends AbstractFacade<ZrtBsegArag> {
    private final static Logger logger = LoggerFactory.getLogger(ZrtBsegAragFacade.class);

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @EJB
    private SkSalesMemberFacade memberFacade;
    @EJB
    private ZrtBsegArtlFacade artlFacade;
    @EJB
    TcUserFacade tcUserFacade;
    @EJB
    SkSalesChannelMemberFacade channelMemberFacade;
    @EJB
    SkSalesChannelsFacade ChannelsFacade;
    @EJB
    private TcScheduleFacade scheduleFacade;

    @Resource(name = "mail/automail")
    private Session mailSession;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZrtBsegAragFacade() {
        super(ZrtBsegArag.class);
    }

    // 每個星期五早上8:00 send mail
    @Schedule(dayOfWeek = "5", hour = "08", minute = "00", persistent=false)
    public void scheduleSendMail() {
        try {
            // 僅一台server可以執行排程
            if (scheduleFacade.canExecute("SendOverdueAccount", 30)) {
                sendOverdueAccount();
            } else {
                logger.warn("SendOverdueAccount not execute");
            }
        } catch (Exception ex) {
            logger.error("scheduleSendMail exception", ex);
        }
    }

    // for all sales
    public void sendOverdueAccount() {
        try {
            List<ZrtBsegArtl> artllist = artlFacade.Ztbsegartl();
            List<ZrtBsegArag> araglist = findArag();
            // for T1
            sendOverdueAccountT1AndT2(artllist, araglist, "T1");
            // For T2
            sendOverdueAccountT1AndT2(artllist, araglist, "T2");

            List<SkSalesMember> saleslist = memberFacade.findAllSelectable();
            for (SkSalesMember sales : saleslist) {
                List<ZrtBsegArag> listArag = new ArrayList<ZrtBsegArag>();
                // Create temp file.
                File detailTempFile = File.createTempFile("OverDueAccountDetail", ".xls");
                File totalTempFile = File.createTempFile("OverDueAccountTotal", ".xls");
                // get sale's 明細資料  
                for (ZrtBsegArag zrtBsegArag : araglist) {
                    if (zrtBsegArag.getVkgrp().equals(sales.getCode())) {
                        listArag.add(zrtBsegArag);
                    }
                }

                // create 明細資料 excel
                createAragExcel(listArag, detailTempFile);
                // create 總表's excel
                createArtlExcelByArtl(sales, totalTempFile, artllist);
                // Add attach file
                List<File> attachFilet = new ArrayList<File>();
                
                 // 沒有明細不發excel 
                if (!listArag.isEmpty()) {
                    attachFilet.add(detailTempFile);
                }
                attachFilet.add(totalTempFile);
                //   send e'mail
                sendMailAction(sales, attachFilet);
                // Delete temp file when program exits.
                detailTempFile.deleteOnExit();
                totalTempFile.deleteOnExit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOverdueAccountT1AndT2(List<ZrtBsegArtl> artllist, List<ZrtBsegArag> araglist, String TT) {
        try {
            String totalFileName = "OverDueAccountTotal" + TT;
            String detailFileName = "OverDueAccountDetail" + TT;
            File totalTempFileTT = File.createTempFile(totalFileName, ".xls");
            // create 總表's excel
            createArtlExcelByArtlT1(totalTempFileTT, artllist, TT);
            // Create detail file.
            File detailTempFileTT = File.createTempFile(detailFileName, ".xls");
            // create 明細's excel
            createAragExcelT1(araglist, detailTempFileTT, TT);

            // Add attach file
            List<File> attachFilet = new ArrayList<File>();
            attachFilet.add(detailTempFileTT);
            attachFilet.add(totalTempFileTT);

            //   send e'mail to T1

            SkSalesMember TTT = new SkSalesMember();
            TTT = memberFacade.findByCode(TT);
            System.out.println("TTT's name >>" + TTT.getMember().getCname());
            sendMailAction(TTT, attachFilet);
            SkSalesMember T1 = memberFacade.findByCode("T1");
            System.out.println("T1's name >>" + T1.getMember().getCname());
            sendMailAction(T1, attachFilet);
            totalTempFileTT.deleteOnExit();
            detailTempFileTT.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // for Single sale 
    public void sendOverdueAccount(SkSalesMember sales) {
        try {
            // Create temp file.
            File detailTempFile = File.createTempFile("OverDueAccountDetail", ".xls");
            File totalTempFile = File.createTempFile("OverDueAccountTotal", ".xls");

            // create 明細's excel
            createAragExcel(findArag(sales), detailTempFile);
            List<ZrtBsegArtl> artllist = artlFacade.Ztbsegartl();
            
            // create 總表's excel
            createArtlExcelByArtl(sales, totalTempFile, artllist);
            // Add attach file
            List<File> attachFilet = new ArrayList<File>();
            
            // 沒有明細不發excel 
            if (!findArag(sales).isEmpty()) {
                attachFilet.add(detailTempFile);
            }
            attachFilet.add(totalTempFile);
            //   send e'mail
            sendMailAction(sales, attachFilet);

            // Delete temp file when program exits.
            detailTempFile.deleteOnExit();
            totalTempFile.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendMailAction(SkSalesMember sales, List<File> attachFilet) throws Exception {
        try {
            if (sales == null) {
                System.out.println("sales is null ");
                return;
            }
                      
            Message msg = new MimeMessage(mailSession);
            // 收件者
            if (sales.getMember() == null) { 
                System.out.println("sales.member is null, " +  sales.toString());
            }  else if (sales.getMember().getDisabled()) {
               System.out.println("sales is disabled, " + sales.getMember().getEmail());
            } else {
                System.out.println("收件者 >>" + sales.getMember().getEmail());
                InternetAddress[] address = InternetAddress.parse(sales.getMember().getEmail(), false);
                msg.setRecipients(Message.RecipientType.TO, address);
            }
            // CC to first level Boss  + Second level Manager
            TcUser boss1 = findFirstLevelBoss(sales);
            List<SkSalesChannels> list = findSecondLevelBoss(sales);
            TcUser boss2 = list.isEmpty() ? null : list.get(0).getManager();
            StringBuilder cc = new StringBuilder();
            if (boss1 != null && !boss1.getDisabled()) {
                System.out.println("boss1:" + boss1.getEmail());
                cc.append(boss1.getEmail());
            }
            if (boss2 != null && !boss2.getDisabled()) {
                System.out.println("boss2:" + boss2.getEmail());
                if (cc.length() > 0) {
                    cc.append(',');
                }
                cc.append(boss2.getEmail());
            }
            if (cc.length() > 0) {
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc.toString()));
            }
            
            String cname =  (sales.getMember() == null) ? "" : sales.getMember().getCname();
            // 主旨 (需編碼)sales.getMember()
            String mailSubject = " 第 " + sales.getCode().toString() + " 區 " + cname + " 逾期帳款通知 !! ";
           
            
            System.out.println("主旨 >>" + mailSubject);
            msg.setSubject(MimeUtility.encodeText(mailSubject, "utf-8", "B"));
            // 寄件時間
            msg.setSentDate(new Date());
            // 內文
            String mailContent = "DEAR ALL..<br/><br/>"
                    + cname + " 逾期帳款通知 !!"
                    + "<br/> PS.此為系統自動郵件發送,請勿回信 <br/> B.R. <br/>";
            msg.setContent(mailContent, "text/html;charset=utf-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailContent, "text/html;charset=utf-8");
            mp.addBodyPart(htmlPart);

            for (File file : attachFilet) {
                MimeBodyPart attachment1 = new MimeBodyPart();
                attachment1.setFileName(file.getAbsolutePath());
                attachment1.setContent(getBytesFromFile(file), "application/xls");
                mp.addBodyPart(attachment1);
            }

            msg.setContent(mp);
            Transport.send(msg);

        } catch (Exception me) {
            me.printStackTrace();
        }
    }

    public TcUser findFirstLevelBoss(SkSalesMember sales) {
        TcUser tcuser = new TcUser();
        List<SkSalesChannelMember> channelMemberlist = null;
        channelMemberlist = channelMemberFacade.findAll();

        List<SkSalesChannels> channelslist = null;
        channelslist = ChannelsFacade.findAll();

        for (SkSalesChannelMember skSalesChannelMember : channelMemberlist) {
            if (skSalesChannelMember.getSalesMember().equals(sales)) {
                for (SkSalesChannels skSalesChannels : channelslist) {
                    if (skSalesChannels.equals(skSalesChannelMember.getSalesChannel())) {
                        tcuser = skSalesChannels.getManager();
                    }
                }
            }
        }
        return tcuser;
    }

    public List<SkSalesChannels> findSecondLevelBoss(SkSalesMember sales) {
        StringBuilder sb = new StringBuilder();
        sb.append("select *  from sk_sales_channels  ");
        sb.append("WHERE id IN (select parent from SK_SALES_CHANNELS ");
        sb.append("where id in (SELECT SALES_CHANNEL FROM SK_SALES_CHANNEL_MEMBER ");
        sb.append("where sales_member in (select id from sk_sales_member t where  t.id= ? ))) ");
        Query query = getEntityManager().createNativeQuery(sb.toString(), SkSalesChannels.class);
        query.setParameter(1, sales.getId());
        return query.getResultList();
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    private void createArtlExcelByArtlT1(File totalTempFileT1,
            List<ZrtBsegArtl> artllist, String TT) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFRow titleRow = sheet.createRow(0);
        createAragCells(titleRow);
        if (TT.equals("T1")) {
            titleRow.getCell(0).setCellValue("銷售區域");
            titleRow.getCell(1).setCellValue("181-270天");
            titleRow.getCell(2).setCellValue("271-365天");
            titleRow.getCell(3).setCellValue(">365天");
            titleRow.getCell(4).setCellValue("合計");
        }
        if (TT.equals("T2")) {
            titleRow.getCell(0).setCellValue("銷售區域");
            titleRow.getCell(1).setCellValue("31-60天");
            titleRow.getCell(2).setCellValue("61-90天");
            titleRow.getCell(3).setCellValue("91-120天");
            titleRow.getCell(4).setCellValue("121-150天");
            titleRow.getCell(5).setCellValue("151-180天");
            titleRow.getCell(6).setCellValue("181-270天");
            titleRow.getCell(7).setCellValue("271-365天");
            titleRow.getCell(8).setCellValue(">365天");
            titleRow.getCell(9).setCellValue("合計");
        }


        int i = 1;
        double sumAtm060 = 0, sumAtm090 = 0, sumAtm120 = 0,
                sumAtm150 = 0, sumAtm180 = 0, sumAtm270 = 0, sumAtm365 = 0,
                sumAtmlyr = 0, sumAtmTot = 0;
        String vkgrp = null;
        for (ZrtBsegArtl ztbsegartl : artllist) {

            if (vkgrp == null) {
                vkgrp = ztbsegartl.getVkgrp().substring(0, 2);
            }

            // 印小計       
            if (!vkgrp.equals(ztbsegartl.getVkgrp().substring(0, 2))) {
                String startVkgrp = vkgrp + '0';
                String endVkgrp = ztbsegartl.getVkgrp().substring(0, 2) + '0';
                HSSFRow row1 = sheet.createRow(i++);
                createAragCells(row1);
                setArtlCellsDataT1(sumArtlExcelByBetween(artllist, startVkgrp, endVkgrp), row1, TT);
                // GP
                if (vkgrp.equals("22")) {
                    row1 = sheet.createRow(i++);
                    createAragCells(row1);
                    setArtlCellsDataT1(sumArtlExcelByBetween(artllist, "000", "299"), row1, TT);
                }
                // HP
                if (vkgrp.equals("43")) {
                    row1 = sheet.createRow(i++);
                    createAragCells(row1);
                    setArtlCellsDataT1(sumArtlExcelByBetween(artllist, "300", "900"), row1, TT);
                }
            }

            HSSFRow row = sheet.createRow(i++);
            createAragCells(row);
            setArtlCellsDataT1(ztbsegartl, row, TT);

            sumAtm060 += ztbsegartl.getAmt060().doubleValue();
            sumAtm090 += ztbsegartl.getAmt090().doubleValue();
            sumAtm120 += ztbsegartl.getAmt120().doubleValue();
            sumAtm150 += ztbsegartl.getAmt150().doubleValue();
            sumAtm180 += ztbsegartl.getAmt180().doubleValue();
            sumAtm270 += ztbsegartl.getAmt270().doubleValue();
            sumAtm365 += ztbsegartl.getAmt365().doubleValue();
            sumAtmlyr += ztbsegartl.getAmtlyr().doubleValue();

            sumAtmTot += ztbsegartl.getAmttot().doubleValue();
            vkgrp = ztbsegartl.getVkgrp().substring(0, 2);

        }


        // 總計
        ZrtBsegArtl zrtBsegArtl = new ZrtBsegArtl();
        zrtBsegArtl.setId((long) artllist.size() + 2);
        zrtBsegArtl.setSarea("");
        zrtBsegArtl.setVkgrp("TOT");
        zrtBsegArtl.setAmt060(new java.math.BigDecimal(sumAtm060));
        zrtBsegArtl.setAmt090(new java.math.BigDecimal(sumAtm090));
        zrtBsegArtl.setAmt120(new java.math.BigDecimal(sumAtm120));
        zrtBsegArtl.setAmt150(new java.math.BigDecimal(sumAtm150));
        zrtBsegArtl.setAmt180(new java.math.BigDecimal(sumAtm180));
        zrtBsegArtl.setAmt270(new java.math.BigDecimal(sumAtm270));
        zrtBsegArtl.setAmt365(new java.math.BigDecimal(sumAtm365));
        zrtBsegArtl.setAmtlyr(new java.math.BigDecimal(sumAtmlyr));
        zrtBsegArtl.setAmttot(new java.math.BigDecimal(sumAtmTot));

        HSSFRow row = sheet.createRow(i++);
        createAragCells(row);
        System.out.println("zrtBsegArtl TOT>>" + zrtBsegArtl);
        setArtlCellsDataT1(zrtBsegArtl, row, TT);

        System.out.println("detailTempFileT1 >>" + totalTempFileT1);
        FileOutputStream fileOut = new FileOutputStream(totalTempFileT1);

        wb.write(fileOut);
        fileOut.close();

    }
    // 區間加總

    private ZrtBsegArtl sumArtlExcelByBetween(List<ZrtBsegArtl> artllist, String starvkgrp, String endvkgrp) {
        ZrtBsegArtl zrtBsegArtl = new ZrtBsegArtl();

        double sumAtm060 = 0, sumAtm090 = 0, sumAtm120 = 0,
                sumAtm150 = 0, sumAtm180 = 0, sumAtm270 = 0, sumAtm365 = 0,
                sumAtmlyr = 0, sumAtmTot = 0;

        for (ZrtBsegArtl ztbsegartl : artllist) {
            if (Integer.parseInt(ztbsegartl.getVkgrp())
                    >= Integer.parseInt(starvkgrp) && Integer.parseInt(ztbsegartl.getVkgrp())
                    < Integer.parseInt(endvkgrp)) {

                sumAtm060 += ztbsegartl.getAmt060().doubleValue();
                sumAtm090 += ztbsegartl.getAmt090().doubleValue();
                sumAtm120 += ztbsegartl.getAmt120().doubleValue();
                sumAtm150 += ztbsegartl.getAmt150().doubleValue();
                sumAtm180 += ztbsegartl.getAmt180().doubleValue();
                sumAtm270 += ztbsegartl.getAmt270().doubleValue();
                sumAtm365 += ztbsegartl.getAmt365().doubleValue();
                sumAtmlyr += ztbsegartl.getAmtlyr().doubleValue();
                sumAtmTot += ztbsegartl.getAmttot().doubleValue();
            }
        }
        zrtBsegArtl.setId((long) artllist.size() + 1);
        zrtBsegArtl.setVkgrp(starvkgrp);
        zrtBsegArtl.setSarea("TT");

        zrtBsegArtl.setAmt060(new java.math.BigDecimal(sumAtm060));
        zrtBsegArtl.setAmt090(new java.math.BigDecimal(sumAtm090));
        zrtBsegArtl.setAmt120(new java.math.BigDecimal(sumAtm120));
        zrtBsegArtl.setAmt150(new java.math.BigDecimal(sumAtm150));
        zrtBsegArtl.setAmt180(new java.math.BigDecimal(sumAtm180));
        zrtBsegArtl.setAmt270(new java.math.BigDecimal(sumAtm270));
        zrtBsegArtl.setAmt365(new java.math.BigDecimal(sumAtm365));
        zrtBsegArtl.setAmtlyr(new java.math.BigDecimal(sumAtmlyr));
        zrtBsegArtl.setAmttot(new java.math.BigDecimal(sumAtmTot));

        return zrtBsegArtl;
    }

    private void createArtlExcelByArtl(SkSalesMember sales, File totalTempFile,
            List<ZrtBsegArtl> artllist) throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFRow titleRow = sheet.createRow(0);
        createAragCells(titleRow);
        titleRow.getCell(0).setCellValue("銷售區域");
        titleRow.getCell(1).setCellValue("31-60天");
        titleRow.getCell(2).setCellValue("61-90天");
        titleRow.getCell(3).setCellValue("91-120天");
        titleRow.getCell(4).setCellValue("121-150天");
        titleRow.getCell(5).setCellValue("151-180天");
        titleRow.getCell(6).setCellValue("181-270天");
        titleRow.getCell(7).setCellValue("271-365天");
        titleRow.getCell(8).setCellValue(">365天");
        titleRow.getCell(9).setCellValue("合計");

        int i = 1;
        double sumAtm060 = 0, sumAtm090 = 0, sumAtm120 = 0,
                sumAtm150 = 0, sumAtm180 = 0, sumAtm270 = 0, sumAtm365 = 0,
                sumAtmlyr = 0, sumAtmTot = 0;
        for (ZrtBsegArtl ztbsegartl : artllist) {

            if ((ztbsegartl.getSarea().substring(0, 2).equals(sales.getCode().substring(0, 2)))) {
                HSSFRow row = sheet.createRow(i++);
                createAragCells(row);
                setArtlCellsData(ztbsegartl, row);

                sumAtm060 += ztbsegartl.getAmt060().doubleValue();
                sumAtm090 += ztbsegartl.getAmt090().doubleValue();
                sumAtm120 += ztbsegartl.getAmt120().doubleValue();
                sumAtm150 += ztbsegartl.getAmt150().doubleValue();
                sumAtm180 += ztbsegartl.getAmt180().doubleValue();
                sumAtm270 += ztbsegartl.getAmt270().doubleValue();
                sumAtm365 += ztbsegartl.getAmt365().doubleValue();
                sumAtmlyr += ztbsegartl.getAmtlyr().doubleValue();
                sumAtmTot += ztbsegartl.getAmttot().doubleValue();

            }

        }

        // 小計
        ZrtBsegArtl zrtBsegArtl = new ZrtBsegArtl();
        zrtBsegArtl.setId((long) i++);
        zrtBsegArtl.setSarea(sales.getCode().substring(0, 2));
        zrtBsegArtl.setVkgrp("TOT");
        zrtBsegArtl.setAmt060(new java.math.BigDecimal(sumAtm060));
        zrtBsegArtl.setAmt090(new java.math.BigDecimal(sumAtm090));
        zrtBsegArtl.setAmt120(new java.math.BigDecimal(sumAtm120));
        zrtBsegArtl.setAmt150(new java.math.BigDecimal(sumAtm150));
        zrtBsegArtl.setAmt180(new java.math.BigDecimal(sumAtm180));
        zrtBsegArtl.setAmt270(new java.math.BigDecimal(sumAtm270));
        zrtBsegArtl.setAmt365(new java.math.BigDecimal(sumAtm365));
        zrtBsegArtl.setAmtlyr(new java.math.BigDecimal(sumAtmlyr));
        zrtBsegArtl.setAmttot(new java.math.BigDecimal(sumAtmTot));

        HSSFRow row = sheet.createRow(i++);
        createAragCells(row);
        setArtlCellsData(zrtBsegArtl, row);

        System.out.println("detailTempFile >>" + totalTempFile);
        FileOutputStream fileOut = new FileOutputStream(totalTempFile);

        wb.write(fileOut);
        fileOut.close();

    }

    private void setArtlCellsData(ZrtBsegArtl zrtBsegArtl, HSSFRow row) {

        if (!zrtBsegArtl.getVkgrp().equals("TOT")) {
            row.getCell(0).setCellValue(zrtBsegArtl.getVkgrp());
        } else {
            row.getCell(0).setCellValue(zrtBsegArtl.getSarea() + "合計");
        }
        row.getCell(1).setCellValue(zrtBsegArtl.getAmt060().doubleValue());
        row.getCell(2).setCellValue(zrtBsegArtl.getAmt090().doubleValue());
        row.getCell(3).setCellValue(zrtBsegArtl.getAmt120().doubleValue());
        row.getCell(4).setCellValue(zrtBsegArtl.getAmt150().doubleValue());
        row.getCell(5).setCellValue(zrtBsegArtl.getAmt180().doubleValue());
        row.getCell(6).setCellValue(zrtBsegArtl.getAmt270().doubleValue());
        row.getCell(7).setCellValue(zrtBsegArtl.getAmt365().doubleValue());
        row.getCell(8).setCellValue(zrtBsegArtl.getAmtlyr().doubleValue());
        row.getCell(9).setCellValue(zrtBsegArtl.getAmt060().doubleValue()
                + zrtBsegArtl.getAmt090().doubleValue()
                + zrtBsegArtl.getAmt120().doubleValue()
                + zrtBsegArtl.getAmt150().doubleValue()
                + zrtBsegArtl.getAmt180().doubleValue()
                + zrtBsegArtl.getAmt270().doubleValue()
                + zrtBsegArtl.getAmt365().doubleValue()
                + zrtBsegArtl.getAmtlyr().doubleValue());
    }

    private void setArtlCellsDataT1(ZrtBsegArtl zrtBsegArtl, HSSFRow row, String TT) {
        if (!zrtBsegArtl.getSarea().equals("TT")) {
            row.getCell(0).setCellValue(zrtBsegArtl.getVkgrp());
        } else {
            row.getCell(0).setCellValue(zrtBsegArtl.getVkgrp().substring(0, 2) + "0合計");
        }
        if (zrtBsegArtl.getVkgrp().equals("000") && zrtBsegArtl.getSarea().equals("TT")) {
            row.getCell(0).setCellValue("GP合計");
        }
        if (zrtBsegArtl.getVkgrp().substring(0, 2).equals("31") && zrtBsegArtl.getSarea().equals("TT")) {
            row.getCell(0).setCellValue("中盤合計");
        }
        if (zrtBsegArtl.getVkgrp().equals("300") && zrtBsegArtl.getSarea().equals("TT")) {
            row.getCell(0).setCellValue("HP合計");
        }
        if (zrtBsegArtl.getVkgrp().equals("TOT")) {
            row.getCell(0).setCellValue("總計");
        }
        if (TT.equals("T1")) {
            row.getCell(1).setCellValue(zrtBsegArtl.getAmt270().doubleValue());
            row.getCell(2).setCellValue(zrtBsegArtl.getAmt365().doubleValue());
            row.getCell(3).setCellValue(zrtBsegArtl.getAmtlyr().doubleValue());
            row.getCell(4).setCellValue(zrtBsegArtl.getAmt270().doubleValue()
                    + zrtBsegArtl.getAmt365().doubleValue()
                    + zrtBsegArtl.getAmtlyr().doubleValue());
        }

        if (TT.equals("T2")) {
            row.getCell(1).setCellValue(zrtBsegArtl.getAmt060().doubleValue());
            row.getCell(2).setCellValue(zrtBsegArtl.getAmt090().doubleValue());
            row.getCell(3).setCellValue(zrtBsegArtl.getAmt120().doubleValue());
            row.getCell(4).setCellValue(zrtBsegArtl.getAmt150().doubleValue());
            row.getCell(5).setCellValue(zrtBsegArtl.getAmt180().doubleValue());
            row.getCell(6).setCellValue(zrtBsegArtl.getAmt270().doubleValue());
            row.getCell(7).setCellValue(zrtBsegArtl.getAmt365().doubleValue());
            row.getCell(8).setCellValue(zrtBsegArtl.getAmtlyr().doubleValue());
            row.getCell(9).setCellValue(
                    zrtBsegArtl.getAmt060().doubleValue()
                    + zrtBsegArtl.getAmt090().doubleValue()
                    + zrtBsegArtl.getAmt120().doubleValue()
                    + zrtBsegArtl.getAmt150().doubleValue()
                    + zrtBsegArtl.getAmt180().doubleValue()
                    + zrtBsegArtl.getAmt270().doubleValue()
                    + zrtBsegArtl.getAmt365().doubleValue()
                    + zrtBsegArtl.getAmtlyr().doubleValue());
        }
    }

    public List<ZrtBsegArag> findArag() {
        List<ZrtBsegArag> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<ZrtBsegArag> cq = builder.createQuery(ZrtBsegArag.class);
        Root<ZrtBsegArag> root = cq.from(ZrtBsegArag.class);

        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = builder.greaterThan(root.get("arday").as(Integer.class), 30);
        predicateList.add(p);

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    public List<ZrtBsegArag> findArag(SkSalesMember sales) {
        List<ZrtBsegArag> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<ZrtBsegArag> cq = builder.createQuery(ZrtBsegArag.class);
        Root<ZrtBsegArag> root = cq.from(ZrtBsegArag.class);

        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (sales != null) {
            Predicate p = builder.equal(root.get("vkgrp"), sales.getCode());
            predicateList.add(p);
        }
        Predicate p = builder.greaterThan(root.get("arday").as(Integer.class), 0);
        predicateList.add(p);

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }

    private void createAragExcelT1(List<ZrtBsegArag> araglist, File tempfile, String TT) throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFRow titleRow = sheet.createRow(0);
        createAragCells(titleRow);
        if (TT.equals("T1")) {
            titleRow.getCell(0).setCellValue("客戶");
            titleRow.getCell(1).setCellValue("客戶名稱1");
            titleRow.getCell(2).setCellValue("年度");
            titleRow.getCell(3).setCellValue("會計文件號碼");
            titleRow.getCell(4).setCellValue("指派");
            titleRow.getCell(5).setCellValue("內文");
            titleRow.getCell(6).setCellValue("原發票日期");
            titleRow.getCell(7).setCellValue("應收帳款總額");
            titleRow.getCell(8).setCellValue("銷售區域");
            titleRow.getCell(9).setCellValue("原基準日");
            titleRow.getCell(10).setCellValue("關鍵分析日期");
            titleRow.getCell(11).setCellValue("原付款條件");
            titleRow.getCell(12).setCellValue("原折扣天數");
            titleRow.getCell(13).setCellValue("帳齡天數");
            titleRow.getCell(14).setCellValue("逾期181-270天");
            titleRow.getCell(15).setCellValue("逾期271-365天");
            titleRow.getCell(16).setCellValue("逾期>365天");
            titleRow.getCell(17).setCellValue("帳款未結原因");
            titleRow.getCell(18).setCellValue("預計完成日");
            titleRow.getCell(19).setCellValue("主管確認");
        }

        if (TT.equals("T2")) {
            titleRow.getCell(0).setCellValue("客戶");
            titleRow.getCell(1).setCellValue("客戶名稱1");
            titleRow.getCell(2).setCellValue("年度");
            titleRow.getCell(3).setCellValue("會計文件號碼");
            titleRow.getCell(4).setCellValue("指派");
            titleRow.getCell(5).setCellValue("內文");
            titleRow.getCell(6).setCellValue("原發票日期");
            titleRow.getCell(7).setCellValue("應收帳款總額");
            titleRow.getCell(8).setCellValue("銷售區域");
            titleRow.getCell(9).setCellValue("原基準日");
            titleRow.getCell(10).setCellValue("關鍵分析日期");
            titleRow.getCell(11).setCellValue("原付款條件");
            titleRow.getCell(12).setCellValue("原折扣天數");
            titleRow.getCell(13).setCellValue("帳齡天數");
            titleRow.getCell(14).setCellValue("逾期31-60天");
            titleRow.getCell(15).setCellValue("逾期61-90天");
            titleRow.getCell(16).setCellValue("逾期91-120天");
            titleRow.getCell(17).setCellValue("逾期121-150天");
            titleRow.getCell(18).setCellValue("逾期151-180天");
            titleRow.getCell(19).setCellValue("逾期181-270天");
            titleRow.getCell(20).setCellValue("逾期271-365天");
            titleRow.getCell(21).setCellValue("逾期>365天");
            titleRow.getCell(22).setCellValue("帳款未結原因");
            titleRow.getCell(23).setCellValue("預計完成日");
            titleRow.getCell(24).setCellValue("主管確認");
        }


        int i = 1;
        for (ZrtBsegArag zrtBsegArag : araglist) {
            HSSFRow row = sheet.createRow(i++);
            createAragCells(row);
            if (TT.equals("T1")) {
                if (zrtBsegArag.getArday() > 180) {
                    row.getCell(0).setCellValue(zrtBsegArag.getKunnr());
                    row.getCell(1).setCellValue(zrtBsegArag.getName1());
                    row.getCell(2).setCellValue(zrtBsegArag.getZgjahr());
                    row.getCell(3).setCellValue(zrtBsegArag.getBuzei());
                    row.getCell(4).setCellValue(zrtBsegArag.getZuonr());
                    row.getCell(5).setCellValue(zrtBsegArag.getSgtxt());
                    row.getCell(6).setCellValue(zrtBsegArag.getZivdt());
                    row.getCell(7).setCellValue(zrtBsegArag.getAmttot().doubleValue());
                    row.getCell(8).setCellValue(zrtBsegArag.getVkgrp());
                    row.getCell(9).setCellValue(zrtBsegArag.getZfbdt());
                    row.getCell(10).setCellValue(zrtBsegArag.getAndate());
                    row.getCell(11).setCellValue(zrtBsegArag.getZterm());
                    row.getCell(12).setCellValue(zrtBsegArag.getZbd1t().doubleValue());
                    row.getCell(13).setCellValue(zrtBsegArag.getArday().doubleValue());
                    row.getCell(14).setCellValue(zrtBsegArag.getAmt270().doubleValue());
                    row.getCell(15).setCellValue(zrtBsegArag.getAmt365().doubleValue());
                    row.getCell(16).setCellValue(zrtBsegArag.getAmtlyr().doubleValue());
                }
            }

            if (TT.equals("T2")) {
                row.getCell(0).setCellValue(zrtBsegArag.getKunnr());
                row.getCell(1).setCellValue(zrtBsegArag.getName1());
                row.getCell(2).setCellValue(zrtBsegArag.getZgjahr());
                row.getCell(3).setCellValue(zrtBsegArag.getBuzei());
                row.getCell(4).setCellValue(zrtBsegArag.getZuonr());
                row.getCell(5).setCellValue(zrtBsegArag.getSgtxt());
                row.getCell(6).setCellValue(zrtBsegArag.getZivdt());
                row.getCell(7).setCellValue(zrtBsegArag.getAmttot().doubleValue());
                row.getCell(8).setCellValue(zrtBsegArag.getVkgrp());
                row.getCell(9).setCellValue(zrtBsegArag.getZfbdt());
                row.getCell(10).setCellValue(zrtBsegArag.getAndate());
                row.getCell(11).setCellValue(zrtBsegArag.getZterm());
                row.getCell(12).setCellValue(zrtBsegArag.getZbd1t().doubleValue());
                row.getCell(13).setCellValue(zrtBsegArag.getArday().doubleValue());
                row.getCell(14).setCellValue(zrtBsegArag.getAmt060().doubleValue());
                row.getCell(15).setCellValue(zrtBsegArag.getAmt090().doubleValue());
                row.getCell(16).setCellValue(zrtBsegArag.getAmt120().doubleValue());
                row.getCell(17).setCellValue(zrtBsegArag.getAmt150().doubleValue());
                row.getCell(18).setCellValue(zrtBsegArag.getAmt180().doubleValue());
                row.getCell(19).setCellValue(zrtBsegArag.getAmt270().doubleValue());
                row.getCell(20).setCellValue(zrtBsegArag.getAmt365().doubleValue());
                row.getCell(21).setCellValue(zrtBsegArag.getAmtlyr().doubleValue());
            }
        }
        System.out.println("detailTempFileT1 >>" + tempfile);
        FileOutputStream fileOut = new FileOutputStream(tempfile);

        wb.write(fileOut);
        fileOut.close();

    }

    private void createAragExcel(List<ZrtBsegArag> araglist, File tempfile) throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        HSSFRow titleRow = sheet.createRow(0);
        createAragCells(titleRow);
        titleRow.getCell(0).setCellValue("客戶");
        titleRow.getCell(1).setCellValue("客戶名稱1");
        titleRow.getCell(2).setCellValue("年度");
        titleRow.getCell(3).setCellValue("會計文件號碼");
        titleRow.getCell(4).setCellValue("指派");
        titleRow.getCell(5).setCellValue("內文");
        titleRow.getCell(6).setCellValue("原發票日期");
        titleRow.getCell(7).setCellValue("應收帳款總額");
        titleRow.getCell(8).setCellValue("銷售區域");
        titleRow.getCell(9).setCellValue("原基準日");
        titleRow.getCell(10).setCellValue("關鍵分析日期");
        titleRow.getCell(11).setCellValue("原付款條件");
        titleRow.getCell(12).setCellValue("原折扣天數");
        titleRow.getCell(13).setCellValue("帳齡天數");
        titleRow.getCell(14).setCellValue("逾期31-60天");
        titleRow.getCell(15).setCellValue("逾期61-90天");
        titleRow.getCell(16).setCellValue("逾期91-120天");
        titleRow.getCell(17).setCellValue("逾期121-150天");
        titleRow.getCell(18).setCellValue("逾期151-180天");
        titleRow.getCell(19).setCellValue("逾期181-270天");
        titleRow.getCell(20).setCellValue("逾期271-365天");
        titleRow.getCell(21).setCellValue("逾期>365天");
        titleRow.getCell(22).setCellValue("帳款未結原因");
        titleRow.getCell(23).setCellValue("預計完成日");
        titleRow.getCell(24).setCellValue("主管確認");


        int i = 1;
        for (ZrtBsegArag zrtBsegArag : araglist) {
            HSSFRow row = sheet.createRow(i++);
            createAragCells(row);
            row.getCell(0).setCellValue(zrtBsegArag.getKunnr());
            row.getCell(1).setCellValue(zrtBsegArag.getName1());
            row.getCell(2).setCellValue(zrtBsegArag.getZgjahr());
            row.getCell(3).setCellValue(zrtBsegArag.getBuzei());
            row.getCell(4).setCellValue(zrtBsegArag.getZuonr());
            row.getCell(5).setCellValue(zrtBsegArag.getSgtxt());
            row.getCell(6).setCellValue(zrtBsegArag.getZivdt());
            row.getCell(7).setCellValue(zrtBsegArag.getAmttot().doubleValue());
            row.getCell(8).setCellValue(zrtBsegArag.getVkgrp());
            row.getCell(9).setCellValue(zrtBsegArag.getZfbdt());
            row.getCell(10).setCellValue(zrtBsegArag.getAndate());
            row.getCell(11).setCellValue(zrtBsegArag.getZterm());
            row.getCell(12).setCellValue(zrtBsegArag.getZbd1t().doubleValue());
            row.getCell(13).setCellValue(zrtBsegArag.getArday().doubleValue());
            row.getCell(14).setCellValue(zrtBsegArag.getAmt060().doubleValue());
            row.getCell(15).setCellValue(zrtBsegArag.getAmt090().doubleValue());
            row.getCell(16).setCellValue(zrtBsegArag.getAmt120().doubleValue());
            row.getCell(17).setCellValue(zrtBsegArag.getAmt150().doubleValue());
            row.getCell(18).setCellValue(zrtBsegArag.getAmt180().doubleValue());
            row.getCell(19).setCellValue(zrtBsegArag.getAmt270().doubleValue());
            row.getCell(20).setCellValue(zrtBsegArag.getAmt365().doubleValue());
            row.getCell(21).setCellValue(zrtBsegArag.getAmtlyr().doubleValue());
        }
        System.out.println("detailTempFile >>" + tempfile);
        FileOutputStream fileOut = new FileOutputStream(tempfile);

        wb.write(fileOut);
        fileOut.close();

    }

    private void createAragCells(HSSFRow row) {
        for (int i = 0; i < 30; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
    }
}
