/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcVendor;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcCompanyFacade;
import com.tcci.ec.facade.EcVendorFacade;
import com.tcci.ec.facade.EcPersonFacade;
import com.tcci.ec.model.criteria.VendorCriteriaVO;
import com.tcci.ec.model.VendorVO;
import com.tcci.ec.model.rs.BaseListResponseVO;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.ec.model.rs.SubmitVO;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author peter.pan
 */
@Path("/vendors")
public class VendorREST extends AbstractWebREST {
    @EJB EcVendorFacade vendorFacade;
    @EJB EcPersonFacade personFacade;
    @EJB EcCompanyFacade companyFacade;
    
    public VendorREST(){
        logger.debug("VendorREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        // Vender
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("id", "S.ID");// ID
        sortFieldMap.put("code", "S.CODE");// 供應商代碼
        sortFieldMap.put("cname", "C.CNAME");// 姓名(中)
        sortFieldMap.put("ename", "C.ENAME");// 姓名(英)
        sortFieldMap.put("nickname", "C.NICKNAME");// 簡稱
        sortFieldMap.put("idCode", "C.ID_CODE");// 統編
        sortFieldMap.put("tel1", "C.TEL1");// 電話(1)
        sortFieldMap.put("email1", "C.EMAIL1");// E-mail(1)
        sortFieldMap.put("addr1", "C.ADDR1");// 地址(1)
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Vendor Main">
    /**
     * 客戶查詢 - 先抓總筆數
     * /services/vendors/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countVendors(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countVendors ...");
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

            VendorCriteriaVO criteriaVO = new VendorCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            
            int totalRows = vendorFacade.countByCriteria(criteriaVO);
            logger.debug("countVendors totalRows = "+totalRows);
            BaseResponseVO resVO = genCountSuccessRepsone(request, totalRows);
            
            return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/vendors/list
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
    @JWTTokenNeeded
    public Response findVendors(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findVendors offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
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

            VendorCriteriaVO criteriaVO = new VendorCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<VendorVO> list = vendorFacade.findByCriteria(criteriaVO);

            BaseListResponseVO resVO = genSuccessListRepsone(request, list);
            
            return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/vendors/full/{id}
     * @param request
     * @param id
     * @return 
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findVendorFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findVendorFullInfo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            //boolean admin = hasAdminRights(request, member);
            logger.info("findVendorFullInfo id = "+id);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            VendorVO vo = vendorFacade.findById(store.getId(), id, true);
            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/vendors/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveVendor(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveVendor ...");
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
            if( formVO==null ){
                logger.error("saveVendor formVO==null");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(store.getId());

            Long vendorId = formVO.getVendorId();
            logger.error("saveVendor vendorId = "+vendorId);
            VendorVO vo = (vendorId==null)?new VendorVO():vendorFacade.findById(store.getId(), vendorId, false);
            if( vo==null ){
                logger.error("saveVendor vo==null");
                return Response.notAcceptable(null).build();
            }
            Long companyId = vo.getId();// 預存 companyId, 避免被 SubmitVO.id 覆蓋
            ExtBeanUtils.copyProperties(vo, formVO);
            vo.setId(companyId);
            vo.setDisabled(Boolean.FALSE);
            
            if( vendorFacade.checkInput(vo, member, locale, errors) ){// 輸入檢查
                vendorFacade.saveVO(vo, member, false);
                //return this.genSuccessRepsoneWithId(request, vo.getVendorId());
                return this.genSuccessRepsone(request, vo);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * /services/vendors/remove
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeVendor(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeVendor ...");
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
            if( formVO==null ){
                logger.error("removeVendor formVO==null");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(store.getId());

            Long vendorId = formVO.getVendorId();
            logger.info("removeVendor vendorId = "+vendorId);
            EcVendor entity = vendorFacade.find(vendorId);
            if( entity!=null && store.getId().equals(entity.getStoreId()) ){
                entity.setDisabled(Boolean.TRUE);
                vendorFacade.save(entity, member, false);
            }else{
                logger.error("removeVendor invalid vendorId="+vendorId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            return this.genSuccessRepsoneWithId(request, vendorId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
}
