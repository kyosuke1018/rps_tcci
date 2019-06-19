/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmFactoryGroupR;
import com.tcci.cm.entity.admin.CmFactorygroup;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.CmFactoryGroupVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ResultSetHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author peter.pan
 */
@Stateless
public class CmFactorygroupFacade extends AbstractFacade<CmFactorygroup> {
    
    @EJB
    private CmFactoryGroupRFacade cmFactoryGroupRFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public CmFactorygroupFacade() {
        super(CmFactorygroup.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     */
    public void save(CmFactorygroup entity, TcUser operator){
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

    @Override
    public List<CmFactorygroup> findAll(){
        Query q = em.createNamedQuery("CmFactorygroup.findAll");
        return q.getResultList();
    }
    
    /**
     * 依類別取出群組資料
     * @param typeCode
     * @return
     */
    public List<CmFactorygroup> findByType(String typeCode){
        Query q = em.createNamedQuery("CmFactorygroup.findByType");
        q.setParameter("grouptype", typeCode);
        return q.getResultList();
    }
    
    /**
     * 依類別與代碼取出群組資料
     * @param typeCode
     * @param code
     * @return
     */
    public CmFactorygroup findByTypeAndCode(String typeCode, String code){
        Query q = em.createNamedQuery("CmFactorygroup.findByTypeAndCode");
        q.setParameter("grouptype", typeCode);
        q.setParameter("code", code);
        List<CmFactorygroup> resList = q.getResultList();
        
        return (resList!=null && !resList.isEmpty())? resList.get(0):null;
    }
    
    /**
     * 包含 factoryCode 的 group (只取第一筆 for 油價分區)
     * @param typeCode
     * @param factoryId
     * @return 
     */
    public CmFactorygroup findSingleGroupByIncFactory(String typeCode, Long factoryId){
        List<CmFactorygroup> resList = findByIncFactory(typeCode, factoryId);
        
        return (resList!=null && !resList.isEmpty())? resList.get(0):null;
    }
    public List<CmFactorygroup> findByIncFactory(String typeCode, Long factoryId){
        Map<String, Object> params = new HashMap<String, Object>();

        StringBuilder sql = new StringBuilder();
        sql.append("select id, groupname, description, createtimestamp, modifytimestamp, \n");
        sql.append("      grouptype, code, sortnum, cur, unit \n");
        sql.append("from cm_factorygroup a \n");
        sql.append("where a.disabled=0 and a.grouptype=#grouptype \n");
        sql.append("and exists ( \n");
        sql.append("    select * from cm_factory_group_r b where b.factorygroup_id=a.id and b.factory_id=#factory_id \n");
        sql.append(") \n");

        params.put("grouptype", typeCode);
        params.put("factory_id", factoryId);
        
        logger.debug("findByIncFactory sql = \n"+sql.toString());
        
        ResultSetHelper<CmFactorygroup> resultSetHelper = new ResultSetHelper(CmFactorygroup.class);
        List<CmFactorygroup> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }
    
    /**
     * 特殊廠別群組
     * @param code
     * @return 
     */
    /*public boolean isSpecialFactoryGroup(String code){
        //List<String> groupSpecial = new ArrayList<String>();
        //groupSpecial.addAll(Arrays.asList(GlobalConstant.FG_SPEC));
        return GlobalConstant.FG_SPEC.contains(code);
    }
    public boolean isPDParentFactoryGroup(String code){
        return GlobalConstant.FG_PD_PARENTS.contains(code);
    }*/
    
    /**
     * 新增 CmFactorygroup 含 CmFactorygroupR 設定
     * @param cmFactorygroup
     * @param factoryCheckMap
     * @param crearor
     */
    public void add(CmFactorygroup cmFactorygroup, Map<CmFactory, Boolean> factoryCheckMap, TcUser crearor){
        List<CmFactoryGroupR> cmFactoryGroupRList = new ArrayList<CmFactoryGroupR>();
        for (Map.Entry entry : factoryCheckMap.entrySet()) {
            CmFactory cmFactory = (CmFactory)entry.getKey();
            //String res = (String) entry.getValue();
            //if( res.equals("true") ){
            if( (Boolean)entry.getValue() ){
                CmFactoryGroupR cmFactoryGroupR = new CmFactoryGroupR();
                cmFactoryGroupR.setFactorygroupId(cmFactorygroup);
                cmFactoryGroupR.setFactoryId(cmFactory);
                cmFactoryGroupR.setCreator(crearor);
                cmFactoryGroupR.setCreatetimestamp(new Date());
                
                cmFactoryGroupRList.add(cmFactoryGroupR);
            }
        }
        cmFactorygroup.setCmFactoryGroupRList(cmFactoryGroupRList);
        
        create(cmFactorygroup);
    }
    
    /**
     * 修改 CmFactorygroup 含 CmFactorygroupR 設定
     * @param cmFactorygroup
     * @param factoryCheckMap
     * @param crearor
     */
    public void modify(CmFactorygroup cmFactorygroup, Map<CmFactory, Boolean> factoryCheckMap, TcUser crearor){
        List<CmFactoryGroupR> orilist = cmFactorygroup.getCmFactoryGroupRList();
        List<CmFactoryGroupR> newList = new ArrayList<CmFactoryGroupR>();
        
        // 移除未勾選
        for(int i=0; orilist!=null && i<orilist.size(); i++){
            boolean selected = false;
            
            Iterator iterator = factoryCheckMap.entrySet().iterator();
            while (!selected && iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                CmFactory cmFactory = (CmFactory)entry.getKey();
                //String res = (String) entry.getValue();
                //if( res.equals("true") ){
                if( (Boolean)entry.getValue() ){
                    if( orilist.get(i).getFactoryId().getId().longValue() == cmFactory.getId().longValue() ){
                        logger.debug("selected => "+cmFactory.getId().longValue());
                        selected = true;
                    }
                }
            }
            
            if( selected ){
                newList.add(orilist.get(i));
            }else{
                cmFactoryGroupRFacade.remove(orilist.get(i));
            }
        }
        for (Map.Entry entry : factoryCheckMap.entrySet()) {
            CmFactory cmFactory = (CmFactory)entry.getKey();
            //String res = (String) entry.getValue();
            //if( res.equals("true") ){
            if( (Boolean)entry.getValue() ){
                boolean isNew = true;
                for(CmFactoryGroupR cmFactoryGroupR : orilist){
                    if( cmFactoryGroupR.getFactoryId().getId().longValue() == cmFactory.getId().longValue() ){
                        isNew = false;
                    }
                }
                
                if( isNew ){
                    CmFactoryGroupR cmFactoryGroupR = new CmFactoryGroupR();
                    cmFactoryGroupR.setFactorygroupId(cmFactorygroup);
                    cmFactoryGroupR.setFactoryId(cmFactory);
                    cmFactoryGroupR.setCreator(crearor);
                    cmFactoryGroupR.setCreatetimestamp(new Date());
                    
                    newList.add(cmFactoryGroupR);
                }
            }
        }
        
        cmFactorygroup.setCmFactoryGroupRList(newList);
        
        edit(cmFactorygroup);
    }
    
    /**
     * 取得工廠資訊 (因回饋 TB 在 UDC 無法直接 JOIN)
     * @return 
     */
    /*public Map<String, CmFactoryGroupVO> fetchFactorygroupInfo(String groupType){
        Map<String, CmFactoryGroupVO> factorygroupMap = new HashMap<String, CmFactoryGroupVO>();

        if( GlobalConstant.ENABLED_SUB_PLANT ){// 含子廠，用實際廠別代碼與名稱
            List<CmFactoryGroupVO> allList = findFactoryBaseInfo(groupType, null, 0, null);

            if( allList!=null ){
                for(CmFactoryGroupVO cmFactoryGroupVO : allList){
                    factorygroupMap.put(cmFactoryGroupVO.getFactoryCode(), cmFactoryGroupVO);
                }
            }
        }else{// 不含子廠，只需用CmnRpt廠別群組代號與名稱即可
            List<CmFactorygroup> plants = findByType(groupType);

            if( plants!=null && !plants.isEmpty() ){
                for(CmFactorygroup plant : plants){
                    CmFactoryGroupVO cmFactoryGroupVO = new CmFactoryGroupVO();
                    cmFactoryGroupVO.setFactoryName(plant.getGroupname());
                    cmFactoryGroupVO.setFactoryCode(plant.getCode());
                    cmFactoryGroupVO.setGroupName(plant.getGroupname());
                    cmFactoryGroupVO.setGroupCode(plant.getCode());

                    factorygroupMap.put(cmFactoryGroupVO.getFactoryCode(), cmFactoryGroupVO);
                }
            }
        }
        
        return factorygroupMap;
    }*/
    
    /**
     * 取得下轄廠(子廠)資訊
     * @param groupType
     * @param plantGroupCode
     * @return 
     */
    public List<CmFactoryGroupVO> findSubFacotry(String groupType, String plantGroupCode){
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("select d.code factoryCode, d.name factoryName \n");
        sql.append("from CM_FACTORYGROUP b \n");
        sql.append("join CM_FACTORY_GROUP_R c on c.factorygroup_id=b.id \n");
        sql.append("join CM_FACTORY d on d.id=c.factory_id \n");
        sql.append("where 1=1 \n");
        
        sql.append("and b.GROUPTYPE=#GROUPTYPE \n");
        params.put("GROUPTYPE", groupType);
        
        sql.append("and b.code=#plantGroupCode \n");
        params.put("plantGroupCode", plantGroupCode);
        
        ResultSetHelper<CmFactoryGroupVO> resultSetHelper = new ResultSetHelper(CmFactoryGroupVO.class);
        List<CmFactoryGroupVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return resList;
    }
    
    /**
     * 取得下轄廠(子廠)代碼
     * @param groupType
     * @param plantGroupCode
     * @return 
     */
    public List<String> findSubFacotryCodes(String groupType, String plantGroupCode){
        List<CmFactoryGroupVO> list = findSubFacotry(groupType, plantGroupCode);
        List<String> codes = new ArrayList<String>();
        if( list!=null ){
            for(CmFactoryGroupVO vo : list){
                codes.add(vo.getFactoryCode());
            }
        }
        return codes;
    }
    
    /**
     * 指定廠別是否包含於某特殊群組
     * @param groupCode
     * @param factoryCode
     * @param specGroupFacoryMap
     * @return 
     */
    public boolean inSpecFactoryGroup(String groupCode, String factoryCode, Map<String, List<String>> specGroupFacoryMap){
        return specGroupFacoryMap!=null 
            && specGroupFacoryMap.get(groupCode)!=null 
            && specGroupFacoryMap.get(groupCode).contains(factoryCode);
    }
    public boolean inFactoryGroup(String factoryCode, List<String> factoryList){
        return factoryList!=null 
            && factoryList.contains(factoryCode);
    }
    /*public boolean inTWFactoryGroup(String factoryCode, Map<String, List<String>> specGroupFacoryMap){
        return specGroupFacoryMap!=null 
            && inFactoryGroup(factoryCode, specGroupFacoryMap.get(GlobalConstant.FG_TW));
    }
    public boolean inCNFactoryGroup(String factoryCode, Map<String, List<String>> specGroupFacoryMap){
        return specGroupFacoryMap!=null 
            && inFactoryGroup(factoryCode, specGroupFacoryMap.get(GlobalConstant.FG_CN));
    }*/
    
    /**
     * 是否是下轄廠(子廠)
     * @param groupType
     * @param groupCode
     * @param factoryCode
     * @return 
     */
    public boolean isSubFactory(String groupType, String groupCode, String factoryCode){
        List<String> codes = findSubFacotryCodes(groupType, groupCode);
        return (codes!=null && codes.contains(factoryCode));
    }
   
    /**
     * 廠別群組資訊 (為跨 DB 取得廠別名稱) -- 含群組代碼、下轄廠別代碼
     * @param groupType
     * @param plantCode
     * @param userId
     * @param usergroupIds
     * @return 
     */
    public List<CmFactoryGroupVO> findFactoryBaseInfo(String groupType, String plantCode, long userId, List<Long> usergroupIds){
        Map<String, Object> params = new HashMap<String, Object>();
        
        // for TEST
        /*if( usergroupIds!=null ){
            for(Long id : usergroupIds){
                logger.debug("findFactoryBaseInfo usergroupId="+id.longValue());
            }
        }*/
        StringBuilder sql = new StringBuilder();
        sql.append("select d.code factoryCode, d.name factoryName \n");
        sql.append("from CM_FACTORYGROUP b \n");
        sql.append("join CM_FACTORY_GROUP_R c on c.factorygroup_id=b.id \n");
        sql.append("join CM_FACTORY d on d.id=c.factory_id \n");
        
        if( userId>0 ){
            sql.append("join CM_USER_FACTORYGROUP_R a on a.factorygroup_id=b.id \n");
        }
        
        sql.append("where 1=1 and b.disabled=0 \n");
        
        if( userId>0 ){
            sql.append("and a.user_id=#userId \n");
            params.put("userId", userId);
        }

        sql.append("and b.GROUPTYPE=#GROUPTYPE \n");
        params.put("GROUPTYPE", groupType);
        
        // 指定廠別代碼
        if( StringUtils.isNotEmpty(plantCode) ){
            sql.append("and (b.code=#plantCode or d.code=#plantCode) \n");// *等於群組代碼或下轄廠別代碼
            params.put("plantCode", plantCode);
        }
        
        sql.append("group by d.code, d.name \n");
        sql.append("order by d.code");

        logger.debug("findFactoryBaseInfo sql = \n"+sql.toString());
        
        ResultSetHelper<CmFactoryGroupVO> resultSetHelper = new ResultSetHelper(CmFactoryGroupVO.class);
        List<CmFactoryGroupVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }
    
    /**
     * List<CmFactoryGroupVO> ==> FactoryCode List<String> 方便當條件
     * @param factoryGroupList
     * @return 
     */
    public List<String> toFactoryCodeList(List<CmFactoryGroupVO> factoryGroupList){
        List<String> list = new ArrayList<String>();
        if( factoryGroupList!=null ){
            for(CmFactoryGroupVO vo : factoryGroupList){
                list.add(vo.getFactoryCode());
            }
        }
        return list;
    }
    
    /**
     * List<CmFactoryGroupVO> ==> FactoryCode List<String> 方便當條件
     * @param factoryGroupList
     * @return 
     */
    public List<String> toFactoryGroupCodeList(List<CmFactorygroup> factoryGroupList){
        List<String> list = new ArrayList<String>();
        if( factoryGroupList!=null ){
            for(CmFactorygroup vo : factoryGroupList){
                list.add(vo.getCode());
            }
        }
        return list;
    }

    /**
     * 準備特殊廠別群組與下轄廠對應
     * @param specCodes
     * @return 
     */
    public Map<String, List<CmFactoryGroupVO>> prepareSpecGroupFacoryMap(List<String> specCodes, String groupType){
        Map<String, List<CmFactoryGroupVO>> specGroupFacoryMap = new HashMap<String, List<CmFactoryGroupVO>>();
        for(String group : specCodes){
            List<CmFactoryGroupVO> allList = findFactoryBaseInfo(groupType, group, 0, null);
            specGroupFacoryMap.put(group, allList);
        }
        return specGroupFacoryMap;
    }   

    /**
     * 杭州本部及下轄廠
     * @param plantCode
     * @param plantsInHZ
     * @return 
     */
    /*public boolean isHZPlant(String plantCode, List<String> plantsInHZ){
        if( GlobalConstant.FG_HZ.equals(plantCode) ){
            return true;
        }else if( plantsInHZ!=null && plantsInHZ.contains(plantCode) ){
            return true;
        }
        return false;
    }*/

    /**
     * 台灣製品廠及下轄廠
     * @param plantCode
     * @param plantsInPD
     * @return 
     */
    /*public boolean isPDPlant(String plantCode, List<String> plantsInPD){
        if( GlobalConstant.FG_PDTW.equals(plantCode) ){
            return true;
        }else if( plantsInPD!=null && plantsInPD.contains(plantCode) ){
            return true;
        }
        return false;
    }*/

    /**
     * 準備製品廠母廠群組與下轄廠對應
     * @return 
     */
    /*public Map<String, List<CmFactoryGroupVO>> preparePDParentFacoryMap(String groupType){
        Map<String, List<CmFactoryGroupVO>> specGroupFacoryMap = new HashMap<String, List<CmFactoryGroupVO>>();
        for(String group : GlobalConstant.FG_PD_PARENTS){
            List<CmFactoryGroupVO> allList = findFactoryBaseInfo(groupType, group, 0, null);
            specGroupFacoryMap.put(group, allList);
        }
        return specGroupFacoryMap;
    }*/  
    
    /**
     * 製品廠母廠及下轄廠，取得母廠代號
     * @param plantCode
     * @return 
     */
    /*public String getPDPlantParentCode(String plantCode, String groupType){
        for(String parentPlant : GlobalConstant.FG_PD_PARENTS){
            if( parentPlant.equals(plantCode) ){// 母廠代號本身
                return parentPlant;
            }else{
                List plants = findSubFacotryCodes(groupType, parentPlant);
                if( plants!=null && plants.contains(plantCode) ){// 下轄廠
                    return parentPlant;
                }
            }
        }
        return plantCode;
    }           
    public String getPDPlantParentCode(String plantCode, Map<String, List<String>> subPlants){
        if( subPlants!=null ){
            for(String parentPlant : GlobalConstant.FG_PD_PARENTS){
                if( parentPlant.equals(plantCode) ){// 母廠代號本身
                    return parentPlant;
                }else{
                    List<String> plants = subPlants.get(parentPlant);
                    for(String subplant : plants){
                        if( subplant.equals(plantCode) ){
                            return parentPlant; // 下轄廠
                        }
                    }
                }
            }
        }
        return plantCode;
    }*/
    
}
