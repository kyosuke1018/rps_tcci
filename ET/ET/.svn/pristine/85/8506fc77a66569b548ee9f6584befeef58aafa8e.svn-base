/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmCompany;
import com.tcci.cm.entity.admin.CmFactoryCategory;
import com.tcci.cm.entity.admin.CmUserFactorygroupR;
import com.tcci.cm.entity.admin.CmUsercompany;
import com.tcci.cm.enums.SapClientEnum;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ResultSetHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jackson.Lee
 */
@Stateless
public class CmFactoryFacade extends AbstractFacade<CmFactory> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmFactoryFacade() {
        super(CmFactory.class);
    }
    
    /**
     * 依 sapClientCode 找出工廠列表
     * @param sapClientCode
     * @return 
     */
    public List<CmFactory> findBySapClientCode(String sapClientCode){ // tcc, tcc_c, ...
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM CmFactory m");
        
        if( sapClientCode!=null && !sapClientCode.isEmpty() ){
            sb.append(" WHERE m.sapClientCode = :sapClientCode ");
        }
        
        sb.append(" ORDER BY m.code");

        Query q = em.createQuery(sb.toString());
        
        if( sapClientCode!=null && !sapClientCode.isEmpty() ){
            q.setParameter("sapClientCode", sapClientCode); 
        }

        List<CmFactory> list = q.getResultList();
        
        return list;
    }
    
    /**
     * 依工廠代碼，取得工廠資料
     * @param code 工廠代碼
     * @return 
     */
    public CmFactory getByCode(String code) {

        if (StringUtils.isBlank(code)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT m FROM CmFactory m WHERE m.code = :code");
        Query q = em.createQuery(sb.toString());
        q.setParameter("code", code);
        List<CmFactory> list = q.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);

    }    
    
    /**
     * Get FactoryName By factory code
     * @param factoryCode 工廠代號
     * @return 
     */
    public String getFactoryName(String factoryCode) {
        CmFactory cmFactory = getByCode(factoryCode);
        return cmFactory.getName();
    }    

    /**
     *  依公司別(SAP CLIENT)取出關聯工廠 
     * @param cmUsercompanyList
     * @return 
     */
    public List<CmFactory> findByCompany(List<CmUsercompany> cmUsercompanyList){
        if (null==cmUsercompanyList || cmUsercompanyList.isEmpty() ){
            return null;
        }
        
        List<String> companies = new ArrayList<String>();
        for(CmUsercompany cmUsercompany : cmUsercompanyList){
            companies.add(cmUsercompany.getSapClientCode());
        }
        
        return findByCompanyCodes(companies);
    }
    
    /**
     * 依公司別(SAP CLIENT)取出關聯工廠 (移除非台泥)
     * @param companies
     * @return 
     */
    public List<CmFactory> findByCompanyCodesTCCI(List<String> companies){
        // 移除非台泥
        if( companies!=null ){
            for(int i=companies.size()-1; i>=0; i--){
                if( !companies.get(i).equals(SapClientEnum.SAP_TW.getSapClientCode()) 
                        && !companies.get(i).equals(SapClientEnum.SAP_CN.getSapClientCode()) ){
                    companies.remove(i);
                }
            }
        }
        
        return findByCompanyCodes(companies);
    }
    
    /**
     *  依公司別(SAP CLIENT)取出關聯工廠 
     * @param sapClientCode
     * @return 
     */
    public List<CmFactory> findByCompanyCode(String sapClientCode){
        List<String> companies = new ArrayList<String>();
        companies.add(sapClientCode);
        
        return findByCompanyCodes(companies);
    }
    public List<CmFactory> findByCompanyCodes(List<String> companies){
        if (null==companies || companies.isEmpty() ){
            return null;
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f FROM CmFactory f \n");
        //sb.append(" JOIN CmFactoryCategory c ON c.id = f.categoryId \n");
        sb.append(" WHERE 1=1 \n");
        
        String cond = NativeSQLUtils.getInSQL("f.sapClientCode", companies, params);
        cond = cond.replaceAll("#", ":");
        sb.append(cond);
        
        sb.append(" ORDER BY f.code \n");
        
        Query q = em.createQuery(sb.toString());       
        for(String key : params.keySet()){
            q.setParameter(key, params.get(key));
        }
        
        List<CmFactory> list = q.getResultList();
        
        return list;
    }
    
    public List<CmFactory> findByCategory(CmFactoryCategory cmFactoryCategory){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryId", cmFactoryCategory);
        return this.findByNamedQuery("CmFactory.findByCategory", params);
    }
    
    public List<CmFactory> findByCompany(Long companyId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyId", companyId);
        return this.findByNamedQuery("CmFactory.findByCompany", params);
    }
    
    public List<CmFactoryVO> findVOByCompany(Long companyId){
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("select f.* \n");
        sb.append("from cm_factory f \n");
        sb.append("where 1=1 \n");
        sb.append("and company_id=#company_id \n");
        sb.append("order by f.sap_client_code, f.code ");
        
        params.put("company_id", companyId);

        return this.selectBySql(CmFactoryVO.class, sb.toString(), params);
    }
    
    /**
     * 依廠別權限 找出 關聯公司 (for 跨廠不跨公司功能)
     * @param cmFactoryList
     * @return 
     */
    public List<String> findCompanyByPermission(List<CmFactory> cmFactoryList){
        List<String> sapClientCodes = findSapClientCodeByPermission(cmFactoryList);
        if (null==sapClientCodes || sapClientCodes.isEmpty() ){
            return null;
        }        
        
        // 台泥台灣、大陸有任一設定即可 (for 跨廠不跨公司功能)
        List<String> companies = new ArrayList<String>();
        if( sapClientCodes.contains(CmCompany.COMPANY_CODE_TCC) && sapClientCodes.contains(CmCompany.COMPANY_CODE_TCC_CN) ){
            return sapClientCodes;
        }else{
            if( sapClientCodes.contains(CmCompany.COMPANY_CODE_TCC) || sapClientCodes.contains(CmCompany.COMPANY_CODE_TCC_CN) ){
                companies.add(CmCompany.COMPANY_CODE_TCC);
                companies.add(CmCompany.COMPANY_CODE_TCC_CN);
            }
            
            // 其他公司
            for(String code : sapClientCodes){
                if( !(code.equals(CmCompany.COMPANY_CODE_TCC) || code.equals(CmCompany.COMPANY_CODE_TCC_CN)) ){
                    companies.add(code);
                }
            }
        }
        
        return companies;
    }
    
    /**
     * 依廠別權限 找出 關聯SapClientCode (for 跨廠不跨公司功能)
     * @param cmFactoryList
     * @return 
     */
    public List<String> findSapClientCodeByPermission(List<CmFactory> cmFactoryList){
        if (null==cmFactoryList || cmFactoryList.isEmpty() ){
            return null;
        }
        
        List<String> sapClientCodes = new ArrayList<String>();
        for(CmFactory cmFactory : cmFactoryList){
            if( cmFactory.getSapClientCode()!=null ){
                if( !sapClientCodes.contains(cmFactory.getSapClientCode()) ){
                    sapClientCodes.add(cmFactory.getSapClientCode());
                }
            }
        }
        
        return sapClientCodes;
    }
    
    /**
     * 特殊跨廠權限 - 工廠列表
     * @param cmUserFactorygroupRList
     * @return 
     */
    public List<CmFactory> findSpecFactoryByGroup(List<CmUserFactorygroupR> cmUserFactorygroupRList, String groupType, List<Long> usergroups) {
        if (null==cmUserFactorygroupRList || cmUserFactorygroupRList.isEmpty() ){
            return null;
        }
        
        List<Long> grouplist = new ArrayList<Long>();
        
        for(CmUserFactorygroupR cmUserFactorygroupR : cmUserFactorygroupRList){ 
            // 針對特定USER群組的設定
            if( usergroups!=null && !usergroups.isEmpty() ){
                if( usergroups.contains(cmUserFactorygroupR.getUsergroupId()) ){
                    grouplist.add(cmUserFactorygroupR.getFactorygroupId().getId());
                }
            }else{// 全部聯集
                grouplist.add(cmUserFactorygroupR.getFactorygroupId().getId());
            }
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("select distinct f.* \n");
        sb.append(" from cm_factory f \n");
        sb.append(" join cm_factory_group_r a on a.factory_id=f.id \n");
        sb.append(" join cm_factorygroup g on g.disabled=0 and g.id=a.FACTORYGROUP_ID \n");
        sb.append(" where 1=1 \n");
        sb.append(" and g.grouptype = #groupType \n");
        
        params.put("groupType", groupType);
        
        String cond = NativeSQLUtils.getInSQL("a.factorygroup_id", grouplist, params);
        sb.append(cond);      
        
        sb.append(" order by f.sap_client_code, f.code ");
        
        Query q = em.createNativeQuery(sb.toString(), CmFactory.class);       
        for(String key : params.keySet()){
            q.setParameter(key, params.get(key));
        }
        
        List<CmFactory> list = q.getResultList();
        
        return list;
    }
    
    /**
     * 依廠別代碼查詢
     * @param sapClientCode
     * @param codes
     * @return 
     */
    public List<CmFactory> findByCodes(String sapClientCode, List<String> codes){
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("select f.* \n");
        sb.append("from cm_factory f \n");
        sb.append("where 1=1 \n");
        
        if( sapClientCode!=null && !sapClientCode.isEmpty() ){
            sb.append(NativeSQLUtils.genEqualSQL("f.sap_client_code", sapClientCode, params)).append(" \n");
        }
        if( codes!=null && !codes.isEmpty() ){
            sb.append(NativeSQLUtils.getInSQL("f.code", codes, params)).append(" \n");
        }
        
        sb.append(" order by f.sap_client_code, f.code ");
        
        logger.debug("findByCodes sql = \n"+sb.toString());
        
        ResultSetHelper<CmFactory> resultSetHelper = new ResultSetHelper(CmFactory.class);
        List<CmFactory> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);

        return resList;
    }
    
    //對應地區(ET_OPTION area) 的廠別
    public List<CmFactory> findByAreaCode(List<CmFactory> owenerfactoryList, Long areaId, String areaCode){ // 
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
//        sb.append("select DISTINCT cf.ID from CM_FACTORY cf \n");
        sb.append("select cf.ID from CM_FACTORY cf \n");
        sb.append("INNER JOIN CM_FACTORY_GROUP_R cfgr on cfgr.FACTORY_ID = cf.id \n");
        sb.append("INNER JOIN CM_FACTORYGROUP cfg on cfgr.FACTORYGROUP_ID = cfg.id AND cfg.DISABLED = 0 \n");
        sb.append("INNER JOIN ET_OPTION eo on eo.code = cfg.code and eo.type = 'area' AND eo.DISABLED = 0 \n");
        sb.append("WHERE 1=1 \n");
        if(areaId != null){
            sb.append(" AND eo.ID=#areaId \n");
            params.put("areaId", areaId);
        }
        if (StringUtils.isNotBlank(areaCode)) {
            sb.append(" AND eo.code=#areaCode \n");
            params.put("areaCode", areaCode);
        }
        sb.append("order by cf.code asc \n");
        
        Query q = em.createNativeQuery(sb.toString());
        for(String key : params.keySet()){
            q.setParameter(key, params.get(key));
        }
        
        List<CmFactory> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
//            list.addAll(owenerfactoryList);
        }
        
        List<BigDecimal> templist = q.getResultList();
        if (CollectionUtils.isNotEmpty(templist)) {
            for(BigDecimal temp : templist){
                CmFactory factory = this.find(temp.longValue());
//            list.add(temp.longValue());
//                if(!list.contains(factory)){
//                    list.add(factory);
//                }
                if (CollectionUtils.isNotEmpty(owenerfactoryList)) {//符合權限的選項
                    if(owenerfactoryList.contains(factory)){
                        list.add(factory);
                    }
                }else{
                    list.add(factory);//不限制
                }
            }
        }
        return list;
    }

    /**
     * 
     * @param factoryId
     * @return 
     */
    public CmFactoryVO findById(Long factoryId) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("select f.* \n");
        sb.append("from cm_factory f \n");
        sb.append("where 1=1 \n");
        sb.append("and id=#factoryId");
        
        params.put("factoryId", factoryId);

        List<CmFactoryVO> list=this.selectBySql(CmFactoryVO.class, sb.toString(), params);
        return sys.isEmpty(list)?null:list.get(0);
    }

}
