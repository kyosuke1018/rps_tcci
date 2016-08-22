/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.customer;

import com.tcci.storeadmin.facade.sync.SyncStatus;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcCustomerSales;
import com.tcci.tccstore.entity.EcCustomerVkorg;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.sales.EcSalesFacade;
import com.tcci.tccstore.util.SapUtil;
import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class CustomerSyncFacade {

    private final static String ZPER_CN_FIELDS = "VKORG,KUNNR,NAME1,PERNR,SNAME,BZIRK";

    @EJB
    private EcCustomerFacade ecCustomerFacade;
    @EJB
    private EcSalesFacade ecSalesFacade;

    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;

    public CustomerSyncData sync() throws Exception {
        CustomerSyncData syncData = new CustomerSyncData();
        loadCSVData(syncData);
        loadDBData(syncData);
        compareCustomer(syncData);
        compareSales(syncData);
        compareCustomerSales(syncData);
        compareCustomerVkorg(syncData);
        return syncData;
    }

    public void save(CustomerSyncData syncData) {
        boolean success1 = saveResult1(syncData);
        boolean success2 = saveResult2(syncData);
        boolean success3 = saveResult3(syncData);
        boolean success4 = saveResult4(syncData);
        if (!success1 || !success2 || !success3 || !success4) {
            throw new EJBException("save failure!"); // rollback transaction
        }
    }

    private void loadCSVData(CustomerSyncData syncData) throws Exception {
        List<ZPER_CN> zpercnList = SapUtil.readTableData("ZPER_CN", ZPER_CN_FIELDS, ZPER_CN.class, getCsvFolder());
        for (ZPER_CN zpercn : zpercnList) {
            String customerCode = zpercn.getKunnr();
            if (null == customerCode || customerCode.length() != 5) {
                continue;
            }
            CustomerVO cuvo = new CustomerVO(customerCode, zpercn.getName1());
            syncData.add(cuvo);
            String pernr = zpercn.getPernr();
            //20160614, 厂 总经理不用過濾
            //if (!"20000000".equals(pernr)) { // 20000000: 厂 总经理, ignore
                String sname = zpercn.getSname();
                boolean active = !sname.contains("离职");
                SalesVO savo = new SalesVO(pernr, sname, active);
                syncData.add(savo);
                if (active) {
                    CustomerSalesVO cusa = new CustomerSalesVO(cuvo.getCode(), pernr);
                    syncData.add(cusa);
                }
            //}
            CustomerVkorgVO cuvk = new CustomerVkorgVO(customerCode, zpercn.getVkorg());
            syncData.add(cuvk);
        }
    }

    private void loadDBData(CustomerSyncData syncData) {
        List<EcCustomer> eccuList = ecCustomerFacade.findAll();
        for (EcCustomer eccu : eccuList) {
            syncData.add(eccu);
        }
        List<EcSales> ecsaList = ecSalesFacade.findAll();
        for (EcSales ecsa : ecsaList) {
            syncData.add(ecsa);
        }
        List<EcCustomerSales> eccsList = ecCustomerFacade.findAllCustomerSales();
        for (EcCustomerSales eccs : eccsList) {
            syncData.add(eccs);
        }
        List<EcCustomerVkorg> eccvList = ecCustomerFacade.findAllCustomerVkorg();
        for (EcCustomerVkorg eccv : eccvList) {
            syncData.add(eccv);
        }
    }

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

    private void compareCustomer(CustomerSyncData syncData) {
        for (String pk1 : syncData.getSetPK1()) {
            CustomerVO cuvo = syncData.getCUVO(pk1);
            EcCustomer eccu = syncData.getECCU(pk1);
            if (cuvo != null && eccu != null) { // update
                cuvo.setEntity(eccu);
                boolean nameChanged = !StringUtils.equals(cuvo.getName(), eccu.getName());
                boolean activeChanged = cuvo.isActive() ^ eccu.isActive();
                if (nameChanged || activeChanged) {
                    cuvo.setEntity(eccu);
                    cuvo.setStatusMessage(SyncStatus.UPDATE, "update");
                    syncData.addResult(cuvo);
                }
            } else if (cuvo != null) { // insert
                cuvo.setStatusMessage(SyncStatus.INSERT, "insert");
                syncData.addResult(cuvo);
            } else { // ignore
            }
        }
    }

    private void compareSales(CustomerSyncData syncData) {
        for (String pk2 : syncData.getSetPK2()) {
            SalesVO savo = syncData.getSAVO(pk2);
            EcSales ecsa = syncData.getECSA(pk2);
            if (savo != null && ecsa != null) { // update
                savo.setEntity(ecsa);
                boolean nameChanged = !StringUtils.equals(savo.getName(), ecsa.getName());
                boolean activeChanged = savo.isActive() ^ ecsa.isActive();
                if (nameChanged || activeChanged) {
                    savo.setEntity(ecsa);
                    savo.setStatusMessage(SyncStatus.UPDATE, "update");
                    syncData.addResult(savo);
                }
            } else if (savo != null) { // insert
                savo.setStatusMessage(SyncStatus.INSERT, "insert");
                syncData.addResult(savo);
            } else { // disable sales
                if (ecsa.isActive()) {
                    savo = new SalesVO(ecsa.getCode(), ecsa.getName(), false);
                    savo.setEntity(ecsa);
                    savo.setStatusMessage(SyncStatus.UPDATE, "disable");
                    syncData.addResult(savo);
                }
            }
        }
    }

    private void compareCustomerSales(CustomerSyncData syncData) {
        for (String pk3 : syncData.getSetPK3()) {
            CustomerSalesVO cusa = syncData.getCUSA(pk3);
            EcCustomerSales eccs = syncData.getECCS(pk3);
            if (cusa != null && eccs != null) { // same, skip
            } else if (cusa != null) { // insert
                cusa.setStatusMessage(SyncStatus.INSERT, "insert");
                syncData.addResult(cusa);
            } else { // delete
                cusa = new CustomerSalesVO(eccs.getEcCustomer().getCode(), eccs.getEcSales().getCode());
                cusa.setEntity(eccs);
                cusa.setStatusMessage(SyncStatus.DELETE, "delete");
                syncData.addResult(cusa);
            }
        }
    }

    private void compareCustomerVkorg(CustomerSyncData syncData) {
        for (String pk4 : syncData.getSetPK4()) {
            CustomerVkorgVO cuvk = syncData.getCUVK(pk4);
            EcCustomerVkorg eccv = syncData.getECCV(pk4);
            if (cuvk != null && eccv != null) { // same, skip
            } else if (cuvk != null) { // insert
                cuvk.setStatusMessage(SyncStatus.INSERT, "insert");
                syncData.addResult(cuvk);
            } else { // delete
                cuvk = new CustomerVkorgVO(eccv.getEcCustomer().getCode(), eccv.getEcCustomerVkorgPK().getVkorg());
                cuvk.setEntity(eccv);
                cuvk.setStatusMessage(SyncStatus.DELETE, "delete");
                syncData.addResult(cuvk);
            }
        }
    }

    private boolean saveResult1(CustomerSyncData syncData) {
        boolean success = true;
        for (CustomerVO vo : syncData.getResult1()) {
            SyncStatus status = vo.getStatus();
            if (!vo.isValid() || SyncStatus.NOCHANGE == status) {
                continue;
            }
            try {
                EcCustomer eccu = vo.getEntity();
                if (null == eccu) {
                    eccu = new EcCustomer();
                    eccu.setCode(vo.getCode());
                    vo.setEntity(eccu);
                }
                eccu.setName(vo.getName());
                eccu.setActive(vo.isActive());
                ecCustomerFacade.save(eccu);
                vo.setStatusMessage(SyncStatus.NOCHANGE, vo.getMessage() + " success");
            } catch (Exception ex) {
                vo.setMessage(ex.getMessage());
                vo.setValid(false);
                success = false;
            }
        }
        return success;
    }

    private boolean saveResult2(CustomerSyncData syncData) {
        boolean success = true;
        for (SalesVO vo : syncData.getResult2()) {
            SyncStatus status = vo.getStatus();
            if (!vo.isValid() || SyncStatus.NOCHANGE == status) {
                continue;
            }
            try {
                EcSales ecsa = vo.getEntity();
                if (null == ecsa) {
                    ecsa = new EcSales();
                    ecsa.setCode(vo.getCode());
                    vo.setEntity(ecsa);
                }
                ecsa.setName(vo.getName());
                ecsa.setActive(vo.isActive());
                ecSalesFacade.save(ecsa);
                vo.setStatusMessage(SyncStatus.NOCHANGE, vo.getMessage() + " success");
            } catch (Exception ex) {
                vo.setMessage(ex.getMessage());
                vo.setValid(false);
                success = false;
            }
        }
        return success;
    }

    private boolean saveResult3(CustomerSyncData syncData) {
        boolean success = true;
        for (CustomerSalesVO vo : syncData.getResult3()) {
            SyncStatus status = vo.getStatus();
            if (!vo.isValid() || SyncStatus.NOCHANGE == status) {
                continue;
            }
            try {
                if (SyncStatus.INSERT == status) {
                    EcCustomer eccu = syncData.getCUVO(vo.getCustomer()).getEntity();
                    EcSales ecSales = syncData.getSAVO(vo.getSales()).getEntity();
                    ecCustomerFacade.insertCustomerSales(eccu, ecSales);
                } else if (SyncStatus.DELETE == status) {
                    ecCustomerFacade.removeCustomerSales(vo.getEntity());
                }
                vo.setStatusMessage(SyncStatus.NOCHANGE, vo.getMessage() + " success");
            } catch (Exception ex) {
                vo.setMessage(ex.getMessage());
                vo.setValid(false);
                success = false;
            }
        }
        return success;
    }

    private boolean saveResult4(CustomerSyncData syncData) {
        boolean success = true;
        for (CustomerVkorgVO vo : syncData.getResult4()) {
            SyncStatus status = vo.getStatus();
            if (!vo.isValid() || SyncStatus.NOCHANGE == status) {
                continue;
            }
            try {
                if (SyncStatus.INSERT == status) {
                    EcCustomer eccu = syncData.getCUVO(vo.getCustomer()).getEntity();
                    ecCustomerFacade.insertCustomerVkorg(eccu, vo.getVkorg());
                } else if (SyncStatus.DELETE == status) {
                    ecCustomerFacade.removeCustomerVkorg(vo.getEntity());
                }
                vo.setStatusMessage(SyncStatus.NOCHANGE, vo.getMessage() + " success");
            } catch (Exception ex) {
                vo.setMessage(ex.getMessage());
                vo.setValid(false);
                success = false;
            }
        }
        return success;
    }

}
