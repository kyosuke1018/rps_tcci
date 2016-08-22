package com.tcci.sapproxy.jco;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sap.jco.model.RfcOutputTypeEnum;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyOutput;
import com.tcci.sap.jco.rest.RfcHttpClient;
import com.tcci.sap.jco.util.JCoUtil;
import com.tcci.sksp.controller.util.DateUtil;
import com.tcci.sksp.util.GlobalConstant;
import com.tcci.worklist.entity.datawarehouse.ZtabExpRelfilenoSd;
import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFilter;
import com.tcci.sapproxy.PpProxy;
import com.tcci.sapproxy.dto.SapProxyResponseDto;
import com.tcci.sapproxy.dto.SapTableDto;
import com.tcci.sapproxy.enums.SapProxyResponseEnum;
import com.tcci.sapproxy.enums.SapSystemEnum;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.vo.FiInterfaceVO;
import com.tcci.worklist.vo.ZtabExpRelfilenoSdVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 透過 SAPService Call RFC
 *
 * @author Neo.Fu
 */
public class PpSapProxyImpl implements PpProxy {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * RFC功能名稱: ZSAP_JAVA_IMP_SD_RELEASE (維護銷售文件)
     */
    final static String RFC_ZSAP_JAVA_IMP_SD_RELEASE = "ZSAP_JAVA_IMP_SD_RELEASE";
    /**
     * RFC功能名稱: SD ZSAP_JAVA_EXP_RELFILENO_SD (帶出銷售文件待核文件號碼)
     */
    final static String ZSAP_JAVA_EXP_RELFILENO_SD = "ZSAP_JAVA_EXP_RELFILENO_SD";
    /**
     * RFC功能名稱: ZWIS_FI_CLAR_SKPC (上傳傳票)
     */
    final static String RFC_ZWIS_FI_CLAR_SKPC = "ZWIS_FI_CLAR_SKPC";
    /**
     * RFC功能名稱: ZSAP_JAVA_IMP_QT_CREATE (報價單(496)建立).
     */
    final static String RFC_ZSAP_JAVA_IMP_QT_CREATE = "ZSAP_JAVA_IMP_QT_CREATE";

    protected List<TcUser> notifyUsers;
    protected String sapClientCode;
    protected String sapServiceUrl;
    protected String operator;

    @Override
    public void init(String sapClientCode, String sapServiceUrl, String operator) {
        setSapClientCode(sapClientCode);
        setSapServiceUrl(sapServiceUrl);
        setOperator(operator);
    }

    @Override
    public void dispose() {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    @Override
    public void setSapServiceUrl(String sapServiceUrl) {
        this.sapServiceUrl = sapServiceUrl;
    }

    @Override
    public void setNotifyUsers(List<TcUser> notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    @Override
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 日期時間欄位轉換
     *
     * @param src
     * @return
     */
    public String toJCoDate(Date src) {
        return DateUtil.formatDateString(src, GlobalConstant.FORMAT_DATE_STR);
    }

    public String toJCoTime(Date src) {
        return DateUtil.formatDateString(src, GlobalConstant.FORMAT_TIME_STR);
    }

    public String toJCoDateTime(Date src) {
        return DateUtil.formatDateString(src, GlobalConstant.FORMAT_DATETIME_STR);
    }

    @Override
    public SapProxyResponseDto doSdDocumentRelease(List<ZtabExpRelfilenoSdVO> VOs, String usermode) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SapProxyResponseDto sapProxyReponseDto;
        try {
            // 準備Proxy輸入參數
            String rfcName = RFC_ZSAP_JAVA_IMP_SD_RELEASE;
            RfcProxyInput input = new RfcProxyInput(GlobalConstant.AP_NAME, rfcName, sapClientCode, operator);

            // 準備RFC輸入參數
            Map<String, List<Map<String, Object>>> impTableParams = new HashMap();

            List<Map<String, Object>> tables = new ArrayList();
            String bname = "";
            String bersl = "";
            for (ZtabExpRelfilenoSdVO vo : VOs) {
                ZtabExpRelfilenoSd ztabExpRelfilenoSd = vo.getZtabExpRelfilenoSd();
                bname = ztabExpRelfilenoSd.getBname();
                bersl = ztabExpRelfilenoSd.getBersl();
                logger.debug("doSdDocumentRelease(), bname={},bersl={},vbeln={},posnr={},objnr={},stsma={},cstat={},nstat={},relText={},usermode={}",
                        new Object[]{ztabExpRelfilenoSd.getBname(),
                            ztabExpRelfilenoSd.getBersl(),
                            ztabExpRelfilenoSd.getVbeln(),
                            ztabExpRelfilenoSd.getPosnr(),
                            ztabExpRelfilenoSd.getObjnr(),
                            ztabExpRelfilenoSd.getStsma(),
                            ztabExpRelfilenoSd.getCstat(),
                            ztabExpRelfilenoSd.getNstat(),
                            ztabExpRelfilenoSd.getRelText(),
                            usermode});
                Map<String, Object> table = new HashMap();
                table.put("VBELN", ztabExpRelfilenoSd.getVbeln());
                table.put("POSNR", ztabExpRelfilenoSd.getPosnr());
                table.put("OBJNR", ztabExpRelfilenoSd.getObjnr());
                table.put("STSMA", ztabExpRelfilenoSd.getStsma());
                table.put("CSTAT", ztabExpRelfilenoSd.getCstat());
                table.put("NSTAT", ztabExpRelfilenoSd.getNstat());
                table.put("REL_TEXT", ztabExpRelfilenoSd.getRelText());
                tables.add(table);
            }
            impTableParams.put("ZTAB_IMP_SD_NSTAT", tables);
            Map<String, Object> usrmodeMap = new HashMap();
            usrmodeMap.put("BNAME", bname);
            usrmodeMap.put("BERSL", bersl);
            usrmodeMap.put("USRMODE", usermode);
            List<Map<String, Object>> tables2 = new ArrayList();
            tables2.add(usrmodeMap);
            impTableParams.put("ZTAB_IMP_USRMODE", tables2);
            input.setTables(impTableParams);

            // 準備RFC輸出類型參數
            String expTableName = "ZTAB_EXP_SD_MESSAGE";
            Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
            outputType.put(expTableName, RfcOutputTypeEnum.TABLE);
            input.setOutputTypes(outputType);

            // 呼叫 Proxy 執行 RFC // http://localhost:8080/SAPService/resources
            RfcHttpClient client = new RfcHttpClient(sapServiceUrl);
            client.setSsoProtected(GlobalConstant.SAP_SERVICE_SSO);
            RfcProxyOutput out = client.callRfc(input);
            client.close();
            sapProxyReponseDto = handleResult(out.getTable(expTableName).get(0));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sapProxyReponseDto = new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_CONNECTION,
                    null,
                    e.getMessage(),
                    null);
        }
        stopWatch.stop();
        logger.info("StopWatch => findExpRelFileNoSDs (" + ZSAP_JAVA_EXP_RELFILENO_SD + "): ms " + stopWatch.getTime());
        return sapProxyReponseDto;

    }

    private SapProxyResponseDto handleResult(Map<String, Object> result) {
        //若回傳的訊息類型欄位(TYPE)不等於'S'，表示執行失敗
        String rtnType = (String) result.get("TYPE");
        if (!StringUtils.equals(rtnType, "S")) {
            String errMsg = (String) result.get("MESSAGE");
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                    rtnType,
                    errMsg,
                    result);
        }

        return new SapProxyResponseDto(
                SapSystemEnum.JCO,
                SapProxyResponseEnum.SUCCESS,
                result);
    }

    @Override
    public SapProxyResponseDto findExpRelFileNoSDs(ZtabExpRelfilenoSdFilter filter, String userMode) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SapProxyResponseDto sapProxyReponseDto;
        try {
            // 準備Proxy輸入參數
            String rfcName = ZSAP_JAVA_EXP_RELFILENO_SD;
            RfcProxyInput input = new RfcProxyInput(GlobalConstant.AP_NAME, rfcName, sapClientCode, operator);

            // 準備RFC輸入參數
            Map<String, List<Map<String, Object>>> impTableParams = new HashMap<String, List<Map<String, Object>>>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (filter.getAudatBegin() != null && filter.getAudatEnd() != null) {
                Calendar audatEnd = new GregorianCalendar();
                audatEnd.setTime(filter.getAudatEnd());
                audatEnd.add(Calendar.DATE, 1);
                logger.debug("between {} and {}", new Object[]{filter.getAudatBegin(), audatEnd});
                List<Map<String, Object>> tables = JCoUtil.buildSimpleImpList("I", "BT", sdf.format(filter.getAudatBegin()), sdf.format(audatEnd.getTime()));
                impTableParams.put("ZFLD_IMP_ERDAT", tables);
            } else if (filter.getAudatBegin() != null && filter.getAudatEnd() == null) {
                logger.debug("greater than or equal to {}", filter.getAudatBegin());
                List<Map<String, Object>> tables = JCoUtil.buildSimpleImpList("I", "GE", sdf.format(filter.getAudatBegin()), null);
                impTableParams.put("ZFLD_IMP_ERDAT", tables);
            } else if (filter.getAudatBegin() == null && filter.getAudatEnd() != null) {
                Calendar audatEnd = new GregorianCalendar();
                audatEnd.setTime(filter.getAudatEnd());
                audatEnd.add(Calendar.DATE, 1);
                logger.debug("less than {}", audatEnd);
                List<Map<String, Object>> tables = JCoUtil.buildSimpleImpList("I", "LT", sdf.format(audatEnd.getTime()), null);
                impTableParams.put("ZFLD_IMP_ERDAT", tables);
            }

            if (StringUtils.isNotEmpty(filter.getVkorg())) {
                logger.debug("vkorg={}", filter.getVkorg());
                List<Map<String, Object>> tables = JCoUtil.buildSimpleImpList("I", "EQ", filter.getVkorg(), "");
                impTableParams.put("ZFLD_IMP_VKORG", tables);
            }
            if (StringUtils.isNotEmpty(filter.getVtweg())) {
                logger.debug("vtweg={}", filter.getVtweg());
                List<Map<String, Object>> tables = JCoUtil.buildSimpleImpList("I", "EQ", filter.getVtweg(), "");
                impTableParams.put("ZFLD_IMP_VTWEG", tables);
            }
            if (StringUtils.isNotEmpty(filter.getVbeln())) {
                logger.debug("vbeln={}", filter.getVbeln());
                List<Map<String, Object>> tables = JCoUtil.buildSimpleImpList("I", "CP", filter.getVbeln(), "");
                impTableParams.put("ZFLD_IMP_VBELN", tables);
            }

            Map<String, Object> usrModeMap = new HashMap();
            usrModeMap.put("BNAME", filter.getBname());
            usrModeMap.put("BERSL", filter.getBersl());
            usrModeMap.put("USRMODE", userMode);
            List<Map<String, Object>> tables = new ArrayList();
            tables.add(usrModeMap);
            impTableParams.put("ZTAB_IMP_USRMODE", tables);

            input.setTables(impTableParams);

            //debug mode
            //input.setDebugMode(true);
            // 準備RFC輸出類型參數
            String expTableName = "ZTAB_EXP_RELFILENO_SD";
            Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
            outputType.put(expTableName, RfcOutputTypeEnum.TABLE);
            input.setOutputTypes(outputType);

            // 呼叫 Proxy 執行 RFC // http://localhost:8080/SAPService/resources
            RfcHttpClient client = new RfcHttpClient(sapServiceUrl);
            client.setSsoProtected(GlobalConstant.SAP_SERVICE_SSO);
            RfcProxyOutput out = client.callRfc(input);
            client.close();

            //JCoUtils.logFunctionResult(function);// log RFC result
            SapTableDto result = new SapTableDto();
            if (out != null && out.getTable(expTableName) != null) {
                result.setDataMapList(out.getTable(expTableName));
            }

            sapProxyReponseDto = new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sapProxyReponseDto = new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_CONNECTION,
                    null,
                    e.getMessage(),
                    null);
        }
        stopWatch.stop();
        logger.info("StopWatch => findExpRelFileNoSDs (" + ZSAP_JAVA_EXP_RELFILENO_SD + "): ms " + stopWatch.getTime());
        return sapProxyReponseDto;
    }

    @Override
    public SapProxyResponseDto doUpload(List<FiInterfaceVO> interfaceVOList) throws Exception {
        List<Map<String, Object>> result = null;
        try {
            // 準備Proxy輸入參數
            String rfcName = RFC_ZWIS_FI_CLAR_SKPC;
            RfcProxyInput input = new RfcProxyInput(GlobalConstant.AP_NAME, rfcName, sapClientCode, operator);

            // 準備RFC輸入參數
            Map<String, List<Map<String, Object>>> impTableParams = new HashMap<String, List<Map<String, Object>>>();
            List<Map<String, Object>> tables = new ArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            for (FiInterfaceVO vo : interfaceVOList) {
                Map<String, Object> table = new HashMap();
                table.put("TRNNO", vo.getDetail().getMaster().getTransactionNo());
                table.put("TRNITM", vo.getDetail().getTransactionItem());
                table.put("TRNDT", sdf.format(vo.getDetail().getTransactionDate()));
                table.put("TRNTP", vo.getDetail().getTransactionType());
                table.put("FGLID", vo.getDetail().getGeneralLedgerCode());
                table.put("FDBCR", vo.getDetail().getSummonsCode());
                table.put("CUSNO", vo.getDetail().getCustomerNumber());
                table.put("INVNO", vo.getDetail().getInvoiceNumber());
                table.put("SALNO", vo.getDetail().getOrderNumber());
                table.put("SALGRP", vo.getDetail().getSalesGroup());
                double amount = vo.getDetail().getTransactionAmount().doubleValue();
                table.put("TRNAMT", amount);
                //table.put("TRNQTY",vo.getDetail().getQuantity()); //not in use currently
                table.put("CHKNO", vo.getDetail().getCheckNumber());
                //table.put("CHKDT", vo.getDetail().getCheckDueDate());
                if (vo.getDetail().getCheckDueDate() != null) {
                    table.put("CHKDT", sdf.format(vo.getDetail().getCheckDueDate()));
                }
                table.put("CHKBK", vo.getDetail().getCheckBank());
                table.put("CHKAC", vo.getDetail().getCheckAccount());
                table.put("OWNER", vo.getDetail().getOwner());
                tables.add(table);
            }
            impTableParams.put("CLAR_DATA", tables);
            input.setTables(impTableParams);

            // 準備RFC輸出類型參數
            String expTableName = "CLAR_DATA";
            Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
            outputType.put(expTableName, RfcOutputTypeEnum.TABLE);
            input.setOutputTypes(outputType);

            // 呼叫 Proxy 執行 RFC // http://localhost:8080/SAPService/resources
            RfcHttpClient client = new RfcHttpClient(sapServiceUrl);
            client.setSsoProtected(GlobalConstant.SAP_SERVICE_SSO);
            RfcProxyOutput out = client.callRfc(input);
            client.close();

            result = out.getTable("CLAR_DATA");
            logger.debug("result=" + result);
            return handleResult(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_CONNECTION,
                    null,
                    e.getMessage(),
                    null);
        }

    }

    /**
     * 處理回傳值，當RETCODE不為000時，回傳錯誤。
     *
     * @param result
     * @return
     */
    private SapProxyResponseDto handleResult(List<Map<String, Object>> results) {
        for (Map<String, Object> result : results) {
            String rtnType = (String) result.get("RETCODE");
            if (!StringUtils.equals(rtnType, "000")) {
                String errMsg = (String) result.get("RETMESG");
                return new SapProxyResponseDto(
                        SapSystemEnum.JCO,
                        SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                        rtnType,
                        errMsg,
                        results);
            }
        }
        return new SapProxyResponseDto(
                SapSystemEnum.JCO,
                SapProxyResponseEnum.SUCCESS,
                results);
    }

    @Override
    public SapProxyResponseDto createQuotation(SkQuotationMaster quotationMaster, String mandt) throws Exception {
        List<Map<String, Object>> result = null;
        try {
            // 準備Proxy輸入參數
            String rfcName = RFC_ZSAP_JAVA_IMP_QT_CREATE;
            RfcProxyInput input = new RfcProxyInput(GlobalConstant.AP_NAME, rfcName, sapClientCode, operator);

            // 準備RFC輸入參數
            Map<String, List<Map<String, Object>>> impTableParams = new HashMap<String, List<Map<String, Object>>>();
            List<Map<String, Object>> tables = new ArrayList();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //master
            Map<String, Object> table = new HashMap();
            //用戶端
            logger.debug("MANDT(用戶端)={}", mandt);
            table.put("MANDT", mandt);

            //銷售文件類型
            logger.debug("AUART(銷售文件類型)=ZQT1");
            table.put("AUART", "ZQT1");

            //訂貨原因 (for 退貨)
//            logger.debug("AUGRU(訂貨原因)={}", "");
//            table.put("AUGRU", "");
            //文件幣別 (不必填, SAP預設)
//            logger.debug("WAERK(文件幣別)=TWD");
//            table.put("WAERK", "TWD");
            //銷售組織
            logger.debug("VKORG(銷售組織)=9600");
            table.put("VKORG", "9600");

            //配銷通路
            logger.debug("VTWEG(配銷通路)={}", quotationMaster.getCustomer().getSapid().substring(0, 2));
            table.put("VTWEG", quotationMaster.getCustomer().getSapid().substring(0, 2));

            //部門 (不必填, SAP預設)
//            logger.debug("SPART(部門)={}",);
//            table.put("SPART", );
            //銷售據點 (不必填, SAP預設)
//            logger.debug("VKBUR(銷售據點)={}",);
//            table.put("VKBUR", );
            //銷售群組 (不必填, SAP預設)
//            logger.debug("VKGRP(銷售群組)={}", quotationMaster.getCustomer().getSapid());
//            table.put("VKGRP", quotationMaster.getCustomer().getSapid());
            //請求交貨日期 (預設當天日期)
//            logger.debug("VDATU(請求交貨日期 (預設當天日期))={}", sdf.format(quotationMaster.getQuotationDate()));
//            table.put("VDATU", sdf.format(quotationMaster.getQuotationDate()));
            //買方
            logger.debug("KUNNR(買方)={}", quotationMaster.getCustomer().getSimpleCode());
            table.put("KUNNR", quotationMaster.getCustomer().getSimpleCode());

            //收貨人
            logger.debug("KUNWE(收貨人)={}", quotationMaster.getConsignee().getSimpleCode());
            table.put("KUNWE", quotationMaster.getConsignee().getSimpleCode());

//            //參考文件號碼
//            logger.debug("XBLNR(參考文件號碼)={}", "");
//            table.put("XBLNR", "");
////            logger.debug("XBLNR(參考文件號碼)={}",);
////            table.put("XBLNR", );
            //指派號碼 (報價單編號)
            logger.debug("ZUONR(指派號碼 (報價單編號))={}", quotationMaster.getId());
            table.put("ZUONR", quotationMaster.getId());

            //先前 SD 文件的文件種類 (不必填, SAP預設)
//            logger.debug("VGTYP(先前 SD 文件的文件種類)={}",);
//            table.put("VGTYP", );
            //客戶採購單號碼
            if (StringUtils.isNotEmpty(quotationMaster.getPoNo())) {
                logger.debug("BSTNK(客戶採購單號碼)={}", quotationMaster.getPoNo());
                table.put("BSTNK", quotationMaster.getPoNo());
            }

            //客戶群組一(下拉備註1)
            if (StringUtils.isNotEmpty(quotationMaster.getRemark1())) {
                logger.debug("KVGR1(客戶群組一(下拉備註1))={}", quotationMaster.getRemark1());
                table.put("KVGR1", quotationMaster.getRemark1());
            }
//            logger.debug("KVGR1(客戶群組一)={}",);
//            table.put("KVGR1", );

            //客戶群組二(下拉備註2)
            if (StringUtils.isNotEmpty(quotationMaster.getRemark2())) {
                logger.debug("KVGR2(客戶群組二(下拉備註2))={}", quotationMaster.getRemark2());
                table.put("KVGR2", quotationMaster.getRemark2());
            }
//            logger.debug("KVGR2(客戶群組二)={}",);
//            table.put("KVGR2", );

            //客戶群組三 (下拉備註3)
            if (StringUtils.isNotEmpty(quotationMaster.getRemark3())) {
                logger.debug("KVGR3(客戶群組三(下拉備註3))={}", quotationMaster.getRemark3());
                table.put("KVGR3", quotationMaster.getRemark3());
            }
//            logger.debug("KVGR3(客戶群組三)={}",);
//            table.put("KVGR3", );

            //客戶群組四 (目前沒用)
//            logger.debug("KVGR4(客戶群組四)={}",);
//            table.put("KVGR4", );
            //客戶群組五 (目前沒用)
//            logger.debug("KVGR5(客戶群組五)={}",);
//            table.put("KVGR5", );
            //內文編輯器內文行 (備註)
            if (StringUtils.isNotEmpty(quotationMaster.getRemark())) {
                logger.debug("TXLINE(內文編輯器內文行(備註))={}", quotationMaster.getRemark());
                table.put("TXLINE", quotationMaster.getRemark());
            }
//            logger.debug("TXLINE(內文編輯器內文行)={}",);
//            table.put("TXLINE", );
            logger.debug("table={}", table);
            tables.add(table);
            impTableParams.put("ZTAB_IMP_QT_HEAD", tables);

            //detail
            List<Map<String, Object>> detailTables = new ArrayList();
            int index = 10;
            for (SkQuotationDetail detail : quotationMaster.getDetailCollection()) {
                Map<String, Object> detailTable = new HashMap();
                //用戶端
                logger.debug("MANDT(用戶端)={}", mandt);
                detailTable.put("MANDT", mandt);

                //銷售文件項目
                logger.debug("POSNR(銷售文件項目)={}", index);
                int parentIndex = index;
                detailTable.put("POSNR", index);

                //物料
                logger.debug("MATNR(物料)={}", detail.getProductNumber());
                detailTable.put("MATNR", detail.getProductNumber());

                //物料表結構中的上層項目 (訂單品項不需要, 贈品才需要)
//                logger.debug("UEPOS(物料表結構中的上層項目)={}", );
//                detailTable.put("UEPOS", );
                //幣別 (不必填, SAP預設)
//                logger.debug("WAERK(幣別)=TWD");
//                detailTable.put("WAERK", "TWD");
                //累計訂購數量
                logger.debug("KWMENG(累計訂購數量)={}", "000000000" + String.valueOf(detail.getQuantity()));
                detailTable.put("KWMENG", "000000000" + String.valueOf(detail.getQuantity()));

                //銷售單位 (不必填, SAP預設)
//                logger.debug("VRKME(銷售單位)={}",);
//                detailTable.put("VRKME",);
                //工廠 (不必填, SAP預設 (但測試時要填9620))
//                //test
//                logger.debug("WERKS(工廠)=9620");
//                detailTable.put("WERKS", "9620");
                //儲存地點 (不必填, SAP預設 (但測試時要填9626))
//                //test
//                logger.debug("LGORT(儲存地點)={}", "9626");
//                detailTable.put("LGORT", "9626");
//                logger.debug("LGORT(儲存地點)={}",);
//                detailTable.put("LGORT",);
                //出貨點/收貨點 (不必填, SAP預設)
//                logger.debug("VSTEL(出貨點/收貨點)={}",);
//                detailTable.put("VSTEL",);
                //條件基值 (不必填, SAP預設)
//                logger.debug("KAWRT(條件基值)={}", "0000000000000");
//                detailTable.put("KAWRT", "0000000000000");
                //費率
                int multiplier = 1;
                logger.debug("detail.getPrice().scale()={}", detail.getPrice().scale());
                for (int i = 1; i <= detail.getPrice().scale(); i++) {
                    multiplier *= 10;
                }
                logger.debug("KBETR(費率)={}", "0000000" + String.valueOf(detail.getPrice().multiply(BigDecimal.valueOf(multiplier)).longValue()));
                detailTable.put("KBETR", "0000000" + String.valueOf(detail.getPrice().multiply(BigDecimal.valueOf(multiplier)).longValue()));
//                logger.debug("KBETR(費率)={}",);
//                detailTable.put("KBETR",);

                //條件定價單位
                logger.debug("KPEIN(條件定價單位)={}", "000" + String.valueOf(multiplier));
                detailTable.put("KPEIN", "000" + String.valueOf(multiplier));

                //條件單位 (不必填, SAP預設)
//                logger.debug("KMEIN(條件單位)={}",);
//                detailTable.put("KMEIN",);
                //溢價折讓
                if (detail.getPremiumDiscount() != null && detail.getPremiumDiscount().compareTo(BigDecimal.ZERO) != 0) {
                    index = index + 10;
                    //用戶端
                    logger.debug("MANDT(用戶端)={}", mandt);
                    detailTable.put("MANDT", mandt);

                    //銷售文件項目
                    logger.debug("POSNR(銷售文件項目)={}", index);
                    detailTable.put("POSNR", index);

                    //物料
                    logger.debug("MATNR(物料)={}", detail.getProductNumber());
                    detailTable.put("MATNR", detail.getProductNumber());

                    //物料表結構中的上層項目 (訂單品項不需要, 贈品才需要)
                    logger.debug("UEPOS(物料表結構中的上層項目)={}", parentIndex);
                    detailTable.put("UEPOS", parentIndex);

                    //幣別 (不必填, SAP預設)
//                    logger.debug("WAERK(幣別)=TWD");
//                    detailTable.put("WAERK", "TWD");
                    //累計訂購數量 (溢價折讓無數量)
//                    logger.debug("KWMENG(累計訂購數量)={}", "000000000" + String.valueOf(detail.getQuantity()));
//                    detailTable.put("KWMENG", "000000000" + String.valueOf(detail.getQuantity()));
                    //銷售單位 (不必填, SAP預設)
//                    logger.debug("VRKME(銷售單位)={}",);
//                    detailTable.put("VRKME",);
                    //工廠 (不必填, SAP預設 (但測試時要填9620))
//                    //test
//                    logger.debug("WERKS(工廠)=9620");
//                    detailTable.put("WERKS", "9620");
                    //儲存地點 (不必填, SAP預設 (但測試時要填9626))
//                    //test
//                    logger.debug("LGORT(儲存地點)={}", "9626");
//                    detailTable.put("LGORT", "9626");
//                    logger.debug("LGORT(儲存地點)={}",);
//                    detailTable.put("LGORT",);
                    //出貨點/收貨點 (不必填, SAP預設)
//                    logger.debug("VSTEL(出貨點/收貨點)={}",);
//                    detailTable.put("VSTEL",);
                    //條件基值 (不必填, SAP預設)
//                    logger.debug("KAWRT(條件基值)={}", "0000000000000");
//                    detailTable.put("KAWRT", "0000000000000");
                    //費率
                    multiplier = 1;
                    logger.debug("detail.getPrice().scale()={}", detail.getPremiumDiscount().scale());
                    for (int i = 1; i <= detail.getPremiumDiscount().scale(); i++) {
                        multiplier *= 10;
                    }
                    logger.debug("KBETR(費率)={}", "0000000" + String.valueOf(detail.getPremiumDiscount().multiply(BigDecimal.valueOf(multiplier)).longValue()));
                    detailTable.put("KBETR", "0000000" + String.valueOf(detail.getPremiumDiscount().multiply(BigDecimal.valueOf(multiplier)).longValue()));
//                    logger.debug("KBETR(費率)={}",);
//                    detailTable.put("KBETR",);

                    //條件定價單位
                    logger.debug("KPEIN(條件定價單位)={}", "000" + String.valueOf(multiplier));
                    detailTable.put("KPEIN", "000" + String.valueOf(multiplier));

                    //條件單位 (不必填, SAP預設)
//                    logger.debug("KMEIN(條件單位)={}",);
//                    detailTable.put("KMEIN",);                    
                }
                for (SkQuotationGift gift : detail.getGiftList()) {
                    Map<String, Object> giftTable = new HashMap();
                    index = index + 10;
                    //用戶端
                    logger.debug("MANDT(用戶端)={}", mandt);
                    giftTable.put("MANDT", mandt);

                    //銷售文件項目
                    logger.debug("POSNR(銷售文件項目)={}", index);
                    giftTable.put("POSNR", index);

                    //物料
                    logger.debug("MATNR(物料)={}", gift.getProductNumber());
                    giftTable.put("MATNR", gift.getProductNumber());

                    //物料表結構中的上層項目 (訂單品項不需要, 贈品才需要)
                    logger.debug("UEPOS(物料表結構中的上層項目)={}", parentIndex);
                    giftTable.put("UEPOS", parentIndex);

                    //幣別 (不必填, SAP預設)
//                    logger.debug("WAERK(幣別)=TWD");
//                    giftTable.put("WAERK", "TWD");
                    //累計訂購數量
                    logger.debug("KWMENG(累計訂購數量)={}", gift.getQuantity());
                    giftTable.put("KWMENG", gift.getQuantity());

                    //銷售單位 (不必填, SAP預設)
//                    logger.debug("VRKME(銷售單位)={}",);
//                    giftTable.put("VRKME",);
                    //工廠 (不必填, SAP預設 (但測試時要填9626))
//                    //test
//                    logger.debug("WERKS(工廠)=9620");
//                    giftTable.put("WERKS", "9620");
                    //儲存地點 (不必填, SAP預設 (但測試時要填9626))
//                    //test
//                    logger.debug("LGORT(儲存地點)={}", "9626");
//                    giftTable.put("LGORT", "9626");
//                    logger.debug("LGORT(儲存地點)={}",);
//                    giftTable.put("LGORT",);
                    //出貨點/收貨點 (不必填, SAP預設)
//                    logger.debug("VSTEL(出貨點/收貨點)={}",);
//                    giftTable.put("VSTEL",);
                    //條件基值 (不必填, SAP預設)
//                    logger.debug("KAWRT(條件基值)={}",);
//                    giftTable.put("KAWRT",);
                    //費率
                    logger.debug("KBETR(費率)={}", 0);
                    giftTable.put("KBETR", 0);

                    //條件定價單位
                    logger.debug("KPEIN(條件定價單位)={}", 1);
                    giftTable.put("KPEIN", 1);

                    //條件單位 (不必填, SAP預設)
//                    logger.debug("KMEIN(條件單位)={}",);
//                    giftTable.put("KMEIN",);        
                    detailTables.add(giftTable);
                }
                index = index + 10;
                detailTables.add(detailTable);
            }
            logger.debug("detailTable={}", detailTables);
            impTableParams.put("ZTAB_IMP_QT_ITEM", detailTables);
            input.setTables(impTableParams);

            // 準備RFC輸出類型參數
            String expTableName = "ZTAB_EXP_SD_MESSAGE";
            Map<String, RfcOutputTypeEnum> outputType = new HashMap<String, RfcOutputTypeEnum>();
            outputType.put(expTableName, RfcOutputTypeEnum.TABLE);
            input.setOutputTypes(outputType);

            // 呼叫 Proxy 執行 RFC // http://localhost:8080/SAPService/resources
            RfcHttpClient client = new RfcHttpClient(sapServiceUrl);
            client.setSsoProtected(GlobalConstant.SAP_SERVICE_SSO);
            RfcProxyOutput out = client.callRfc(input);
            client.close();
            result = out.getTable("ZTAB_EXP_SD_MESSAGE");
            return handleResult(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_CONNECTION,
                    null,
                    e.getMessage(),
                    null);
        }

    }

    //<editor-fold defaultstate="collapsed" desc="unsupport methods">
    @Override
    public void init(Properties props) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearRepository() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    //</editor-fold>
}
