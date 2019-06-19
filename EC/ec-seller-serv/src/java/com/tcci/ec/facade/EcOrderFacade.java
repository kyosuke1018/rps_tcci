package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderDetail;
import com.tcci.ec.enums.CreditLogEnum;
import com.tcci.ec.enums.OrderLogEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayMethodEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.model.OrderCarInfoVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.OrderDetailVO;
import com.tcci.ec.model.OrderLogVO;
import com.tcci.ec.model.OrderMessageVO;
import com.tcci.ec.model.OrderProcessVO;
import com.tcci.ec.model.OrderVO;
import com.tcci.ec.model.TccOrderVO;
import com.tcci.ec.model.rs.OrderQuoteVO;
import com.tcci.ec.model.statistic.StatisticOrderStatusVO;
import com.tcci.ec.model.statistic.StatisticOrderVO;
import com.tcci.ec.model.statistic.StatisticPrdVO;
import com.tcci.ec.model.statistic.StatisticRfqVO;
import com.tcci.ec.model.statistic.StatisticCusVO;
import com.tcci.ec.model.statistic.StatisticVO;
import com.tcci.ec.model.statistic.TimeSeriesDataVO;
import com.tcci.fc.util.CollectionUtils;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
public class EcOrderFacade extends AbstractFacade<EcOrder> {
    @EJB EcMemberFacade memberFacade;
    @EJB EcOrderDetailFacade orderDetailFacade;
    @EJB EcOrderProcessFacade orderProcessFacade;
    @EJB EcOrderMessageFacade orderMessageFacade;
    @EJB EcOrderLogFacade orderLogFacade;
    @EJB EcStockLogFacade stockLogFacade;
    @EJB EcCustomerFacade customerFacade;
    @EJB EcOrderCarInfoFacade orderCarInfoFacade;
    @EJB EcTccOrderFacade tccOrderFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public EcOrderFacade() {
        super(EcOrder.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcOrder entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getCurrencyId()==null ){ entity.setCurrencyId(GlobalConstant.DEF_CURRENCY_ID); }

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
    public void saveVO(OrderVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcOrder entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcOrder();
            // 需保存的系統產生欄位
            //vo.setCreator(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
            save(entity, operator, simulated);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            vo.setModifytime(entity.getModifytime());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Statistic OLD">
    /**
     * 訂單狀態統計 (在途PO)
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticOrderStatusVO> findGroupByStatus(OrderCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.STATUS, S.SHIP_STATUS, S.PAY_STATUS, COUNT(S.ID) VALUE \n"); 
        sql.append("FROM EC_ORDER S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getWaitingListStr()).append(") \n");

        sql.append("GROUP BY S.STATUS, S.SHIP_STATUS, S.PAY_STATUS");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        List<StatisticOrderStatusVO> list = this.selectBySql(StatisticOrderStatusVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticOrderStatusVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }
 
    /**
     *  累計銷售金額
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticOrderVO> findSumByPeriod(OrderCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * \n");
        sql.append("FROM ( \n");
        sql.append("  SELECT 1 SORTNUM, 'D' PERIOD, NVL(SUM(NVL(S.TOTAL, 0)), 0) VALUE FROM EC_ORDER S  \n");
        sql.append("  WHERE 1=1 AND S.STORE_ID=#STORE_ID  \n");
        sql.append("  AND S.APPROVETIME IS NOT NULL \n");
        sql.append("  AND trunc(S.APPROVETIME)=#KEYDATE \n");
        // 週
        sql.append("  union \n");
        sql.append("  SELECT 2 SORTNUM, 'W' PERIOD, NVL(SUM(NVL(S.TOTAL, 0)), 0) VALUE FROM EC_ORDER S  \n");
        sql.append("  WHERE 1=1 AND S.STORE_ID=#STORE_ID  \n");
        sql.append("  AND S.APPROVETIME IS NOT NULL \n");
        //sql.append("  AND trunc(S.APPROVETIME)>=#KEYDATE AND trunc(S.APPROVETIME)<= Next_day(#KEYDATE, 7) \n");
        sql.append("  AND to_char(S.APPROVETIME,'yyyyMMdd')>=to_char(trunc(#KEYDATE, 'D'), 'yyyyMMdd') \n");
        sql.append("  AND to_char(S.APPROVETIME,'yyyyMMdd')<=to_char(trunc(#KEYDATE, 'D')+6, 'yyyyMMdd') \n");
        // 月
        sql.append("  union \n");
        sql.append("  SELECT 3 SORTNUM, 'M' PERIOD, NVL(SUM(NVL(S.TOTAL, 0)), 0) VALUE FROM EC_ORDER S  \n");
        sql.append("  WHERE 1=1 AND S.STORE_ID=#STORE_ID  \n");
        sql.append("  AND S.APPROVETIME IS NOT NULL \n");
        sql.append("  AND to_char(S.APPROVETIME,'yyyymm')=to_char(#KEYDATE,'yyyymm') \n");
        // 季
        sql.append("  union \n");
        sql.append("  SELECT 4 SORTNUM, 'Q' PERIOD, NVL(SUM(NVL(S.TOTAL, 0)), 0) VALUE FROM EC_ORDER S  \n");
        sql.append("  WHERE 1=1 AND S.STORE_ID=#STORE_ID  \n");
        sql.append("  AND S.APPROVETIME IS NOT NULL \n");
        sql.append("  AND to_char(S.APPROVETIME,'yyyyq')=to_char(#KEYDATE,'yyyyq') \n");
        // 年
        sql.append("  union \n");
        sql.append("  SELECT 5 SORTNUM, 'Y' PERIOD, NVL(SUM(NVL(S.TOTAL, 0)), 0) VALUE FROM EC_ORDER S  \n");
        sql.append("  WHERE 1=1 AND S.STORE_ID=#STORE_ID  \n");
        sql.append("  AND S.APPROVETIME IS NOT NULL \n");
        sql.append("  AND to_char(S.APPROVETIME,'yyyy')=to_char(#KEYDATE,'yyyy') \n");
        sql.append(") S ");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        Date keyDate = (criteriaVO.getEndAt()!=null)?criteriaVO.getEndAt():new Date();
        keyDate = DateUtils.truncate(keyDate, Calendar.DATE);
        params.put("KEYDATE", keyDate);
        
        List<StatisticOrderVO> list = this.selectBySql(StatisticOrderVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticOrderVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }
    
    /**
     * 訂單狀態統計
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticRfqVO> findGroupByRfqStatus(OrderCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.STATUS, COUNT(S.ID) VALUE \n"); 
        sql.append("FROM EC_ORDER S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append(NativeSQLUtils.getInSQL("S.STATUS", RfqStatusEnum.getCodes(), params));
        sql.append("GROUP BY S.STATUS");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        List<StatisticRfqVO> list = this.selectBySql(StatisticRfqVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticRfqVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Statistic">
    /**
     * 商品銷售量統計
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticPrdVO> findGroupByPrdSales(OrderCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupByPrdSales ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT P.ID, P.CNAME, P.ENAME, SUM(D.QUANTITY) VALUE \n"); 
        sql.append("FROM EC_ORDER S \n"); 
        sql.append("JOIN EC_ORDER_DETAIL D ON D.ORDER_ID=S.ID \n"); 
        sql.append("JOIN EC_PRODUCT P ON P.ID=D.PRODUCT_ID \n"); 
        sql.append("WHERE 1=1  \n"); 
        sql.append("AND S.STORE_ID=#STORE_ID \n"); 

        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getCanCountListStr()).append(") \n"); 

        sql.append("AND to_char(S.APPROVETIME,'yyyymm')=to_char(#END_AT,'yyyymm') \n"); 
        sql.append("GROUP BY P.ID, P.CNAME, P.ENAME \n"); 
        sql.append("ORDER BY VALUE DESC \n"); 
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("END_AT", criteriaVO.getEndAt());
        
        List<StatisticPrdVO> list = this.selectBySql(StatisticPrdVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticPrdVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }

    /**
     * 商品未出貨統計
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticPrdVO> findGroupByPrdUndelivered(OrderCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupByPrdUndelivered ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT P.ID, P.CNAME, P.ENAME, SUM(D.QUANTITY) VALUE \n"); 
        sql.append("FROM EC_ORDER S \n"); 
        sql.append("JOIN EC_ORDER_DETAIL D ON D.ORDER_ID=S.ID \n"); 
        sql.append("JOIN EC_PRODUCT P ON P.ID=D.PRODUCT_ID \n"); 
        sql.append("WHERE 1=1 \n"); 
        sql.append("AND S.STORE_ID=#STORE_ID \n"); 

        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getWaitingListStr()).append(") \n"); 

        sql.append("AND S.SHIP_STATUS=#SHIP_STATUS \n"); 
        sql.append("AND to_char(S.APPROVETIME,'yyyymm')=to_char(#END_AT,'yyyymm') \n"); 
        sql.append("GROUP BY P.ID, P.CNAME, P.ENAME \n"); 
        sql.append("ORDER BY VALUE DESC \n"); 
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("SHIP_STATUS", ShipStatusEnum.NOT_SHIPPED.getCode());
        params.put("END_AT", criteriaVO.getEndAt());

        List<StatisticPrdVO> list = this.selectBySql(StatisticPrdVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticPrdVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }    

    /**
     * 逾期金額統計
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticCusVO> findGroupByCusOrderDue(OrderCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupByCusOrderDue ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT M.ID, M.LOGIN_ACCOUNT, M.NAME, SUM(S.TOTAL) VALUE \n"); 
        sql.append("FROM EC_ORDER S \n"); 
        sql.append("JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n"); 
        sql.append("WHERE 1=1 \n");  
        sql.append("AND S.STORE_ID=#STORE_ID \n"); 

        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getWaitingListStr()).append(") \n"); 

        sql.append("AND S.SHIP_STATUS IN (#SHIP_STATUS1, #SHIP_STATUS2) \n"); 
        sql.append("AND to_char(S.APPROVETIME,'yyyymm')=to_char(#END_AT,'yyyymm') \n"); 
        sql.append("GROUP BY M.ID, M.Login_Account, M.NAME \n"); 
        sql.append("ORDER BY VALUE DESC"); 
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("SHIP_STATUS1", ShipStatusEnum.SHIPPED.getCode());// 已出貨
        params.put("SHIP_STATUS2", ShipStatusEnum.ARRIVED.getCode());// 已到貨
        params.put("END_AT", criteriaVO.getEndAt());

        List<StatisticCusVO> list = this.selectBySql(StatisticCusVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticCusVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }
    
    /**
     * 銷售分析 by 客戶 (當期比較：當月 & 去年同月)
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticCusVO> findGroupBySalesCus(OrderCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupBySalesCus ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT M.ID, M.LOGIN_ACCOUNT, M.NAME \n"); 
        sql.append(", SUM(CASE WHEN to_char(S.APPROVETIME,'yyyymm')=to_char(#END_AT,'yyyymm') THEN S.TOTAL ELSE 0 END) VALUE \n"); 
        sql.append(", SUM(CASE WHEN to_char(S.APPROVETIME,'yyyymm')=to_char(ADD_MONTHS(#END_AT, -12),'yyyymm') THEN S.TOTAL ELSE 0 END) VALUE2 \n"); 
        sql.append("FROM EC_ORDER S \n");
        sql.append("JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n"); 
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n"); 

        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getCanCountListStr()).append(") \n"); 

        sql.append("AND to_char(S.APPROVETIME,'yyyymm') IN (to_char(#END_AT,'yyyymm'), to_char(ADD_MONTHS(#END_AT, -12),'yyyymm')) \n"); 
        sql.append("GROUP BY M.ID, M.Login_Account, M.NAME \n"); 
        sql.append("ORDER BY VALUE DESC"); 
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("END_AT", criteriaVO.getEndAt());

        List<StatisticCusVO> list = this.selectBySql(StatisticCusVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticCusVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }    

    /**
     * 銷售分析 by 商品 (當期比較：當月 & 去年同月)
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticPrdVO> findGroupBySalesPrd(OrderCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupBySalesPrd ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT P.ID, P.CNAME, P.ENAME \n"); 
        sql.append(", SUM(CASE WHEN to_char(S.APPROVETIME,'yyyymm')=to_char(#END_AT,'yyyymm') THEN S.TOTAL ELSE 0 END) VALUE \n"); 
        sql.append(", SUM(CASE WHEN to_char(S.APPROVETIME,'yyyymm')=to_char(ADD_MONTHS(#END_AT, -12),'yyyymm') THEN S.TOTAL ELSE 0 END) VALUE2 \n"); 
        sql.append("FROM EC_ORDER S \n"); 
        sql.append("JOIN EC_ORDER_DETAIL D ON D.ORDER_ID=S.ID \n"); 
        sql.append("JOIN EC_PRODUCT P ON P.ID=D.PRODUCT_ID \n"); 
        sql.append("WHERE 1=1 \n"); 
        sql.append("AND S.STORE_ID=#STORE_ID \n"); 
        
        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getCanCountListStr()).append(") \n"); 

        sql.append("AND to_char(S.APPROVETIME,'yyyymm') IN (to_char(#END_AT,'yyyymm'), to_char(ADD_MONTHS(#END_AT, -12),'yyyymm')) \n"); 
        sql.append("GROUP BY P.ID, P.CNAME, P.ENAME \n"); 
        sql.append("ORDER BY VALUE DESC");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("END_AT", criteriaVO.getEndAt());

        List<StatisticPrdVO> list = this.selectBySql(StatisticPrdVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticPrdVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }    
    
    /**
     * 銷售分析 by 市場 (與客戶比較)
     * 
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public TimeSeriesDataVO findGroupByMarket(OrderCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupByMarket storeId = "+criteriaVO.getStoreId());
        if( criteriaVO.getEndAt()==null || criteriaVO.getStoreId()==null ){
            logger.error("findGroupByMarket criteriaVO.getEndAt()==null || criteriaVO.getStoreId()==null");
            return null;
        }
        Long storeId = criteriaVO.getStoreId();
        // 取得 Time Series
        Calendar endAt = DateUtils.toCalendar(criteriaVO.getEndAt());
        List<String> timeSeries = DateUtils.genYearMonthList(endAt, 6, false);// yyyy/MM
        logger.debug("findGroupByMarket timeSeries = "+(timeSeries!=null?timeSeries.size():0));
        
        List<String> datasetLabels = new ArrayList<String>();
        // 取得所有客戶總計 (代表 Market)
        datasetLabels.add(ResourceBundleUtils.getMessage(locale, "msg.market.total"));
        List<BigDecimal> totalList = findTimeSeriesTotalSales(storeId, timeSeries);
        logger.debug("findGroupByMarket totalList = "+(totalList!=null?totalList.size():0));
        // 取得個別客戶統計
        List<List<BigDecimal>> cusSalesList = findTimeSeriesCusSales(storeId, timeSeries, datasetLabels);
        logger.debug("findGroupByMarket cusSalesList = "+(cusSalesList!=null?cusSalesList.size():0));

        // 彙總
        TimeSeriesDataVO resVO = new TimeSeriesDataVO();
        resVO.setTimeSeriesLabels(timeSeries);
        resVO.setDatasetLabels(datasetLabels);
        
        List<List<BigDecimal>> datasets = new ArrayList<List<BigDecimal>>();
        datasets.add(totalList);
        datasets.addAll(cusSalesList);
        resVO.setDatasets(datasets);
        
        resVO.setNodata(sys.isEmpty(cusSalesList));// for 方便前端判斷
        
        return resVO;
    }
    /**
     * 時間序列 銷售總計
     * @param storeId
     * @param timeSeries
     * @return 
     */
    public List<BigDecimal> findTimeSeriesTotalSales(Long storeId, List<String> timeSeries){
        logger.debug("findGroupByMarket storeId = "+storeId);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setStartYM(timeSeries.get(0));
        criteriaVO.setEndYM(timeSeries.get(timeSeries.size()-1));
        
        sql.append("SELECT to_char(S.APPROVETIME,'yyyy/mm') LABEL, SUM(S.TOTAL) VALUE \n");
        sql.append(getCommonByMarketSql(criteriaVO, params));
        sql.append("GROUP BY to_char(S.APPROVETIME,'yyyy/mm') \n");
        sql.append("ORDER BY LABEL \n");
        
        List<StatisticVO> list = this.selectBySql(StatisticVO.class, sql.toString(), params);
        
        List<BigDecimal> resList = new ArrayList<BigDecimal>();
        for(String ym : timeSeries){
            BigDecimal val = BigDecimal.ZERO;
            if( list!=null ){
                for(StatisticVO vo : list){
                    if( ym.equals(vo.getLabel()) ){
                        val = vo.getValue();
                        break;
                    }
                }
            }
            resList.add(val);
        }
        
        return resList;
    }
    /**
     * 時間序列 個別客戶銷售統計
     * @param storeId
     * @param timeSeries
     * @param datasetLabels
     * @return 
     */
    public List<List<BigDecimal>> findTimeSeriesCusSales(Long storeId, List<String> timeSeries, List<String> datasetLabels){
        logger.debug("findTimeSeriesCusSales storeId = "+storeId);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setStartYM(timeSeries.get(0));
        criteriaVO.setEndYM(timeSeries.get(timeSeries.size()-1));

        sql.append("SELECT M.ID, M.Login_Account, M.NAME, to_char(S.APPROVETIME,'yyyy/mm') LABEL, SUM(S.TOTAL) VALUE \n");
        sql.append(getCommonByMarketSql(criteriaVO, params));
        sql.append("GROUP BY M.ID, M.Login_Account, M.NAME, to_char(S.APPROVETIME,'yyyy/mm') \n");
        sql.append("ORDER BY ID, NAME, LABEL \n");
        
        List<StatisticCusVO> list = this.selectBySql(StatisticCusVO.class, sql.toString(), params);
        
        // 有那些客戶
        List<Long> cusIds = new ArrayList<Long>();
        if( list!=null ){
            for(StatisticCusVO vo : list){
                if( !cusIds.contains(vo.getId()) ){
                    cusIds.add(vo.getId());
                    datasetLabels.add(vo.getName());
                }
            }
        }
        
        List<List<BigDecimal>> cusSalesList = new ArrayList<List<BigDecimal>>();
        
        for(Long id : cusIds){// by customer
            logger.debug("findTimeSeriesCusSales id = "+id);
            List<BigDecimal> salesList = new ArrayList<BigDecimal>();
            for(String ym : timeSeries){// by ym
                BigDecimal val = BigDecimal.ZERO;
                if( list!=null ){
                    for(StatisticCusVO vo : list){
                        //logger.debug("findTimeSeriesCusSales id="+id+", ym="+ym+", vo.getId()="+vo.getId()+", vo.getLabel()="+vo.getLabel()+", vo.getValue()="+vo.getValue());
                        if( id.equals(vo.getId()) ){// the customer
                            if( ym.equals(vo.getLabel()) ){// the ym
                                val = vo.getValue();
                                logger.debug("findTimeSeriesCusSales id="+id+", ym="+ym+", vo.getId()="+vo.getId()+", vo.getLabel()="+vo.getLabel()+", vo.getValue()="+vo.getValue());
                                break;
                            }
                        }
                    }
                }
                salesList.add(val);
            }
            cusSalesList.add(salesList);
        }
        
        return cusSalesList;
    }
    public String getCommonByMarketSql(OrderCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EC_ORDER S \n");
        sql.append("JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n");
        sql.append("WHERE 1=1 \n"); 
        sql.append("AND S.STORE_ID=#STORE_ID \n");

        sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getCanCountListStr()).append(") \n");

        sql.append("AND to_char(S.APPROVETIME,'yyyy/mm')>=#START_YM \n");
        sql.append("AND to_char(S.APPROVETIME,'yyyy/mm')<=#END_YM \n");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("END_YM", criteriaVO.getEndYM());
        params.put("START_YM", criteriaVO.getStartYM());
        
        return sql.toString();
    }
    //</editor-fold>
    
    /**
     * 產生併單完整查詢條件
     * @param criteriaVO
     * @return 
     */
    public boolean genCombineEC10Criteria(OrderCriteriaVO criteriaVO){
            criteriaVO.setStatus(OrderStatusEnum.Approve.getCode());
            criteriaVO.setShipStatus(ShipStatusEnum.NOT_SHIPPED.getCode());
            criteriaVO.setBuyerCheck(Boolean.TRUE);
            criteriaVO.setTranToEC10(Boolean.FALSE);// 未轉單成功
            
            try{
                String keys = criteriaVO.getCombineKeys();
                String[] keyAry = keys.split("~");
                String productCode = keyAry[0];
                Long deliveryPlaceId = Long.parseLong(keyAry[1]);

                criteriaVO.setProductCode(productCode);
                criteriaVO.setDeliveryPlaceId(deliveryPlaceId);
                return true;
            }catch(Exception e){
                logger.error("findByCriteria Exception :\n", e.toString());
            }
            return false;
    }
    
    /**
     * 依條件查詢
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<OrderVO> findByCriteria(OrderCriteriaVO criteriaVO, Locale locale, boolean tccDealer){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        // 併單
        if( sys.isTrue(criteriaVO.getForCombine()) ){
            if( !genCombineEC10Criteria(criteriaVO) ){
                return null;
            }
        }

        sql.append("SELECT S.* \n");
        sql.append(", CUS.ID CUSTOMER_ID \n");
        sql.append(", M.MEMBER_ID, M.LOGIN_ACCOUNT, M.CNAME, M.ENAME \n");
        sql.append(", M.EMAIL1, M.EMAIL2, M.TEL1, M.TEL2 \n");
        sql.append(", CUR.CODE CURRENCY, CUR.NAME CUR_NAME \n");
        sql.append(", SH.SHIPPING, SH.SHIPPING_ID, SH.RECIPIENT, SH.ADDRESS, SH.PHONE \n");
        sql.append(", SH.CAR_NO, SH.DRIVER, SH.SHIPPING_CODE, SH.PATROL_LATITUDE, SH.PATROL_LONGITUDE \n");
        sql.append(", PAY.TITLE PAYMENT, PAY.CODE PAY_CODE, PAY.TYPE PAY_TYPE \n");
        sql.append(", D.ITEM_COUNT \n");
        sql.append(", RA.CUSTOMER_RATE, RA.CUSTOMER_MESSAGE, RA.SELLER_RATE, RA.SELLER_MESSAGE \n");
        // for EC1.0
        if( tccDealer ){
            // 只有單筆 DETAIL
            sql.append(", OD.QUANTITY \n");
            sql.append(", PD.ID PRODUCT_ID, PD.CNAME PRODUCT_NAME, PD.CODE PRODUCT_CODE \n");
            // 送達地點
            sql.append(", DP10.CODE DELIVERY_PLACE_CODE, DP10.NAME DELIVERY_PLACE_NAME, DP10.PROVINCE, DP10.CITY, DP10.DISTRICT, DP10.TOWN \n");
            // 銷售區域
            sql.append(", SA10.CODE SALESAREA_CODE, SA10.NAME SALESAREA_NAME \n");
            // 轉 EC1.0 成功紀錄 (TCC_ORDER_ID>0)
            // 拆單時有多筆 sql.append(", TO10.TCC_ORDER_ID \n");
        }
        sql.append(findByCriteriaSQL(criteriaVO, params, tccDealer));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.ORDER_NUMBER DESC");
        }
        
        List<OrderVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OrderVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OrderVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(OrderVO.class, sql.toString(), params);
        }
        if( list!=null ){
            for(OrderVO vo : list){
                vo.genStatusLabel(locale);
                vo.genPayStatusLabel(locale);
                vo.genShipStatusLabel(locale);
            }
        }
        return list;
    }
    public Double sumByCriteria(OrderCriteriaVO criteriaVO, String colname){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT NVL(SUM(NVL(").append(colname).append(", 0)),0) TOTAL \n");
        sql.append(findByCriteriaSQL(criteriaVO, params, false));
        
        return this.sum(sql.toString(), params);
    }
    public int countByCriteria(OrderCriteriaVO criteriaVO, Boolean tccDealer){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        // 併單
        if( sys.isTrue(criteriaVO.getForCombine()) ){
            if( !genCombineEC10Criteria(criteriaVO) ){
                return 0;
            }
        }
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params, tccDealer));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(OrderCriteriaVO criteriaVO, Map<String, Object> params, boolean tccDealer){
        StringBuilder sql = new StringBuilder();
                
        sql.append("FROM EC_ORDER S \n");// 訂單主檔
        sql.append("LEFT OUTER JOIN EC_CUSTOMER CUS ON CUS.STORE_ID=S.STORE_ID AND CUS.MEMBER_ID=S.MEMBER_ID \n");// 買家資訊
        sql.append("LEFT OUTER JOIN ( \n");// 買家資訊
        sql.append("    SELECT M.ID MEMBER_ID, M.LOGIN_ACCOUNT, M.NAME, M.EMAIL, M.PHONE, M.ACTIVE, M.ADMIN_USER \n"); 
        sql.append("    , D.CNAME, D.ENAME, D.ID_CODE, D.ID_TYPE, D.BRIEF, D.NICKNAME \n");
        sql.append("    , D.EMAIL1, D.EMAIL2, D.TEL1, D.TEL2, D.TEL3, D.FAX1, D.COUNTRY, D.STATE, D.ADDR1, D.ADDR2 \n");
        sql.append("    , D.MEM_TYPE \n");
        sql.append("    FROM EC_MEMBER M \n");
        // 買家資訊
        sql.append("    LEFT OUTER JOIN ( \n");
        sql.append(memberFacade.getMemberDetailInfoSQL());// 會員詳細資訊
        sql.append("    ) D ON D.TYPE='M' AND D.MAIN_ID=M.ID AND D.MEM_TYPE=M.TYPE \n"); 
        sql.append(") M ON M.MEMBER_ID=S.MEMBER_ID \n");
        
        sql.append("LEFT OUTER JOIN EC_CURRENCY CUR ON CUR.ID=S.CURRENCY_ID \n");// 幣別
        
        //sql.append("LEFT OUTER JOIN EC_SHIPPING SH ON SH.ID=S.SHIPPING_ID \n");// 運送方式
        sql.append("LEFT OUTER JOIN ( \n");// 運送方式
        sql.append("     SELECT I.*, SP.TITLE SHIPPING \n"); 
        sql.append("     FROM EC_ORDER_SHIP_INFO I \n"); 
        sql.append("     LEFT OUTER JOIN EC_SHIPPING SP ON SP.ID=I.SHIPPING_ID \n"); 
        sql.append(") SH ON SH.ORDER_ID=S.ID \n"); 
        
        sql.append("LEFT OUTER JOIN EC_PAYMENT PAY ON PAY.ID=S.PAYMENT_ID \n");// 付款方式
        
        sql.append("LEFT OUTER JOIN ( \n");// 商品筆數
        sql.append("     SELECT ORDER_ID, COUNT(*) ITEM_COUNT \n");
        sql.append("     FROM EC_ORDER_DETAIL \n");
        sql.append("     WHERE 1=1 \n");
        // 特定商家
        if( criteriaVO.getStoreId()!=null ){
            sql.append("     AND STORE_ID=#STORE_ID \n");
        }
        sql.append("     GROUP BY ORDER_ID \n");
        sql.append(") D ON D.ORDER_ID=S.ID \n");
        
        sql.append("LEFT OUTER JOIN EC_ORDER_RATE RA ON RA.ORDER_ID=S.ID \n");// 賣家評價
        
        // for EC1.0
        if( GlobalConstant.TCC_DEALER_ENABLED ){
            sql.append("LEFT OUTER JOIN EC_ORDER_DETAIL OD ON OD.ORDER_ID=S.ID \n");// 只有單筆 DETAIL
            sql.append("LEFT OUTER JOIN EC_PRODUCT PD ON PD.ID=OD.PRODUCT_ID \n");
            
            sql.append("LEFT OUTER JOIN TCCSTORE_USER.EC_DELIVERY_PLACE DP10 ON DP10.ID=S.DELIVERY_ID \n");// 送達地點
            sql.append("LEFT OUTER JOIN TCCSTORE_USER.EC_SALESAREA SA10 ON SA10.ID=S.SALESAREA_ID \n");// 銷售區域
            // 轉 EC1.0 成功紀錄 (TCC_ORDER_ID>0)
            // 拆單時有多筆 sql.append("LEFT OUTER JOIN TCCSTORE_USER.EC_TCC_ORDER TO10 ON TO10.SRC_ORDER_ID=S.ID AND TO10.TCC_ORDER_ID>0 \n");
        }
        
        sql.append("WHERE 1=1 \n");
        // 特定商家
        if( criteriaVO.getStoreId()!=null ){
            // sql.append("AND EXISTS (SELECT * FROM EC_ORDER_DETAIL WHERE STORE_ID=#STORE_ID AND ORDER_ID=S.ID) \n");
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        // ID
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        // ID
        if( !sys.isEmpty(criteriaVO.getOrderList()) ){
            sql.append(NativeSQLUtils.getInSQL("S.ID", criteriaVO.getOrderList(), params)).append(" \n");
        }
        // ORDER_NUMBER
        if( criteriaVO.getOrderNumber()!=null ){
            String kw = "%"+criteriaVO.getOrderNumber().trim()+"%";
            sql.append("AND S.ORDER_NUMBER LIKE #ORDER_NUMBER \n");
            params.put("ORDER_NUMBER", kw);
        }
        // 訂單日期
        if( criteriaVO.getStartAt()!=null ){
            sql.append("AND TRUNC(S.APPROVETIME)>=TRUNC(#APPROVETIME_S) \n");
            params.put("APPROVETIME_S", criteriaVO.getStartAt());
        }
        if( criteriaVO.getEndAt()!=null ){
            sql.append("AND TRUNC(S.APPROVETIME)<=TRUNC(#APPROVETIME_E) \n");
            params.put("APPROVETIME_E", criteriaVO.getEndAt());
        }
        // 出貨日期
        if( criteriaVO.getShipStartAt()!=null ){
            sql.append("AND TRUNC(S.SHIPPING_TIME)>=TRUNC(#SHIPPING_TIME_S) \n");
            params.put("SHIPPING_TIME_S", criteriaVO.getShipStartAt());
        }
        if( criteriaVO.getShipEndAt()!=null ){
            sql.append("AND TRUNC(S.SHIPPING_TIME)<=TRUNC(#SHIPPING_TIME_E) \n");
            params.put("SHIPPING_TIME_E", criteriaVO.getShipEndAt());
        }
        // 待買方確認(訂單量價)
        if( OrderStatusEnum.Waiting.getCode().equals(criteriaVO.getOrderStatus()) ){
            criteriaVO.setBuyerCheck(Boolean.FALSE);
            criteriaVO.setOrderStatus(null);
        }
        // 待買方確認訂單量、價
        if( OrderStatusEnum.Waiting.getCode().equals(criteriaVO.getOrderStatus()) ){
            criteriaVO.setBuyerCheck(true);
            criteriaVO.setOrderStatus(null);
        }
        if( criteriaVO.getBuyerCheck()!=null ){
            sql.append("AND S.BUYER_CHECK=#BUYER_CHECK \n");
            params.put("BUYER_CHECK", criteriaVO.getBuyerCheck());
        }
        
        if( !StringUtils.isBlank(criteriaVO.getRfqStatus()) ){// 詢價單狀態
            sql.append("AND S.STATUS = #RFQ_STATUS \n");
            params.put("RFQ_STATUS", criteriaVO.getRfqStatus());
        }else if( !StringUtils.isBlank(criteriaVO.getOrderStatus()) ){// 訂單狀態
            sql.append("AND S.STATUS = #ORDER_STATUS \n");
            params.put("ORDER_STATUS", criteriaVO.getOrderStatus());
        }else if( sys.isFalse(criteriaVO.getClosed()) ){// [未結案訂單]：符合查詢條件，已確認且未結案的訂單。
            // 目前即為 - 狀態為賣家[已確認]的訂單
            sql.append("AND S.STATUS IN (").append(OrderStatusEnum.getWaitingListStr()).append(") \n");
        }else if( criteriaVO.getStatusList()!=null && !criteriaVO.getStatusList().isEmpty() ){// 詢價單 || 訂單
            sql.append(NativeSQLUtils.getInSQL("S.STATUS", criteriaVO.getStatusList(), params)).append(" \n");
        }
        // 付款狀態
        if( !StringUtils.isBlank(criteriaVO.getPayStatus()) ){
            sql.append("AND S.PAY_STATUS = #PAY_STATUS \n");
            params.put("PAY_STATUS", criteriaVO.getPayStatus());
        }
        // 出貨狀態
        if( !StringUtils.isBlank(criteriaVO.getShipStatus()) ){
            sql.append("AND S.SHIP_STATUS = #SHIP_STATUS \n");
            params.put("SHIP_STATUS", criteriaVO.getShipStatus());
        }
        // 買家
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND S.MEMBER_ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        // 客戶關鍵字
        if( !StringUtils.isBlank(criteriaVO.getCusKeyword()) ){
            String kw = "%" + criteriaVO.getCusKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("M.LOGIN_ACCOUNT LIKE #CUSKW OR M.NAME LIKE #CUSKW \n");
            sql.append("OR M.CNAME LIKE #CUSKW OR M.ENAME LIKE #CUSKW OR M.ID_CODE LIKE #CUSKW OR M.NICKNAME LIKE #CUSKW \n");
            sql.append(") \n");
            params.put("CUSKW", kw);
        }
        // 商品關鍵字
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT T.ID \n");
            sql.append("    FROM EC_ORDER_DETAIL T \n");
            sql.append("    JOIN EC_PRODUCT P ON P.STORE_ID=T.STORE_ID AND P.ID=T.PRODUCT_ID \n");
            sql.append("    WHERE T.STORE_ID=P.STORE_ID \n");
            sql.append("    AND (P.CNAME LIKE #PRDKW OR P.CODE LIKE #PRDKW) \n");
            sql.append("    AND T.ORDER_ID=S.ID \n");
            sql.append(") \n");

            params.put("PRDKW", kw);
        }
        // 有待回應訊息
        if( criteriaVO.getReplyMsg()!=null && criteriaVO.getReplyMsg()){
            sql.append("AND EXISTS ( \n");
            // 同 EcStoreFacade.countTodo() 條件
            sql.append("    SELECT MS.* \n");
            sql.append("    FROM EC_ORDER_MESSAGE MS \n");
            sql.append("    JOIN ( \n");
            sql.append("         SELECT MAX(ID) MID, ORDER_ID FROM EC_ORDER_MESSAGE GROUP BY ORDER_ID \n");
            sql.append("    ) ML ON ML.MID=MS.ID \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    AND MS.BUYER=1 \n");// 買家
            sql.append("    AND MS.DISABLED=0 \n");
            sql.append("    AND MS.ORDER_ID=S.ID \n");
            sql.append(") \n");
        }
        
        // for EC1.0
        if( GlobalConstant.TCC_DEALER_ENABLED ){
            if( criteriaVO.getProductCode()!=null ){
                sql.append("AND PD.CODE=#PRODUCT_CODE \n");
                params.put("PRODUCT_CODE", criteriaVO.getProductCode());
            }
            if( criteriaVO.getDeliveryPlaceId()!=null ){
                sql.append("AND S.DELIVERY_ID=#DELIVERY_ID \n");
                params.put("DELIVERY_ID", criteriaVO.getDeliveryPlaceId());
            }
            if( sys.isFalse(criteriaVO.getTranToEC10()) ){
                sql.append("AND NOT EXISTS ( \n");
                sql.append("    SELECT * FROM EC_TCC_ORDER WHERE TCC_ORDER_ID>0 AND SRC_ORDER_ID=S.ID \n");
                sql.append(") \n");
            }
            if( sys.isTrue(criteriaVO.getTranToEC10()) ){
                sql.append("AND EXISTS ( \n");
                sql.append("    SELECT * FROM EC_TCC_ORDER WHERE TCC_ORDER_ID>0 AND SRC_ORDER_ID=S.ID \n");
                sql.append(") \n");
            }
        }
        
        return sql.toString();
    }
    
    public List<OrderVO> findByCustomer(Long memberId, Long storeId, Locale locale, boolean tccDealer){
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setMemberId(memberId);
        criteriaVO.setStoreId(storeId);
        List<OrderVO> list = findByCriteria(criteriaVO, locale, tccDealer);
        return list;
    }
    public List<OrderVO> findByIds(Long storeId, List<Long> orderList, Locale locale, boolean tccDealer){
        if( storeId==null || orderList==null ){
            logger.error("findByIds error storeId="+storeId+", orderList="+orderList);
            return null;
        }
        
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setOrderList(orderList);
        List<OrderVO> list = findByCriteria(criteriaVO, locale, tccDealer);
        return list;
    }
    public OrderVO findById(Long storeId, Long id, boolean fullInfo, Locale locale, boolean tccDealer){
        if( storeId==null || id==null ){
            logger.error("findById error storeId="+storeId+", id="+id);
            return null;
        }
        
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setId(id);
        List<OrderVO> list = findByCriteria(criteriaVO, locale, tccDealer);
        
        OrderVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        
        if( vo==null ){
            logger.error("findById error storeId="+storeId+", id = "+id);
            return null;
        }
        
        // 其他關聯資訊
        // 商品明細
        List<OrderDetailVO> items = orderDetailFacade.findByOrderId(storeId, id);
        vo.setItems(items);
        
        if( fullInfo ){
            OrderStatusEnum orderStatus = OrderStatusEnum.getFromCode(vo.getStatus());
            RfqStatusEnum rfqStatus = RfqStatusEnum.getFromCode(vo.getStatus());

            // 車號 EC_ORDER_CAR_INFO
            List<OrderCarInfoVO> carList = orderCarInfoFacade.findByOrderId(storeId, id);
            String cars = orderCarInfoFacade.getCarListStr(carList);
            vo.setCarList(carList);
            vo.setCars(cars);
    
            // 轉 EC1.0 訂單資訊
            List<OrderCarInfoVO> carListEC10 = new ArrayList<OrderCarInfoVO>();
            OrderCriteriaVO tccCriteriaVO = new OrderCriteriaVO();
            tccCriteriaVO.setOrderId(vo.getId());
            List<TccOrderVO> tccOrderList = tccOrderFacade.findByCriteria(tccCriteriaVO);
            if( !sys.isEmpty(tccOrderList) ){
                vo.setTranToEC10(true);
                // 拆單時雖有多筆，但除車號、數量不同，其他欄位應相同，只傳一份即可
                TccOrderVO tccOrder = tccOrderList.get(0);
                tccOrder.genDeliveryDateLabel();
                tccOrder.genMethodName(locale);
                tccOrder.genTranModeName(locale);
                tccOrder.genTranTypeName(locale);
                vo.setTccOrder(tccOrder);
                
                for(TccOrderVO tccOrderVO : tccOrderList){// 承上，傳 ID、車號、數量 欄位
                    carListEC10.add(new OrderCarInfoVO(tccOrderVO.getId(), tccOrderVO.getVehicle(), tccOrderVO.getQuantity()));
                }
                vo.setCarListEC10(carListEC10);
            }
            
            // 處理記錄
            if( orderStatus!=null ){
                List<OrderProcessVO> records = orderProcessFacade.findByOrderId(storeId, id);
                vo.setRecords(records);
            }
            // 洽談紀錄
            //if( rfqStatus!=null ){
                List<OrderMessageVO> messages = orderMessageFacade.findByOrderId(storeId, id);
                vo.setMessages(messages);
            //}

            List<OrderLogVO> logs = orderLogFacade.findForQuotation(storeId, id);
            vo.setLogs(logs);
        }
        
        return vo;
    }

    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(OrderVO vo, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, locale, errors);

        return pass;
    }

    /**
     * 訂單改單價、改量 (for EC1.5 報單價 only)
     * ==============================
     * 賣家已確認的訂單(在途)，隨時都可改價、改量 - 20190326 EC1.5
     * @param vo
     * @param quoteList
     * @param doApprove
     * @param operator
     * @param locale
     * @param errors
     * @return 
     */
    public boolean changeOrder(OrderVO vo, List<OrderQuoteVO> quoteList, boolean doApprove, 
            EcMember operator, Locale locale, List<String> errors) {
        if( vo==null || quoteList==null ){
            logger.error("changeOrder vo==null || quoteList==null ");
            return false;
        }
        
        Long orderId = vo.getId();
        Long storeId = vo.getStoreId();
        EcOrder entity = this.find(orderId);
        if( entity==null || entity.getStoreId()==null || !entity.getStoreId().equals(storeId) ){
            logger.error("changeOrder EcOrder==null ");
            return false;
        }
        boolean ec15 = (operator.getTccDealer()!=null && operator.getTccDealer());
        logger.info("changeOrder orderId="+orderId+", status="+entity.getStatus()+", shipStatus="+entity.getShipStatus());
        
        String message = null;
        // EC_ORDER_DETAIL
        BigDecimal subTotal = BigDecimal.ZERO;// 總貨款 (不含運費)
        BigDecimal shipTotal = BigDecimal.ZERO;// 總運費
        for(OrderQuoteVO quoteVO : quoteList){
            EcOrderDetail detail = orderDetailFacade.find(quoteVO.getId());
            if( detail.getOrderId().equals(entity.getId()) ){
                // 改單價 (改單位運費)
                detail.setOriUnitPrice(detail.getPrice());
                detail.setPrice(quoteVO.getPrice()==null?BigDecimal.ZERO:quoteVO.getPrice());
                detail.setShipping(quoteVO.getShipping()==null?BigDecimal.ZERO:quoteVO.getShipping());
                // 改量
                detail.setOriQuantity(detail.getQuantity());
                detail.setQuantity(quoteVO.getQuantity());
                detail.setPrice(quoteVO.getPrice()==null?BigDecimal.ZERO:quoteVO.getPrice());
                // NULL to 0
                detail.setShipping(quoteVO.getShipping()==null?BigDecimal.ZERO:quoteVO.getShipping());
                detail.setQuantity(quoteVO.getQuantity()==null?BigDecimal.ZERO:quoteVO.getQuantity());
                // 總價 = 量 * (單價 + 單位運費)
                BigDecimal itemSubTotal = detail.getQuantity().multiply(detail.getPrice());// 貨款
                BigDecimal itemShipTotal = detail.getQuantity().multiply(detail.getShipping());// 運費
                BigDecimal itemTotal = itemSubTotal.add(itemShipTotal);// 總價
                detail.setTotal(itemTotal);

                subTotal = subTotal.add(itemSubTotal);
                shipTotal = shipTotal.add(itemShipTotal);
                orderDetailFacade.save(detail, operator, false);
                logger.info("changeOrder save detail = "+detail);
            }else{
                logger.error("changeOrder detail.getOrderId() error : "+detail.getOrderId()+"!="+entity.getId());
                return false;
            }
        }
        logger.debug("changeOrder subTotal = "+subTotal+", shipTotal = "+shipTotal);
        
        if( !subTotal.equals(vo.getSubTotal()) ){// Client UI 計算檢查
            logger.error("changeOrder SubTotal error "+subTotal+" != "+vo.getSubTotal());
            //return false;
        }
        
        // EC_ORDER
        entity.setShippingTotal(shipTotal);//vo.getShippingTotal());
        BigDecimal total = entity.getShippingTotal()==null?BigDecimal.ZERO:entity.getShippingTotal();
        total = total.add(subTotal);
        entity.setSubTotal(subTotal);
        entity.setOriTotal(entity.getTotal());// 原總價
        entity.setTotal(total);
        entity.setBuyerCheck(false);// 等買方確認中
        if( doApprove && OrderStatusEnum.Pending.getCode().equals(vo.getStatus()) ){
            entity.setStatus(OrderStatusEnum.Approve.getCode());
            entity.setApprovetime(new Date());
        }
        this.save(entity, operator, false);
        logger.info("changeOrder save entity = "+entity);

        // save order log
        orderLogFacade.saveByOrderStatus(entity, false, locale, operator);
        return true;
    }
    
    /**
     * 改單價、改量
     *    可改單價
     *      訂單報價 for EC2.0
     *      訂單確認 for EC1.5
     *    可改量
     *      出貨 for EC2.0 及EC1.5
     * 
     * @param vo
     * @param quoteList 
     */
    public boolean quoteOrder(OrderVO vo, List<OrderQuoteVO> quoteList, String statusType, EcMember operator, Locale locale, List<String> errors) {
        OrderLogEnum logEnum = OrderLogEnum.getFromCode(statusType);
        if( vo==null || quoteList==null || statusType==null ){
            logger.error("quoteOrder vo==null || quoteList==null || statusType==null");
            return false;
        }
        
        Long orderId = vo.getId();
        Long storeId = vo.getStoreId();
        EcOrder entity = this.find(orderId);
        if( entity==null || entity.getStoreId()==null || !entity.getStoreId().equals(storeId) ){
            logger.error("quoteOrder EcOrder==null ");
            return false;
        }
        
        RfqStatusEnum rfqStatusEnum = RfqStatusEnum.getFromCode(vo.getStatus());
        OrderStatusEnum statusEnum = OrderStatusEnum.getFromCode(vo.getStatus());
        //PayStatusEnum payStatusEnum = PayStatusEnum.getFromCode(vo.getPayStatus());
        ShipStatusEnum shipStatusEnum = ShipStatusEnum.getFromCode(vo.getShipStatus());
        
        boolean ec15 = (operator.getTccDealer()!=null && operator.getTccDealer());
        boolean changePrice = false;
        boolean changeQuantity = false;
        // 改單價 (改單位運費)
        if( (RfqStatusEnum.Quotation==rfqStatusEnum 
                && RfqStatusEnum.Inquiry.getCode().equals(entity.getStatus()))// 報價 for EC2.0
         || (OrderStatusEnum.Approve==statusEnum && ec15
                && OrderStatusEnum.Pending.getCode().equals(entity.getStatus()))// 訂單確認 for EC1.5
        ){ 
            changePrice = true;
        }
        // 改量
        if( ShipStatusEnum.SHIPPED==shipStatusEnum ){// 出貨 for EC2.0、EC1.5
            if( OrderStatusEnum.Approve.getCode().equals(entity.getStatus()) // 目前須為[已確認]PO
             && ShipStatusEnum.NOT_SHIPPED.getCode().equals(entity.getShipStatus()) // 未出貨 
            ){
                changeQuantity = true;
            }
        }
        
        logger.info("quoteOrder orderId="+orderId+", changePrice = "+changePrice+", changeQuantity="+changeQuantity
                    +", status="+entity.getStatus()+", shipStatus="+entity.getShipStatus()
                    +", changePrice="+changePrice+", changeQuantity="+changeQuantity);
        if( !changePrice && !changeQuantity ){// 未改價也未改量
            logger.error("quoteOrder orderId="+orderId+" error no change !");
            return false;
        }
        
        String message = null;
        String eventType = null;
        
        if( OrderLogEnum.ORDER==logEnum ){
            message = rfqStatusEnum!=null?rfqStatusEnum.getDisplayName(locale):
                        statusEnum!=null?statusEnum.getDisplayName(locale):"";
            eventType = rfqStatusEnum!=null?rfqStatusEnum.getCode():
                        statusEnum!=null?statusEnum.getCode():"";
            
            entity.setStatus(vo.getStatus());
            if( OrderStatusEnum.Approve==statusEnum ){
                entity.setApprovetime(new Date());// 訂單成立日期
            }
        //}else if( OrderLogEnum.PAYMENT==logEnum ){//此處無用到
        }else if( OrderLogEnum.SHIPPING==logEnum ){
            message = shipStatusEnum.getDisplayName(locale);
            eventType = shipStatusEnum.getCode();
            
            entity.setShipStatus(vo.getShipStatus());
        }else{
            logger.error("quoteOrder error orderId="+orderId+", logEnum="+logEnum);
            return false;
        }
        
        // EC_ORDER_DETAIL
        BigDecimal subTotal = BigDecimal.ZERO;// 總貨款 (不含運費)
        BigDecimal shipTotal = BigDecimal.ZERO;// 總運費
        for(OrderQuoteVO quoteVO : quoteList){
            EcOrderDetail detail = orderDetailFacade.find(quoteVO.getId());
            if( detail.getOrderId().equals(entity.getId()) ){
                // 改單價 (改單位運費)
                if( changePrice ){
                    detail.setPrice(quoteVO.getPrice()==null?BigDecimal.ZERO:quoteVO.getPrice());
                    detail.setShipping(quoteVO.getShipping()==null?BigDecimal.ZERO:quoteVO.getShipping());
                }
                // 改量
                if( changeQuantity ){
                    detail.setQuantity(quoteVO.getQuantity());
                }
                detail.setPrice(quoteVO.getPrice()==null?BigDecimal.ZERO:quoteVO.getPrice());
                detail.setShipping(quoteVO.getShipping()==null?BigDecimal.ZERO:quoteVO.getShipping());
                detail.setQuantity(quoteVO.getQuantity()==null?BigDecimal.ZERO:quoteVO.getQuantity());
                // 總價 = 量 * (單價 + 單位運費)
                BigDecimal itemSubTotal = detail.getQuantity().multiply(detail.getPrice());// 貨款
                BigDecimal itemShipTotal = detail.getQuantity().multiply(detail.getShipping());// 運費
                BigDecimal itemTotal = itemSubTotal.add(itemShipTotal);// 總價
                detail.setTotal(itemTotal);

                subTotal = subTotal.add(itemSubTotal);
                shipTotal = shipTotal.add(itemShipTotal);
                orderDetailFacade.save(detail, operator, false);
                logger.info("quoteOrder save detail = "+detail);
            }else{
                logger.error("quoteOrder detail.getOrderId() error : "+detail.getOrderId()+"!="+entity.getId());
                return false;
            }
        }
        logger.debug("quoteOrder subTotal = "+subTotal);
        
        if( !subTotal.equals(vo.getSubTotal()) ){// Client UI 計算檢查
            logger.error("quoteOrder SubTotal error "+subTotal+" != "+vo.getSubTotal());
            //return false;
        }
        
        // EC_STOCK_LOG
        if( !ec15 && changeQuantity ){// 量即使沒改也須改 EC_STOCK_LOG.TYPE
            stockLogFacade.shippingByOrder(storeId, orderId, quoteList, operator, false);
        }
        // EC_ORDER
        if( changePrice ){
            entity.setShippingTotal(shipTotal);//vo.getShippingTotal());
        }
        BigDecimal total = entity.getShippingTotal()==null?BigDecimal.ZERO:entity.getShippingTotal();
        total = total.add(subTotal);
        entity.setSubTotal(subTotal);
        entity.setOriTotal(entity.getTotal());// 原總價
        entity.setTotal(total);
        this.save(entity, operator, false);
        logger.info("quoteOrder save entity = "+entity);
        
        // EC_ORDER_LOG (總價) save order log
        orderLogFacade.saveByOrder(entity, statusType, eventType, message, operator);
        
        if( !ec15 ){
            // 因出貨時可改量，可能會造成與付款時金額不同；若用[信用額度]購買須額外處理
            if( changeQuantity && PayMethodEnum.CREDIT.getCode().equals(vo.getShippingCode()) ){
                String reason = CreditLogEnum.ORDER_RETURE.getDisplayName(locale);
                // for 記錄差額
                //boolean subtract = true;// 統一用[減]，可同時處理 changeAmt 正或負狀況
                //BigDecimal changeAmt = entity.getTotal().subtract(entity.getOriTotal());
                //customerFacade.changeCredits(storeId, vo.getCustomerId(), subtract, changeAmt, CreditLogEnum.ORDER_RETURE, 
                //        reason, orderId, operator, locale, errors);
                // for 記錄明細
                // 回補預扣
                customerFacade.changeCredits(storeId, vo.getCustomerId(), false, entity.getOriTotal(), CreditLogEnum.ORDER_RETURE, 
                        reason, orderId, operator, locale, errors);
                // 最終扣款
                customerFacade.changeCredits(storeId, vo.getCustomerId(), true, entity.getTotal(), CreditLogEnum.ORDER_FINAL_PAY, 
                        reason, orderId, operator, locale, errors);
            }
        }
        
        return true;
    }
    
}
