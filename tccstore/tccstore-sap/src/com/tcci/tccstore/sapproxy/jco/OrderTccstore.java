/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.sapproxy.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.dto.SapTableDto;
import com.tcci.tccstore.sapproxy.dto.ZfldImpXxxxDto;
import com.tcci.tccstore.sapproxy.enums.SapProxyResponseEnum;
import com.tcci.tccstore.sapproxy.enums.SapSystemEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class OrderTccstore {

    protected static final Logger logger = LoggerFactory.getLogger(OrderTccstore.class);

    /**
     * RFC 功能名稱: ZSAP_JAVA_EXP_SD_GET (帶出銷售文件內容)
     */
    final static String RFC_ZSAP_JAVA_EXP_SD_GET = "ZSAP_JAVA_EXP_SD_GET";

    /**
     * RFC 功能名稱: ZSAP_JAVA_EXP_SO_AMOUNT_GET (讀取預開訂單之訂單總額)
     */
    final static String RFC_ZSAP_JAVA_EXP_SO_AMOUNT_GET = "ZSAP_JAVA_EXP_SO_AMOUNT_GET";

    /**
     * RFC 功能名稱: Z_SD_CREATE_SO_BATCH2 (批次處理電子商務(手機/CRM) /出貨系統轉入SAP訂單作業)
     */
    final static String RFC_Z_SD_CREATE_SO_BATCH2 = "Z_SD_CREATE_SO_BATCH2";
    
    // SAP錯誤訊息轉換表
    private final static Map<String, String> sapErrorMapping;
    static {
        sapErrorMapping = new HashMap<>();
        sapErrorMapping.put("MV1423", "账号产生问题，请联络业务");
        sapErrorMapping.put("MV1391", "此产品不能在你选择的出货工厂出货，请联络业务");
        sapErrorMapping.put("MV1042", "订单正在变动中，请稍后查看");
        sapErrorMapping.put("MV1150", "帐上余额不足，请尽快打款");
    }

    public static SapProxyResponseDto findShippedSalesDocument(JCoDestination destination, List<String> vbelnList) throws Exception {
        return findSalesDocument(destination, vbelnList, true);
    }

    public static SapProxyResponseDto findSalesDocument(JCoDestination destination, List<String> vbelnList, Boolean shipped) throws Exception {
        Map<String, SapTableDto> result = new HashMap();
        try {

            JCoFunction function = destination.getRepository().getFunction(RFC_ZSAP_JAVA_EXP_SD_GET);
            if (function == null) {
                throw new RuntimeException(RFC_ZSAP_JAVA_EXP_SD_GET + " not found in SAP.");
            }
            List<ZfldImpXxxxDto> inputTables = new ArrayList();
            logger.debug("vbelnList.size()={}", vbelnList.size());
            if (vbelnList.size() > 0) {
                for (String vbeln : vbelnList) {
                    logger.debug("vbeln={}", vbeln);
                    ZfldImpXxxxDto inputTable = new ZfldImpXxxxDto("ZFLD_IMP_VBELN", "I", "EQ", vbeln, null);
                    inputTables.add(inputTable);
                }
                if (shipped != null) {
                    if (shipped) {
                        ZfldImpXxxxDto inputTable = new ZfldImpXxxxDto("ZFLD_IMP_GBSTK", "I", "EQ", "C", null);
                        inputTables.add(inputTable);
                    }
                }
                JCoParameterList tables = function.getTableParameterList(); //獲得Tables變數列表
                for (ZfldImpXxxxDto inputParam : inputTables) {
                    JCoTable tValues = tables.getTable(inputParam.getTableName()); //Input Table
                    tValues.appendRow();
                    tValues.setValue("SCOPE", inputParam.getScope());
                    tValues.setValue("OPERA", inputParam.getOpera());
                    if (null != inputParam.getLow()) {
                        tValues.setValue("LOW", inputParam.getLow());
                    }
                    if (null != inputParam.getHigh()) {
                        tValues.setValue("HIGH", inputParam.getHigh());
                    }
                }

                //Execute function
                function.execute(destination);
                JCoTable outputTable = function.getTableParameterList().getTable("ZTAB_EXP_VBAK");
                int numRows = outputTable.getNumRows();
                logger.debug("ZTAB_EXP_VBAK output rows = " + numRows);
                outputTable.setRow(0);

                SapTableDto dto = new SapTableDto();
                dto.setDataMapList(JcoUtils.getJCoTableData(outputTable, null));
                result.put("ZTAB_EXP_VBAK", dto);

                JCoTable outputTable2 = function.getTableParameterList().getTable("ZTAB_EXP_VBAP");
                int numRows2 = outputTable2.getNumRows();
                logger.debug("ZTAB_EXP_VBAP output rows = " + numRows2);
                outputTable2.setRow(0);

                SapTableDto dto2 = new SapTableDto();
                dto2.setDataMapList(JcoUtils.getJCoTableData(outputTable2, null));
                result.put("ZTAB_EXP_VBAP", dto2);

                JCoTable outputTable3 = function.getTableParameterList().getTable("ZTAB_EXP_VBKD");
                int numRows3 = outputTable3.getNumRows();
                logger.debug("ZTAB_EXP_VBKD output rows = " + numRows3);
                outputTable3.setRow(0);

                SapTableDto dto3 = new SapTableDto();
                dto3.setDataMapList(JcoUtils.getJCoTableData(outputTable3, null));
                result.put("ZTAB_EXP_VBKD", dto3);

                JCoTable outputTable4 = function.getTableParameterList().getTable("ZTAB_EXP_VBPA");
                int numRows4 = outputTable4.getNumRows();
                logger.debug("ZTAB_EXP_VBPA output rows = " + numRows4);
                outputTable4.setRow(0);

                SapTableDto dto4 = new SapTableDto();
                dto4.setDataMapList(JcoUtils.getJCoTableData(outputTable4, null));
                result.put("ZTAB_EXP_VBPA", dto4);

                JCoTable outputTable5 = function.getTableParameterList().getTable("ZTAB_EXP_VBUK");
                int numRows5 = outputTable5.getNumRows();
                logger.debug("ZTAB_EXP_VBUK output rows = " + numRows5);
                outputTable5.setRow(0);

                SapTableDto dto5 = new SapTableDto();
                dto5.setDataMapList(JcoUtils.getJCoTableData(outputTable5, null));
                result.put("ZTAB_EXP_VBUK", dto5);

                JCoTable outputTable6 = function.getTableParameterList().getTable("ZTAB_EXP_VBUP");
                int numRows6 = outputTable6.getNumRows();
                logger.debug("ZTAB_EXP_VBUP output rows = " + numRows6);
                outputTable6.setRow(0);

                SapTableDto dto6 = new SapTableDto();
                dto6.setDataMapList(JcoUtils.getJCoTableData(outputTable6, null));
                result.put("ZTAB_EXP_VBUP", dto6);
            }
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);

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

    /*
    public static SapProxyResponseDto findOrderPrice(JCoDestination destination, Order order) throws Exception {
        SapTableDto result = null;
        try {
            JCoFunction function = destination.getRepository().getFunction(RFC_ZSAP_JAVA_EXP_SO_AMOUNT_GET);
            if (function == null) {
                throw new RuntimeException(RFC_ZSAP_JAVA_EXP_SO_AMOUNT_GET + " not found in SAP.");
            }
            JCoParameterList tables = function.getImportParameterList(); //獲得Tables變數列表
            logger.debug("tables.getListMetaData().getName()={}", tables.getListMetaData().getName());
            JCoStructure zstImpKomk = tables.getStructure("ZST_IMP_KOMK");

            //銷售組織
            String vkorg = order.getPlant().getCode().substring(0, 2) + "00";
            logger.debug("kvorg={}", vkorg);
            zstImpKomk.setValue("VKORG", vkorg);

//            //产品组 (不用傳, sap直接抓主檔)
//            String spart = "10";
//            logger.debug("spart");
//            zstImpKomk.setValue("SPART", spart);
            //销售地区 TODO: 前面一定要補0嗎?
            String bzirk = "0" + order.getSalesarea().getCode();
            logger.debug("bzirk={}", bzirk);
            zstImpKomk.setValue("BZIRK", bzirk);

            //价格组(客户) TODO: KONDA (价格组(客户)) 不知道哪裡抓
            String konda = "02";
            logger.debug("konda={}", konda);
            zstImpKomk.setValue("KONDA", konda);

            //售达方
            String kunnr = order.getCustomer().getCode().replace("00000", "");
            logger.debug("kunnr={}", kunnr);
            zstImpKomk.setValue("KUNNR", kunnr);

            //定价日期和汇率
            //String prsdt = new SimpleDateFormat("yyyy/MM/dd").format(order.getCreatetime());
            //TODO for test
            String prsdt = "2015/03/19";
            logger.debug("prsdt={}", prsdt);
            zstImpKomk.setValue("PRSDT", new SimpleDateFormat("yyyy/MM/dd").parse(prsdt));

            //国际贸易条件(部分1)
            String inco1 = order.getDelivery().getCode();
            logger.debug("inco1={}", inco1);
            zstImpKomk.setValue("INCO1", inco1);

            //工厂
            String werks = order.getPlant().getCode();
            logger.debug("werks={}", werks);
            zstImpKomk.setValue("WERKS", werks);

            //装运步骤的装运类型 (01:車運, 04:船運)
            String vsart = "01";
            logger.debug("vsart={}", vsart);
            zstImpKomk.setValue("VSART", vsart);

//            //特殊处理标记 (不用傳, sap直接抓主檔)
//            String sdabw = "Z10";
//            logger.debug("sdabw={}", sdabw);
//            zstImpKomk.setValue("SDABW", sdabw);
//            //内部表索引编号 (與Wenny確認可不傳)
//            String ixKomk = "0";
//            logger.debug("ixKomk={}", ixKomk);
//            zstImpKomk.setValue("IX_KOMK", ixKomk);
            JCoStructure zstImpKomp = tables.getStructure("ZST_IMP_KOMP");

//            //条件项目号 (與Wenny確認可不傳)
//            String kposn = "000000";
//            logger.debug("kposn={}", kposn);
//            zstImpKomp.setValue("KPOSN", kposn);
            //物料号
            String matnr = order.getProduct().getCode();
            logger.debug("matnr={}", matnr);
            zstImpKomp.setValue("MATNR", matnr);

            //工厂
            zstImpKomp.setValue("WERKS", werks);

            //数量
            BigDecimal mglme = order.getQuantity();
            logger.debug("mglme={}", mglme);
            zstImpKomp.setValue("MGLME", mglme);

//            //数量 (與Wenny確認可不傳)
//            BigDecimal mgame = BigDecimal.ZERO;
//            logger.debug("mgame={}", mgame);
//            zstImpKomp.setValue("MGAME", 0);
//
//            //销售数量转换成SKU的分子(因子) (與Wenny確認可不傳)
//            BigDecimal umvkz = BigDecimal.ZERO;
//            logger.debug("umvkz={}", umvkz);
//            zstImpKomp.setValue("UMVKZ", umvkz);
//
//            //条件定价单位 (與Wenny確認可不傳)
//            BigDecimal kpein = BigDecimal.ZERO;
//            logger.debug("kpein={}", kpein);
//            zstImpKomp.setValue("KPEIN", kpein);
//
//            //天数 (與Wenny確認可不傳)
//            BigDecimal anzTage = BigDecimal.ZERO;
//            zstImpKomp.setValue("ANZ_TAGE", anzTage);
            //Execute function
            function.execute(destination);
            JCoTable outputTable = function.getTableParameterList().getTable("ZTAB_EXP_AMOUNT");

            SapTableDto dto = new SapTableDto();
            dto.setDataMapList(JcoUtils.getJCoTableData(outputTable, null));
            result = dto;

            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);
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
    */

    public static SapProxyResponseDto createOrder(JCoDestination destination, Map<String, Object> order) throws Exception {
        SapTableDto result = null;
        try {
            result = createSoBatch(destination, order, OrderActionEnum.CREATE);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);
        } catch (Exception e) {
            logger.error("createOrder exception", e);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                    null,
                    e.getMessage(),
                    null);
        }
    }

    public static SapProxyResponseDto cancelOrder(JCoDestination destination, Map<String, Object> order) throws Exception {
        SapTableDto result = null;
        try {

            result = createSoBatch(destination, order, OrderActionEnum.CANCEL);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);
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

    private static SapTableDto createSoBatch(JCoDestination destination, Map<String, Object> params, OrderActionEnum action) throws Exception {
        JCoFunction function = destination.getRepository().getFunction(RFC_Z_SD_CREATE_SO_BATCH2);
        if (function == null) {
            throw new RuntimeException(RFC_Z_SD_CREATE_SO_BATCH2 + " not found in SAP.");
        }
        JCoParameterList tables = function.getTableParameterList(); //獲得Tables變數列表
        logger.debug("tables.getListMetaData().getName()={}", tables.getListMetaData().getName());
        JCoTable piZtsdSoinput = tables.getTable("PI_ZTSD_SOINPUT");
        piZtsdSoinput.appendRow();

        Long orderId = (Long) params.get("ZRECORD_COUNT"); // 電商訂單id
        String sapOrderNumber = (String) params.get("VBELV"); // SAP訂單號碼
        
        //纪录码 (ID)
        logger.debug("ZRECORD_COUNT={}", orderId);
        piZtsdSoinput.setValue("ZRECORD_COUNT", orderId);
        
        // SAP訂單號碼
        logger.debug("VBELV={}", sapOrderNumber);
        piZtsdSoinput.setValue("VBELV", sapOrderNumber);

        //订单动作码2
        String orderAction = "";
        if (OrderActionEnum.CREATE.equals(action)) {
            orderAction = "1";
        } else if (OrderActionEnum.CANCEL.equals(action)) {
            orderAction = "2";
        }
        logger.debug("ORDER_FLAG2={}", orderAction);
        piZtsdSoinput.setValue("ORDER_FLAG_2", orderAction);
        
        if (OrderActionEnum.CREATE.equals(action)) {
            // 建立訂單參數
            String vehicle = (String) params.get("EBELN_OLD"); // 車牌
            String contractCode = (String) params.get("EBELN"); // 合約號碼
            Integer posnr = (Integer) params.get("EBELP"); // 合約項次
            String siteLoc = (String) params.get("SITE_LOC"); // 袋裝噴碼
            String plantCode = (String) params.get("WERKS"); // 工廠代碼
            String audat = (String) params.get("AUDAT"); // 開單日(yyyymmdd)
            String inco1 = (String) params.get("INCO1"); // 国际贸易条件(部分1)
            String salesCode = (String) params.get("PERNR"); // 業務員工號
            String quantity = (String) params.get("KMENGE"); // 訂單數量
            String customerCode = (String) params.get("KUNAG"); // 買方(客戶代碼)
            String deliveryCode = (String) params.get("KUNNR_NAME1"); // 送達地點代碼
            String saleareaCode = (String) params.get("LZONE"); // 銷售地區代碼
            String productCode = (String) params.get("MATNR"); // 商品代碼
            String deliveryDate = (String) params.get("SHIP_END"); // 出貨日期

            //訂單附號 (B: 出貨系統補單,M: 手機下單,W: 網路下單,Y(手動補單))
            String ipcItemno = "M";
            logger.debug("IPC_ITEMNO={}", ipcItemno);
            piZtsdSoinput.setValue("IPC_ITEMNO", ipcItemno);

            //車號
            logger.debug("EBELN_OLD={}", vehicle);
            piZtsdSoinput.setValue("EBELN_OLD", vehicle);

            if (contractCode != null) {
                //合約號碼 
                logger.debug("EBELN={}", contractCode);
                piZtsdSoinput.setValue("EBELN", contractCode);

                //合約項次
                logger.debug("EBELP={}", posnr);
                piZtsdSoinput.setValue("EBELP", posnr);
            }
            
            //袋裝噴碼
            if (siteLoc != null) {
                logger.debug("SITE_LOC={}", siteLoc);
                piZtsdSoinput.setValue("SITE_LOC", siteLoc);
            }
            
            //工廠代碼
            logger.debug("WERKS={}", plantCode);
            piZtsdSoinput.setValue("WERKS", plantCode);

            //開單日期
            logger.debug("AUDAT={}", audat);
            piZtsdSoinput.setValue("AUDAT", audat);

            //国际贸易条件(部分1)
            logger.debug("INCO1={}", inco1);
            piZtsdSoinput.setValue("INCO1", inco1);

            //国际贸易术语解释通则(部分2) hard code by method.
            String inco2 = "EXW".equals(inco1) ? "廠交自提" :
                           "FCA".equals(inco1) ? "工地交自提" : "";
            logger.debug("INCO2={}", inco2);
            piZtsdSoinput.setValue("INCO2", inco2);

            //業務員代號
            logger.debug("PERNR={}", salesCode);
            piZtsdSoinput.setValue("PERNR", salesCode);

            //訂單數量
            logger.debug("KMENGE={}", quantity);
            piZtsdSoinput.setValue("KMENGE", quantity);
            
            //買方
            logger.debug("KUNAG={}", customerCode);
            piZtsdSoinput.setValue("KUNAG", customerCode);

            //客戶編號 1(一次性客戶) ?
            logger.debug("KUNNR={}", customerCode);
            piZtsdSoinput.setValue("KUNNR", customerCode);

            //1~10碼:送達地點; 11~20碼:承運商 (EXW:廠交自提及FCA:工地交自提不需承運商)
            if (deliveryCode != null) {
                logger.debug("KUNNR_NAME1={}", deliveryCode);
                piZtsdSoinput.setValue("KUNNR_NAME1", deliveryCode);
            }

            //銷售地區
            logger.debug("LZONE={}", saleareaCode);
            piZtsdSoinput.setValue("LZONE", saleareaCode);

            //物料號碼
            logger.debug("MATNR={}", productCode);
            piZtsdSoinput.setValue("MATNR", productCode);

            //出貨類型
            String vsart = "01";
            logger.debug("VSART={}", vsart);
            piZtsdSoinput.setValue("VSART", vsart);

            //出貨日期 
            logger.debug("SHIP_END={}", deliveryDate);
            piZtsdSoinput.setValue("SHIP_END", deliveryDate);
        }
        
        //Execute function
        function.execute(destination);
        JCoTable outputTable = function.getTableParameterList().getTable("PO_ZTSD_CREATELOG");

        SapTableDto dto = new SapTableDto();
        List<Map<String, Object>> dataMapList = JcoUtils.getJCoTableData(outputTable, null);
        // 訊息轉換
        if (dataMapList.size() > 0) {
            Map<String, Object> dataMap = dataMapList.get(0);
            String errorMessage = (String) dataMap.get("MESSAGE");
            if (errorMessage != null && errorMessage.length() > 5) {
                String storeMessage = sapErrorMapping.get(errorMessage.substring(0, 6));
                if (storeMessage != null) {
                    dataMap.put("MESSAGE", storeMessage);
                }
            }
        }
        dto.setDataMapList(dataMapList);
        return dto;
    }

    public enum OrderActionEnum {
        CREATE,
        CANCEL;
    }

}
