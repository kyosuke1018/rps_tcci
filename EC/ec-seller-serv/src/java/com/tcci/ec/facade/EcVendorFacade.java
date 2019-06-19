/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.entity.EcVendor;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.VendorEnum;
import com.tcci.ec.model.criteria.VendorCriteriaVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.VendorVO;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcVendorFacade extends AbstractFacade<EcVendor> {
    @EJB EcCompanyFacade companyFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcVendorFacade() {
        super(EcVendor.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcVendor entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }

            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    public void saveVO(VendorVO vo, EcMember operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EcCompany company = (vo.getId()!=null)?companyFacade.find(vo.getId()):new EcCompany();
        EcVendor vendor = (vo.getVendorId()!=null)?this.find(vo.getVendorId()):new EcVendor();
        
        vendor.setCode(vo.getCode());
        vendor.setStoreId(vo.getStoreId());
        this.save(vendor, operator, simulated);
        
        ExtBeanUtils.copyProperties(company, vo);
        company.setType(CompanyTypeEnum.VENDOR.getCode());
        company.setMainId(vendor.getId());
        companyFacade.save(company, operator, simulated);
    }
    
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(VendorCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EC_VENDOR S \n");
        sql.append("LEFT OUTER JOIN EC_COMPANY C ON C.TYPE='V' AND C.MAIN_ID=S.ID \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");

        sql.append("AND S.STORE_ID=#STORE_ID \n");       
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");
            params.put("CODE", criteriaVO.getCode());
        }
        
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.CODE LIKE #KW OR C.CNAME LIKE #KW OR C.ENAME LIKE #KW \n");
            sql.append("OR C.ID_CODE LIKE #KW OR C.NICKNAME LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getTelKeyword()) ){
            String kw = "%" + criteriaVO.getTelKeyword().trim() + "%";
            sql.append("AND (C.TEL1 LIKE #TELKW OR C.TEL2 LIKE #TELKW OR C.TEL3 LIKE #TELKW) \n");
            params.put("TELKW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getEmailKeyword()) ){
            String kw = "%" + criteriaVO.getEmailKeyword().trim() + "%";
            sql.append("AND (C.EMAIL1 LIKE #EMAILKW OR C.EMAIL2 LIKE #EMAILKW OR C.EMAIL3 LIKE #EMAILKW) \n");
            params.put("EMAILKW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getAddrKeyword()) ){
            String kw = "%" + criteriaVO.getAddrKeyword().trim() + "%";
            sql.append("AND (C.ADDR1 LIKE #ADDRKW OR C.ADDR2 LIKE #ADDRKW) \n");
            params.put("ADDRKW", kw);
        }
        return sql.toString();
    }
    
    public int countByCriteria(VendorCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    /**
     * 依商品查詢
     * @param criteriaVO
     * @return 
     */
    public List<VendorVO> findByCriteria(VendorCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID VENDOR_ID, S.CODE, C.* \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CNAME");
        }
        
        List<VendorVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(VendorVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(VendorVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(VendorVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public List<VendorVO> findByStore(Long storeId){
        if( storeId==null ){
            logger.error("findByStore error storeId="+storeId);
            return null;
        }
        VendorCriteriaVO criteriaVO = new VendorCriteriaVO();
        criteriaVO.setStoreId(storeId);
        
        return findByCriteria(criteriaVO);
    }

    public VendorVO findById(Long storeId, Long id, boolean fullData) {
        if( storeId==null ){
            logger.error("findById error storeId="+storeId);
            return null;
        }
        VendorCriteriaVO criteriaVO = new VendorCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<VendorVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }

    public VendorVO findByCode(Long storeId, String code, boolean fullData) {
        if( storeId==null ){
            logger.error("findByCode error storeId="+storeId);
            return null;
        }
        VendorCriteriaVO criteriaVO = new VendorCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setCode(code);
        criteriaVO.setFullData(fullData);
        
        List<VendorVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }

    /**
     * 店家供應商選單 
     * @param storeId
     * @return 
     */
    public List<LongOptionVO> findVendorOptions(Long storeId, String opLang) {
        List<VendorVO> list = findByStore(storeId);
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        if( list!=null ){
            for(VendorVO vo : list){
                // use cname or ename by opLang (C/E)
                String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
                LongOptionVO op = new LongOptionVO(vo.getVendorId(), name);
                ops.add(op);
            }
        }
        return ops;
    }
    
    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(VendorVO vo, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, "vendorId", locale, errors);

        return pass;
    }

    /**
     *  for import check
     * @param storeId
     * @return 
     */
    public Map<String, Long> findForNameMap(Long storeId) {
        List<VendorVO> list = findByStore(storeId);
        Map<String, Long> map = new HashMap<String, Long>();
        if( list!=null ){
            for(VendorVO vo : list){
                if( vo.getCname()!=null ){
                    map.put(vo.getCname().trim().toUpperCase(), vo.getVendorId());
                }
            }
        }
        return map;
    }

    /**
     * 
     * @param storeId
     * @param item
     * @param sortnum
     * @param locale
     * @return 
     */
    public VendorVO genByVendorEnum(Long storeId, VendorEnum enumItem, int sortnum, Locale locale){
        VendorVO vo = null;
        if( enumItem!=null ){
            vo = new VendorVO();
            vo.setCode(enumItem.getCode());
            vo.setCname(enumItem.getDisplayName(locale));
            vo.setEname(enumItem.getEname());
            vo.setDisabled(Boolean.FALSE);
            vo.setStoreId(storeId);
        }else{
            logger.error("genByVendorEnum error enumItem = null");
        }
        return vo;
    }
}
