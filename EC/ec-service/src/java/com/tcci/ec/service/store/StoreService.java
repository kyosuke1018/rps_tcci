/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.store;

import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcFavoriteStore;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.facade.EcCompanyFacade;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.facade.member.EcFavoriteStoreFacade;
import com.tcci.ec.facade.payment.EcPaymentFacade;
import com.tcci.ec.facade.seller.EcSellerFacade;
import com.tcci.ec.facade.shipping.EcShippingFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.facade.store.EcStoreUserFacade;
import com.tcci.ec.facade.util.StoreFilter;
import com.tcci.ec.model.LongOptionVO;
import com.tcci.ec.model.StoreVO;
import com.tcci.ec.model.criteria.StoreCriteriaVO;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.vo.Store;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("store")
public class StoreService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(StoreService.class);
    @EJB
    protected EcSellerFacade ecSellerFacade;
    @EJB
    protected EcStoreFacade ecStoreFacade;
    @EJB 
    protected EcFavoriteStoreFacade favoriteStoreFacade;
    @EJB 
    protected EcCompanyFacade ecCompanyFacade;
    @EJB 
    protected EcPaymentFacade ecPaymentFacade;
    @EJB 
    protected EcShippingFacade ecShippingFacade;
    @EJB 
    protected EcOptionFacade ecOptionFacade;
    @EJB
    protected EcStoreUserFacade ecStoreUserFacade;
    
    @GET
    @Path("list")
    @Produces("application/json; charset=UTF-8;")
    public List<Store> list(@Context HttpServletRequest request,
            StoreFilter filter) {
        logger.debug("list ...");
        List<Store> result = new ArrayList<>();
        try{
            if(filter==null){
                filter = new StoreFilter();
            }else{
                if( CollectionUtils.isNotEmpty(filter.getAreaList()) ){//指定銷售地區 多選
                    List<Long> storeIdList = ecStoreFacade.findIdListByArea(filter.getAreaList());//符合商店
                    if(CollectionUtils.isEmpty(storeIdList)){//沒有符合查詢區域的商店
                        return result;
                    }
                    logger.debug("product storeIdList:"+storeIdList);
                    filter.setStoreIdList(storeIdList);
                }
            }
            List<EcStore> list = ecStoreFacade.findByCriteria(filter);
            if(CollectionUtils.isNotEmpty(list)){
                for(EcStore entity:list){
                    result.add(entityTransforVO.storeTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("myList")
    @Produces("application/json; charset=UTF-8;")
    public List<Store> myList() {
        logger.debug("myList ...");
        List<Store> result = new ArrayList<>();
        try{
            EcMember member = getAuthMember();
//            EcStoreUser storeUser = ecStoreUserFacade.findByMember(member.getId());
//            if(storeUser!=null){
//                EcStore entity = ecStoreFacade.find(storeUser.getStoreId());
//                result.add(entityTransforVO.storeTransfor(entity));
//            }
            List<Long> list = ecStoreFacade.findIdListByMember(member);
            if(CollectionUtils.isNotEmpty(list)){
                for(Long id:list){
                    EcStore entity = ecStoreFacade.find(id);
                    result.add(entityTransforVO.storeTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("findStore")
    @Produces("application/json; charset=UTF-8;")
    public Store findStore(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {    
        logger.debug("findStore ...");
        try{
            logger.info("findStore storeId = "+storeId);
            EcStore store = ecStoreFacade.find(storeId);
            return entityTransforVO.storeTransfor(store);
        }catch(Exception e){
            logger.error("findStore Exception:\n", e);
        }
        return null;
    }
    
    /*
    @GET
    @Path("disabled")
    @Produces("application/json; charset=UTF-8;")
    public String disabled(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId,
            @QueryParam(value = "disabled") boolean disabled) {
        logger.debug("disabled storeId..."+storeId);
        try{
            EcMember member = getAuthMember();
            if(member==null || storeId==null){
                return "查無會員及賣場資訊!";
            }
            EcStore entity = ecStoreFacade.find(storeId);
            if(entity!=null){
                entity.setDisabled(disabled);
                entity.setModifytime(new Date());
                ecStoreFacade.save(entity);
            }else{
                return "賣場不存在! storeId..."+storeId;
            }
            return "SUCCESS";
        }catch(Exception e){
            logger.error("Exception:"+e);
            return "FAIL";
        }
    }*/
    
    @GET
    @Path("opened")
    @Produces("application/json; charset=UTF-8;")
    public String opened(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId,
            @QueryParam(value = "opened") boolean opened) {
        logger.debug("disabled storeId..."+storeId);
        try{
            Locale locale = getLocale(request);
            EcMember member = getAuthMember();
            if(member==null || storeId==null){
//                return "查無會員及賣場資訊!";
                return ResourceBundleUtils.getMessage(locale, "varify.member.store");
            }
            EcStore entity = ecStoreFacade.find(storeId);
            if(entity!=null){
                //開店檢查必要欄位
                if(opened){
                    List<EcCompany> ecCompanylist = ecCompanyFacade.findByStore(entity.getId());
                    if(CollectionUtils.isNotEmpty(ecCompanylist)){
                        EcCompany ecCompany = ecCompanylist.get(0);
                        if(StringUtils.isBlank(entity.getCname()) 
                                || StringUtils.isBlank(ecCompany.getTel1())
                                || StringUtils.isBlank(ecCompany.getAddr1())
                                || CollectionUtils.isEmpty(ecPaymentFacade.findByStore(entity))
                                || CollectionUtils.isEmpty(ecShippingFacade.findByStore(entity))){
//                            return "賣場基本資料未維護! storeId..."+storeId;
                            return ResourceBundleUtils.getMessage(locale, "varify.store.info")+" storeId..."+storeId;
                        }
                    }
                }
                
                entity.setOpened(opened);
                entity.setModifytime(new Date());
                ecStoreFacade.save(entity);
            }else{
//                return "賣場不存在! storeId..."+storeId;
                return ResourceBundleUtils.getMessage(locale, "varify.store")+" storeId..."+storeId;
            }
            return "SUCCESS";
        }catch(Exception e){
            logger.error("Exception:"+e);
            return "FAIL";
        }
    }
    
    /**
     * /services/stores/full/{id}
     * @param request
     * @param storeId
     * @return 
     */
//    @GET
//    @Path("/full/{id}")
//    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
//    public Response findStoreFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
//        logger.debug("findStoreFullInfo ...");
//        EcMember member = getReqUser(request);
//        EcStore store = getStore(request);
//        boolean admin = hasAdminRights(request, member);
//        try{
//            Long storeId = admin?id:store.getId();
//            logger.info("findStoreFullInfo storeId = "+store.getId());
//            
//            StoreVO vo = storeFacade.findById(storeId, true);
//
//            return this.genSuccessRepsone(request, vo);
//        }catch(Exception e){
//            logger.error("findStoreFullInfo Exception:\n", e);
//        }
//        return this.genFailRepsone(request);
//    }
    @GET
//    @Path("/full/{id}")
    @Path("full")
    @Produces("application/json; charset=UTF-8;")
//    public StoreVO findStoreFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
    public StoreVO findStoreFullInfo(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {    
        logger.debug("findStoreFullInfo ...");
//        EcMember member = getReqUser(request);
//        EcStore store = getStore(request);
//        boolean admin = hasAdminRights(request, member);
        try{
            logger.info("findStoreFullInfo storeId = "+storeId);
            EcStore store = ecStoreFacade.find(storeId);
            if(store != null && storeId != null){
                StoreVO vo = ecStoreFacade.findById(storeId, true);
                return vo;
            }
        }catch(Exception e){
            logger.error("findStoreFullInfo Exception:\n", e);
        }
        return null;
    }
    
    
    @GET
    @Path("favorite")
    @Produces("application/json; charset=UTF-8;")
    public String favorite(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId,
            @QueryParam(value = "flag") boolean flag) {
        logger.debug("favorite storeId..."+storeId);
        try{
            Locale locale = getLocale(request);
            EcMember member = getAuthMember();
            if(member==null || storeId==null){
//                return "查無會員及賣場資訊!";
                return ResourceBundleUtils.getMessage(locale, "varify.member.store");
            }
            EcStore store = ecStoreFacade.find(storeId);
            if(store!=null){
                List<EcFavoriteStore> storeList = favoriteStoreFacade.findByPrimary(member, store);
                if(flag && CollectionUtils.isEmpty(storeList)){
                    EcFavoriteStore entity = new EcFavoriteStore(member, store);
                    entity.setCreator(member);
                    favoriteStoreFacade.save(entity);
                }else if(!flag && CollectionUtils.isNotEmpty(storeList)){
                    for(EcFavoriteStore entity:storeList){
                        favoriteStoreFacade.remove(entity);
                    }
                }
            }else{
//                return "賣場不存在! storeId..."+storeId;
                return ResourceBundleUtils.getMessage(locale, "varify.store")+" storeId..."+storeId;
            }
            return "SUCCESS";
        }catch(Exception e){
            logger.error("Exception:"+e);
            return "FAIL";
        }
    }
    
    @GET
    @Path("findFavorite")
    @Produces("application/json; charset=UTF-8;")
    public List<Store> findFavorite() {
        logger.debug("memberInfo ...");
        EcMember member = getAuthMember();
        if(member!=null){
            logger.debug("memberInfo member..."+member.getLoginAccount());
        }else{
            return null;
        }
        List<EcFavoriteStore> storeList = favoriteStoreFacade.findByMember(member);
        List<Store> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(storeList)){
            for(EcFavoriteStore entity:storeList){
                list.add(entityTransforVO.storeTransfor(entity.getStore()));
            }
        }
        return list;
    }
    
    @GET
    @Path("unitOption")
    @Produces("application/json; charset=UTF-8;")
    public List<LongOptionVO> unitOption(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {
        logger.debug("unitOptions storeId..."+storeId);
        try{
            List<LongOptionVO> result = new ArrayList<>();
            EcMember member = getAuthMember();
            if(member==null || storeId==null){
                return null;
            }
            EcStore store = ecStoreFacade.find(storeId);
            if(store!=null){
               result =  ecOptionFacade.findByTypeOptions(storeId, OptionEnum.PRD_UNIT.getCode());
            }
            return result;
        }catch(Exception e){
            logger.error("Exception:"+e);
            return null;
        }
    }
    
    @GET
    @Path("salesAreaOption")
    @Produces("application/json; charset=UTF-8;")
    public List<LongOptionVO> salesAreaOption(@Context HttpServletRequest request) {
//        logger.debug("salesAreaOption");
        try{
//            return ecOptionFacade.findByTypeOptions(Long.parseLong("0"), OptionEnum.SALES_AREA.getCode());
            List<LongOptionVO> list = ecOptionFacade.findByTypeOptions(Long.parseLong("0"), OptionEnum.SALES_AREA.getCode());
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    LongOptionVO g1 = (LongOptionVO) o1;
                    LongOptionVO g2 = (LongOptionVO) o2;
                    return g1.getValue().compareTo(g2.getValue());//asc
//                    return g2.getValue().compareTo(g1.getValue());//desc
                }
            });
            return list;
        }catch(Exception e){
            logger.error("Exception:"+e);
            return null;
        }
    }
    
    @GET
    @Path("newOpen")
    @Produces("application/json; charset=UTF-8;")
    public List<StoreVO> newOPen() {
        logger.debug("newOPen ...");
        List<StoreVO> result = new ArrayList<>();
        try{
            StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
            criteriaVO.setOpened(Boolean.TRUE);
            criteriaVO.setDisabled(Boolean.FALSE);
//            criteriaVO.setFirstResult(0);
//            criteriaVO.setMaxResults(6);
            criteriaVO.setOrderBy("S.MODIFYTIME DESC");
            
            List<StoreVO> list = ecStoreFacade.findByCriteria(criteriaVO);
            for(StoreVO vo:list){
                vo = ecStoreFacade.findById(vo.getStoreId(), Boolean.FALSE);//找logo
                if(vo.getLogo()!=null){//有logo的
                    result.add(vo);
                }
                if(result.size()==6){//前六筆
                    break;
                }
            }
            
            return result;
        }catch(Exception e){
            logger.error("Exception:"+e);
            return result;
        }
    }
}
