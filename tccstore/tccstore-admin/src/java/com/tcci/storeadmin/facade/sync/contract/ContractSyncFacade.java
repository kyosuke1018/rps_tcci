/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.contract;

import com.tcci.storeadmin.facade.sync.SyncStatus;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcContractProduct;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.contract.EcContractFacade;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import com.tcci.tccstore.facade.product.EcProductFacade;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
import com.tcci.tccstore.rfc.RFCExec;
import com.tcci.tccstore.util.SapUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.event.AbortProcessingException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class ContractSyncFacade {

    private final static Logger logger = LoggerFactory.getLogger(ContractSyncFacade.class);
    private final static String VBAK_FIELDS = "MANDT,VBELN,ERDAT,ERZET,ERNAM,AUDAT,VBTYP,AUART,AUGRU,NETWR,WAERK,VKORG,VTWEG,SPART,VKGRP,VKBUR,VDATU,KUNNR,AEDAT,VGBEL,OBJNR,XBLNR,ZUONR,VGTYP,ZZADD00";
    private final static String VBAP_FIELDS = "MANDT,VBELN,POSNR,MATNR,MATKL,ARKTX,PSTYV,UEPOS,NETWR,WAERK,ZMENG,KWMENG,VRKME,VGBEL,VGPOS,WERKS,LGORT,VSTEL,ERDAT,ERNAM,ERZET,NETPR,KPEIN,KMEIN,AEDAT,CMPRE,MWSBP";
    private final static String VBKD_FIELDS = "MANDT,VBELN,POSNR,KONDA,KDGRP,BZIRK,INCO1,INCO2,ZTERM,PRSDT,BSTKD,VSART";
    private final static String VBUP_FIELDS = "MANDT,VBELN,POSNR,LFSTA,ABSTA,GBSTA";

    // 需要同步合約的廠 英德|貴港|重慶|安順|港安|靖州|广安|辽宁|华蓥|纳溪|叙永|怀化 (針對新增合約)
    private final static String vkorgMatches = "2000|2500|3300|3400|3100|6300|3200|3600|4100|4200|4300|6100"; 

    //@Resource(mappedName = "jndi/sapclient.config")
    //transient private Properties jndiConfig;
    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;
    @Resource(mappedName = "jndi/global.config")
    transient private Properties globalConfig;

    @EJB
    transient private EcContractFacade ecContractFacade;
    @EJB
    transient private EcCustomerFacade ecCustomerFacade;
    @EJB
    transient private EcProductFacade ecProductFacade;
    @EJB
    transient private EcSalesareaFacade ecSalesareaFacade;
    @EJB
    transient private EcPlantFacade ecPlantFacade;

    // cache
    private Map<String, EcCustomer> mapCustomer = new HashMap<>();
    private Map<String, EcProduct> mapProduct = new HashMap<>();
    private Map<String, EcPlant> mapPlant = new HashMap<>();
    private Map<String, EcSalesarea> mapSalesarea = new HashMap<>();

    public ContractSyncData sync(boolean fromSap) throws Exception {
        String csvFolder = getCsvFolder(); // /opt/FileTransfer/tccstore/sync, D:\FileTransfer\tccstore\sync
        ContractSyncData syncData = new ContractSyncData();
        if (fromSap) {
            sapContract2Csv();
        }
        mapCustomer.values().remove(null);
        loadCsvContract(syncData, csvFolder);
        loadDBContract(syncData);
        compare1(syncData);
        compare2(syncData);
        return syncData;
    }

    public void save(ContractSyncData syncData) {
        boolean success1 = saveResult1(syncData);
        boolean success2 = saveResult2(syncData);
        if (!success1 || !success2) {
            throw new EJBException("save failure!"); // rollback transaction
        }
    }

    // helper
    private String getCsvFolder() {
        // /opt/FileTransfer/tccstore/sync, D:\FileTransfer\tccstore\sync
        String fileTransDir = (String) tccstoreConfig.get("fileTransDir");
        if (null == fileTransDir) {
            throw new EJBException("jndi/tccstore.config fileTransDir not found!");
        }
        StringBuilder sb = new StringBuilder(fileTransDir);
        char lastChar = fileTransDir.charAt(fileTransDir.length() - 1);
        if ('/' != lastChar && '\\' != lastChar) {
            sb.append(File.separator);
        }
        sb.append("tccstore").append(File.separator).append("sync");
        return sb.toString();
    }

    /*
    private void loadSapContract(ContractSyncData syncData, String csvFolder) throws Exception {
        Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
        SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp); //建立連線
        SapProxyResponseDto response = sdProxy.findActiveContract();
        List<VBAK> vbakList = SapUtil.readTableData(response, "ZTAB_EXP_VBAK", VBAK_FIELDS, VBAK.class, csvFolder);
        List<VBAP> vbapList = SapUtil.readTableData(response, "ZTAB_EXP_VBAP", VBAP_FIELDS, VBAP.class, csvFolder);
        List<VBKD> vbkdList = SapUtil.readTableData(response, "ZTAB_EXP_VBKD", VBKD_FIELDS, VBKD.class, csvFolder);
        List<VBUP> vbupList = SapUtil.readTableData(response, "ZTAB_EXP_VBUP", VBUP_FIELDS, VBUP.class, csvFolder);
        for (VBAK vbak : vbakList) {
            syncData.add(vbak);
        }
        for (VBAP vbap : vbapList) {
            syncData.add(vbap);
        }
        for (VBKD vbkd : vbkdList) {
            syncData.add(vbkd);
        }
        for (VBUP vbup : vbupList) {
            syncData.add(vbup);
        }
    }
    */

    private void loadCsvContract(ContractSyncData syncData, String csvFolder) throws Exception {
        List<VBAK> vbakList = SapUtil.readTableData("ZTAB_EXP_VBAK", VBAK_FIELDS, VBAK.class, csvFolder);
        List<VBAP> vbapList = SapUtil.readTableData("ZTAB_EXP_VBAP", VBAP_FIELDS, VBAP.class, csvFolder);
        List<VBKD> vbkdList = SapUtil.readTableData("ZTAB_EXP_VBKD", VBKD_FIELDS, VBKD.class, csvFolder);
        List<VBUP> vbupList = SapUtil.readTableData("ZTAB_EXP_VBUP", VBUP_FIELDS, VBUP.class, csvFolder);
        for (VBAK vbak : vbakList) {
            syncData.add(vbak);
        }
        for (VBAP vbap : vbapList) {
            syncData.add(vbap);
        }
        for (VBKD vbkd : vbkdList) {
            syncData.add(vbkd);
        }
        for (VBUP vbup : vbupList) {
            syncData.add(vbup);
        }
    }
    
    private void loadDBContract(ContractSyncData syncData) {
        List<EcContract> eccoList = ecContractFacade.findAll();
        for (EcContract ecco : eccoList) {
            syncData.add(ecco);
        }
        List<EcContractProduct> eccpList = ecContractFacade.findAllProduct();
        for (EcContractProduct eccp : eccpList) {
            syncData.add(eccp);
        }
    }

    private void compare1(ContractSyncData syncData) {
        for (String pk1 : syncData.getSetPK1()) {
            VBAK vbak = syncData.getVBAK(pk1);
            EcContract ecco = syncData.getECCO(pk1);
            // 測試用的合約不同步
            if (ecco != null && ecco.getCode().matches("20001234|25001234")) {
                continue;
            }
            if (null == vbak) { // delete -> disable: SAP合約已停用 (ecco不為null且有有效的)
                if (ecco.isActive()) {
                    ContractVO vo1 = new ContractVO(pk1, ecco.getName(), ecco.getEcCustomer().getCode(), false);
                    syncData.add(vo1);
                    vo1.setEntity(ecco);
                    vo1.setEcCustomer(ecco.getEcCustomer());
                    vo1.setStatusMessage(SyncStatus.DELETE, "disable");
                }
            } else if (null == ecco) { // insert (只針對上線的廠及有合約名稱)
                if (null == vbak.getZzadd00() || !vbak.getVkorg().matches(vkorgMatches)) {
                    continue;
                }
                ContractVO vo1 = new ContractVO(pk1, vbak.getZzadd00(), vbak.getKunnr(), true);
                vo1.setStatusMessage(SyncStatus.INSERT, "insert");
                syncData.add(vo1);
                EcCustomer ecCustomer = findCustomer(vbak.getKunnr());
                vo1.setEcCustomer(ecCustomer);
                if (null == ecCustomer) {
                    vo1.setValid(false);
                    vo1.setMessage(vbak.getKunnr() + " customer not found");
                }
            } else { // update (只允許名稱,啟用修改)
                boolean nameChanged = !StringUtils.equals(vbak.getZzadd00(), ecco.getName());
                boolean customerChanged = !StringUtils.equals(vbak.getKunnr(), ecco.getEcCustomer().getCode());
                boolean activeChange = !ecco.isActive();
                if (!nameChanged && !customerChanged && !activeChange) {
                    continue;
                }
                // 沒有名稱視為停用
                if (null == vbak.getZzadd00() && !ecco.isActive()) {
                    continue;
                }
                ContractVO vo1 = new ContractVO(pk1, vbak.getZzadd00(), vbak.getKunnr(), true);
                vo1.setEntity(ecco);
                vo1.setStatusMessage(SyncStatus.UPDATE, "udpate");
                syncData.add(vo1);
                if (null == vbak.getZzadd00()) {
                    vo1.setValid(false);
                    vo1.setMessage("contract name should not blank");
                    continue;
                } else if (customerChanged) {
                    vo1.setValid(false);
                    vo1.setMessage("customer should not change");
                    continue;
                }
                vo1.setEcCustomer(ecco.getEcCustomer());
            }
        }
    }

    private void compare2(ContractSyncData syncData) {
        for (String pk2 : syncData.getSetPK2()) {
            String[] ary = pk2.split(":");
            String contract = ary[0];
            int posnr = Integer.parseInt(ary[1]);
            EcContract ecco = syncData.getECCO(contract);
            // 測試用的合約不同步
            if (ecco != null && ecco.getCode().matches("20001234|25001234")) {
                continue;
            }
            VBAP vbap = syncData.getVBAP(pk2);
            VBKD vbkd = syncData.getVBKD(pk2);
            // 如果無vbkd, 用 contract:0 取預設值
            if (null == vbkd) {
                vbkd = syncData.getVBKD(contract + ":" + 0);
            }
            VBUP vbup = syncData.getVBUP(pk2);
            EcContractProduct eccp = syncData.getECCP(pk2);
            ContractProductVO vo2 = new ContractProductVO(contract, posnr);
            vo2.setEntity(eccp);
            // 合約商品是否有效
            // RMB, EXW, VBUD.GBSTA<>'C'
            boolean effective = false;
            if (vbap != null) {
                vo2.setProduct(vbap.getMatnr());
                vo2.setPlant(vbap.getWerks());
                if (vbkd != null) {
                    vo2.setSalesarea(vbkd.getBzirk());
                    vo2.setMethod(vbkd.getInco1());
                }
                effective = "RMB".equals(vbap.getWaerk())
                        && "EXW".equals(vo2.getMethod())
                        && vbup != null && !"C".equals(vbup.getGbsta());
            } else {
                vo2.setProduct(eccp.getEcProduct().getCode());
                vo2.setPlant(eccp.getEcPlant().getCode());
                vo2.setSalesarea(eccp.getEcSalesarea().getCode());
                vo2.setMethod(eccp.getMethod());
            }
            if (vbap != null && eccp != null) { // update
                if (!effective) {
                    vo2.setStatusMessage(SyncStatus.DELETE, "delete");
                    syncData.add(vo2);
                } else if (isContProdChanged(eccp, vo2) && validContProd(vo2)) {
                    vo2.setStatusMessage(SyncStatus.UPDATE, "udpate");
                    syncData.add(vo2);
                }
            } else if (vbap != null) { // insert (只針對上線的廠及有合約名稱)
                VBAK vbak = syncData.getVBAK(contract);
                if (!effective || null == vbak || null == vbak.getZzadd00() || !vbak.getVkorg().matches(vkorgMatches)) {
                    continue;
                }
                if (validContProd(vo2)) {
                    vo2.setStatusMessage(SyncStatus.INSERT, "insert");
                }
                syncData.add(vo2);
            } else if (eccp != null) { // delete
                VBAK vbak = syncData.getVBAK(contract); // 將停用的合約不用刪了
                if (vbak != null) {
                    vo2.setStatusMessage(SyncStatus.DELETE, "delete");
                    syncData.add(vo2);
                }
            }
        }
    }

    private boolean isContProdChanged(EcContractProduct eccp, ContractProductVO vo2) {
        boolean changed = !StringUtils.equals(eccp.getEcProduct().getCode(), vo2.getProduct());
        if (!changed) {
            changed = !StringUtils.equals(eccp.getEcPlant().getCode(), vo2.getPlant());
        }
        if (!changed) {
            changed = !StringUtils.equals(eccp.getEcSalesarea().getCode(), vo2.getSalesarea());
        }
        if (!changed) {
            changed = !StringUtils.equals(eccp.getMethod(), vo2.getMethod());
        }
        return changed;
    }

    private boolean validContProd(ContractProductVO vo2) {
        boolean success = true;
        StringBuilder sb = new StringBuilder();
        EcProduct ecProduct = findProduct(vo2.getProduct());
        vo2.setEcProduct(ecProduct);
        EcPlant ecPlant = findPlant(vo2.getPlant());
        vo2.setEcPlant(ecPlant);
        EcSalesarea ecSalesarea = findSalesarea(vo2.getSalesarea());
        vo2.setEcSalesarea(ecSalesarea);
        if (null == ecProduct) {
            success = false;
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(vo2.getProduct()).append(" product not found");
        }
        if (null == ecPlant) {
            success = false;
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(vo2.getPlant()).append(" product not found");
        }
        if (null == ecSalesarea) {
            success = false;
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(vo2.getSalesarea()).append(" salesarea not found");
        }
        return success;
    }

    private EcCustomer findCustomer(String customer) {
        if (mapCustomer.containsKey(customer)) {
            return mapCustomer.get(customer);
        }
        EcCustomer ecCustomer = ecCustomerFacade.findByCode(customer);
        mapCustomer.put(customer, ecCustomer);
        return ecCustomer;
    }

    private EcProduct findProduct(String product) {
        if (mapProduct.containsKey(product)) {
            return mapProduct.get(product);
        }
        EcProduct ecProduct = ecProductFacade.findByCode(product);
        mapProduct.put(product, ecProduct);
        return ecProduct;
    }

    private EcPlant findPlant(String plant) {
        if (mapPlant.containsKey(plant)) {
            return mapPlant.get(plant);
        }
        EcPlant ecPlant = ecPlantFacade.findByCode(plant);
        mapPlant.put(plant, ecPlant);
        return ecPlant;
    }

    private EcSalesarea findSalesarea(String salesarea) {
        if (mapSalesarea.containsKey(salesarea)) {
            return mapSalesarea.get(salesarea);
        }
        EcSalesarea ecSalesarea = ecSalesareaFacade.findByCode(salesarea);
        mapSalesarea.put(salesarea, ecSalesarea);
        return ecSalesarea;
    }

    private boolean saveResult1(ContractSyncData syncData) {
        boolean success = true;
        for (ContractVO vo1 : syncData.getResult1()) {
            SyncStatus status = vo1.getStatus();
            if (!vo1.isValid() || SyncStatus.NOCHANGE == status) {
                continue;
            }
            try {
                if (SyncStatus.INSERT == status) {
                    EcContract ecco = new EcContract();
                    ecco.setCode(vo1.getCode());
                    ecco.setName(vo1.getName());
                    ecco.setEcCustomer(vo1.getEcCustomer());
                    ecco.setActive(true);
                    ecContractFacade.save(ecco);
                    syncData.add(ecco);
                } else if (SyncStatus.UPDATE == status) { // 僅修改名稱,啟用
                    EcContract ecco = vo1.getEntity();
                    ecco.setName(vo1.getName());
                    ecco.setActive(vo1.isActive());
                    ecContractFacade.edit(ecco);
                } else if (SyncStatus.DELETE == status) { // 停用
                    EcContract ecco = vo1.getEntity();
                    ecco.setActive(false);
                    ecContractFacade.edit(ecco);
                }
                vo1.setMessage(vo1.getMessage() + " success");
                vo1.setStatus(SyncStatus.NOCHANGE);
            } catch (Exception ex) {
                logger.error("saveResult2 exception", ex);
                vo1.setValid(false);
                vo1.setMessage(ex.getMessage());
                success = false;
            }
        }
        return success;
    }

    private boolean saveResult2(ContractSyncData syncData) {
        boolean success = true;
        for (ContractProductVO vo2 : syncData.getResult2()) {
            SyncStatus status = vo2.getStatus();
            if (!vo2.isValid() || SyncStatus.NOCHANGE == status) {
                continue;
            }
            try {
                EcContractProduct eccp = vo2.getEntity();
                if (SyncStatus.INSERT == status) {
                    EcContract ecContract = syncData.getECCO(vo2.getContract());
                    if (null == ecContract) {
                        // 不存，不rollback
                        vo2.setValid(false);
                        vo2.setMessage(vo2.getContract() + " contract not exist");
                        continue;
                    }
                    eccp = new EcContractProduct();
                    eccp.setEcContract(ecContract);
                    eccp.setEcProduct(vo2.getEcProduct());
                    eccp.setEcPlant(vo2.getEcPlant());
                    eccp.setEcSalesarea(vo2.getEcSalesarea());
                    eccp.setMethod(vo2.getMethod());
                    ecContractFacade.save(eccp, vo2.getPosnr());
                } else if (SyncStatus.UPDATE == status) {
                    eccp.setEcProduct(vo2.getEcProduct());
                    eccp.setEcPlant(vo2.getEcPlant());
                    eccp.setEcSalesarea(vo2.getEcSalesarea());
                    eccp.setMethod(vo2.getMethod());
                    ecContractFacade.save(eccp, vo2.getPosnr());
                } else if (SyncStatus.DELETE == status) {
                    ecContractFacade.removeContractProduct(eccp);
                }
                vo2.setMessage(vo2.getMessage() + " success");
                vo2.setStatus(SyncStatus.NOCHANGE);
            } catch (Exception ex) {
                logger.error("saveResult2 exception", ex);
                vo2.setValid(false);
                vo2.setMessage(ex.getMessage());
                success = false;
            }
        }
        return success;
    }

    public void sapContract2Csv() {
        String jcoServiceUrl = globalConfig.getProperty("SAP_REST_ROOT");
        Map<String, List<Map<String, Object>>> result = RFCExec.getActiveContract(jcoServiceUrl);
        List<Map<String, Object>> vbakList = result.get("ZTAB_EXP_VBAK");
        List<Map<String, Object>> vbapList = result.get("ZTAB_EXP_VBAP");
        List<Map<String, Object>> vbkdList = result.get("ZTAB_EXP_VBKD");
        List<Map<String, Object>> vbupList = result.get("ZTAB_EXP_VBUP");
        list2Csv(vbakList, getCsvFolder() + File.separator + "ZTAB_EXP_VBAK.csv", VBAK_FIELDS);
        list2Csv(vbapList, getCsvFolder() + File.separator + "ZTAB_EXP_VBAP.csv", VBAP_FIELDS);
        list2Csv(vbkdList, getCsvFolder() + File.separator + "ZTAB_EXP_VBKD.csv", VBKD_FIELDS);
        list2Csv(vbupList, getCsvFolder() + File.separator + "ZTAB_EXP_VBUP.csv", VBUP_FIELDS);
    }
    
    private void list2Csv(List<Map<String, Object>> list, String pathname, String header) {
        if (null == list || list.isEmpty()) {
            return;
        }
        String[] columns = header.split(",");
        Writer fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.EXCEL;
        File fileDir = new File(pathname);
        try {
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(Arrays.asList(columns));
            SimpleDateFormat dfDATS = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat dfTIMS = new SimpleDateFormat("HH:mm:ss");
            for (Map<String, Object> row : list) {
                List record = new ArrayList();
                for (String column : columns) {
                    Object value = row.get(column);
                    if (null != value) { 
                        if (column.matches("VBELN|KUNNR")) {
                            value = value.toString().replaceFirst("^0+(?!$)", ""); // remove leading zeros
                        } else if (column.matches("ERDAT|AUDAT|AEDAT|PRSDT")) {
                            value = dfDATS.format(new Date(Long.parseLong(value.toString())));
                        } else if (column.matches("ERZET")) {
                            value = dfTIMS.format(new Date(Long.parseLong(value.toString())));
                        }
                    }
                    record.add(value);
                }
                csvFilePrinter.printRecord(record);
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
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
    }

}
