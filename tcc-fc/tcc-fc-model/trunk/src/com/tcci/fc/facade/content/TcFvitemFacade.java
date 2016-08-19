/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
public class TcFvitemFacade extends AbstractFacade<TcFvitem> {

    private static final Logger logger = LoggerFactory.getLogger(TcFvitemFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Inject
    TcFvvaultFacade tcFvvaultFacade;
    @Inject
    TcDomainFacade domainFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TcFvitemFacade() {
        super(TcFvitem.class);
    }

    public List<TcFvitem> findAllRecycledTcFvitem() {
        return findByDomain(domainFacade.getTrashCanDomain());
    }

    /**
     * 依 TcDomain 取得關聯 TcFvitem List
     *
     * @param tcDomain
     * @return
     */
    List<TcFvitem> findByDomain(TcDomain tcDomain) {
        return em.createNamedQuery("TcFvitem.findByTcDomain")
                .setParameter("domain", tcDomain)
                .getResultList();
    }

    /**
     * 刪除 tcDomain 中 [所有tcFvvault] 無關聯的資料 (FVItem & 關聯實體檔案)
     *
     * @param tcDomain
     * @param backupFileFlag 是否要備份檔案
     */
    public void destroyTcFvitemNoRef(TcDomain tcDomain, boolean backupFileFlag) {
        destroyTcFvitemNoRef(tcDomain, new ArrayList(), backupFileFlag);
    }

    /**
     * 刪除 tcDomain 中無關聯的資料 (Fvitem & 關聯實體檔案)
     *
     * @param tcDomain
     * @param tcFvvaultList 要處裡的實體檔案存放位置
     * @param backupFileFlag 是否要備份檔案
     */
    public void destroyTcFvitemNoRef(TcDomain tcDomain, List<TcFvvault> tcFvvaultList, boolean backupFileFlag) {
        List<TcFvitem> tcFvitemList = findByDomain(tcDomain);

        for (TcFvitem tcFvitem : tcFvitemList) {
            if (tcFvitem.getTcApplicationdataCollection() == null
                    || tcFvitem.getTcApplicationdataCollection().isEmpty()) {// 與 TcApplicationdata 無關聯
                destroyTcFvitem(tcFvitem, tcFvvaultList, backupFileFlag);
            }
        }
    }

    /**
     * 刪除指定Fvvault 中的 FvItem 及關聯實體檔案
     *
     * @param tcFvitem
     * @param tcFvvault 要處理的 TcFvvault
     * @param backupFileFlag 是否要備份檔案
     */
    public void destroyTcFvitem(TcFvitem tcFvitem, TcFvvault tcFvvault, boolean backupFileFlag) {
        List<TcFvvault> tcFvvaultList = new ArrayList();
        tcFvvaultList.add(tcFvvault);
        destroyTcFvitem(tcFvitem, tcFvvaultList, backupFileFlag);
    }

    /**
     * 刪除 FvItem 及關聯實體檔案
     *
     * @param tcFvitem
     * @param tcFvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     * @param backupFileFlag 是否要備份檔案
     */
    public void destroyTcFvitem(TcFvitem tcFvitem, List<TcFvvault> tcFvvaultList, boolean backupFileFlag) {
        if (tcFvitem == null) {
            return;
        }
        TcDomain trashCanDomain = null;
        if (backupFileFlag) {
            trashCanDomain = domainFacade.getTrashCanDomain();
        }
        try {
            if (backupFileFlag) {
                moveTcFvitem(tcFvitem, tcFvvaultList, trashCanDomain);
            } else {
                removeTcFvitem(tcFvitem, tcFvvaultList);
            }
        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }

    public void removeTcFvitem(TcFvitem fvitem, List<TcFvvault> fvvaultList) throws Exception {
        moveTcFvitem(fvitem, fvvaultList, null, "");
    }

    /**
     *
     * @param fvitem 要處理的 TcFvitem
     * @param fvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     * @param domain 目的地路徑 domain
     * @throws TcFvvaultNotFoundException when domain + host can't find mapping
     * TcFvvault will cause thie exception
     */
    public void moveTcFvitem(TcFvitem fvitem, List<TcFvvault> fvvaultList, TcDomain domain) throws TcFvvaultNotFoundException {
        moveTcFvitem(fvitem, fvvaultList, domain, "localhost");
    }

    /**
     *
     * @param fvitem 要處理的 TcFvitem
     * @param fvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     * @param targetDomain 目的地 domain
     * @param host 目的地 hostname
     * @throws TcFvvaultNotFoundException when domain + host can't find mapping
     * TcFvvault will cause thie exception
     *
     */
    public void moveTcFvitem(TcFvitem fvitem, List<TcFvvault> fvvaultList, TcDomain targetDomain, String host)
            throws TcFvvaultNotFoundException {
        // 取得實體檔案位置
        TcDomain tcDomain = fvitem.getDomain();
        //fvitem domain 與 targetDomain 相同時直接跳過不處理.
        if (tcDomain.equals(targetDomain)) {
            return;
        }
        List<TcFvvault> tcFvvaultListAll = tcFvvaultFacade.getTcFvvaultByDomain(tcDomain);
        boolean allVaultCleaned = true;
        String targetLocation = "";
        boolean remove = true;
        if (targetDomain != null) {
            targetLocation = getDomainLocation(targetDomain, host);
            remove = false;
        }
        boolean alreadyBackup = false; //用來判斷 multi-fvvault 是否已搬移過, 因為不同 fvvault 內存放的檔案是相同的, 所以只搬移一次即可.
        for (TcFvvault tcFvvault : tcFvvaultListAll) {
            // TcFvvault 取得檔案夾位置
            logger.debug("tcFvvault.getLocation()={}", tcFvvault.getLocation());
            File folder = new File(tcFvvault.getLocation());
            // TcFvitem 取得實體檔名
            File file = new File(folder, fvitem.getFilename());
            logger.debug("folder.exists()={},file.exists()={}", new Object[]{folder.exists(), file.exists()});

            if (!folder.exists() || !file.exists()) {
                allVaultCleaned = false;
            }
            if (fvvaultList == null || fvvaultList.isEmpty() || fvvaultList.contains(tcFvvault)) {
                try {
                    if (file.exists() && file.isFile()) {
                        if (!remove && !alreadyBackup) {
                            //將檔案搬到目的地目錄.
                            logger.debug("move file: fullFileName = {}", file.getPath() + file.getName());
                            File backupFolder = new File(targetLocation);
                            file.renameTo(new File(backupFolder, fvitem.getFilename()));
                            alreadyBackup = true;
                        } else {
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    logger.error("moveTcFvitem Exception !", e);
                }
            }
            logger.debug("allVaultCleaned={}", allVaultCleaned);
        }
        try {
            if (allVaultCleaned) {
                if (!remove) {
                    // 將 fvitem 切換到 target domain.
                    fvitem.setDomain(targetDomain);
                    em.merge(fvitem);
                    logger.info("moveTcFvitem => move => tcFvitem id ={}", fvitem.getId());
                } else {
                    em.remove(em.merge(fvitem));
                    logger.info("moveTcFvitem => delete => tcFvitem id ={}", fvitem.getId());
                }
            }

        } catch (Exception e) {
            logger.error("e={}", e);
        }
    }

    private String getDomainLocation(TcDomain domain, String host) throws TcFvvaultNotFoundException {
        TcFvvault vault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        if (vault != null) {
            return vault.getLocation();
        } else {
            throw new TcFvvaultNotFoundException("target domain vault not set yet!");
        }
    }

    public void destroyRecycledTcFvitem(TcFvitem fvitem) {
        if (fvitem == null) {
            return;
        }
        // 取得實體檔案位置
        TcDomain tcDomain = fvitem.getDomain();
        List<TcFvvault> fvvaultList = tcFvvaultFacade.getTcFvvaultByDomain(tcDomain);
        for (TcFvvault fvvault : fvvaultList) {
            // TcFvvault 取得檔案夾位置
            String path = fvvault.getLocation();
            if (!path.endsWith("/") && !path.endsWith("\\")) {
                path = path + File.separator;
            }

            // TcFvitem 取得系統自動產生的檔名
            String fullFileName = path + fvitem.getFilename();
            // 刪除實體檔案
            File file = new File(fullFileName);
            try {
                if (file.exists() && file.isFile()) {
                    //刪除檔案
                    file.delete();
                    // 刪除 tcFvitem.
                    em.remove(em.merge(fvitem));
                }
                logger.info("delete file: fullFileName = {}", fullFileName);
            } catch (Exception e) {
                logger.error("destroyRecycledTcFvitem Exception !", e);
            }
        }
    }
}
