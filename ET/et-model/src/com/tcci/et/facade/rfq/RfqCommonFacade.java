/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rfq;

import com.tcci.cm.facade.AbstractFacadeNE;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class RfqCommonFacade extends AbstractFacadeNE {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
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

}
