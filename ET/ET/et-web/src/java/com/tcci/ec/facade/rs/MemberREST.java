/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtFile;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.CompanyTypeEnum;
import com.tcci.et.enums.FileEnum;
import com.tcci.et.enums.MemberTypeEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.et.facade.EtFileFacade;
import com.tcci.et.model.FileVO;
import com.tcci.et.model.rs.ImportResultVO;
import com.tcci.et.model.criteria.MemberCriteriaVO;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
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
import javax.inject.Inject;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 *
 * @author peter.pan
 */
@Path("/members")
public class MemberREST extends AbstractWebREST {   
    @EJB EtFileFacade fileFacade;
    @Inject
    private TcUserFacade userFacade;
    
    //@Inject
    //private SecurityContext securityContext;
    
    private Map<String, String> sortFieldMapForMsg = new HashMap<>();
    
    public MemberREST(){
        logger.debug("MemberREST init ...");
        // for 支援排序
        // 會員查詢
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<>();
        sortFieldMap.put("memberId", "S.ID");// ID
//        sortFieldMap.put("loginAccount", "S.LOGIN_ACCOUNT");// 帳號
//        sortFieldMap.put("name", "S.NAME");// 顯示名稱
//        sortFieldMap.put("email", "S.EMAIL");// E-mail
//        sortFieldMap.put("phone", "S.PHONE");// 電話
//        sortFieldMap.put("sellerApprove", "S.SELLER_APPROVE");// 賣家
//        sortFieldMap.put("adminUser", "S.ADMIN_USER");// 系統管理員
//        sortFieldMap.put("active", "S.ACTIVE");// 有效
//        sortFieldMap.put("totalAmt", "O.TOTAL_AMT");// 累計消費
//        sortFieldMap.put("lastBuyDate", "O.lastBuyDate");// 最近消費日
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
        
        sortFieldMapForMsg = new HashMap<>();
//        sortFieldMap.put("type", "S.TYPE");// 類別
//        sortFieldMap.put("loginAccount", "S.LOGIN_ACCOUNT");// 帳號
//        sortFieldMap.put("name", "S.NAME");// 顯示名稱
//        sortFieldMap.put("createtime", "S.CREATETIME");// 顯示名稱
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            
            int totalRows = memberFacade.countByCriteria(criteriaVO);
            logger.debug("countMembers totalRows = "+totalRows);
            
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
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
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    @GET
    @Path("/full")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findMemberFullInfo(@Context HttpServletRequest request){
        logger.info("findMemberFullInfo ...");
        EtMember member = getReqUser(request);
        
        return this.findMemberFullInfo(request, member.getId());
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
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            if(member==null){
                member = memberFacade.find(id);
//                return this.genFailRepsone(request);
            }
            Long memberId = member.getId();
            
            // 賣家、管理員共用 RESTful
//            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
//                return genUnauthorizedResponse();
//            }
            logger.info("findMemberFullInfo memberId = "+memberId);
            
            MemberVO vo = memberFacade.findById(memberId, true, locale);

            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 會員儲存　　前台修改　考慮開放條件
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
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
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
                //前台修改 以系統管理員執行
                TcUser adminUser = userFacade.findUserByLoginAccount("administrator", null);
                memberFacade.saveVO(vo, adminUser, locale, false);
//                memberFacade.saveVO(vo, locale, false);
                
                vo = memberFacade.findById(vo.getMemberId(), true, locale);
                return this.genSuccessRepsone(request, vo);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    /**
     * 修改會員基本資料
     * /services/members/modify
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/modify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    @JWTTokenNeeded
    public Response modify(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findMemberFullInfo ...");
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            if(member == null){
                member = memberFacade.find(formVO.getMemberId());
            }
            Locale locale = getLocale(request);
            if(member==null || formVO==null){
                return this.genFailRepsone(request);
            }
            if (formVO.getName() == null || formVO.getPhone() == null) {
//                errors.add("無供應商資訊");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, errors);//未輸入必要參數
            }else{
                String name = formVO.getName().trim();
                String phone = formVO.getPhone().trim();
                boolean matched = name.matches(GlobalConstant.PATTEN_MEM_NAME);// 名稱檢查
                if( !matched ){
                    // [會員名稱]長度須介於2~60。
                    errors.add(ResourceBundleUtils.getMessage(locale, "member.check.msg2"));
                    logger.error("register name not matched!");
                }else{
                    matched = phone.matches(GlobalConstant.PATTEN_MEM_PHONE);// phone檢查
                    if( !matched ){
                        // [電話]只能包含英文大小寫、數字、底線、減號和英文句點，且長度須介於6~20。
                        errors.add("[電話]格式錯誤!");
                        logger.error("register phone not matched!");
                    }
                }
                
                if( !matched ){
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
                
                member.setName(name);
                member.setPhone(phone);
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
                memberFacade.save(member, admin, false);
                
                return this.genSuccessRepsoneWithId(request, member.getId());// 回傳會員ID
            }
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( formVO==null || StringUtils.isBlank(formVO.getPwdOri()) 
                ||  StringUtils.isBlank(formVO.getPwdNew()) || StringUtils.isBlank(formVO.getPwdNew2()) ){
                logger.error("changePassword formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long memberId = admin?formVO.getMemberId():member.getId();// 賣家只看自己資料
            logger.info("changePassword memberId = "+memberId);
            EtMember entity = memberFacade.find(memberId);
            if( entity==null ){
                logger.error("changePassword entity==null");
                return Response.notAcceptable(null).build();
            }
            if( formVO.getPwdNew().trim().length()<GlobalConstant.MIN_PWD_LEN ){
                // 密碼長度不可小於
                errors.add(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "pwd.min.len"), GlobalConstant.MIN_PWD_LEN));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            if( formVO.getPwdNew().trim().length()>GlobalConstant.MAX_PWD_LEN ){
                // 密碼長度不可小於
                errors.add(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "pwd.max.len"), GlobalConstant.MAX_PWD_LEN));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            if( !formVO.getPwdNew().equals(formVO.getPwdNew2()) ){
                // 確認密碼輸入錯誤
                errors.add(ResourceBundleUtils.getMessage(locale, "pwd.conform.err"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(formVO.getPwdOri());
            if( encrypted==null || !encrypted.equals(entity.getPassword()) ){
                // 原密碼輸入錯誤
                errors.add(ResourceBundleUtils.getMessage(locale, "pwd.ori.err"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            encrypted = aes.encrypt(formVO.getPwdNew());
            entity.setPassword(encrypted);
            
            TcUser adminUser = userFacade.findUserByLoginAccount("administrator", null);
            memberFacade.save(entity, adminUser, false);
//            memberFacade.save(entity, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful

            if( formVO==null ){
                logger.error("removeMember formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long memberId = formVO.getId();
            logger.info("removeMember memberId = "+memberId);
            EtMember entity = memberFacade.find(memberId);
            if( entity!=null ){
                entity.setDisabled(Boolean.TRUE);
                TcUser adminUser = userFacade.findUserByLoginAccount("administrator", null);
                memberFacade.save(entity, adminUser, false);
//                memberFacade.save(entity, false);
            }else{
                logger.error("removeMember not exists id = "+memberId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }

            return this.genSuccessRepsoneWithId(request, memberId);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    @POST
    @Path("/checkVenderCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response checkVenderCategory(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("checkVenderCategory ...");
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            if(formVO==null || formVO.getTenderId()==null){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, null);
            }
            
            
            member = getReqUser(request);
            Locale locale = getLocale(request);
            if(member==null){
                member = memberFacade.find(formVO.getMemberId());
//                return this.genFailRepsone(request);
            }
            Long memberId = member.getId();
            Long tenderId = formVO.getTenderId();
            TenderVO tenderVO = etTenderFacade.findById(tenderId, true);
            
            if(tenderVO==null || tenderVO.getCategoryId()==null){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            Long categoryId = tenderVO.getCategoryId();
            
            // 賣家、管理員共用 RESTful
//            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
//                return genUnauthorizedResponse();
//            }
            logger.info("checkVenderCategory memberId = "+memberId);
            MemberVO vo = memberFacade.findById(memberId, true, locale);
            List<VenderVO> result = new ArrayList<>();
            if(vo!=null && CollectionUtils.isNotEmpty(vo.getVenderList())){
                for(VenderVO vender:vo.getVenderList()){
                    String cids = vender.getCids();
                    boolean matched = false;
                    if(StringUtils.isNotBlank(cids)){
                        String[] ids = cids.split(",");
                        for(String idStr:ids){
                            if(categoryId.equals(Long.parseLong(idStr))){
                                matched = true;
                                break;
                            }
                        }
                    }
                    
                    if(matched){
                        result.add(vender);
                    }
                }
            }
            return this.genSuccessRepsoneWithList(request, result);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
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
//            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, null);
            FileVO fileVO = new FileVO();
            
            logger.info("uploadMemberPicture write real file finish.");
            
            // Save ET_FILE
            EtFile fileEntity = (id!=null)?fileFacade.find(id):new EtFile();
            if( fileEntity!=null && fileEnum.getPrimaryType().equals(fileEntity.getPrimaryType())
             && fileEntity.getPrimaryId()!=null && !fileEntity.getPrimaryId().equals(memberId) ){
                logger.error("uploadMemberPicture error : {} : {}", fileEntity.getPrimaryId(), memberId);
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }else if( fileEntity==null ){
                fileEntity = new EtFile();
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
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            Long memberId = admin?formVO.getMemberId():(member!=null?member.getId():null);
            EtFile entity = null;
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
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            
            return uploadTccMember(request, multiPart);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            return uploadTccMember(request, multiPart);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
    public Response uploadTccMember(HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadTccMember ...");
        List<String> errors = new ArrayList<String>();
        EtMember member = null;
        try{
            member = getReqUser(request);
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
//            memberFacade.importTccMembers(fileVO.getSaveFileNameFull(), fileContentType, autoAddClass, locale, memType, resVO);
            logger.info("uploadTccMember importTccMembers finish.");
            
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            if( formVO==null ){
                logger.error("saveDealers formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            
            return this.genSuccessRepsoneWithList(request, formVO.getImportPrdList());
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
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
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            if( formVO==null ){
                logger.error("saveDownstreams formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = null;
            Long dealerId = null;
            if( admin ){
                //storeId = formVO.getStoreId();
                dealerId = formVO.getDealerId();
            }else{// 經銷商自行匯入
                dealerId = (member!=null)?member.getId():null;
                //storeId = (store!=null)?store.getId():null;
            }
            // 改 EXCEL 第一欄標示
            /*if( dealerId==null ){
                logger.error("saveDownstreams dealerId==null");
                return Response.notAcceptable(null).build();
            }*/
            
            
            return this.genSuccessRepsoneWithList(request, formVO.getImportPrdList());
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
}
