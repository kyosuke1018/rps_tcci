/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacadeNE;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.rfq.RfqEkpoVO;
import com.tcci.et.model.rfq.RfqPmVO;
import com.tcci.et.model.rfq.RfqVO;
import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * AbstractFacade for no entity
 * @author Peter.pan
 */
@Stateless
public class RfqCommonFacade extends AbstractFacadeNE {
    @EJB SysResourcesFacade sys;
    @EJB EtRfqEkkoFacade rfqEkkoFacade;
    @EJB EtRfqEkkotxFacade rfqEkkotxFacade;
    @EJB EtRfqEkpoFacade rfqEkpoFacade;
    @EJB EtRfqEkpotxFacade rfqEkpotxFacade;
    @EJB EtRfqPmFacade rfqPmFacade;
    @EJB EtRfqVenderFacade rfqVenderFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * 找標案ID 關聯 RFQ資訊
     * @param tenderId
     * @return
     */
    public RfqVO findRfqByTenderId(Long tenderId){
        logger.debug("findRfqByTenderId tenderId = "+tenderId);
        RfqVO vo = new RfqVO();
        // 標頭
        RfqEkkoVO ekko = rfqEkkoFacade.findByTenderId(tenderId);
        vo.setEkko(ekko);
        if( ekko!=null && ekko.getId()!=null ){
            // 項目明細
            List<RfqEkpoVO> list = rfqEkpoFacade.findByRfqId(tenderId, ekko.getId());
            vo.setEkpoList(list);
            logger.debug("findRfqByTenderId EkpoList = "+sys.size(list));
            
            if( !sys.isEmpty(list) ){
                // 關聯 PR
                List<String> prNos = new ArrayList<String>();
                for(RfqEkpoVO ekpo : list){
                    if( !prNos.contains(ekpo.getBanfn()) ){
                        prNos.add(ekpo.getBanfn());
                    }
                }
                vo.setPrNos(prNos);
                logger.debug("findRfqByTenderId prNos = "+sys.size(prNos));
            }
            
            // 服務類明細
            List<RfqPmVO> pmList = rfqPmFacade.findByRfqId(tenderId, ekko.getId());
            vo.setPmList(pmList);
            logger.debug("findRfqByTenderId PmList = "+sys.size(pmList));
        }
        
        return vo;
    }
    
    /**
     *  儲存 完整 RFQ
     * @param tenderVO
     * @param rfqVO
     * @param operator 
     * @param simulated 
     */
    public void saveRfq(TenderVO tenderVO, RfqVO rfqVO, TcUser operator, boolean simulated){
        //  RFQ 主檔 EKKO
        prepareRfqEkko(tenderVO, rfqVO, operator);
        RfqEkkoVO ekkoVO = rfqVO.getEkko();
        rfqEkkoFacade.saveVO(ekkoVO, operator, simulated);
        logger.debug("onSaveRfq ekko id = "+ ekkoVO.getId());
        rfqVO.setEkko(ekkoVO);
        
        // RFQ 明細 EKPO
        prepareRfqEkpo(tenderVO, rfqVO, operator);
        rfqEkpoFacade.saveRfqEkpo(rfqVO, operator, simulated);
        // 服務類明細
        rfqPmFacade.saveRfqPm(rfqVO, operator, simulated);
    }
    
    /**
     * 準備 EKKO 完整儲存資料
     * @param tenderVO
     * @param vo
     * @param operator
     */
    public void prepareRfqEkko(TenderVO tenderVO, RfqVO vo, TcUser operator){
        // TODO
        //  RFQ 主檔 EKKO
        RfqEkkoVO ekko = vo.getEkko();
        ekko.setAedat(new Date());
        //ekko.setAngdt(angdt);// 報價截止日 UI 輸入
        //ekko.setBedat(bedat);// 文件日 UI 輸入
        ekko.setBsart("ZRFQ");
        //ekko.setBstyp();// copy from pr
        ekko.setBukrs(vo.getBukrs());
        //ekko.setEbeln(); // X
        //ekko.setEkgrp();// UI 輸入
        //ekko.setEkorg();// UI 輸入
        ekko.setErnam(operator.getCname());// operator
        //ekko.setKufix();// 固定匯率 ?
        ekko.setMandt(tenderVO.getSapClient());
        ekko.setRfqNo(tenderVO.getCode());// 直接使用標案編號
        //ekko.getRlwrt();
        ekko.setSpras(tenderVO.getLanguage());// lang ?
        //ekko.setSubmi(bukrs);// 彙總 ?
        ekko.setTenderId(tenderVO.getId());
        //ekko.setUnsez();// 我方參考 ?
        ekko.setWaers(tenderVO.getCurrency());// 幣別 ?
        //ekko.setWkurs();// 匯率 ?
        //ekko.setZterm();// 付款條件 ?
        //vo.setCompany(company);
        vo.setCompanyId(tenderVO.getCompanyId());
        vo.setEkko(ekko);
    }
    
    /**
     *  準備 EKPO 完整儲存資料
     * @param tenderVO
     * @param vo
     * @param operator
     */
    public void prepareRfqEkpo(TenderVO tenderVO, RfqVO vo, TcUser operator){
        // RFQ 明細檔 EKPO
        if( vo.getEkpoList()!=null ){
            logger.debug("prepareRfqEkpo vo.getEkpoList() = "+vo.getEkpoList().size());
            for(RfqEkpoVO ekpo : vo.getEkpoList()){
                ekpo.setTenderId(tenderVO.getId());
                ekpo.setRfqId(vo.getEkko().getId());
                logger.debug("prepareRfqEkpo Eindt = "+ekpo.getEindt());
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Company & Factory">
    /**
     * CM_COMPANY
     * @param companys
     */
    public List<SelectItem> buildCompanyOptions(List<CmCompanyVO> companys){
        List<SelectItem> ops = new ArrayList<SelectItem>();
        
        if( companys!=null ){
            for(CmCompanyVO vo : companys){
                ops.add(new SelectItem(vo.getId(), vo.getCompanyName()));
            }
        }
        
        return ops;
    }
    
    /**
     *
     * @param companys
     * @param id
     * @return
     */
    public CmCompanyVO getSelectedCompany(List<CmCompanyVO> companys, Long id){
        if( companys!=null ){
            for(CmCompanyVO vo : companys){
                if( id!=null && id.equals(vo.getId()) ){
                    return vo;
                }
            }
        }
        return null;
    }
    
    /**
     *
     * @param factorys
     * @return
     */
    public List<SelectItem> buildFactoryOptions(List<CmFactoryVO> factorys) {
        List<SelectItem> ops = new ArrayList<SelectItem>();
        
        if( factorys!=null ){
            for(CmFactoryVO vo : factorys){
                ops.add(new SelectItem(vo.getId(), vo.getDisplayLabel()));
            }
        }
        
        return ops;
    }
    
    /**
     *
     * @param factorys
     * @param id
     * @return
     */
    public CmFactoryVO getSelectedFactory(List<CmFactoryVO> factorys, Long id){
        if( factorys!=null ){
            for(CmFactoryVO vo : factorys){
                if( id!=null && id.equals(vo.getId()) ){
                    return vo;
                }
            }
        }
        return null;
    }
    //</editor-fold>
}
