/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.AbstractFacade;
import java.io.File;
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
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;

    @Inject
    TcFvvaultFacade tcFvvaultFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public TcFvitemFacade() {
        super(TcFvitem.class);
    }
    
    /**
     * 依 TcDomain 取得關聯 TcFvitem List
     * @param tcDomain
     * @return 
     */
    List<TcFvitem> findByDomain(TcDomain tcDomain){
        return em.createNamedQuery("TcFvitem.findByTcDomain")
                    .setParameter("domain", tcDomain)
                    .getResultList();
    }

    /**
     * 刪除 tcDomain 中 [所有tcFvvault] 無關聯的資料 (FVItem & 關聯實體檔案)
     * @param tcDomain 
     * @param tcFvvaultList 要處裡的實體檔案存放位置
     */
    public void destroyTcFvitemNoRef(TcDomain tcDomain){
        destroyTcFvitemNoRef(tcDomain, null);
    }
    
    /**
     * 刪除 tcDomain 中無關聯的資料 (FVItem & 關聯實體檔案)
     * @param tcDomain 
     * @param tcFvvaultList 要處裡的實體檔案存放位置
     */
    public void destroyTcFvitemNoRef(TcDomain tcDomain, List<TcFvvault> tcFvvaultList){
        List<TcFvitem> tcFvitemList = findByDomain(tcDomain);
        
        for(TcFvitem tcFvitem : tcFvitemList){
            if( tcFvitem.getTcApplicationdataCollection()==null 
                    || tcFvitem.getTcApplicationdataCollection().isEmpty() ){// 與 TcApplicationdata 無關聯
                destroyTcFvitem(tcFvitem, tcFvvaultList);
            }
        }
    }
    
    /**
     * 刪除 FVItem 及關聯實體檔案
     * @param tcDomain 
     * @param tcFvvaultList 要處理的 TcFvvault (null 值表 TcDomain 關聯的全部 TcFvvault)
     */
    public void destroyTcFvitem(TcFvitem tcFvitem, List<TcFvvault> tcFvvaultList){
        if( tcFvitem==null ) {
            return;
        }
        // 取得實體檔案位置
        TcDomain tcDomain = tcFvitem.getDomain();
        List<TcFvvault> tcFvvaultListAll = tcFvvaultFacade.getTcFvvaultByDomain(tcDomain);
        
        for(TcFvvault tcFvvault : tcFvvaultListAll){
            if( tcFvvaultList==null || tcFvvaultList.isEmpty() || tcFvvaultList.contains(tcFvvault) ){
                // TcFvvault 取得檔案夾位置
                String path = tcFvvault.getLocation();
                if (!path.endsWith("/") && !path.endsWith("\\")) {
                    path = path + File.separator;
                }

                // TcFvitem 取得系統自動產生的檔名
                String fullFileName = path + tcFvitem.getFilename();
                
                try{
                    // 刪除實體檔案
                    File file = new File(fullFileName);
                    if( file.exists() && file.isFile() ){
                        file.delete();
                    }
                    
                    // 刪除 tcFvitem
                    em.remove(tcFvitem);
                    
                    logger.info("destroyTcFvitem => delete => tcFvitem id ="+ tcFvitem.getId() + ": fullFileName = " + fullFileName);
                }catch(Exception e){
                    logger.error("destroyTcFvitem Exception !", e);
                }
            }
        }
    }    
}
