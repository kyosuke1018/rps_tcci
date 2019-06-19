/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.et.entity.KbLink;
import com.tcci.et.model.LinkVO;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author peter.pan
 */
@Stateless
public class KbLinkFacade extends AbstractFacade<KbLink> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KbLinkFacade() {
        super(KbLink.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(KbLink entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity, simulated);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity, simulated);
            }
        }
    }    
    public void saveVO(LinkVO pubVO, TcUser operator, boolean simulated){
        KbLink entity = (pubVO.getId()!=null)?find(pubVO.getId()):new KbLink();
        this.copyVoToEntity(pubVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, pubVO);
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(KbLink entity, LinkVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(LinkVO vo, KbLink entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }
    
}
