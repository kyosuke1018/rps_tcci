/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcMemberMsg;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.enums.TccMemberEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcCustomerFacade;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcMemberMsgFacade;
import com.tcci.ec.facade.EcTccDealerDsFacade;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.rs.ImportResultVO;
import com.tcci.ec.model.MemberMsgVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.StoreVO;
import com.tcci.ec.model.ImportTccDealerVO;
import com.tcci.ec.model.ImportTccDsVO;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import java.text.MessageFormat;
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
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 *
 * @author peter.pan
 */
@Path("/members")
public class MemberREST extends AbstractWebREST {   
    @EJB EcFileFacade fileFacade;
    @EJB EcMemberMsgFacade memberMsgFacade;
    @EJB EcCustomerFacade customerFacade;
    @EJB EcTccDealerDsFacade tccDealerDsFacade;
    
    //@Inject
    //private SecurityContext securityContext;
    
    private Map<String, String> sortFieldMapForMsg = new HashMap<String, String>();
    
    public MemberREST(){
        logger.debug("MemberREST init ...");
        // for 支援排序
        // 會員查詢
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("memberId", "S.ID");// ID
        sortFieldMap.put("loginAccount", "S.LOGIN_ACCOUNT");// 帳號
        sortFieldMap.put("name", "S.NAME");// 顯示名稱
        sortFieldMap.put("email", "S.EMAIL");// E-mail
        sortFieldMap.put("phone", "S.PHONE");// 電話
        sortFieldMap.put("sellerApprove", "S.SELLER_APPROVE");// 賣家
        sortFieldMap.put("adminUser", "S.ADMIN_USER");// 系統管理員
        sortFieldMap.put("active", "S.ACTIVE");// 有效
        sortFieldMap.put("totalAmt", "O.TOTAL_AMT");// 累計消費
        sortFieldMap.put("lastBuyDate", "O.lastBuyDate");// 最近消費日
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
        
        sortFieldMapForMsg = new HashMap<String, String>();
        sortFieldMap.put("type", "S.TYPE");// 類別
        sortFieldMap.put("loginAccount", "S.LOGIN_ACCOUNT");// 帳號
        sortFieldMap.put("name", "S.NAME");// 顯示名稱
        sortFieldMap.put("createtime", "S.CREATETIME");// 顯示名稱
    }

    //<editor-fold defaultstate="collapsed" desc="for Member Main">
    /**
     * 查詢 - 先抓總筆數
     * /services/members/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countMembers(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countMembers ...");
        //if( securityContext.getCallerPrincipal()!=null ){// for TEST
        //    logger.debug("countMembers getCallerPrincipal().getName() = "+securityContext.getCallerPrincipal().getName());
        //    logger.debug("countMembers isCallerInRole(\"ADMIN\") = "+securityContext.isCallerInRole("ADMIN"));
        //}
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( !admin ){// 賣家目前只開放查TCC經銷商的下游客戶 or 商店管理員
                if( store==null ){
                    logger.error("countMembers error store==null ");
                    return genUnauthorizedResponse();
                }
                if( (formVO.getDealerId()==null || formVO.getTccDs()==null || !formVO.getTccDs())
                 && (formVO.getStoreManager()==null || !formVO.getStoreManager() || store.getId()==null) 
                ){
                    logger.error("countMembers not supported opteration!");
                    return genUnauthorizedResponse();
                }else{
                    if( Boolean.TRUE.equals(formVO.getStoreManager()) ){
                        formVO.setManageStoreId(store.getId());
                    }
                }
            }
            MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            
            int totalRows = memberFacade.countByCriteria(criteriaVO);
            logger.debug("countMembers totalRows = "+totalRows);
            
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/members/list
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
    public Response findMembers(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findMembers offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( !admin ){// 賣家目前只開放查TCC經銷商的下游客戶 or 商店管理員
                if( store==null ){
                    logger.error("countMembers error store==null ");
                    return genUnauthorizedResponse();
                }
                if( (formVO.getDealerId()==null || formVO.getTccDs()==null || !formVO.getTccDs())
                 && (formVO.getStoreManager()==null || !formVO.getStoreManager() || store.getId()==null) 
                ){
                    logger.error("countMembers not supported opteration!");
                    return genUnauthorizedResponse();
                }else{
                    if( Boolean.TRUE.equals(formVO.getStoreManager()) ){
                        formVO.setManageStoreId(store.getId());
                    }
                }
            }

            MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            
            criteriaVO.setFullData(true);
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<MemberVO> list = memberFacade.findByCriteria(criteriaVO, locale);
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/members/full/{id}
     * @param request
     * @param id
     * @return 
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findMemberFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findMemberFullInfo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            Long memberId = admin?id:member.getId();// 賣家只看自己資料
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            logger.info("findMemberFullInfo memberId = "+memberId);
            
            MemberVO vo = memberFacade.findById(memberId, true, locale);

            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 會員儲存
     * /services/members/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveMember(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveMember ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null ){
                logger.error("saveMember formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long memberId = admin?formVO.getMemberId():member.getId();// 賣家只看自己資料
            logger.info("saveMember memberId = "+memberId);

            MemberVO vo = (memberId==null)?new MemberVO():memberFacade.findById(memberId, false, locale);
            if( vo==null ){
                logger.error("saveMember vo==null");
                return Response.notAcceptable(null).build();
            }
            
            MemberVO oriVO = new MemberVO();
            ExtBeanUtils.copyProperties(oriVO, vo);// for 預存不可修改欄位
            
            ExtBeanUtils.copyProperties(vo, formVO);
            vo.setId(oriVO.getId());// 回覆
            // 回覆有可能傳入但不可修改的欄位
            if( !admin ){
                vo.setDisabled(oriVO.getDisabled());
                vo.setActive(oriVO.getActive());
                vo.setAdminUser(oriVO.getAdminUser());
                vo.setLoginAccount(oriVO.getLoginAccount());
                vo.setMemberId(oriVO.getMemberId());
                
                vo.setSellerApply(oriVO.getSellerApply());
                vo.setSellerApprove(oriVO.getSellerApprove());
                vo.setApplytime(oriVO.getApplytime());
                vo.setApprovetime(oriVO.getApprovetime());
                
                vo.setComApply(oriVO.getComApply());
                vo.setComApprove(oriVO.getComApprove());
                vo.setComApplytime(oriVO.getComApplytime());
                vo.setComApprovetime(oriVO.getComApprovetime());
                
                vo.setState(oriVO.getState());
                vo.setTccDealer(oriVO.getTccDealer());
                vo.setTccDs(oriVO.getTccDs());
                vo.setStoreOwner(oriVO.getStoreOwner());
                vo.setFiUser(oriVO.getFiUser());
            }else{
                vo.setDisabled(oriVO.getDisabled());
                vo.setTccDs(oriVO.getTccDs());
            }
            
            if( memberFacade.checkInput(vo, member, locale, errors) ){// 輸入檢查
                member.setAdminUser(false);// 不提供線上建立系統管理員
                memberFacade.saveVO(vo, member, locale, false);
                
                vo = memberFacade.findById(vo.getMemberId(), true, locale);
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
     * 會員賣家身份異動
     * /services/members/seller
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/seller")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveSeller(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveSeller ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getMemberId()==null 
              || (formVO.getSellerApply()==null && formVO.getSellerApprove()==null) ){
                logger.error("saveSeller formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long memberId = admin?formVO.getMemberId():member.getId();// 賣家只看自己資料
            logger.info("saveSeller memberId = "+memberId);

            EcMember entity = (memberId==null)?new EcMember():memberFacade.find(memberId);
            if( entity==null ){
                logger.error("saveSeller entity==null");
                return Response.notAcceptable(null).build();
            }
            if( admin && formVO.getSellerApprove()!=null ){// 核准/駁回
                entity.setSellerApprove(formVO.getSellerApprove());
                entity.setApprovetime(new Date());
            }else{// 申請/取消
                entity.setSellerApply(formVO.getSellerApply());
            }
            
            memberFacade.save(entity, member, false);
            logger.info("saveSeller entity save ...");
            MemberVO vo = memberFacade.findById(memberId, true, locale);
            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 變更密碼
     * /services/members/changePwd
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/changePwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response changePassword(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("changePassword ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || StringUtils.isBlank(formVO.getPwdOri()) 
                ||  StringUtils.isBlank(formVO.getPwdNew()) || StringUtils.isBlank(formVO.getPwdNew2()) ){
                logger.error("changePassword formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long memberId = admin?formVO.getMemberId():member.getId();// 賣家只看自己資料
            logger.info("changePassword memberId = "+memberId);
            EcMember entity = memberFacade.find(memberId);
            if( entity==null ){
                logger.error("changePassword entity==null");
                return Response.notAcceptable(null).build();
            }
            if( formVO.getPwdNew().trim().length()<GlobalConstant.MIN_PWD_LEN ){
                // 密碼長度不可小於
                errors.add(MessageFormat.format(getResourceMsg(locale, "pwd.min.len"), GlobalConstant.MIN_PWD_LEN));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            if( formVO.getPwdNew().trim().length()>GlobalConstant.MAX_PWD_LEN ){
                // 密碼長度不可小於
                errors.add(MessageFormat.format(getResourceMsg(locale, "pwd.max.len"), GlobalConstant.MAX_PWD_LEN));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            if( !formVO.getPwdNew().equals(formVO.getPwdNew2()) ){
                // 確認密碼輸入錯誤
                errors.add(getResourceMsg(locale, "pwd.conform.err"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(formVO.getPwdOri());
            if( encrypted==null || !encrypted.equals(entity.getPassword()) ){
                // 原密碼輸入錯誤
                errors.add(getResourceMsg(locale, "pwd.ori.err"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            encrypted = aes.encrypt(formVO.getPwdNew());
            entity.setPassword(encrypted);
            memberFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 申請轉為公司戶帳號
     * /services/members/applyType
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/applyType")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response applyType(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("applyType ...");
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
            if( formVO==null || MemberTypeEnum.getFromCode(formVO.getMemberType())==null ){
                logger.error("applyType formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long memberId = member.getId();// 賣家只看自己資料
            logger.info("applyType memberId = "+memberId);
            EcMember entity = memberFacade.find(memberId);
            if( entity==null ){
                logger.error("applyType entity==null");
                return Response.notAcceptable(null).build();
            }
            entity.setComApply(true);
            entity.setComApplytime(new Date());
            entity.setComApprove(false);
            entity.setComApprovetime(null);
            entity.setComApprover(null);
            memberFacade.save(entity, member, false);
            
            MemberVO vo = memberFacade.findById(memberId, true, locale);
            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
        
    /**
     * 變更帳號類型
     * /services/members/changeType
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/changeType")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response changeType(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("changeType ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getMemberId()==null 
                    || MemberTypeEnum.getFromCode(formVO.getMemberType())==null ){
                logger.error("changeType formVO==null");
                return Response.notAcceptable(null).build();
            }
            //Long memberId = admin?formVO.getMemberId():member.getId();// 賣家只看自己資料
            Long memberId = formVO.getMemberId();
            logger.info("changeType memberId = "+memberId);
            EcMember entity = memberFacade.find(memberId);
            if( entity==null ){
                logger.error("changeType entity==null");
                return Response.notAcceptable(null).build();
            }
            entity.setType(formVO.getMemberType());
            memberFacade.save(entity, member, false);
            
            MemberVO vo = memberFacade.findById(memberId, true, locale);
            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
        
    /**
     * /services/members/remove
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeMember(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeMember ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null ){
                logger.error("removeMember formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long memberId = formVO.getId();
            logger.info("removeMember memberId = "+memberId);
            EcMember entity = memberFacade.find(memberId);
            if( entity!=null ){
                entity.setDisabled(Boolean.TRUE);
                memberFacade.save(entity, member, false);
            }else{
                logger.error("removeMember not exists id = "+memberId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }

            return this.genSuccessRepsoneWithId(request, memberId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Member Picture">
    /**
     * /services/members/picture/upload
     * @param request
     * @param imgSrc
     * @param multiPart
     * @return 
     */
    @Path("/picture/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadMemberPicture(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadMemberPicture ...");
        FileEnum fileEnum = FileEnum.MEMBER_PIC;
        String root = fileEnum.getRootDir();
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("uploadMemberPicture error bodyParts==null || bodyParts.isEmpty()");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            Long memberId = this.getMultiPartLong(multiPart, "memberId");
            memberId = admin?memberId:(member!=null?member.getId():null);
            if( memberId == null ){
                logger.error("uploadMemberPicture memberId==null ");
                return this.genFailRepsone(request); 
            }
            
            Long id = this.getMultiPartLong(multiPart, "id");
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadMemberPicture fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");

            byte[] content  = null;
            for (int i = 0; i < bodyParts.size(); i++) {
                //fileName = bodyParts.get(i).getContentDisposition().getFileName();// 中文會亂碼
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                fileContentType = (fileContentType==null)?bodyParts.get(i).getContentDisposition().getType():fileContentType;
                //logger.debug("uploadStoreLogo file ContentType = " + fileContentType);
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("uploadMemberPicture content size = "+((content!=null)?content.length:0));
                break; // 暫時段落，只支援一個圖檔
            }
            
            if( fileName==null || content==null ){
                logger.error("uploadMemberPicture error fileName==null || content==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            // 儲存實體檔案
            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, null);
            logger.info("uploadMemberPicture write real file finish.");
            
            // Save EcFile
            EcFile fileEntity = (id!=null)?fileFacade.find(id):new EcFile();
            if( fileEntity!=null && fileEnum.getPrimaryType().equals(fileEntity.getPrimaryType())
             && fileEntity.getPrimaryId()!=null && !fileEntity.getPrimaryId().equals(memberId) ){
                logger.error("uploadMemberPicture error : {} : {}", fileEntity.getPrimaryId(), memberId);
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }else if( fileEntity==null ){
                fileEntity = new EcFile();
            }
            
            fileEntity.setStoreId(null);
            fileEntity.setPrimaryType(fileEnum.getPrimaryType());
            fileEntity.setPrimaryId(memberId);// 此處可能為 null 或 0，後續再 update
            fileEntity.setDescription("member picture");
            fileEntity.setContentType(fileContentType);
            
            fileEntity.setFilename(fileVO.getFilename());
            fileEntity.setName(fileVO.getName());
            fileEntity.setSavedir(fileVO.getSavedir());
            fileEntity.setSavename(fileVO.getSavename());
            fileEntity.setFileSize(fileVO.getFileSize());
            fileFacade.save(fileEntity, member, false);
            logger.info("uploadMemberPicture save db finish.");
            
            FileVO resVO = fileFacade.findSingleByPrimary(fileEntity.getPrimaryType(), memberId);
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/members/picture/remove
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/picture/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeMemberPicture(@Context HttpServletRequest request, SubmitVO formVO) {
        FileEnum fileEnum = FileEnum.MEMBER_PIC;
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeMemberPicture ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long memberId = admin?formVO.getMemberId():(member!=null?member.getId():null);
            EcFile entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = fileFacade.find(formVO.getId());
            }else{
                logger.error("removeMemberPicture entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            if( memberId == null ){
                logger.error("removeMemberPicture memberId==null ");
                return this.genFailRepsone(request); 
            }
            
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // ID驗證
                if( !memberId.equals(entity.getPrimaryId()) && fileEnum.getPrimaryType().equals(entity.getPrimaryType()) ){
                    logger.error("removeMemberPicture memberId error : "+memberId+" != "+entity.getPrimaryId());
                    return Response.notAcceptable(null).build();
                }
                // 不刪實體檔，避免有共用狀況(排程刪除即可)
                fileFacade.remove(entity, false);
                return this.genSuccessRepsoneWithId(request, formVO.getId());
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Member Message">
    /**
     * 查詢 - 先抓總筆數
     * /services/members/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/message/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countMessage(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countMessage ...");
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
            // for 待回覆訪客留言
            Long memberId = formVO.getMemberId();
            if( formVO.getReplyMsg()!=null && formVO.getReplyMsg() ){
                memberId = (memberId==null)?member.getId():memberId;
            }

            MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
            //ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(memberId);
            criteriaVO.setReply(false);// 選呈現非回覆的留言
            criteriaVO.setReplyMsg(formVO.getReplyMsg());// for 待回覆訪客留言
            
            int totalRows = memberMsgFacade.countByCriteria(criteriaVO);
            logger.debug("countMessage totalRows = "+totalRows);
            BaseResponseVO resVO = genCountSuccessRepsone(request, totalRows);
            
            return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
        }catch(Exception e){
            logger.error("countMessage Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/members/message/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/message/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findMessages(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findMessages offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
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
            // for 待回覆訪客留言
            Long memberId = formVO.getMemberId();
            if( formVO.getReplyMsg()!=null && formVO.getReplyMsg() ){
                memberId = (memberId==null)?member.getId():memberId;
            }

            MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
            //ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(store.getId());
            criteriaVO.setMemberId(memberId);
            criteriaVO.setReply(false);// 選呈現非回覆的留言
            criteriaVO.setReplyMsg(formVO.getReplyMsg());// for 待回覆訪客留言
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMapForMsg.get(sortField), sortOrderMap.get(sortOrder));
            List<MemberMsgVO> list = memberMsgFacade.findByCriteria(criteriaVO);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    @GET
    @Path("/message/reply/list")
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findReplyMessages(@Context HttpServletRequest request, @QueryParam("parent")Long parent){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findReplyMessages parent = "+parent);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( parent==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, null);
            }
            MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
            criteriaVO.setParent(parent);
            criteriaVO.setReply(true);// 選呈現非回覆的留言

            criteriaVO.setOrderBy(sortFieldMap.get("createtime"), "");// 固定照時間順序
            List<MemberMsgVO> list = memberMsgFacade.findByCriteria(criteriaVO);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    @POST
    @Path("/message/reply/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveReplyMessage(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("saveReplyMessage ...");
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
            
            if( formVO.getParent()!=null ){
                EcMemberMsg oriMsg = memberMsgFacade.find(formVO.getParent());
                if( oriMsg!=null && store.getId().equals(oriMsg.getStoreId()) ){
                    EcMemberMsg entity = new EcMemberMsg();
                    entity.setMemberId(member.getId());
                    entity.setMessage(formVO.getMessage());
                    entity.setParent(formVO.getParent());
                    entity.setPrdId(oriMsg.getPrdId());
                    entity.setStoreId(oriMsg.getStoreId());
                    entity.setType(oriMsg.getType());

                    if( memberMsgFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                        memberMsgFacade.save(entity, member, false);
                        return this.genSuccessRepsone(request);
                    }else{
                        return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                    }
                }
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for EC1.5 Member Import">
    /**
     * /members/import/dealer/upload
     * 上傳匯入檔 - 台泥經銷商
     * @param request
     * @param multiPart
     * @return
     */
    @Path("/import/dealer/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadDealer(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadDealer ...");
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            return uploadTccMember(request, multiPart, TccMemberEnum.DEALER);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /members/import/dealer/upload
     * 上傳匯入檔 - 台泥經銷商下游客戶(攪拌站、檔口)
     * @param request
     * @param multiPart
     * @return
     */
    @Path("/import/ds/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadDownstream(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadDownstream ...");
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            return uploadTccMember(request, multiPart, TccMemberEnum.DOWNSTREAM);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
            
    /**
     * 
     * 上傳匯入檔 - 台泥經銷商、下游客戶(攪拌站、檔口)
     * @param request
     * @param multiPart
     * @param memType
     * @return
     */
    public Response uploadTccMember(HttpServletRequest request, final FormDataMultiPart multiPart, TccMemberEnum memType) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadTccMember ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            //Long storeId = null;
            Long dealerId = 0L;

            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("uploadTccMember error bodyParts.isEmpty()");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadTccMember fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");

            FileEnum fileEnum = FileEnum.IMP_TCC_DEALER;
            if( TccMemberEnum.DOWNSTREAM==memType ){
                fileEnum = FileEnum.IMP_TCC_DS;
                if( admin ){// 管理員代匯入
                    dealerId = this.getMultiPartLong(multiPart, "dealerId");
                    logger.info("uploadTccMember dealerId = "+dealerId);
                    //storeId = this.getMultiPartLong(multiPart, "storeId");
                    //logger.info("uploadTccMember fileName = "+fileName);
                }else{// 經銷商自行匯入
                    dealerId = member!=null?member.getId():null;
                    //storeId = (store==null||store.getId()==null)?null:store.getId();
                }
                // 改 EXCEL 第一欄標示
                /*if( dealerId==null ){//&& storeId==null ){
                    logger.error("uploadTccMember error dealerId==null");
                    return Response.notAcceptable(null).build(); // HTTP STATUS 406
                }*/
            }
            
            byte[] content  = null;
            for (int i = 0; i < bodyParts.size(); i++) {
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("uploadTccMember content size = "+((content!=null)?content.length:0));
                break;// 單一EXCEL檔
            }
            
            if( fileName==null || content==null ){
                logger.error("uploadTccMember error fileName==null || content==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            // 儲存實體檔案
            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, false, dealerId);
            logger.info("uploadTccMember write real file finish.");

            // 資料匯入處理
            boolean autoAddClass = false;
            ImportResultVO resVO = new ImportResultVO();
            // memType for 匯入台泥經銷商下游客戶(攪拌站、檔口)可能已存在，避免帳號重複檢核
            memberFacade.importTccMembers(fileVO.getSaveFileNameFull(), fileContentType, autoAddClass, locale, memType, resVO);
            logger.info("uploadTccMember importTccMembers finish.");
            
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /members/import/dealer/save
     * 儲存匯入 - 台泥經銷商
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/import/dealer/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveDealers(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveDealers ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            if( formVO==null || formVO.getImportTccDealerList()==null ){
                logger.error("saveDealers formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            List<Long> storeIds = new ArrayList<Long>();
            int addCount = 0;
            int updateCount = 0;
            for(ImportTccDealerVO vo : formVO.getImportTccDealerList()){
                // 檢查STORE是否存在 for 可多人管理STORE
                if( vo.getIdCode()==null ){
                    logger.error("saveDealers vo.getIdCode()==null !");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
                StoreVO storeVO = storeFacade.findByIdCode(vo.getIdCode());
                // 帳號是否存在
                EcMember entity = memberFacade.findByLoginAccount(vo.getLoginAccount());
                // 存在修改、不存在新增
                vo.setAdd(entity==null);
                MemberVO memVO = (entity!=null)?memberFacade.findById(entity.getId(), false, locale):new MemberVO();
                ExtBeanUtils.copyProperties(memVO, vo);
                // 暫用 verifyCode 辨別同批匯入
                String verifyCode = DateUtils.formatDate(new Date(), GlobalConstant.FORMAT_DATETIME_STR);
                if( storeVO==null ){// STORE 不存在，正常建立 EC_MEMBER、EC_SELLER、EC_STORE、EC_COMPANY*2、EC_STORE_USER
                    //MemberVO memVO = new MemberVO();
                    //ExtBeanUtils.copyProperties(memVO, vo);
                    // for EC_MEMBER
                    memVO.setActive(Boolean.TRUE);
                    memVO.setAdminUser(Boolean.FALSE);
                    memVO.setMemberType(MemberTypeEnum.COMPANY.getCode());// 直接成為公司戶
                    memVO.setDisabled(Boolean.FALSE);
                    memVO.setTccDealer(Boolean.TRUE);// 台泥經銷商
                    memVO.setTccDs(Boolean.FALSE);
                    //memVO.setStoreOwner(Boolean.TRUE);// 第一筆帳號預設為 owner
                    memVO.setStoreOwner(sys.isTrue(vo.getStoreOwner()));
                    memVO.setFiUser(sys.isTrue(vo.getFinUser()));
                    memVO.setVerifyCode(verifyCode);
                    
                    memVO.setApplytime(new Date());// 直接成為賣家
                    memVO.setApprovetime(new Date());
                    memVO.setSellerApply(Boolean.TRUE);
                    memVO.setSellerApprove(Boolean.TRUE);
                    
                    // for EC_COMPAMY
                    //memVO.setCname(memVO.getName());// 公司名稱
                    memVO.setTel1(memVO.getPhone());
                    memVO.setEmail1(memVO.getEmail());
                    memVO.setAddr1(vo.getStateName());// 區域別也放到地址欄
                    memVO.setBrief(vo.getCategoryName());// 產業別也放在簡介欄
                    memVO.setType(CompanyTypeEnum.MEMBER.getCode());

                    memberFacade.saveVO(memVO, member, locale, false);
                    logger.info("saveDealers new store memberId = "+memVO.getMemberId()+", storeId = "+memVO.getStoreId());
                    storeIds.add(memVO.getStoreId());
                }else{
                    // STORE 已存在，只能存 EC_MEMBER、EC_COMPANY、EC_STORE_USER
                    //MemberVO memVO = new MemberVO();
                    //ExtBeanUtils.copyProperties(memVO, vo);
                    memVO.setStoreId(storeVO.getId());// storeId
                    // for EC_MEMBER
                    memVO.setActive(Boolean.TRUE);
                    memVO.setAdminUser(Boolean.FALSE);
                    memVO.setMemberType(MemberTypeEnum.COMPANY.getCode());// 直接成為公司戶
                    memVO.setDisabled(Boolean.FALSE);
                    memVO.setTccDealer(Boolean.TRUE);// 台泥經銷商
                    memVO.setTccDs(Boolean.FALSE);
                    memVO.setStoreOwner(sys.isTrue(vo.getStoreOwner()));
                    memVO.setFiUser(sys.isTrue(vo.getFinUser()));
                    memVO.setVerifyCode(verifyCode);
                    
                    // for EC_COMPAMY
                    //memVO.setCname(memVO.getName());// 公司名稱
                    memVO.setTel1(memVO.getPhone());
                    memVO.setEmail1(memVO.getEmail());
                    memVO.setAddr1(vo.getStateName());// 區域別也放到地址欄
                    memVO.setBrief(vo.getCategoryName());// 產業別也放在簡介欄
                    memVO.setType(CompanyTypeEnum.MEMBER.getCode());
                    
                    memberFacade.saveVO(memVO, member, locale, false);
                    logger.info("saveDealers existed store memberId = "+memVO.getMemberId()+", storeId = "+memVO.getStoreId());
                    if( !storeIds.contains(memVO.getStoreId()) ){
                        storeIds.add(memVO.getStoreId());
                    }
                }
                
                if( vo.isAdd() ){
                    addCount++;
                }else{
                    updateCount++;
                }
            }
            
            for(Long storeId : storeIds){
                // 控制每家 store 唯一 StoreOwner
                // 1. 無 StoreOwner，取所有管理員第一筆為 Owner
                // 2. 多 StoreOwner，除第一筆以外，改為非 StoreOwner
                memberFacade.decideStoreOwner(storeId, member, locale, false);
            }
            
            // 匯入完成：新增 {0} 筆，修改 {1} 筆。
            String resmsg = MessageFormat.format(getResourceMsg(locale, "imp.res.count"), addCount, updateCount);
            return this.genSuccessRepsoneWithMsg(request, resmsg);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /members/import/ds/save
     * 儲存匯入 - 台泥經銷商下游客戶(攪拌站、檔口)
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/import/ds/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveDownstreams(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveDownstreams ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null || formVO.getImportTccDsList()==null ){
                logger.error("saveDownstreams formVO==null");
                return Response.notAcceptable(null).build();
            }
            // 改 EXCEL 第一欄標示 (*所属经销商统一社会信用代码(绑定关联使用))
            /*
            Long storeId = null;
            Long dealerId = null;
            if( admin ){
                //storeId = formVO.getStoreId();
                dealerId = formVO.getDealerId();
            }else{// 經銷商自行匯入
                dealerId = (member!=null)?member.getId():null;
                //storeId = (store!=null)?store.getId():null;
            }*/
            // 改 EXCEL 第一欄標示
            /*if( dealerId==null ){
                logger.error("saveDownstreams dealerId==null");
                return Response.notAcceptable(null).build();
            }*/
            
            // 暫用 verifyCode 辨別同批匯入
            String verifyCode = DateUtils.formatDate(new Date(), GlobalConstant.FORMAT_DATETIME_STR);
            int addCount = 0;
            int updateCount = 0;
            for(ImportTccDsVO vo : formVO.getImportTccDsList()){
                // 帳號是否存在
                EcMember entity = memberFacade.findByLoginAccount(vo.getLoginAccount());
                // 存在修改、不存在新增
                vo.setAdd(entity==null);
                MemberVO memVO = (entity!=null)?memberFacade.findById(entity.getId(), false, locale):new MemberVO();
                ExtBeanUtils.copyProperties(memVO, vo);
                // for EC_MEMBER
                memVO.setActive(Boolean.TRUE);
                memVO.setAdminUser(Boolean.FALSE);
                memVO.setApplytime(new Date());
                memVO.setApprovetime(new Date());
                memVO.setSellerApply(Boolean.FALSE);
                memVO.setSellerApprove(Boolean.FALSE);
                memVO.setMemberType(MemberTypeEnum.COMPANY.getCode());
                memVO.setDisabled(Boolean.FALSE);
                memVO.setTccDealer(Boolean.FALSE);
                memVO.setTccDs(Boolean.TRUE);// 台泥經銷商下游客戶(攪拌站、檔口)
                memVO.setVerifyCode(verifyCode);
                // for EC_COMPAMY
                memVO.setType(CompanyTypeEnum.MEMBER.getCode());
                memVO.setCname(memVO.getName());
                memVO.setTel1(memVO.getPhone());
                memVO.setEmail1(memVO.getEmail());
                
                //EcMember entity = memberFacade.findByLoginAccount(vo.getLoginAccount());
                Long dsId = null;
                //if( entity==null || entity.getDisabled() ){
                    memberFacade.saveVO(memVO, member, locale, false);
                    dsId = memVO.getMemberId();
                    logger.info("saveDownstreams new dsId = "+dsId);
                /*}else{
                    // 帳號已存在的會員，加入下游客戶
                    entity.setTccDs(Boolean.TRUE);
                    memberFacade.save(entity, member, false);
                    dsId = entity.getId();
                    logger.info("saveDownstreams exists dsId = "+dsId);
                }*/
                // for EC_TCC_DEALER_DS
                Long dealerId = vo.getDealerId();// 關聯供應商由 EXCEL 匯入
                if( dsId!=null ){
                    // 記錄關聯
                    tccDealerDsFacade.saveByKey(dealerId, dsId, member, false);
                    // 設為客戶
                    customerFacade.saveByTccDealer(dealerId, dsId, member, false);
                }
                
                if( vo.isAdd() ){
                    addCount++;
                }else{
                    updateCount++;
                }
            }
            
            // 匯入完成：新增 {0} 筆，修改 {1} 筆。
            String resmsg = MessageFormat.format(getResourceMsg(locale, "imp.res.count"), addCount, updateCount);
            return this.genSuccessRepsoneWithMsg(request, resmsg);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
}
