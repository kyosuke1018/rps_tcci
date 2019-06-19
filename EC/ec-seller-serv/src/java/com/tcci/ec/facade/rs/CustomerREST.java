/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcCusFeedback;
import com.tcci.ec.entity.EcCustomer;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.ActionEnum;
import com.tcci.ec.enums.CreditLogEnum;
import com.tcci.ec.enums.CustomerEnum;
import com.tcci.ec.enums.FavoriteEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcCreditsLogFacade;
import com.tcci.ec.facade.EcCusAddrFacade;
import com.tcci.ec.facade.EcCusFeedbackFacade;
import com.tcci.ec.facade.EcCustomerFacade;
import com.tcci.ec.facade.EcTccOrderFacade;
import com.tcci.ec.model.CreditsLogVO;
import com.tcci.ec.model.CusAddrVO;
import com.tcci.ec.model.CusFeedbackVO;
import com.tcci.ec.model.criteria.CustomerCriteriaVO;
import com.tcci.ec.model.CustomerVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.e10.DeliveryPlacesEC10VO;
import com.tcci.ec.model.rs.CusAddrRsVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.rs.StrOptionVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.util.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author peter.pan
 */
@Path("/customers")
public class CustomerREST extends AbstractWebREST {
    private @EJB EcCustomerFacade customerFacade;
    private @EJB EcCusFeedbackFacade cusFeedbackFacade;
    private @EJB EcCreditsLogFacade creditsLogFacade;
    private @EJB EcCusAddrFacade cusAddrFacade;
    private @EJB EcTccOrderFacade tccOrderFacade;
    
    private Map<String, String> sortFieldMapFB;
    
    public CustomerREST(){
        logger.debug("CustomerREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("memberId", "S.ID");// ID
        sortFieldMap.put("loginAccount", "S.LOGIN_ACCOUNT");// 帳號
        sortFieldMap.put("name", "S.NAME");// 顯示名稱
        sortFieldMap.put("email", "S.EMAIL");// E-mail
        sortFieldMap.put("phone", "S.PHONE");// 電話
        sortFieldMap.put("active", "S.ACTIVE");// 有效
        //sortFieldMap.put("cname", "D.CNAME");// 姓名(中)
        //sortFieldMap.put("ename", "D.ENAME");// 姓名(英)
        sortFieldMap.put("levelName", "L.CNAME");// 客戶等級
        //sortFieldMap.put("tel1", "D.TEL1");// 電話(1)
        //sortFieldMap.put("email1", "D.EMAIL1");// E-mail(1)
        //sortFieldMap.put("addr1", "D.ADDR1");// 地址(1)
        sortFieldMap.put("noPayAmt", "O.NO_PAY_AMT");// 應收帳款
        sortFieldMap.put("totalAmt", "O.TOTAL_AMT");// 累計消費
        sortFieldMap.put("lastBuyDate", "O.lastBuyDate");// 最近消費日
        sortFieldMap.put("firstBuyDate", "O.firstBuyDate");// 首次消費日
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");

        // 客戶意見反映查詢
        sortFieldMapFB = new HashMap<String, String>();
        sortFieldMapFB.put("createtime", "S.CREATETIME");// 建立時間
        sortFieldMapFB.put("typeName", "T.CNAME");// 類別
        sortFieldMapFB.put("content", "S.CONTENT");// 反映內容
        sortFieldMapFB.put("processTime", "S.PROCESS_TIME");// 處理
        sortFieldMapFB.put("process", "S.PROCESS");// 處理內容
        sortFieldMapFB.put("closeTime", "S.CLOSE_TIME");// 結案
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Customer Main">
    /**
     * 客戶查詢 - 先抓總筆數
     * /services/customers/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response countCustomers(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countCustomers ...");
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            
            int totalRows = customerFacade.countByCriteria(criteriaVO);
            logger.debug("countCustomers totalRows = "+totalRows);

            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/customers/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCustomers(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findCustomers offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<CustomerVO> list = customerFacade.findByCriteria(criteriaVO);

            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/customers/full/{id}
     * @param request
     * @param id
     * @return 
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    public Response findCustomerFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findCustomerFullInfo ...");
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            logger.info("findCustomerFullInfo id = "+id);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            CustomerVO cusVO = customerFacade.findById(store.getId(), id, true);

            return this.genSuccessRepsone(request, cusVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 客戶儲存 - (客戶等級, 信用額度)
     * /services/customers/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCustomer(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveCustomer ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            if( formVO==null ){
                logger.error("saveCustomer formVO==null ");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(store.getId());

            //CustomerVO cusVO = (formVO.getId()==null || formVO.getId()==0)?
            //        new CustomerVO():customerFacade.findById(store.getId(), formVO.getId(), false);
            //ExtBeanUtils.copyProperties(cusVO, formVO);
            EcCustomer entity = customerFacade.find(formVO.getId());
            logger.debug("saveCustomer formVO.getLevelId() = "+formVO.getLevelId());
            entity.setLevelId(formVO.getLevelId());// 客戶等級
            
            // 有變更 [信用額度]
            if( formVO.getCredits()!=null && !formVO.getCredits().equals(entity.getCredits()) ){
                if( !customerFacade.setCredits(entity, formVO.getCredits(), CreditLogEnum.DIRECT_EDIT,
                        CreditLogEnum.DIRECT_EDIT.getDisplayName(locale), null, member, locale, errors) ){
                    logger.error("saveCustomer setCredits fail!");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }else{// 無變更 [信用額度]
                if( customerFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                    customerFacade.save(entity, member, false);
                }else{
                    logger.error("saveCustomer checkInput fail!");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }
            
            CustomerVO cusVO = customerFacade.findById(store.getId(), entity.getId(), true);
            return this.genSuccessRepsone(request, cusVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    /**
     * 客戶新增 (暫不提供使用，用來測試申請信用額度)
     * /services/customers/add
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCustomer(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("addCustomer ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            // 輸入檢查
            if( formVO==null || StringUtils.isBlank(formVO.getLoginAccount()) ){
                logger.error("addCustomer formVO==null");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(store.getId());
            EcMember memEntity = memberFacade.findByLoginAccount(formVO.getLoginAccount());
            if( memEntity==null ){// 會員不存在
                logger.error("addCustomer memEntity = null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST_MEM, errors);
            }
            CustomerVO cusVO = customerFacade.findByMember(store.getId(), formVO.getLoginAccount(), false);
            if( cusVO!=null ){// 此會員已是本商店客戶
                logger.error("addCustomer cusVO = "+cusVO);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EXISTS_CUS, errors);
            }
            
            EcCustomer entity = new EcCustomer();
            entity.setMemberId(memEntity.getId());
            entity.setStoreId(store.getId());
            // 用來測試申請信用額度
            entity.setCusType(CustomerEnum.CREDITS.getCode());
            entity.setApplyTime(new Date());
            //entity.setExpectedCredits(BigDecimal.ZERO);
            customerFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request, cusVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Customer Feedback">
    /**
     * 客戶意見反映查詢 - 先抓總筆數
     * /services/customers/feedback/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/feedback/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response countCusFeedbacks(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countCusFeedbacks ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(formVO.getMemberId());
            
            int totalRows = cusFeedbackFacade.countByCriteria(criteriaVO);
            logger.debug("countCusFeedbacks totalRows = "+totalRows);

            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            logger.error("countCusFeedbacks Exception:\n", e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 客戶意見反映查詢 
     * /services/customers/feedback/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/feedback/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCusFeedbacks(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findCusFeedbacks offset = "+offset+", rows = "+rows
                +", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(formVO.getMemberId());
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMapFB.get(sortField), sortOrderMap.get(sortOrder));
            List<CusFeedbackVO> list = cusFeedbackFacade.findByCriteria(criteriaVO);

            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            logger.error("findCusFeedbacks Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 儲存
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/feedback/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCusFeedback(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveCusFeedback ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null || formVO.getId()==null || formVO.getAction()==null ){
                logger.error("saveCusFeedback formVO.getAction()==null");
                return Response.notAcceptable(null).build();
            }
            
            ActionEnum actionEnum = ActionEnum.getFromCode(formVO.getAction().toUpperCase());
            
            //boolean isNew = (formVO.getId()==null || formVO.getId()==0);
            EcCusFeedback entity = null;
            //if( isNew ){
            //    entity = new EcCusFeedback();
            //    ExtBeanUtils.copyProperties(entity, formVO);
            //}else{
                entity = cusFeedbackFacade.find(formVO.getId());
                if( entity!=null && store.getId().equals(entity.getStoreId()) ){
                    if( actionEnum==ActionEnum.PROCESS ){
                        if( entity.getCloseTime()==null ){// 未結案
                            entity.setProcess(formVO.getProcess());
                            entity.setProcessUser(member.getId());
                            entity.setProcessTime(new Date());
                        }
                    }else if( actionEnum==ActionEnum.CLOSE ){// 結案
                        entity.setCloseUser(member.getId());
                        entity.setCloseTime(new Date());
                    }else if( actionEnum==ActionEnum.CANCEL ){// 取消結案
                        if( entity.getCloseTime()!=null ){// 已結案
                            entity.setCloseUser(null);
                            entity.setCloseTime(null);
                        }
                    }
                }else{
                    logger.error("saveCusFeedback invalid id = "+formVO.getId());
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
                }
            //}

            //entity.setStoreId(store.getId());
            //entity.setMemberId(memberId);
            //entity.setDisabled(formVO.getDisabled()==null? false:formVO.getDisabled());
            if( cusFeedbackFacade.checkInput(entity, member, locale, errors) ){
                cusFeedbackFacade.save(entity, member, false);
                return this.genSuccessRepsone(request);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for 常用設定 EC_CUS_ADDR">
    /**
     * 加入常用
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/used/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCusFavorites(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("addCusFavorites ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            if( formVO==null || formVO.getType()==null ){
                logger.error("addCusFavorites formVO==null ");
                return Response.notAcceptable(null).build();
            }
            FavoriteEnum favoriteType = FavoriteEnum.getFromCode(formVO.getType());
            if( favoriteType==null 
             || (favoriteType==FavoriteEnum.DELIVERY_ID // 送達地點
                    && (
                        sys.isBlank(formVO.getProvince()) ||
                        sys.isBlank(formVO.getCity()) ||
                        sys.isBlank(formVO.getDistrict()) ||
                        sys.isBlank(formVO.getTown())
                    ))
             || (favoriteType==FavoriteEnum.CAR_NO // 車號
                    && sys.isBlank(formVO.getCarNo()))
            ){   
                logger.error("addCusFavorites error favoriteType = "+favoriteType);
                return Response.notAcceptable(null).build();
            } 
            
            // 送達地點
            DeliveryPlacesEC10VO dpVO = null;
            if( favoriteType==FavoriteEnum.DELIVERY_ID ){
                dpVO = tccOrderFacade.findDeliveryPlacesByTown(formVO.getProvince(), formVO.getCity(), formVO.getDistrict(), formVO.getTown());
                if( dpVO==null || !sys.isValidId(dpVO.getId()) ){
                    logger.error("addCusFavorites error input not exists! ");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
                }
            }
            
            BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(member.getId());
            criteriaVO.setOrderBy("NVL(MODIFYTIME, CREATETIME)");// 操過限制，最舊的覆蓋
            List<CusAddrVO> list = cusAddrFacade.findByCriteria(criteriaVO);
            
            CusAddrVO cusAddrVO;
            if( sys.isEmpty(list) || list.size()<GlobalConstant.MAX_CUS_FAVORITES ){
                cusAddrVO = new CusAddrVO();
                cusAddrVO.setStoreId(store.getId());
                cusAddrVO.setMemberId(member.getId());
                if( favoriteType==FavoriteEnum.DELIVERY_ID && dpVO!=null ){
                    cusAddrVO.setDeliveryId(dpVO.getId());
                }else if( favoriteType==FavoriteEnum.CAR_NO ){
                    cusAddrVO.setCarNo(formVO.getCarNo().trim().toUpperCase());
                }
                cusAddrVO.setPrimary(Boolean.TRUE);
                cusAddrFacade.saveVO(cusAddrVO, member, false);
            }else{
                // 操過限制，最舊的覆蓋
                cusAddrVO = list.get(0);
                if( favoriteType==FavoriteEnum.DELIVERY_ID && dpVO!=null ){
                    cusAddrVO.setDeliveryId(dpVO.getId());
                }else if( favoriteType==FavoriteEnum.CAR_NO ){
                    cusAddrVO.setCarNo(formVO.getCarNo().trim().toUpperCase());
                }
                cusAddrFacade.saveVO(cusAddrVO, member, false);
            }
            
            // 回傳現有設定選單
            if( favoriteType==FavoriteEnum.DELIVERY_ID ){
                List<LongOptionVO> ops = cusAddrFacade.findCusAddrOptions(member.getId(), store.getId());
                return this.genSuccessRepsoneWithList(request, ops);
            }else if( favoriteType==FavoriteEnum.CAR_NO ){
                List<StrOptionVO> ops = cusAddrFacade.findCarNoOptions(member.getId(), store.getId());
                return this.genSuccessRepsoneWithList(request, ops);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    /**
     * 選取常用送達地點
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/used/sel")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findFavoriteOptions(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findFavoriteOptions ..");
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
            if( formVO==null || formVO.getDeliveryId()==null ){
                logger.error("findFavoriteOptions formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            FavoriteEnum favoriteType = FavoriteEnum.getFromCode(formVO.getType());
            
            Long deliveryId = formVO.getDeliveryId();
            String carNo = formVO.getCarNo();
            if( favoriteType==null 
             || (favoriteType==FavoriteEnum.DELIVERY_ID // 送達地點
                    && deliveryId==null)
             || (favoriteType==FavoriteEnum.CAR_NO // 車號
                    && sys.isBlank(formVO.getCarNo()))
            ){   
                logger.error("findFavoriteOptions error favoriteType = "+favoriteType);
                return Response.notAcceptable(null).build();
            } 
            
            CusAddrRsVO resVO = new CusAddrRsVO();
            if( favoriteType==FavoriteEnum.DELIVERY_ID ){
                DeliveryPlacesEC10VO dpVO = tccOrderFacade.findDeliveryPlaceById(deliveryId);
                if( dpVO==null ){
                    logger.error("findFavoriteOptions dpVO==null");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
                }

                // 省
                OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                List<StrOptionVO> provinceList = tccOrderFacade.findProvinceOptions(criteriaVO);
                // 市
                criteriaVO.setProvince(dpVO.getProvince());
                List<StrOptionVO> citylist = tccOrderFacade.findCityOptions(criteriaVO);
                // 區
                criteriaVO.setCity(dpVO.getCity());
                List<StrOptionVO> districtList = tccOrderFacade.findDistrictOptions(criteriaVO);
                // 鎮
                criteriaVO.setDistrict(dpVO.getDistrict());
                List<StrOptionVO> townlist = tccOrderFacade.findTownOptions(criteriaVO);

                ExtBeanUtils.copyProperties(resVO, dpVO);
                resVO.setProvinceList(provinceList);
                resVO.setCityList(citylist);
                resVO.setDistrictList(districtList);
                resVO.setTownList(townlist);
            }else if(favoriteType==FavoriteEnum.CAR_NO){ // 車號
                // UI JS 處理即可 carNo 
            }
            
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Customer Credits">
    /**
     * 儲存客戶信用額度異動記錄
     * /services/customers/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/credits/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCusCredits(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveCusCredits ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            if( formVO==null || formVO.getId()==null || formVO.getMemberId()==null
                || formVO.getType()==null || formVO.getQuantity()==null ){
                logger.error("saveCusCredits formVO==null ");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(store.getId());

            EcCustomer entity = customerFacade.find(formVO.getId());
            if( entity==null
                || !formVO.getStoreId().equals(entity.getStoreId()) 
                || !formVO.getId().equals(entity.getId()) 
                || !formVO.getMemberId().equals(entity.getMemberId()) ){
                logger.error("saveCusCredits entity==null! {},{},{}", new Object[]{formVO.getId(), formVO.getStoreId(), formVO.getMemberId()});
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }else if( formVO.getQuantity().compareTo(BigDecimal.ZERO)<=0 ){
                logger.error("saveCusCredits entity==null! {},{},{}", new Object[]{formVO.getId(), formVO.getStoreId(), formVO.getMemberId()});
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }

            boolean subtract = (!"1".equals(formVO.getType()));// 減少/增加
            boolean res = customerFacade.changeCredits(formVO.getStoreId(), formVO.getId(), subtract, 
                    formVO.getQuantity(), CreditLogEnum.ADD_LOG, formVO.getMemo(), null, member, locale, errors);
            if( !res ){
                logger.error("saveCusCredits changeCredits fail!");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            CustomerVO cusVO = customerFacade.findById(store.getId(), entity.getId(), true);
            return this.genSuccessRepsone(request, cusVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 客戶[信用額度]查詢 - 先抓總筆數
     * /services/customers/credits/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/credits/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response countCusCredits(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countCusCredits ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(formVO.getMemberId());
            
            int totalRows = creditsLogFacade.countByCriteria(criteriaVO);
            logger.debug("countCusCredits totalRows = "+totalRows);

            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            logger.error("countCusCredits Exception:\n", e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 客戶[信用額度]查詢 
     * /services/customers/credits/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/credits/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCusCredits(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findCusCredits offset = "+offset+", rows = "+rows
                +", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(formVO.getMemberId());
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            //criteriaVO.setOrderBy(sortFieldMapFB.get(sortField), sortOrderMap.get(sortOrder));
            List<CreditsLogVO> list = creditsLogFacade.findByCriteria(criteriaVO);

            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            logger.error("findCusCredits Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
}
