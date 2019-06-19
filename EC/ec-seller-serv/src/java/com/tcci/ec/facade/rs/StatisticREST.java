/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.StatisticEnum;
import com.tcci.ec.enums.TodoEnum;
import com.tcci.ec.facade.EcCustomerFacade;
import com.tcci.ec.facade.EcOrderFacade;
import com.tcci.ec.facade.EcProductFacade;
import com.tcci.ec.model.criteria.CustomerCriteriaVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.ec.model.statistic.StatisticCusLevelVO;
import com.tcci.ec.model.statistic.StatisticOrderStatusVO;
import com.tcci.ec.model.statistic.StatisticOrderVO;
import com.tcci.ec.model.statistic.StatisticPrdStatusVO;
import com.tcci.ec.model.statistic.StatisticPrdTypeVO;
import com.tcci.ec.model.statistic.StatisticPrdVO;
import com.tcci.ec.model.statistic.StatisticRfqVO;
import com.tcci.ec.model.statistic.StatisticCusVO;
import com.tcci.ec.model.statistic.StatisticTodoVO;
import com.tcci.ec.model.statistic.TimeSeriesDataVO;
import com.tcci.fc.util.DateUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *　
 * @author Peter.pan
 */
@Path("/statistic")
public class StatisticREST extends AbstractWebREST {
    @EJB EcCustomerFacade customerFacade;
    @EJB EcProductFacade productFacade;
    @EJB EcOrderFacade orderFacade;

    //<editor-fold defaultstate="collapsed" desc="for Store Char">
    @GET
    @Path("/store/{type}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findStatisticData(@Context HttpServletRequest request, @PathParam("type")String type){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        SubmitVO formVO = new SubmitVO();
        formVO.setType(type);
        return findStatisticData(request, formVO);
    }
    
    @POST
    @Path("/store")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findStatisticData(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findStatisticData ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            //boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            Long storeId = store.getId();
            String type = formVO.getType();
            StatisticEnum typeEnum = StatisticEnum.getFromCode(type);
            Date endAt = formVO.getEndAt();
            endAt = (endAt==null)?new Date():endAt;
            logger.info("findStatisticData typeEnum= "+typeEnum+", storeId = "+storeId+", endAt="+DateUtils.formatDate(endAt));
            
            if( StatisticEnum.TODO == typeEnum ){// 待辦事項統計
                return findStoreTodo(request, formVO);
            // OLD
            }else if( StatisticEnum.PO_STATUS == typeEnum ){// 訂單狀態
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                List<StatisticOrderStatusVO> list = orderFacade.findGroupByStatus(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.RFQ_STATUS == typeEnum ){// 詢價單狀態
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                List<StatisticRfqVO> list = orderFacade.findGroupByRfqStatus(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.PO_CUMNLATIVE == typeEnum ){// 訂單累計金額
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticOrderVO> list = orderFacade.findSumByPeriod(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if(StatisticEnum.CUS_LEVEL == typeEnum ){// 客戶等級
                CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
                criteriaVO.setStoreId(storeId);
                List<StatisticCusLevelVO> list = customerFacade.findGroupByLevel(criteriaVO);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.PRD_STATUS == typeEnum ){// 商品狀態
                ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
                criteriaVO.setStoreId(storeId);
                List<StatisticPrdStatusVO> list = productFacade.findGroupByStatus(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.PRD_TYPE == typeEnum ){// 銷售商品分類
                ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
                criteriaVO.setStoreId(storeId);
                List<String> statusList = new ArrayList<String>();// 銷售商品
                statusList.add(ProductStatusEnum.PUBLISH.getCode());
                //statusList.add(ProductStatusEnum.OUT_OF_STOCK.getCode());
                criteriaVO.setStatusList(statusList);
                
                List<StatisticPrdTypeVO> list = productFacade.findGroupByType(criteriaVO);
                return this.genSuccessRepsoneWithList(request, list);
            //營運管理：
            }else if( StatisticEnum.PRD_SALES == typeEnum ){// 商品銷售量
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticPrdVO> list = orderFacade.findGroupByPrdSales(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.PRD_INV == typeEnum ){// 商品庫存量
                ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticPrdVO> list = productFacade.findGroupByInventory(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.UNDELIVERED == typeEnum ){// 商品未出貨量
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticPrdVO> list = orderFacade.findGroupByPrdUndelivered(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            /*}else if( StatisticEnum.CASH_FLOW == typeEnum ){// 預計收款金額
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticOrderVO> list = orderFacade.findSumByPeriod(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);*/
            }else if( StatisticEnum.PO_DUE == typeEnum ){// 逾期金額
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticCusVO> list = orderFacade.findGroupByCusOrderDue(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            //銷售分析：
            }else if( StatisticEnum.ANA_CUS == typeEnum ){// 依客戶
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticCusVO> list = orderFacade.findGroupBySalesCus(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.ANA_MARKET == typeEnum ){// 依市場
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                TimeSeriesDataVO res = orderFacade.findGroupByMarket(criteriaVO, locale);
                return this.genSuccessRepsone(request, res);
            }else if( StatisticEnum.ANA_PRD == typeEnum ){// 依商品
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticPrdVO> list = orderFacade.findGroupBySalesPrd(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }else if( StatisticEnum.ANA_AREA == typeEnum ){// 依區域
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                criteriaVO.setStoreId(storeId);
                criteriaVO.setEndAt(endAt);
                List<StatisticOrderVO> list = orderFacade.findSumByPeriod(criteriaVO, locale);
                return this.genSuccessRepsoneWithList(request, list);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="for Store Todo">
    /**
     * Store 待辦事項統計
     * /services/statistic/store/todo
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/storeTodo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findStoreTodo(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findStoreTodo offset ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?formVO.getStoreId():(store!=null?store.getId():null);
            if( storeId==null ){
                logger.error("findStoreTodo storeId==null");
                return Response.notAcceptable(null).build();
            }
            
            List<StatisticTodoVO> list = new ArrayList<StatisticTodoVO>();
            for(TodoEnum todoEnum : TodoEnum.values()){
                if( !todoEnum.isAdmin() ){
                    BigDecimal count = storeFacade.countTodo(todoEnum, storeId, member.getId());
                    if( count!=null ){
                        StatisticTodoVO vo = new StatisticTodoVO();
                        vo.setLabel(todoEnum.getDisplayName(locale));
                        vo.setCode(todoEnum.getCode());
                        vo.setValue(count);

                        list.add(vo);
                    }
                }
            }

            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
}
