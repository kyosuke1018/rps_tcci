/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.delivery;

import com.tcci.storeadmin.facade.sync.SyncStatus;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryPlaceFacade;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryVkorgFacade;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
import com.tcci.tccstore.sapproxy.SdProxy;
import com.tcci.tccstore.sapproxy.SdProxyFactory;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.jco.JcoUtils;
import com.tcci.tccstore.util.SapUtil;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class DeliverySyncFacade {

    private final static Logger logger = LoggerFactory.getLogger(DeliverySyncFacade.class);
    //private final static String KNA1_FIELDS = "MANDT,KUNNR,NAME1,STCEG,STRAS,TELF1,TELFX";
    private final static String KNA1_FIELDS = "MANDT,KUNNR,NAME1,STCEG,REGIO,BEZEI,ORT01,ORT02,STRAS,TELF1,TELFX";
    private final static String KNVV_FIELDS = "MANDT,KUNNR,VKORG,VTWEG,SPART,BZIRK,KDGRP,ZTERM";

    @Resource(mappedName = "jndi/sapclient.config")
    transient private Properties jndiConfig;
    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;

    @EJB
    private EcDeliveryPlaceFacade ecDeliveryPlacdeFacade;
    @EJB
    private EcDeliveryVkorgFacade ecDeliveryVkorgFacade;
    @EJB
    private EcSalesareaFacade ecSalesareaFacade;
    
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    // cache
    private Map<String, EcSalesarea> mapSalesarea = new HashMap<>();

    public DeliverySyncData sync(boolean fromSap) throws Exception {
        String csvFolder = getCsvFolder(); // /opt/FileTransfer/tccstore/sync, D:\FileTransfer\tccstore\sync
        DeliverySyncData syncData = new DeliverySyncData();
        if (fromSap) {
            loadSapDelivery(syncData, csvFolder);
        } else {
            loadCsvDelivery(syncData, csvFolder);
        }
        loadDBDelivery(syncData);
        compare1(syncData);
        compare2(syncData);
        return syncData;
    }
    
    public void save(DeliverySyncData syncData) {
        boolean success1 = saveResult1(syncData.getResult1());
        boolean success2 = saveResult2(syncData.getResult2());
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

    private void loadSapDelivery(DeliverySyncData syncData, String csvFolder) throws Exception {
        Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
        SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp); //建立連線
        SapProxyResponseDto response = sdProxy.findCustomerList("S00000", "SZZZZZ"); // 送達地點: S開頭,6碼
        List<KNA1> kna1List = SapUtil.readTableData(response, "ZTAB_EXP_KNA1", KNA1_FIELDS, KNA1.class, csvFolder);
        List<KNVV> knvvList = SapUtil.readTableData(response, "ZTAB_EXP_KNVV", KNVV_FIELDS, KNVV.class, csvFolder);
        for (KNA1 kna1 : kna1List) {
            syncData.add(kna1);
        }
        for (KNVV knvv : knvvList) {
            syncData.add(knvv);
        }
    }

    private void loadCsvDelivery(DeliverySyncData syncData, String csvFolder) throws Exception {
        List<KNA1> kna1List = SapUtil.readTableData("ZTAB_EXP_KNA1", KNA1_FIELDS, KNA1.class, csvFolder);
        List<KNVV> knvvList = SapUtil.readTableData("ZTAB_EXP_KNVV", KNVV_FIELDS, KNVV.class, csvFolder);
        for (KNA1 kna1 : kna1List) {
            syncData.add(kna1);
        }
        for (KNVV knvv : knvvList) {
            syncData.add(knvv);
        }
    }

    private void loadDBDelivery(DeliverySyncData syncData) {
        List<EcDeliveryPlace> ecdps = ecDeliveryPlacdeFacade.findAll();
        for (EcDeliveryPlace ecdp : ecdps) {
            syncData.add(ecdp);
        }
        List<EcDeliveryVkorg> ecdvs = ecDeliveryVkorgFacade.findAll();
        for (EcDeliveryVkorg ecdv : ecdvs) {
            syncData.add(ecdv);
        }
    }
    
    private boolean compare1(DeliverySyncData syncData) {
        boolean success = true;
        for (String pk1 : syncData.getSetPK1()) {
            KNA1 kna1 = syncData.getKNA1(pk1);
            EcDeliveryPlace ecdp = syncData.getECDP(pk1);
            // 新台泥沒有在SAP維護, 部份資料不需同步到電商來, 所以只做update
            if (null == kna1 || null == ecdp) {
                continue;
            }
            DeliveryVO vo = new DeliveryVO(pk1, kna1.getName1());
            vo.setProvince(kna1.getBezei());
            vo.setCity(kna1.getOrt01());
            vo.setDistrict(kna1.getOrt02());
            vo.setTown(kna1.getStras());
            vo.setActive(true);
            vo.setEcdp(ecdp);
            if (validDeliveryVO(vo)) {
                String[] fields = {"name", "province", "city", "district", "town", "active"};
                boolean changed = isFieldChanged(vo, ecdp, fields);
                if (changed) {
                    vo.setStatusMessage(SyncStatus.UPDATE, "update");
                    syncData.add(vo);
                }
            } else {
                syncData.add(vo);
            }
        }
        return success;
    }
    
    private boolean validDeliveryVO(DeliveryVO vo) {
        Set<ConstraintViolation<DeliveryVO>> constraintViolations = validator.validate(vo);
        if (!constraintViolations.isEmpty()) {
            vo.setValid(false);
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<DeliveryVO> constraintViolation : constraintViolations) {
                sb.append(constraintViolation.getMessage()).append('\n');
            }
            vo.setMessage(sb.toString());
            return false;
        } else {
            return true;
        }
    }
    
    private boolean isFieldChanged(Object src, Object dst, String[] fields) {
        try {
            for (String field : fields) {
                Object f1 = PropertyUtils.getSimpleProperty(src, field);
                Object f2 = PropertyUtils.getSimpleProperty(dst, field);
                if (f1 != null && f2 != null) {
                    if (!f1.equals(f2)) {
                        return true;
                    }
                } else if (f1 != null || f2 != null) {
                    return true;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }
    
    private boolean compare2(DeliverySyncData syncData) {
        boolean success = true;
        for (String pk2 : syncData.getSetPK2()) {
            String[] codeVkorg = pk2.split(":");
            String delivery = codeVkorg[0];
            String vkorg = codeVkorg[1];
            KNA1 kna1 = syncData.getKNA1(delivery);
            EcDeliveryPlace ecdp = syncData.getECDP(delivery);
            // 新台泥沒有在SAP維護, 部份資料不需同步到電商來, 所以只做update
            if (null == kna1 || null == ecdp) {
                continue;
            }
            KNVV knvv = syncData.getKNVV(pk2);
            EcDeliveryVkorg ecdv = syncData.getECDV(pk2);
            if (knvv != null && ecdv != null) { // update
                String salesarea = knvv.getBzirk();
                if (!StringUtils.equals(salesarea, ecdv.getEcSalesarea().getCode())) {
                    EcSalesarea ecSalesarea = findSalesarea(salesarea);
                    DeliveryVkorgVO vo2 = new DeliveryVkorgVO(delivery, vkorg, salesarea, ecdv, ecdp, ecSalesarea);
                    vo2.setStatusMessage(SyncStatus.UPDATE, "update");
                    if (null == ecSalesarea) {
                        vo2.setMessage(salesarea + " salesarea not found!");
                        vo2.setValid(false);
                        success = false;
                        continue;
                    }
                    syncData.add(vo2);
                }
            } else if (knvv != null) { // insert
                String salesarea = knvv.getBzirk();
                EcSalesarea ecSalesarea = findSalesarea(salesarea);
                DeliveryVkorgVO vo2 = new DeliveryVkorgVO(delivery, vkorg, salesarea, ecdv, ecdp, ecSalesarea);
                vo2.setStatusMessage(SyncStatus.INSERT, "insert");
                if (null == ecSalesarea) {
                    vo2.setMessage(salesarea + " salesarea not found!");
                    vo2.setValid(false);
                    success = false;
                    continue;
                }
                syncData.add(vo2);
            } else if (ecdv != null) { // delete
                String salesarea = ecdv.getEcSalesarea().getCode();
                DeliveryVkorgVO vo2 = new DeliveryVkorgVO(delivery, vkorg, salesarea, ecdv, ecdp, ecdv.getEcSalesarea());
                vo2.setStatusMessage(SyncStatus.DELETE, "delete");
                syncData.add(vo2);
            }
        }
        return success;
    }

    private EcSalesarea findSalesarea(String salesarea) {
        if (mapSalesarea.containsKey(salesarea)) {
            return mapSalesarea.get(salesarea);
        }
        EcSalesarea ecSalesarea = ecSalesareaFacade.findByCode(salesarea);
        mapSalesarea.put(salesarea, ecSalesarea);
        return ecSalesarea;
    }

    private boolean saveResult1(List<DeliveryVO> result1) {
        boolean success = true;
        for (DeliveryVO vo : result1) {
            SyncStatus status = vo.getStatus();
            if (!vo.isValid() || SyncStatus.NOCHANGE==status) {
                continue;
            }
            try {
                EcDeliveryPlace ecdp = vo.getEcdp();
                ecdp.setName(vo.getName());
                ecdp.setProvince(vo.getProvince());
                ecdp.setCity(vo.getCity());
                ecdp.setDistrict(vo.getDistrict());
                ecdp.setTown(vo.getTown());
                ecdp.setActive(vo.isActive());
                ecDeliveryPlacdeFacade.save(ecdp);
                vo.setMessage(vo.getMessage() + " success");
                vo.setStatus(SyncStatus.NOCHANGE);
            } catch (Exception ex) {
                logger.error("saveResult1 exception", ex);
                vo.setValid(false);
                vo.setMessage(ex.getMessage());
                success = false;
            }
        }
        return success;
    }
    
    private boolean saveResult2(List<DeliveryVkorgVO> result2) {
        boolean success = true;
        for (DeliveryVkorgVO vo2 : result2) {
            SyncStatus status = vo2.getStatus();
            if (!vo2.isValid() || SyncStatus.NOCHANGE==status) {
                continue;
            }
            try {
                if (SyncStatus.INSERT == status) {
                    ecDeliveryVkorgFacade.insert(vo2.getEcDeliveryPlace(), vo2.getVkorg(), vo2.getEcSalesarea());
                } else if (SyncStatus.UPDATE == status) {
                    EcDeliveryVkorg entity = vo2.getEntity();
                    entity.setEcSalesarea(vo2.getEcSalesarea());
                    ecDeliveryVkorgFacade.edit(entity);
                } else if (SyncStatus.DELETE == status) {
                    EcDeliveryVkorg entity = vo2.getEntity();
                    ecDeliveryVkorgFacade.remove(entity);
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

}
