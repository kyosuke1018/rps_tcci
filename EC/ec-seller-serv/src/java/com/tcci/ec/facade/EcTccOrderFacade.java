/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcTccOrder;
import com.tcci.ec.model.TccOrderVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.e10.ContractE10VO;
import com.tcci.ec.model.e10.ContractProductVO;
import com.tcci.ec.model.e10.CustomerE10VO;
import com.tcci.ec.model.e10.DeliveryPlacesEC10VO;
import com.tcci.ec.model.e10.PlantE10VO;
import com.tcci.ec.model.e10.ProductE10VO;
import com.tcci.ec.model.e10.SalesAreaE10VO;
import com.tcci.ec.model.e10.SalesE10VO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.rs.StrOptionVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcTccOrderFacade extends AbstractFacade<EcTccOrder> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcTccOrderFacade() {
        super(EcTccOrder.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcTccOrder entity, EcMember operator, boolean simulated){
        logger.debug("save ...");
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                //entity.setModifier(operator);
                //entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                //entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    
    public void saveVO(TccOrderVO vo, EcMember operator, boolean simulated){
        logger.debug("saveVO ...");
        if( vo!=null ){
            EcTccOrder entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcTccOrder();
            // 需保存的系統產生欄位
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            logger.debug("saveVO entity.getQuantity() = "+entity.getQuantity());
            // DB 儲存
            save(entity, operator, simulated);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            //vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            //vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            //vo.setModifytime(entity.getModifytime());
        }
    }
    
    /**
     * 依條件查詢
     * @param criteriaVO
     * @return 
     */
    public List<TccOrderVO> findByCriteria(OrderCriteriaVO criteriaVO){
        logger.debug("findByCriteria ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM EC_TCC_ORDER S \n");
        sql.append("WHERE 1=1 \n");
        
        // ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        // SRC_ORDER_ID
        if( criteriaVO.getOrderId()!=null ){
            sql.append("AND S.SRC_ORDER_ID=#SRC_ORDER_ID \n");
            params.put("SRC_ORDER_ID", criteriaVO.getOrderId());
        }
        // TCC_ORDER_ID
        if( criteriaVO.getTccOrderId()!=null ){
            sql.append("AND S.TCC_ORDER_ID=#TCC_ORDER_ID \n");
            params.put("TCC_ORDER_ID", criteriaVO.getTccOrderId());
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIME");
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        List<TccOrderVO> list = this.selectBySql(TccOrderVO.class, sql.toString(), params);

        return list;
    }
    public TccOrderVO findTccOrderById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findTccOrderById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<TccOrderVO> list = findByCriteria(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    // 商品
    public List<ProductE10VO> findProduct(OrderCriteriaVO criteriaVO){
        logger.debug("findProduct ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_PRODUCT S \n");
        
        // CONTRACT_ID
        if( criteriaVO.getContractId()!=null ){
            sql.append("JOIN (\n");
            sql.append("    SELECT A.CONTRACT_ID, A.PLANT_ID, A.PRODUCT_ID FROM TCCSTORE_USER.EC_CONTRACT_PRODUCT A, TCCSTORE_USER.EC_CONTRACT B WHERE A.CONTRACT_ID=B.ID AND B.ACTIVE=1 \n");
            sql.append(") PP ON PP.PRODUCT_ID=S.ID AND PP.CONTRACT_ID=#CONTRACT_ID \n");

            params.put("CONTRACT_ID", criteriaVO.getContractId());
        }else{
            sql.append("JOIN TCCSTORE_USER.EC_PLANT_PRODUCT PP \n");
            sql.append("     ON PP.PRODUCT_ID=S.ID AND PP.ACTIVE=1 \n");
        }
        sql.append("WHERE S.ACTIVE=1 \n");

        // ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        // 代碼
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");
            params.put("CODE", criteriaVO.getCode());
        }
        // 出貨廠
        if( criteriaVO.getPlantId()!=null ){
            sql.append("AND PP.PLANT_ID=#PLANT_ID \n");
            params.put("PLANT_ID", criteriaVO.getPlantId());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        List<ProductE10VO> list = this.selectBySql(ProductE10VO.class, sql.toString(), params);
        return list;
        
    }
    public ProductE10VO findProductByCode(String code, Long plantId, Long contractId){
        if( code==null || !sys.isValidId(plantId) ){
            logger.error("findProductByCode code==null || !sys.isValidId(plantId)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setCode(code);
        criteriaVO.setPlantId(plantId);
        criteriaVO.setContractId(contractId);
        List<ProductE10VO> list = findProduct(criteriaVO);// 可能多筆
        
        return sys.isEmpty(list)?null:list.get(0);
    }
    public ProductE10VO findProductById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findProductById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<ProductE10VO> list = findProduct(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    // 下單客戶
    public List<CustomerE10VO> findCustomers(OrderCriteriaVO criteriaVO){
        logger.debug("findCustomers ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.*, MC.MEMBER_ID \n");
        sql.append("FROM TCCSTORE_USER.EC_CUSTOMER S \n");
        sql.append("JOIN TCCSTORE_USER.EC_MEMBER_CUSTOMER MC ON MC.CUSTOMER_ID=S.ID \n");
        sql.append("JOIN TCCSTORE_USER.EC_MEMBER M ON M.ID=MC.MEMBER_ID AND M.ACTIVE=1 \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        // ID
        if( sys.isValidId(criteriaVO.getId()) ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        // 會員帳號
        if( !sys.isBlank(criteriaVO.getLoginAccount()) ){
            sql.append("AND M.LOGIN_ACCOUNT=#LOGIN_ACCOUNT \n");
            params.put("LOGIN_ACCOUNT", criteriaVO.getLoginAccount());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.NAME");
        }

        List<CustomerE10VO> list = this.selectBySql(CustomerE10VO.class, sql.toString(), params);

        return list;
    }
    public List<LongOptionVO> findCustomerOptions(String loginAccount){
        logger.debug("findCustomerOptions ...");
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setLoginAccount(loginAccount);
        List<CustomerE10VO> list = findCustomers(criteriaVO);
        if( list!=null ){
            List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
            for(CustomerE10VO vo : list){
                ops.add(new LongOptionVO(vo.getId(), vo.getName()));
            }
            return ops;
        }
        return null;
    }
    public CustomerE10VO findCustomerById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findCustomerById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<CustomerE10VO> list = findCustomers(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    // 合約
    public List<ContractE10VO> findContracts(OrderCriteriaVO criteriaVO){
        logger.debug("findContracts ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_CONTRACT S \n");
        /* for DEBUG
        LEFT OUTER JOIN TCCSTORE_USER.EC_CONTRACT_PRODUCT CP ON CP.CONTRACT_ID=S.ID
        LEFT OUTER JOIN TCCSTORE_USER.EC_PRODUCT PD ON PD.ID=CP.PRODUCT_ID 
        LEFT OUTER JOIN TCCSTORE_USER.EC_CUSTOMER C ON C.ID=S.CUSTOMER_ID 
        */
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");
        sql.append("AND S.HIDE=0 \n");
        
        // 依指定客戶
        if( criteriaVO.getCustomerId()!=null ){
            sql.append("AND S.CUSTOMER_ID=#CUSTOMER_ID \n");
            params.put("CUSTOMER_ID", criteriaVO.getCustomerId());
        }
        // 依指定商品
        if( criteriaVO.getProductCode()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT * \n");
            sql.append("    FROM TCCSTORE_USER.EC_CONTRACT_PRODUCT CP \n");
            sql.append("    JOIN TCCSTORE_USER.EC_PRODUCT PD ON PD.ID=CP.PRODUCT_ID \n");
            sql.append("    JOIN TCCSTORE_USER.EC_PLANT PL ON PL.ID=CP.PLANT_ID \n");
            sql.append("    JOIN TCCSTORE_USER.EC_SALESAREA SA ON SA.ID=CP.SALESAREA_ID \n"); 
            sql.append("    WHERE 1=1 \n");
            sql.append("    AND PD.CODE=#PRD_CODE \n");
            sql.append("    AND CP.CONTRACT_ID=S.ID \n");
            sql.append(") \n");
            params.put("PRD_CODE", criteriaVO.getProductCode());
        }
        // 依指定 ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NAME");
        }

        List<ContractE10VO> list = this.selectBySql(ContractE10VO.class, sql.toString(), params);

        return list;

    }
    public List<LongOptionVO> findContractOptions(Long customerId, String prodCode){
        logger.debug("findContractOptions ...");
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setCustomerId(customerId);
        criteriaVO.setProductCode(prodCode);
        List<ContractE10VO> list = findContracts(criteriaVO);
        if( list!=null ){
            List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
            for(ContractE10VO vo : list){
                ops.add(new LongOptionVO(vo.getId(), vo.getName()));
            }
            return ops;
        }
        return null;
    }
    public ContractE10VO findContractById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findContractById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<ContractE10VO> list = findContracts(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    // 合約限制條件、項次
    public List<ContractProductVO> findContractProductInfos(OrderCriteriaVO criteriaVO){
        logger.debug("findContractProductInfo ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_CONTRACT_PRODUCT S \n");
        sql.append("WHERE 1=1 \n");
        
        // 依指定 CONTRACT_ID
        if( criteriaVO.getContractId()!=null ){
            sql.append("AND S.CONTRACT_ID=#CONTRACT_ID \n");
            params.put("CONTRACT_ID", criteriaVO.getContractId());
        }
        // 依指定 PRODUCT_ID
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRODUCT_ID=#PRODUCT_ID \n");
            params.put("PRODUCT_ID", criteriaVO.getPrdId());
        }
        // 依指定 PLANT_ID
        if( criteriaVO.getPlantId()!=null ){
            sql.append("AND S.PLANT_ID=#PLANT_ID \n");
            params.put("PLANT_ID", criteriaVO.getPlantId());
        }
        // 依指定 SALESAREA_ID
        if( criteriaVO.getSalesareaId()!=null ){
            sql.append("AND S.SALESAREA_ID=#SALESAREA_ID \n");
            params.put("SALESAREA_ID", criteriaVO.getSalesareaId());
        }
        // 依指定 METHOD
        if( criteriaVO.getShipMethod()!=null ){
            sql.append("AND S.METHOD=#METHOD \n");
            params.put("METHOD", criteriaVO.getShipMethod());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        List<ContractProductVO> list = this.selectBySql(ContractProductVO.class, sql.toString(), params);
        return list;
    }
    public ContractProductVO findContractProductInfo(long contractId,
           long productId, long plantId, long salesareaId, String method){
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setContractId(contractId);
        criteriaVO.setPrdId(productId);
        criteriaVO.setPlantId(plantId);
        criteriaVO.setSalesareaId(salesareaId);
        criteriaVO.setShipMethod(method);
        List<ContractProductVO> list = findContractProductInfos(criteriaVO);
        
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    // 廠別
    public List<PlantE10VO> findPlants(OrderCriteriaVO criteriaVO){
        logger.debug("findPlants ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_PLANT S \n");
        /* for DEBUG
        LEFT OUTER JOIN TCCSTORE_USER.EC_PLANT_SALESAREA PS ON PS.PLANT_ID=S.ID AND PS.PLANT_ID=25
        LEFT OUTER JOIN TCCSTORE_USER.EC_DELIVERY_PLACE DP ON DP.SALESAREA_ID=PS.SALESAREA_ID
        */
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        // 依合約
        if( criteriaVO.getContractId()!=null && criteriaVO.getProductCode()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT * \n");
            sql.append("    FROM TCCSTORE_USER.EC_CONTRACT_PRODUCT CP \n");
            sql.append("    JOIN TCCSTORE_USER.EC_PRODUCT PD ON PD.ID=CP.PRODUCT_ID \n");
            sql.append("    WHERE PD.ACTIVE=1 \n");
            
            sql.append("    AND CP.CONTRACT_ID=#CONTRACT_ID \n");
            params.put("CONTRACT_ID", criteriaVO.getContractId());
            
            if( criteriaVO.getProductCode()!=null ){
                sql.append("    AND PD.CODE=#PRD_CODE \n");
                params.put("PRD_CODE", criteriaVO.getProductCode());
            }
            
            sql.append("    AND CP.PLANT_ID=S.ID \n");
            sql.append(") \n");
        }else if( criteriaVO.getProductCode()!=null ){// 依商品 // Jimmy:合約採購不需看 EC_PLANT_PRODUCT
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT PP.PLANT_ID, PP.PRODUCT_ID \n");
            sql.append("    FROM TCCSTORE_USER.EC_PRODUCT P \n");
            
            // Jimmy:合約採購不需看 EC_PLANT_PRODUCT
            //sql.append("    JOIN TCCSTORE_USER.EC_PLANT_PRODUCT PP WHERE PP.ACTIVE=1 AND PP.PRODUCT_ID=P.ID \n");
            sql.append("    JOIN ( \n");
            sql.append("         SELECT PLANT_ID, PRODUCT_ID FROM TCCSTORE_USER.EC_PLANT_PRODUCT WHERE ACTIVE=1 \n");
            sql.append("         UNION \n");
            sql.append("         SELECT A.PLANT_ID, A.PRODUCT_ID FROM TCCSTORE_USER.EC_CONTRACT_PRODUCT A, TCCSTORE_USER.EC_CONTRACT B WHERE A.CONTRACT_ID=B.ID AND B.ACTIVE=1 \n");
            sql.append("    ) PP ON PP.PRODUCT_ID=P.ID \n");
            
            sql.append("    WHERE P.ACTIVE=1 \n");
            sql.append("    AND P.CODE=#PRD_CODE \n");
            sql.append("    AND PP.PLANT_ID=S.ID \n");
            
            sql.append(") \n");
            params.put("PRD_CODE", criteriaVO.getProductCode());
        }
        
        // 依銷售區域
        if( criteriaVO.getSalesareaId()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT *  \n");
            sql.append("    FROM TCCSTORE_USER.EC_PLANT_SALESAREA PS \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    AND PS.SALESAREA_ID=#SALESAREA_ID \n");
            sql.append("    AND PS.PLANT_ID=S.ID \n");
            sql.append(") \n");
            params.put("SALESAREA_ID", criteriaVO.getSalesareaId());
        }
        
        // 依指定 ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY CODE");
        }

        List<PlantE10VO> list = this.selectBySql(PlantE10VO.class, sql.toString(), params);

        return list;
    }
    public List<LongOptionVO> findPlantOptions(String prodCode, Long salesareaId, Long contractId){
        logger.debug("findPlantOptions ...");
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setSalesareaId(salesareaId);
        criteriaVO.setProductCode(prodCode);
        
        criteriaVO.setContractId(contractId);
        
        List<PlantE10VO> list = findPlants(criteriaVO);
        if( list!=null ){
            List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
            for(PlantE10VO vo : list){
                ops.add(new LongOptionVO(vo.getId(), vo.getName()));
            }
            return ops;
        }
        return null;
    }
    public PlantE10VO findPlantById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findPlantById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<PlantE10VO> list = findPlants(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    // 運送地點
    public List<DeliveryPlacesEC10VO> findDeliveryPlaces(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_DELIVERY_PLACE S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        if( sys.isValidId(criteriaVO.getId()) ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        
        if( StringUtils.isNotBlank(criteriaVO.getProvince()) ){
            sql.append("AND S.PROVINCE=#PROVINCE \n");
            params.put("PROVINCE", criteriaVO.getProvince());
        }
        if( StringUtils.isNotBlank(criteriaVO.getCity()) ){
            sql.append("AND S.CITY=#CITY \n");
            params.put("CITY", criteriaVO.getCity());
        }
        if( StringUtils.isNotBlank(criteriaVO.getDistrict()) ){
            sql.append("AND S.DISTRICT=#DISTRICT \n");
            params.put("DISTRICT", criteriaVO.getDistrict());
        }
        if( StringUtils.isNotBlank(criteriaVO.getTown()) ){
            sql.append("AND S.TOWN=#TOWN \n");
            params.put("TOWN", criteriaVO.getTown());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NAME");
        }

        List<DeliveryPlacesEC10VO> list = this.selectBySql(DeliveryPlacesEC10VO.class, sql.toString(), params);
        return list;
    }
    public DeliveryPlacesEC10VO findDeliveryPlacesByTown(String province, String city, String district, String town){
        if( province==null || city==null || district==null || town==null ){
            logger.error("findPlantById province==null || city==null || district==null || town==null");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setProvince(province);
        criteriaVO.setCity(city);
        criteriaVO.setDistrict(district);
        criteriaVO.setTown(town);
        List<DeliveryPlacesEC10VO> list = findDeliveryPlaces(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);// 正式區確認只有一筆
    }
    public DeliveryPlacesEC10VO findDeliveryPlaceById(Long id){
        if( id==null ){
            logger.error("findPlantById id==null");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<DeliveryPlacesEC10VO> list = findDeliveryPlaces(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }

    // 省
    public List<StrOptionVO> findProvinceOptions(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT S.PROVINCE VALUE, S.PROVINCE LABEL \n");
        sql.append("FROM TCCSTORE_USER.EC_DELIVERY_PLACE S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY LABEL");
        }

        List<StrOptionVO> list = this.selectBySql(StrOptionVO.class, sql.toString(), params);

        return list;
    }
    // 市
    public List<StrOptionVO> findCityOptions(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT S.CITY VALUE, S.CITY LABEL \n");
        sql.append("FROM TCCSTORE_USER.EC_DELIVERY_PLACE S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        if( StringUtils.isNotBlank(criteriaVO.getProvince()) ){
            sql.append("AND S.PROVINCE=#PROVINCE \n");
            params.put("PROVINCE", criteriaVO.getProvince());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY LABEL");
        }

        List<StrOptionVO> list = this.selectBySql(StrOptionVO.class, sql.toString(), params);

        return list;
    }
    // 區
    public List<StrOptionVO> findDistrictOptions(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT S.DISTRICT VALUE, S.DISTRICT LABEL \n");
        sql.append("FROM TCCSTORE_USER.EC_DELIVERY_PLACE S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        if( StringUtils.isNotBlank(criteriaVO.getProvince()) ){
            sql.append("AND S.PROVINCE=#PROVINCE \n");
            params.put("PROVINCE", criteriaVO.getProvince());
        }
        if( StringUtils.isNotBlank(criteriaVO.getCity()) ){
            sql.append("AND S.CITY=#CITY \n");
            params.put("CITY", criteriaVO.getCity());
        }
        if( params.isEmpty() ){// 不可無條件
            return null;
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY LABEL");
        }

        List<StrOptionVO> list = this.selectBySql(StrOptionVO.class, sql.toString(), params);

        return list;
    }
    // 鎮
    public List<StrOptionVO> findTownOptions(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT S.TOWN VALUE, S.TOWN LABEL \n");
        sql.append("FROM TCCSTORE_USER.EC_DELIVERY_PLACE S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        if( StringUtils.isNotBlank(criteriaVO.getProvince()) ){
            sql.append("AND S.PROVINCE=#PROVINCE \n");
            params.put("PROVINCE", criteriaVO.getProvince());
        }
        if( StringUtils.isNotBlank(criteriaVO.getCity()) ){
            sql.append("AND S.CITY=#CITY \n");
            params.put("CITY", criteriaVO.getCity());
        }
        if( StringUtils.isNotBlank(criteriaVO.getDistrict()) ){
            sql.append("AND S.DISTRICT=#DISTRICT \n");
            params.put("DISTRICT", criteriaVO.getDistrict());
        }
        
        if( params.isEmpty() ){// 不可無條件
            return null;
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY LABEL");
        }

        List<StrOptionVO> list = this.selectBySql(StrOptionVO.class, sql.toString(), params);

        return list;
    }
    
    // 依送貨地點找銷售區域
    public SalesAreaE10VO findSalesArea(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_SALESAREA S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");

        // 依指定 ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }

        if( criteriaVO.getProvince()!=null && criteriaVO.getCity()!=null 
         && criteriaVO.getDistrict()!=null && criteriaVO.getTown()!=null ){
            // 依送貨地點
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT * FROM TCCSTORE_USER.EC_DELIVERY_PLACE DP \n");
            sql.append("    WHERE DP.ACTIVE=1 \n");
            sql.append("    AND DP.PROVINCE=#PROVINCE \n");
            sql.append("    AND DP.CITY=#CITY \n");
            sql.append("    AND DP.DISTRICT=#DISTRICT \n");
            sql.append("    AND DP.TOWN=#TOWN \n");
            sql.append("    AND DP.SALESAREA_ID=S.ID \n");
            sql.append(") \n");
            
            params.put("PROVINCE", criteriaVO.getProvince());
            params.put("CITY", criteriaVO.getCity());
            params.put("DISTRICT", criteriaVO.getDistrict());
            params.put("TOWN", criteriaVO.getTown());
        }

        // 依合約
        if( criteriaVO.getContractId()!=null && criteriaVO.getProductCode()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT * \n");
            sql.append("    FROM TCCSTORE_USER.EC_CONTRACT_PRODUCT CP \n");
            sql.append("    JOIN TCCSTORE_USER.EC_PRODUCT PD ON PD.ID=CP.PRODUCT_ID \n");
            sql.append("    WHERE PD.ACTIVE=1 \n");
            
            sql.append("    AND CP.CONTRACT_ID=#CONTRACT_ID \n");
            params.put("CONTRACT_ID", criteriaVO.getContractId());
            
            if( criteriaVO.getProductCode()!=null ){
                sql.append("    AND PD.CODE=#PRD_CODE \n");
                params.put("PRD_CODE", criteriaVO.getProductCode());
            }
            
            sql.append("    AND CP.SALESAREA_ID=S.ID \n");
            sql.append(") \n");
        }
        
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        List<SalesAreaE10VO> list = this.selectBySql(SalesAreaE10VO.class, sql.toString(), params);
        
        return sys.isEmpty(list)?null:list.get(0);
    }
    public SalesAreaE10VO findSalesAreaById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findSalesAreaById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        return findSalesArea(criteriaVO);
    }

    // 業務
    public List<SalesE10VO> findSales(OrderCriteriaVO criteriaVO){
        logger.debug("findSalesByCustomer ... ");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
 
        sql.append("SELECT S.* \n");
        sql.append("FROM TCCSTORE_USER.EC_SALES S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.ACTIVE=1 \n");
        // 依指定 ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }

        // 依客戶
        if( criteriaVO.getCustomerId()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT * FROM TCCSTORE_USER.EC_CUSTOMER_SALES CS \n"); 
            sql.append("    WHERE CS.SALES_ID=S.ID \n"); 
            sql.append("    AND CS.CUSTOMER_ID=#CUSTOMER_ID \n");
            sql.append(") \n");
            params.put("CUSTOMER_ID", criteriaVO.getCustomerId());
        }
        
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        List<SalesE10VO> list = this.selectBySql(SalesE10VO.class, sql.toString(), params);
        
        return list;
    }
    public List<LongOptionVO> findSalesOptions(Long customerId){
        logger.debug("findSalesOptions ...");
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setCustomerId(customerId);
        List<SalesE10VO> list = findSales(criteriaVO);
        if( list!=null ){
            List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
            for(SalesE10VO vo : list){
                ops.add(new LongOptionVO(vo.getId(), vo.getName()));
            }
            return ops;
        }
        return null;
    }
    public SalesE10VO findSalesById(Long id){
        if( !sys.isValidId(id) ){
            logger.error("findSalesById !sys.isValidId(id)");
            return null;
        }
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setId(id);
        List<SalesE10VO> list = findSales(criteriaVO);
        return sys.isEmpty(list)?null:list.get(0);
    }

    // 可併單訂單 的 產品 及 運送地點 SQL
    public String getCanCombineInfoSQL(Long storeId, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PRODUCT_CODE, DELIVERY_ID, COUNT(*) CC \n"); 
        sql.append("FROM ( \n"); 
        sql.append("  SELECT S.*, P.CODE PRODUCT_CODE \n"); 
        sql.append("  FROM EC_ORDER S \n"); 
        sql.append("  JOIN EC_ORDER_DETAIL D ON D.ORDER_ID=S.ID \n"); 
        sql.append("  JOIN EC_PRODUCT P ON P.ID=D.PRODUCT_ID \n"); 
        sql.append("  WHERE 1=1 \n"); 
        sql.append("  AND S.STORE_ID=#STORE_ID \n"); 
        // 已核准
        sql.append("  AND S.STATUS = 'Approve' \n"); 
        // 未出貨
        sql.append("  AND S.SHIP_STATUS = 'A' \n"); 
        // 買家已確認
        sql.append("  AND S.BUYER_CHECK=1 \n"); 
        // 單一車號
        sql.append("  AND EXISTS ( \n"); 
        sql.append("      SELECT COUNT(*), ORDER_ID  \n"); 
        sql.append("      FROM EC_ORDER_CAR_INFO OC \n"); 
        sql.append("      GROUP BY ORDER_ID  \n"); 
        sql.append("      HAVING COUNT(*)=1 \n");
        sql.append("      AND OC.ORDER_ID=S.ID \n");
        sql.append("  ) \n");
        // 未轉單成功
        sql.append("  AND NOT EXISTS ( \n");
        sql.append("    SELECT * FROM EC_TCC_ORDER WHERE TCC_ORDER_ID>0 AND SRC_ORDER_ID=S.ID \n");
        sql.append("  ) \n");
        
        sql.append(") A \n"); 
        sql.append("GROUP BY PRODUCT_CODE, DELIVERY_ID \n"); 
        sql.append("HAVING COUNT(*)>1 \n");// 兩筆以上訂單
        
        params.put("STORE_ID", storeId);
        
        return sql.toString();
    }
    // 可併單訂單 的 產品 及 運送地點 選單
    public List<StrOptionVO> findCanCombineOptions(Long storeId){
        logger.debug("findCanCombineOptions ... ");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
 
        sql.append("SELECT PRD.CODE||'~'||TO_CHAR(DP.ID) VALUE, PRD.NAME||' - '||DP.NAME LABEL \n"); 
        sql.append("FROM ( \n"); 
        sql.append(getCanCombineInfoSQL(storeId, params));
        sql.append(") M \n"); 
        // 產品
        sql.append("JOIN (SELECT DISTINCT CODE, NAME FROM TCCSTORE_USER.EC_PRODUCT WHERE ACTIVE=1) PRD ON PRD.CODE=M.PRODUCT_CODE \n"); 
        // 送達地點
        sql.append("JOIN TCCSTORE_USER.EC_DELIVERY_PLACE DP ON DP.ACTIVE=1 AND DP.ID=DELIVERY_ID \n");
        
        if( params.isEmpty() ){// 不可無條件
            return null;
        }
        
        List<StrOptionVO> list = this.selectBySql(StrOptionVO.class, sql.toString(), params);        
        return list;
    }
    
}
