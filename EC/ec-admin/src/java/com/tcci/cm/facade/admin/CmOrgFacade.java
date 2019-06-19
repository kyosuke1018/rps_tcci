/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmOrg;
import com.tcci.cm.enums.OrgTypeEnum;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter
 */
@Stateless
public class CmOrgFacade extends AbstractFacade<CmOrg> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmOrgFacade() {
        super(CmOrg.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(CmOrg entity, TcUser operator){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity);
            }
        }
    }
    
    /**
     * 遞迴方式找出所有組織子節點
     * @param orgId
     * @param cmOrgAll 
     * @param level 遞迴階層
     * @param resList 回傳結果
     */
    public void findSubOrg(long orgId, List<CmOrg> cmOrgAll, int level, List<Long> resList){
        if( level > GlobalConstant.MAX_RECURSION_LEVEL ){
            return;
        }
        
        if( cmOrgAll!=null ){
            for(CmOrg cmOrg : cmOrgAll){
                //if( cmOrg.getParent()!=null && cmOrg.getParent().getId().equals(orgId) ){
                if( cmOrg.getParent()!=null && cmOrg.getParent().equals(orgId) ){
                    long subId = cmOrg.getId();
                    if( !resList.contains(subId) ){
                        resList.add(subId);
                    }
                    findSubOrg(subId, cmOrgAll, level+1, resList);
                }
            }
        }
    }
    
    /**
     * 找特定組織所屬公司
     * @param orgId
     * @param cmOrgAll 
     * @return  
     */
    public CmOrg findOrgCompany(long orgId, List<CmOrg> cmOrgAll){
        if( cmOrgAll!=null ){
            for(CmOrg cmOrg : cmOrgAll){
                // logger.debug("cmOrg.getId().longValue()="+cmOrg.getId().longValue()+"; orgId = "+orgId+"; cmOrg.getCtype()="+cmOrg.getCtype());
                if( cmOrg.getId().longValue() == orgId ){
                    if( OrgTypeEnum.COMPANY.getCode().equals(cmOrg.getCtype()) ){// 公司類型
                        return cmOrg;
                    }else{
                        if( cmOrg.getParent()!=null ){
                            // return findOrgCompany(cmOrg.getParent().getId().longValue(), cmOrgAll);
                            return findOrgCompany(cmOrg.getParent().longValue(), cmOrgAll);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 依指定組織查詢該組織及其CHILD 的組織 ID 列表
     * @return 
     */
    public List<Long> findOrgWithChild(long selectedOrgId) {
        List<Long> resList = new ArrayList<Long>();
        List<CmOrg> cmOrgAll = this.findAll();

        resList.add(selectedOrgId);
        findSubOrg(selectedOrgId, cmOrgAll, 0, resList);
       
        return resList;
    }

    /**
     * 依名稱排序
     * @return 
     */
    public List<CmOrg> findAllSortByName() {
        return this.findByNamedQuery("CmOrg.findAll", null);
    }
    
    /**
     * find by Id
     * @param id
     * @return 
     */
    public CmOrg findById(Long id) {
        if( id==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
        List<CmOrg> resList = findByNamedQuery("CmOrg.findById", params);
        
        return (resList==null || resList.isEmpty())? null:resList.get(0);
    }    
    
    /**
     * 有無關連資料
     * @param orgId
     * @return 
     */
    public boolean hasRelationData(long orgId){
        StringBuilder sb = new StringBuilder();
        
        sb.append("select org_id, 'tc_user' tb from tc_user where org_id = #ORG_ID group by org_id \n");
        sb.append("union \n");
        sb.append("select org_id, 'cm_user_org' tb from cm_user_org where org_id = #ORG_ID group by org_id \n");
        
        Query q = getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("ORG_ID", orgId);

        List list = q.getResultList();
        
        return (list!=null && !list.isEmpty());
    }}
