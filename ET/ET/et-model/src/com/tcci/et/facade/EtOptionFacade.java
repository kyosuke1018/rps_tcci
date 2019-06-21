/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.entity.EtMember;
import com.tcci.et.entity.EtOption;
import com.tcci.et.enums.OptionEnum;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.model.OptionVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EtOptionFacade extends AbstractFacade<EtOption> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtOptionFacade() {
        super(EtOption.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtOption entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getReadonly()==null ){ entity.setReadonly(false); }
            if( entity.getSortnum()==null ){ entity.setSortnum(0); } 
            
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
    
    public void saveVO(OptionVO pubVO, TcUser operator, boolean simulated){
        EtOption entity = (pubVO.getId()!=null)?find(pubVO.getId()):new EtOption();
        this.copyVoToEntity(pubVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, pubVO);
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(EtOption entity, OptionVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(OptionVO vo, EtOption entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }
    
    /**
     * 查詢
     * @param criteriaVO
     * @return 
     */
    /*public List<OptionVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM ET_OPTION S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND DISABLED=0 \n");

        sql.append("AND S.TYPE=#TYPE \n");       
        params.put("TYPE", criteriaVO.getType());
        
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");       
            params.put("CODE", criteriaVO.getCode());
        }
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.CODE \n");
        }
        
        List<OptionVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OptionVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OptionVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(OptionVO.class, sql.toString(), params);
        }

        return list;
    }*/
    
    public List<OptionVO> findByType(String type){
        if( type==null ){
            logger.error("findByType error type="+type);
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(type);
        
        return findByCriteria(criteriaVO);
    }

    /**
     * 選單選項
     * @param storeId
     * @param type
     * @return 
     */
    public List<LongOptionVO> findByTypeOptions(String type, String opLang) {
        List<OptionVO> list = findByType(type);
        List<LongOptionVO> ops = new ArrayList<>();
        if( list!=null ){
            for(OptionVO vo : list){
                // use cname or ename by opLang (C/E)
                String label = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
                LongOptionVO op = new LongOptionVO(vo.getId(), label);
                ops.add(op);
            }
        }
        return ops;
    }
    public List<LongOptionVO> findByTypeOptions(String type, String opLang, Boolean includeCodeLabel) {
        List<OptionVO> list = findByType(type);
        List<LongOptionVO> ops = new ArrayList<>();
        if( list!=null ){
            for(OptionVO vo : list){
                // use cname or ename by opLang (C/E)
//                String label = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
                String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
                String label = "";
                if(includeCodeLabel){
                    label = StringUtils.isBlank(vo.getCode())?name:(vo.getCode() + "(" + name + ")");
                }else{
                    label = name;
                }
                LongOptionVO op = new LongOptionVO(vo.getId(), label);
                ops.add(op);
            }
        }
        return ops;
    }

    public boolean checkInput(EtOption entity, EtMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }

    /**
     *  for import check
     * @param storeId
     * @param type
     * @return 
     */
    public Map<String, Long> findForNameMap(String type) {
        List<OptionVO> list = findByType(type);
        Map<String, Long> map = new HashMap<String, Long>();
        if( list!=null ){
            for(OptionVO vo : list){
                if( vo.getCname()!=null ){
                    map.put(vo.getCname().trim().toUpperCase(), vo.getId());
                }
            }
        }
        return map;
    }
    
    /**
     * 特殊代碼查詢
     * @param storeId
     * @param optionEnum
     * @param code
     * @return 
     */
    public OptionVO findByCode(OptionEnum optionEnum, String code){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(optionEnum.getCode());
        criteriaVO.setCode(code);
        List<OptionVO> list = findByCriteria(criteriaVO);
        
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }
    
    //取得廠別對應地區(ET_OPTION area)
    public List<OptionVO> findArea(Long factoryId, String areaCode){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT eo.* \n");
        sql.append("FROM ET_OPTION eo \n");
        sql.append("INNER JOIN CM_FACTORYGROUP cfg on eo.code = cfg.code AND cfg.DISABLED = 0 \n");
        sql.append("INNER JOIN CM_FACTORY_GROUP_R cfgr on cfgr.FACTORYGROUP_ID = cfg.id \n");
        sql.append("INNER JOIN CM_FACTORY cf on cfgr.FACTORY_ID = cf.id \n");
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND eo.type = 'area' AND eo.DISABLED=0 \n");
        if(factoryId != null){
            sql.append(" AND cf.ID=#factoryId \n");
            params.put("factoryId", factoryId);
        }
        if (StringUtils.isNotBlank(areaCode)) {
            sql.append(" AND eo.code=#areaCode \n");
            params.put("areaCode", areaCode);
        }
        sql.append("order by eo.SORTNUM \n");
//        List<OptionVO> list = this.selectBySql(OptionVO.class, sql.toString(), params);

        return this.selectBySql(OptionVO.class, sql.toString(), params);
    }
    
    
    /**
     * 共用查詢SQL
     * @return 
     */
    public String genCommonFindSQL(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", CASE WHEN S.MODIFYTIME IS NULL THEN U1.CNAME ELSE U2.CNAME END LASTUSERNAME \n");
        sql.append(", CASE WHEN S.MODIFYTIME IS NULL THEN S.CREATETIME ELSE S.MODIFYTIME END LASTTIME \n");
        sql.append("FROM ET_OPTION S \n");
        sql.append("LEFT OUTER JOIN TC_USER U1 ON U1.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN TC_USER U2 ON U2.ID=S.MODIFIER \n");        
        sql.append("WHERE 1=1 \n");
        
        return sql.toString();
    }
    
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @return 
     */
    public List<OptionVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL());
        
        if( criteriaVO!=null ){
            // ID
            if( criteriaVO.getId()!=null ){
                sql.append("AND S.ID = #id \n");
                params.put("id", criteriaVO.getId());
            }else{
                // 非指定ID多筆查詢一率只顯示有效的
                sql.append("AND S.DISABLED = 0 \n");
            }
            // TYPE
            if( criteriaVO.getType()!=null ){
                sql.append("AND S.TYPE = #type \n");
                params.put("type", criteriaVO.getType());
            }
            // includeList
            if( criteriaVO.getIncludeList()!=null && !criteriaVO.getIncludeList().isEmpty() ){
                sql.append(NativeSQLUtils.getInSQL("S.ID", criteriaVO.getIncludeList(), params)).append(" \n");
            }
            // ExcludeList
            if( criteriaVO.getExcludeList()!=null && !criteriaVO.getExcludeList().isEmpty() ){
                sql.append(NativeSQLUtils.getNotInSQL("S.ID", criteriaVO.getExcludeList(), params)).append(" \n");
            }
            // 關鍵字
            String keyword = criteriaVO.getKeyword();
            if( keyword!=null && !keyword.isEmpty() ){
                keyword = "%" + keyword + "%";
                sql.append("AND (S.CNAME LIKE #keywork OR S.ENAME LIKE #keywork) \n");
                params.put("keywork", keyword);
            }
            // disabled
            if( criteriaVO.getDisabled()!=null ){
                sql.append("AND S.DISABLED = #DISABLED \n");
                params.put("DISABLED", criteriaVO.getDisabled());
            }
            
            // for import
            if( criteriaVO.getName()!=null ){
                sql.append("AND UPPER(S.CNAME) = #name \n");
                params.put("name", criteriaVO.getName().toUpperCase());
            }
        }
        
        // order by 
        if( criteriaVO!=null && criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.CNAME, S.ENAME");
        }
        
        logger.debug("findByCriteria ...");
        ResultSetHelper<OptionVO> resultSetHelper = new ResultSetHelper(OptionVO.class);
        List<OptionVO> resList;
        if( criteriaVO!=null && criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        }
        
        return resList;
    }
    public OptionVO findById(Long id){
        return findById(null, id);
    }
    public OptionVO findById(OptionEnum type, Long id){
        if( id==null || id<=0 ){
            logger.error("findById id==null || id<=0");
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setType((type!=null)?type.getCode():null);
        List<OptionVO> resList = findByCriteria(criteriaVO);

        return (resList!=null && resList.size()>0)?resList.get(0):null;
    }
    
    /**
     * 取得全部
     * @return 
     */
    /*public List<OptionVO> findAllItems(){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        List<OptionVO> resList = findByCriteria(criteriaVO);
        
        return resList;
    }*/
    
    /**
     * 取得指定類別
     * @param type
     * @return 
     */
    public List<OptionVO> findByTypeEum(OptionEnum type){
        return findByTypeEum(type, null);
    }
    public List<OptionVO> findByTypeEum(OptionEnum type, String orderBy){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(type.getCode());
        criteriaVO.setDisabled(false);
        criteriaVO.setOrderBy(orderBy);
        List<OptionVO> resList = findByCriteria(criteriaVO);
        
        return resList;
    }
    
    /**
     * 依名稱取得 (主要 for import)
     * @param type
     * @param name
     * @return 
     */
    public OptionVO findByName(OptionEnum type, String name){
        if( type==null || name==null || name.isEmpty() ){
            logger.error("findByName type==null || name==null ");
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(type.getCode());
        criteriaVO.setName(name);
        criteriaVO.setDisabled(false);
        List<OptionVO> resList = findByCriteria(criteriaVO);
        
        return (resList!=null && !resList.isEmpty())? resList.get(0):null;
    }
    
    /**
     * 與往來對象尚有關聯
     * @param vo
     * @return 
     */
    /*public boolean isRelToContacts(OptionVO vo){
        if( vo.getTypeEnum()==OptionEnum.CONTACTS ){
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder();

            String keyword = "%~"+vo.getId().toString()+"~%";
            
            sql.append("select count(A.ID) counts \n");
            sql.append("from KB_CONTACTS A \n");
            sql.append("where A.ORGTYPE like #KEYWORD");

            params.put("KEYWORD", keyword);
            
            return count(sql.toString(), params)>0;
        }
        
        return false;
    }*/
    
    
    /**
     *  取得特殊選項資訊 by Code
     * @param list
     * @param code
     * @return 
     */
    public OptionVO getSpecialOption(List<OptionVO> list, String code){
        if( list!=null ){
            for(OptionVO vo : list){
                if( code.equals(vo.getCode()) ){
                    return vo;
                }
            }
        }
        return null;
    }
    
    /**
     * 中文或英文名稱，皆不可與其他同類別項目相同
     * @param vo
     * @return 
     */
    public boolean existsSameName(OptionVO vo){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("select count(S.ID) counts \n");
        sql.append("from ET_OPTION S \n");
        sql.append("where 1=1 \n");
        sql.append("and S.DISABLED=0 \n");
        
        sql.append("and S.TYPE=#TYPE \n");
        params.put("TYPE", vo.getType());
        
        if( org.apache.commons.lang.StringUtils.isNotBlank(vo.getCname()) && org.apache.commons.lang.StringUtils.isNotBlank(vo.getEname()) ){
            sql.append("and (S.CNAME=#CNAME or S.ENAME=#ENAME) \n");
            params.put("CNAME", vo.getCname());
            params.put("ENAME", vo.getEname());
        }else if( org.apache.commons.lang.StringUtils.isNotBlank(vo.getCname()) ){
            sql.append("and S.CNAME=#CNAME \n");
            params.put("CNAME", vo.getCname());
        }else if( org.apache.commons.lang.StringUtils.isNotBlank(vo.getEname()) ){
            sql.append("and S.ENAME=#ENAME \n");
            params.put("CNAME", vo.getEname());
        }

        if( vo.getId()!=null ){
            sql.append("and S.ID<>#ID \n");
            params.put("ID", vo.getId());
        }
        
        return count(sql.toString(), params)>0;
    }
}
