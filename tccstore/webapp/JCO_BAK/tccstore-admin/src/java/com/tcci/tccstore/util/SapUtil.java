/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.util;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.enums.NotifyTypeEnum;
import com.tcci.tccstore.enums.OrderStatusEnum;
import com.tcci.tccstore.facade.notify.EcNotifyFacade;
import com.tcci.tccstore.facade.order.EcOrderFacade;
import com.tcci.tccstore.sapproxy.SdProxy;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.dto.SapTableDto;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class SapUtil {

    private final static Logger logger = LoggerFactory.getLogger(SapUtil.class);

    public static List<String> approveOrder(SdProxy sdProxy, List<EcOrder> ecOrders, TcUser operator, EcOrderFacade ecOrderFacade, EcNotifyFacade notifyFacade) throws Exception {
        return approveOrder(sdProxy, ecOrders, operator, ecOrderFacade, notifyFacade, 0);
    }
    
    public static List<String> approveOrder(SdProxy sdProxy, List<EcOrder> ecOrders, TcUser operator, EcOrderFacade ecOrderFacade, EcNotifyFacade notifyFacade, long maxTime) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.debug("approveOrder startTime:{}", startTime);
        List<String> resultMessages = new ArrayList<>();
        for (EcOrder ecOrder : ecOrders) {
            // 超過時間的就下次再核
            if (maxTime>0 && (System.currentTimeMillis() - startTime)>maxTime) {
                break;
            }
            // 有可能使用者已核過了
            if (!ecOrderFacade.canApprove(ecOrder)) {
                continue;
            }
            ecOrder.setApprover(operator);
            ecOrder.setApprovalTime(new Date());
            logger.debug("order id:{}, approve by {}!", ecOrder.getId(), (operator == null ? "SCHEDULE" : operator.getLoginAccount()));
            SapProxyResponseDto resultDto = createOrder(sdProxy, ecOrder);
            SapTableDto sapTableDto = (SapTableDto) resultDto.getResult();
            if (!resultDto.isSUCCESS()) {
                ecOrder.setMessage("系统错误");
                ecOrder.setStatus(OrderStatusEnum.FAIL);
                ecOrderFacade.edit(ecOrder);
                ecOrderFacade.addLog(ecOrder, "APPROVE_FAIL", operator, resultDto.getDescription());
                String msg = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2}), 系统错误!",
                        new Object[]{ecOrder.getId().toString(),
                            ecOrder.getQuantity(),
                            ecOrder.getVehicle()});
                resultMessages.add(msg);
                notifyFacade.createNotify(NotifyTypeEnum.ORDER_APPROVE_FAIL, msg, ecOrder, ecOrder.getMemberId());
                logger.error(msg);
            } else if (sapTableDto != null && sapTableDto.getDataMapList().size() > 0) {
                List<Map<String, Object>> dataMapList = sapTableDto.getDataMapList();
                Map<String, Object> dataMap = dataMapList.get(0);
                String sapOrdernum = (String) dataMap.get("VBELN");
                String errorMessage = (String) dataMap.get("MESSAGE");
                logger.debug("dataMapList.size()={}", dataMapList.size());
                logger.debug("message={}", errorMessage);
                logger.debug("vbeln={}", sapOrdernum);
                logger.debug("MES_TYPE={}", dataMap.get("MES_TYPE"));
                if (StringUtils.isEmpty(sapOrdernum)) {
                    ecOrder.setMessage(errorMessage);
                    ecOrder.setStatus(OrderStatusEnum.FAIL);
                    ecOrderFacade.edit(ecOrder);
                    ecOrderFacade.addLog(ecOrder, "APPROVE_FAIL", operator, errorMessage);
                    String msg = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2}), SAP订单产生失败(错误讯息:{3})!",
                            new Object[]{ecOrder.getId().toString(),
                                ecOrder.getQuantity(),
                                ecOrder.getVehicle(),
                                errorMessage});
                    resultMessages.add(msg);
                    notifyFacade.createNotify(NotifyTypeEnum.ORDER_APPROVE_FAIL, msg, ecOrder, ecOrder.getMemberId());
                    logger.error(msg);
                } else {
                    // 取得訂單金額及單價
                    List<String> vbelnList = new ArrayList();
                    vbelnList.add(sapOrdernum);
                    SapProxyResponseDto result = sdProxy.findSalesDocument(vbelnList, false);
                    Map<String, SapTableDto> sapTableDtoMap = (Map<String, SapTableDto>) result.getResult();
                    if (sapTableDtoMap.size() > 0) {
                        logger.debug("sapTableDtoMap.size()={}", sapTableDtoMap.size());
                        SapTableDto vbak = sapTableDtoMap.get("ZTAB_EXP_VBAK");
                        logger.debug("vbak.getDataMapList().size()={}", vbak.getDataMapList().size());
                        if (vbak.getDataMapList().size() > 0) {
                            List<Map<String, Object>> dataMapList2 = vbak.getDataMapList();
                            logger.debug("dataMapList2.size()={}", dataMapList2.size());
                            if (dataMapList.size() > 0) {
                                Map<String, Object> map = dataMapList2.get(0);
                                BigDecimal amount = (BigDecimal) map.get("NETWR");
                                ecOrder.setAmount(amount);
                            }
                        }
                        SapTableDto vbap = sapTableDtoMap.get("ZTAB_EXP_VBAP");
                        logger.debug("vbak.getDataMapList().size()={}", vbap.getDataMapList().size());
                        if (vbap.getDataMapList().size() > 0) {
                            List<Map<String, Object>> dataMapList2 = vbap.getDataMapList();
                            logger.debug("dataMapList2.size()={}", dataMapList2.size());
                            if (dataMapList.size() > 0) {
                                Map<String, Object> map = dataMapList2.get(0);
                                BigDecimal unitPrice = (BigDecimal) map.get("NETPR");
                                ecOrder.setUnitPrice(unitPrice);
                            }
                        }
                    }
                    ecOrder.setSapOrdernum(sapOrdernum);
                    ecOrder.setStatus(OrderStatusEnum.APPROVE);
                    ecOrder.setMessage(null);
                    ecOrderFacade.edit(ecOrder);
                    ecOrderFacade.addLog(ecOrder, OrderStatusEnum.APPROVE.name(), operator, "SAP單號:" + sapOrdernum);
                    String msg = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2})已成立, 正式订单号码为{3}!",
                            new Object[]{ecOrder.getId().toString(),
                                ecOrder.getQuantity(),
                                ecOrder.getVehicle(),
                                ecOrder.getSapOrdernum()});
                    notifyFacade.createNotify(NotifyTypeEnum.ORDER_CREATE, msg, ecOrder, ecOrder.getMemberId());
                }
            } else {
                ecOrder.setMessage("SAP未回覆开单结果");
                ecOrder.setStatus(OrderStatusEnum.FAIL);
                ecOrderFacade.edit(ecOrder);
                ecOrderFacade.addLog(ecOrder, "APPROVE_NO_RESULT", operator, null);
                String msg = MessageFormat.format("订单(序号:{0}, 数量:{1}吨, 车号:{2}), SAP未回覆开单结果!",
                        new Object[]{ecOrder.getId().toString(),
                            ecOrder.getQuantity(),
                            ecOrder.getVehicle()});
                resultMessages.add(msg);
                notifyFacade.createNotify(NotifyTypeEnum.ORDER_APPROVE_FAIL, msg, ecOrder, ecOrder.getMemberId());
                logger.error(msg);
            }
        }
        long endTime = System.currentTimeMillis();
        logger.debug("approveOrder endTime:{}, duration:{} ms", endTime, endTime-startTime);
        return resultMessages;
    }

//    public static List<String> cancelOrder(SdProxy sdProxy, List<EcOrder> ecOrders, TcUser operator, EcOrderFacade ecOrderFacade, EcNotifyFacade notifyFacade) throws Exception {
//    }
    
    public static SapProxyResponseDto cancelOrder(SdProxy sdProxy, EcOrder ecOrder) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("ZRECORD_COUNT", ecOrder.getId());
        params.put("VBELV", ecOrder.getSapOrdernum());
        return sdProxy.cancelOrder(params);
    }

    public static <T> List<T> readTableData(SapProxyResponseDto response, String tableName, String header, Class<T> clazz, String csvFolder) throws Exception {
        List<T> result = new ArrayList<>();
        Writer fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.EXCEL;
        String[] headers = header.split(",");
        try {
            String pathname = csvFolder + File.separator + tableName + ".csv";
            backupFileWeekly(pathname);
            File fileDir = new File(pathname);
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(Arrays.asList(headers));
            List<Map<String, Object>> tableData = getTableData(response, tableName);
            if (tableData != null && !tableData.isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                for (Map<String, Object> dataMap : tableData) {
                    List record = new ArrayList();
                    T vo = clazz.newInstance();
                    for (String field : headers) {
                        Object value = dataMap.get(field);
                        Object realValue = value;
                        if (value == null) {
                        } else if (field.matches("VBELN|KUNNR")) { //合約號,客戶代碼移除leading zero
                            realValue = value.toString().replaceAll("^0+", "");
                        } else if (field.matches("ERDAT|AUDAT|AEDAT|PRSDT")) { //日期->yyyy/MM/dd
                            realValue = df.format((Date) value);
                        } else if (field.matches("ERZET")) { // 時間 -> HH:mm:ss
                            realValue = tf.format((Date) value);
                        } else {
                            realValue = StringUtils.trimToNull(value.toString());
                        }
                        // Object realValue = null==value ? null : StringUtils.trimToNull(value.toString());
                        record.add(realValue);
                        if (realValue != null) {
                            try {
                                BeanUtils.setProperty(vo, field.toLowerCase(), realValue);
                            } catch (Exception ex) {
                            }
                        }
                    }
                    csvFilePrinter.printRecord(record);
                    result.add(vo);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
                if (csvFilePrinter != null) {
                    csvFilePrinter.close();
                }
            } catch (IOException e) {
                logger.error("IOException", e);
            }
        }
        return result;
    }
    
    public static <T> List<T> readTableData(String tableName, String header, Class<T> clazz, String csvFolder) throws Exception {
        List<T> result = new ArrayList<>();
        String fileName = csvFolder + File.separator + tableName + ".csv";
        InputStreamReader in = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());
        String[] headers = header.split(",");
        try {
            int count = 0;
            for (CSVRecord record : parser) {
                count++;
                T vo = clazz.newInstance();
                for (String field : headers) {
                    String value = record.get(field);
                    Object realValue = null==value ? null : StringUtils.trimToNull(value.toString());
                    if (realValue != null) {
                        try {
                            BeanUtils.setProperty(vo, field.toLowerCase(), realValue);
                        } catch (Exception ex) {
                        }
                    }
                }
                result.add(vo);
            }
            logger.debug("{} rows = {}", tableName, count);
        } finally {
            parser.close();
            in.close();
        }
        return result;
    }
    
    // helper
    private static SapProxyResponseDto createOrder(SdProxy sdProxy, EcOrder ecOrder) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map<String, Object> params = new HashMap<>();
        params.put("ZRECORD_COUNT", ecOrder.getId());
        params.put("EBELN_OLD", ecOrder.getVehicle()); // 車牌
        params.put("EBELN", ecOrder.getContractCode()); // 合約號碼
        params.put("EBELP", ecOrder.getPosnr()); // 合約項次
        params.put("SITE_LOC", ecOrder.getSiteLoc()); // 袋裝噴碼
        params.put("WERKS", ecOrder.getPlantCode()); // 工廠代碼
        params.put("AUDAT", sdf.format(ecOrder.getCreatetime())); // 開單日(yyyymmdd)
        params.put("INCO1", ecOrder.getMethod()); // 国际贸易条件(部分1)
        params.put("PERNR", ecOrder.getSalesCode()); // 業務員工號
        params.put("KMENGE", ecOrder.getQuantity().toString()); // 訂單數量
        params.put("KUNAG", ecOrder.getCustomerId().getCode()); // 買方(客戶代碼)
        params.put("KUNNR_NAME1", ecOrder.getDeliveryCode()); // 送達地點代碼
        params.put("LZONE", ecOrder.getSalesareaCode()); // 銷售地區代碼
        params.put("MATNR", ecOrder.getProductCode()); // 商品代碼
        params.put("SHIP_END", ecOrder.getDeliveryDate()); // 出貨日期
        return sdProxy.createOrder(params);
    }

    private static List<Map<String, Object>> getTableData(SapProxyResponseDto response, String tableName) {
        Map<String, SapTableDto> result = response == null ? null : (Map<String, SapTableDto>) response.getResult();
        SapTableDto sapTable = result == null ? null : result.get(tableName);
        List<Map<String, Object>> tableData = sapTable == null ? null : sapTable.getDataMapList();
        return tableData;
    }
    
    private static void backupFileWeekly(String pathname) {
        Path source = Paths.get(pathname);
        if (Files.exists(source, LinkOption.NOFOLLOW_LINKS)) {
            try {
                FileTime ftime = Files.getLastModifiedTime(source, LinkOption.NOFOLLOW_LINKS);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(ftime.toMillis());
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                Path target = Paths.get(pathname + "." + dayOfWeek);
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                logger.error("backupFile exception!", ex);
            }
        }
    }

}
